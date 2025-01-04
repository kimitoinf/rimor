package kimit.rimor.screen;

import kimit.rimor.RimorComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class StorageScreenHandler extends GenericContainerScreenHandler
{
	public StorageScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory, int rows)
	{
		super(type, syncId, playerInventory, inventory, rows);
	}
	
	@Override
	public void onClosed(PlayerEntity player)
	{
		super.onClosed(player);
		RimorComponents.PLAYER_DATA.get(player.getScoreboard()).getData().get(player.getUuid()).setStorage(((SimpleInventory) getInventory()).clearToList());
		RimorComponents.PLAYER_DATA.sync(player.getScoreboard());
	}
}
