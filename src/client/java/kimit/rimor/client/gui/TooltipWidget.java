package kimit.rimor.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public interface TooltipWidget
{
	ItemStack getStack();
	
	boolean isMouseTooltipOver(int mouseX, int mouseY);
	
	default List<Text> getTooltip(ItemStack stack)
	{
		return Screen.getTooltipFromItem(MinecraftClient.getInstance(), stack);
	}
}
