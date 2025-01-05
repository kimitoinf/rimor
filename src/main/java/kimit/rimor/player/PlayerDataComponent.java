package kimit.rimor.player;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Uuids;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataComponent implements AutoSyncedComponent
{
	public static final Codec<Map<UUID, PlayerData>> DATA_CODEC = Codec.unboundedMap(Uuids.CODEC, PlayerData.CODEC);
	public static final String DATA_KEY = "data";
	private final Scoreboard Provider;
	private Map<UUID, PlayerData> Data = new HashMap<>();
	
	public PlayerDataComponent(Scoreboard provider, MinecraftServer server)
	{
		Provider = provider;
	}
	
	@Override
	public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries)
	{
		Data = new HashMap<>(DATA_CODEC.parse(registries.getOps(NbtOps.INSTANCE), nbt.get(DATA_KEY)).result().orElse(new HashMap<>()));
	}
	
	@Override
	public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries)
	{
		DATA_CODEC.encodeStart(registries.getOps(NbtOps.INSTANCE), Data).ifSuccess(element -> nbt.put(DATA_KEY, element));
	}
	
	public Map<UUID, PlayerData> getData()
	{
		return Data;
	}
	
	public void setData(Map<UUID, PlayerData> data)
	{
		Data = data;
	}
}
