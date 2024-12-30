package kimit.rimor;

import kimit.rimor.trade.TradeEntry;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Rimor implements ModInitializer
{
	public static final String MOD_ID = "rimor";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	public static Identifier id(String path)
	{
		return Identifier.of(MOD_ID, path);
	}

	@Override
	public void onInitialize()
	{
		ServerWorldEvents.LOAD.register((server, world) ->
		{
			LOGGER.info("Loading Rimor");
			var trade = RimorComponents.TRADE.get(server.getScoreboard());
			for (var loop : Registries.ITEM)
				trade.getTrades().put(Registries.ITEM.getId(loop), List.of(new TradeEntry(UUID.randomUUID(), 10, 10000), new TradeEntry(UUID.randomUUID(), 100, 20000)));
			RimorComponents.TRADE.sync(server.getScoreboard());
			LOGGER.info(String.valueOf(trade.getTrades().size()));
		});
	}
}