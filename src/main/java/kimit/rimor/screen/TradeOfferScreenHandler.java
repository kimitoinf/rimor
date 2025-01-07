package kimit.rimor.screen;

import kimit.rimor.registry.ScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

public class TradeOfferScreenHandler extends ScreenHandler
{
	private final Inventory Inventory;
	private final ScreenHandlerContext Context;
	
	public TradeOfferScreenHandler(int syncId, PlayerInventory playerInventory)
	{
		this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
	}
	
	public TradeOfferScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context)
	{
		super(ScreenHandlerRegistry.TRADE_OFFER_SCREEN_HANDLER, syncId);
		Context = context;
		Inventory = new SimpleInventory(1);
		addSlot(new Slot(Inventory, 0, 26, 18));
		addPlayerSlots(playerInventory, 8, 72);
	}
	
	@Override
	public boolean onButtonClick(PlayerEntity player, int id)
	{
		if (id == 0)
		{
			Context.run((world, pos) -> slots.getFirst().setStack(ItemStack.EMPTY));
			return true;
		}
		else
			return false;
	}
	
	@Override
	public void onClosed(PlayerEntity player)
	{
		super.onClosed(player);
		Context.run((world, pos) -> dropInventory(player, Inventory));
	}
	
	@Override
	public ItemStack quickMove(PlayerEntity player, int slot)
	{
		ItemStack stack = ItemStack.EMPTY;
		Slot slot2 = slots.get(slot);
		if (slot2.hasStack())
		{
			ItemStack stack2 = slot2.getStack();
			stack = stack2.copy();
			if (slot == 0)
			{
				if (!insertItem(stack2, Inventory.size(), slots.size(), true))
					return ItemStack.EMPTY;
			}
			else if (!insertItem(stack2, 0, Inventory.size(), false))
				return ItemStack.EMPTY;
			
			if (stack2.isEmpty())
				slot2.setStack(ItemStack.EMPTY);
			else
				slot2.markDirty();
		}
		return stack;
	}
	
	@Override
	public boolean canUse(PlayerEntity player)
	{
		return Inventory.canPlayerUse(player);
	}
}
