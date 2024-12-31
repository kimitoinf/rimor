package kimit.rimor.trade;

import com.mojang.serialization.Codec;
import kimit.rimor.Rimor;
import kimit.rimor.RimorComponents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.*;

public class TradeComponent implements AutoSyncedComponent
{
	public static final Codec<Map<Identifier, List<TradeEntry>>> TRADES_CODEC = Codec.unboundedMap(Identifier.CODEC, TradeEntry.CODEC.listOf().xmap(LinkedList::new, Collections::unmodifiableList));
	private static final String TRADES_KEY = "trades";
	private final Scoreboard Provider;
	private Map<Identifier, List<TradeEntry>> Trades = new HashMap<>();
	
	public TradeComponent(Scoreboard scoreboard, MinecraftServer server)
	{
		Provider = scoreboard;
	}
	
	@Override
	public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries)
	{
		Trades = TRADES_CODEC.parse(registries.getOps(NbtOps.INSTANCE), nbt.get(TRADES_KEY)).result().orElse(new HashMap<>());
	}
	
	@Override
	public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries)
	{
		TRADES_CODEC.encodeStart(registries.getOps(NbtOps.INSTANCE), Trades).ifSuccess(nbtElement -> nbt.put(TRADES_KEY, nbtElement)).ifError(error -> Rimor.LOGGER.error(error.message()));
	}
	
	public void addEntry(UUID seller, ItemStack stack, int price)
	{
		Identifier item = stack.getRegistryEntry().getKey().get().getValue();
		List<TradeEntry> entries = Trades.get(item);
		if (entries == null)
		{
			LinkedList<TradeEntry> list = new LinkedList<>();
			list.add(new TradeEntry(stack, seller, price));
			Trades.put(item, list);
		}
		else
		{
			entries.add(new TradeEntry(stack, seller, price));
			entries.sort(Comparator.comparingInt(TradeEntry::price));
		}
		RimorComponents.TRADE.sync(Provider);
	}
	
	public Map<Identifier, List<TradeEntry>> getTrades()
	{
		return Trades;
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<Identifier, List<TradeEntry>> loop : Trades.entrySet())
			for (TradeEntry entry : loop.getValue())
				builder.append(loop.getKey()).append(": ").append(entry).append("\n");
		return builder.toString();
	}
}
