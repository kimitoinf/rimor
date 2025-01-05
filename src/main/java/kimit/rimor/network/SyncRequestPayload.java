package kimit.rimor.network;

import kimit.rimor.Rimor;
import kimit.rimor.player.PlayerData;
import kimit.rimor.player.PlayerDataComponent;
import kimit.rimor.trade.TradeComponent;
import kimit.rimor.trade.TradeEntry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SyncRequestPayload implements CustomPayload
{
	public static final CustomPayload.Id<SyncRequestPayload> ID = new CustomPayload.Id<>(Rimor.id("sync_request"));
	public static final PacketCodec<RegistryByteBuf, SyncRequestPayload> CODEC = CustomPayload.codecOf(SyncRequestPayload::write, SyncRequestPayload::new);
	public static final String PLAYER_DATA_KEY = "player_data";
	public static final String TRADES_KEY = "trades";
	private final Map<UUID, PlayerData> PlayerData;
	private final Map<Identifier, List<TradeEntry>> Trades;
	
	public SyncRequestPayload(Map<UUID, PlayerData> data, Map<Identifier, List<TradeEntry>> trades)
	{
		PlayerData = data;
		Trades = trades;
	}
	
	public SyncRequestPayload(RegistryByteBuf buf)
	{
		NbtCompound compound = buf.readNbt();
		PlayerData = new HashMap<>(PlayerDataComponent.DATA_CODEC.parse(buf.getRegistryManager().getOps(NbtOps.INSTANCE), compound.get(PLAYER_DATA_KEY)).result().orElse(new HashMap<>()));
		Trades = new HashMap<>(TradeComponent.TRADES_CODEC.parse(buf.getRegistryManager().getOps(NbtOps.INSTANCE), compound.get(TRADES_KEY)).result().orElse(new HashMap<>()));
	}
	
	public void write(RegistryByteBuf buf)
	{
		NbtCompound compound = new NbtCompound();
		PlayerDataComponent.DATA_CODEC.encodeStart(buf.getRegistryManager().getOps(NbtOps.INSTANCE), PlayerData).ifSuccess(element -> compound.put(PLAYER_DATA_KEY, element));
		TradeComponent.TRADES_CODEC.encodeStart(buf.getRegistryManager().getOps(NbtOps.INSTANCE), Trades).ifSuccess(element -> compound.put(TRADES_KEY, element));
		buf.writeNbt(compound);
	}
	
	public Map<UUID, PlayerData> getPlayerData()
	{
		return PlayerData;
	}
	
	public Map<Identifier, List<TradeEntry>> getTrades()
	{
		return Trades;
	}
	
	@Override
	public Id<? extends CustomPayload> getId()
	{
		return ID;
	}
}
