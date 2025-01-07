package kimit.rimor;

import kimit.rimor.player.PlayerData;
import kimit.rimor.registry.CommandRegistry;
import kimit.rimor.registry.NetworkRegistry;
import kimit.rimor.registry.ScreenHandlerRegistry;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
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
		CommandRegistry.init();
		ScreenHandlerRegistry.init();
		NetworkRegistry.init();
		
		ServerEntityEvents.ENTITY_LOAD.register((entity, world) ->
		{
			if (entity instanceof PlayerEntity player)
			{
				Map<UUID, PlayerData> data = RimorComponents.PLAYER_DATA.get(world.getScoreboard()).getData();
				UUID uuid = player.getUuid();
				data.computeIfAbsent(uuid, key -> new PlayerData(player.getName().getString()));
				data.computeIfPresent(uuid, (key, playerData) -> new PlayerData(player.getName().getString(), playerData));
				RimorComponents.PLAYER_DATA.sync(world.getScoreboard());
			}
		});
	}
}