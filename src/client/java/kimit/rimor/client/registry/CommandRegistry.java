package kimit.rimor.client.registry;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import kimit.rimor.RimorComponents;
import kimit.rimor.client.gui.trade.TradeScreen;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;

public class CommandRegistry
{
	public static void init()
	{
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
		{
			MinecraftClient client = MinecraftClient.getInstance();
			dispatcher.register(ClientCommandManager.literal("trade").executes(context ->
			{
				client.send(() -> client.setScreen(new TradeScreen()));
				return 1;
			}));
			dispatcher.register(ClientCommandManager.literal("cash").then(ClientCommandManager.argument("value", IntegerArgumentType.integer(0)).executes(context ->
			{
				assert client.player != null;
				RimorComponents.PLAYER_DATA.get(client.player.getScoreboard()).getData().get(client.player.getUuid()).setCash(IntegerArgumentType.getInteger(context, "value"));
				RimorComponents.PLAYER_DATA.sync(client.player.getScoreboard());
				return 1;
			})));
		});
	}
}
