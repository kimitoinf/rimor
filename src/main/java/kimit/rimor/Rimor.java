package kimit.rimor;

import kimit.rimor.network.SyncRequestPayload;
import kimit.rimor.player.PlayerData;
import kimit.rimor.registry.CommandRegistry;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.scoreboard.Scoreboard;
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
		PayloadTypeRegistry.playC2S().register(SyncRequestPayload.ID, SyncRequestPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(SyncRequestPayload.ID, (payload, context) ->
		{
			Scoreboard scoreboard = context.player().getScoreboard();
			RimorComponents.PLAYER_DATA.get(scoreboard).setData(payload.getPlayerData());
			RimorComponents.PLAYER_DATA.sync(scoreboard);
			RimorComponents.TRADE.get(scoreboard).setTrades(payload.getTrades());
			RimorComponents.TRADE.sync(scoreboard);
		});
		ServerEntityEvents.ENTITY_LOAD.register((entity, world) ->
		{
			if (entity instanceof PlayerEntity player)
			{
				var trade = RimorComponents.TRADE.get(world.getScoreboard());
				for (int loop = 0; loop < 10; loop++)
				{
					if (Registries.ITEM.getId(Registries.ITEM.get(loop)).toString().equals("minecraft:air"))
						continue;
					for (int loop2 = 0; loop2 < 10; loop2++)
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