package kimit.rimor.registry;

import kimit.rimor.RimorComponents;
import kimit.rimor.player.PlayerData;
import kimit.rimor.screen.StorageScreenHandler;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class CommandRegistry
{
	public static void init()
	{
		CommandRegistrationCallback.EVENT.register((dispatcher, access, environment) ->
		{
			dispatcher.register(CommandManager.literal("storage").executes(context ->
			{
				ServerPlayerEntity player = context.getSource().getPlayer();
				if (player != null)
				{
					ScreenHandlerFactory factory;
					PlayerData data = RimorComponents.PLAYER_DATA.get(player.getServerWorld().getScoreboard()).getData().get(player.getUuid());
					SimpleInventory inventory = new SimpleInventory(9 * data.getStorageRow());
					for (ItemStack stack : data.getStorage())
						inventory.addStack(stack);
					factory = ((syncId, playerInventory, playerEntity) -> new StorageScreenHandler(
							switch (data.getStorageRow())
							{
								case 2 -> ScreenHandlerType.GENERIC_9X2;
								case 3 -> ScreenHandlerType.GENERIC_9X3;
								case 4 -> ScreenHandlerType.GENERIC_9X4;
								case 5 -> ScreenHandlerType.GENERIC_9X5;
								case 6 -> ScreenHandlerType.GENERIC_9X6;
								default -> ScreenHandlerType.GENERIC_9X1;
							}, syncId, playerInventory, inventory, data.getStorageRow()));
					player.openHandledScreen(new SimpleNamedScreenHandlerFactory(factory, Text.translatable("rimor.storage")));
				}
				return 1;
			}));
		});
	}
}
