package kimit.rimor.client.gui;

import kimit.rimor.Rimor;
import kimit.rimor.trade.TradeEntry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TradeEntryWidget extends ClickableWidget implements TooltipWidget
{
	public static final Identifier BACKGROUND_TEXTURE = Rimor.id("trade_entry");
	public static final int BACKGROUND_WIDTH = 240;
	public static final int BACKGROUND_HEIGHT = 25;
	private final ButtonWidget BuyButton;
	private final TradeEntry Entry;
	
	public TradeEntryWidget(int x, int y, TradeEntry entry)
	{
		super(x, y, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, Text.empty());
		Entry = entry;
		BuyButton = ButtonWidget.builder(Text.translatable("rimor.trade.buy"), button -> {Rimor.LOGGER.info(String.valueOf(Entry.price()));}).position(getX() + 206, getY() + 3).size(30, 18).build();
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		return BuyButton.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta)
	{
		context.drawGuiTexture(RenderLayer::getGuiTextured, BACKGROUND_TEXTURE, getX(), getY(), getWidth(), getHeight());
		context.drawItem(Entry.stack(), getX() + 4, getY() + 4);
		TextRenderHelper.drawTextFromRight(context, Entry.stack().getCount() + " EA", getX() + 124, getY() + 13, 9, 0);
		TextRenderHelper.drawTextFromRight(context, String.format("%,d", Entry.price()), getX() + 191, getY() + 13, 9, 0);
		BuyButton.render(context, mouseX, mouseY, delta);
	}
	
	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder)
	{
	
	}
	
	@Override
	public ItemStack getStack()
	{
		return Entry.stack();
	}
	
	@Override
	public boolean isMouseTooltipOver(int mouseX, int mouseY)
	{
		return mouseX >= getX() && mouseX <= getX() + 25 && mouseY >= getY() && mouseY <= getY() + 25;
	}
}
