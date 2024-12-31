package kimit.rimor.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TradeItemButton extends ClickableWidget implements TooltipWidget
{
	public static final Identifier BACKGROUND_TEXTURE = Identifier.ofVanilla("recipe_book/slot_craftable");
	public static final int BACKGROUND_WIDTH = 25;
	public static final int BACKGROUND_HEIGHT = 25;
	private final TradeScreen Parent;
	private final ItemStack Item;
	
	public TradeItemButton(TradeScreen parent, int x, int y, ItemStack item)
	{
		super(x, y, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, Text.empty());
		Parent = parent;
		Item = item;
	}
	
	@Override
	public void onClick(double mouseX, double mouseY)
	{
		Parent.selectItem(Registries.ITEM.getId(Item.getItem()));
	}
	
	@Override
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta)
	{
		context.drawGuiTexture(RenderLayer::getGuiTextured, BACKGROUND_TEXTURE, getX(), getY(), getWidth(), getHeight());
		context.drawItem(Item, getX() + 4, getY() + 4);
	}
	
	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder)
	{
	
	}
	
	@Override
	public ItemStack getStack()
	{
		return Item;
	}
	
	@Override
	public boolean isMouseTooltipOver(int mouseX, int mouseY)
	{
		return isMouseOver(mouseX, mouseY);
	}
}
