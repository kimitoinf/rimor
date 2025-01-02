package kimit.rimor;

import kimit.rimor.player.PlayerData;
import kimit.rimor.player.PlayerDataComponent;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.ladysnake.cca.api.v3.component.ComponentProvider;
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
		ServerEntityEvents.ENTITY_LOAD.register((entity, world) ->
		{
			if (entity instanceof PlayerEntity player)
			{
				var trade = RimorComponents.TRADE.get(world.getScoreboard());
				for (int loop = 0; loop < 100; loop++)
				{
					if (Registries.ITEM.getId(Registries.ITEM.get(loop)).toString().equals("minecraft:air"))
						continue;
					for (int loop2 = 0; loop2 < 20; loop2++)
						trade.addEntry(player.getUuid(), new ItemStack(Registries.ITEM.get(loop), loop2 + 1), loop2 * 500);
				}
				
				Map<UUID, PlayerData> data = RimorComponents.PLAYER_DATA.get(world.getScoreboard()).getData();
				UUID uuid = player.getUuid();
				data.computeIfAbsent(uuid, key -> new PlayerData(player.getName().getString()));
				data.computeIfPresent(uuid, (key, playerData) -> new PlayerData(player.getName().getString(), playerData));
				RimorComponents.PLAYER_DATA.sync(world.getScoreboard());
			}
		});
	}
}