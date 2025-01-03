package kimit.rimor.client.registry;

import kimit.rimor.client.gui.trade.TradeScreen;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;

public class CommandRegistry
{
	public static void Initialize()
	{
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
		{
			dispatcher.register(ClientCommandManager.literal("trade").executes(context ->
			{
				MinecraftClient client = MinecraftClient.getInstance();
				client.send(() -> client.setScreen(new TradeScreen()));
				return 1;
			}));
		});
	}
}
