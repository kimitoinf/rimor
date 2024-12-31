package kimit.rimor;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
//		ServerEntityEvents.ENTITY_LOAD.register((entity, world) ->
//		{
//			if (entity instanceof PlayerEntity)
//			{
//				LOGGER.info("Loading Rimor");
//				var trade = RimorComponents.TRADE.get(world.getScoreboard());
//				for (int loop = 0; loop < 100; loop++)
//				{
//					if (Registries.ITEM.getId(Registries.ITEM.get(loop)).toString().equals("minecraft:air"))
//						continue;
//					for (int loop2 = 0; loop2 < 20; loop2++)
//						trade.addEntry(UUID.randomUUID(), new ItemStack(Registries.ITEM.get(loop), loop2 + 1), loop2 * 500);
//				}
//				LOGGER.info(String.valueOf(trade.getTrades().size()));
//			}
//		});
		
		ServerWorldEvents.LOAD.register((server, world) ->
		{
			if (world.getRegistryKey() == World.OVERWORLD)
				RimorComponents.TRADE.sync(server.getScoreboard());
		});
	}
}