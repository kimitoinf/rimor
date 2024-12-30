package kimit.rimor.client.gui;

import kimit.rimor.Rimor;
import kimit.rimor.trade.TradeEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TradeEntryWidget extends ClickableWidget
{
	public static final Identifier BACKGROUND_TEXTURE = Rimor.id("trade_entry");
	public static final int BACKGROUND_WIDTH = 140;
	public static final int BACKGROUND_HEIGHT = 25;
	public static final int BUY_BUTTON_X = 106;
	public static final int BUY_BUTTON_Y = 3;
	public static final int BUY_BUTTON_WIDTH = 30;
	public static final int BUY_BUTTON_HEIGHT = 18;
	private final ButtonWidget BuyButton;
	private final TradeEntry Entry;
	private final ItemStack Item;
	
	public TradeEntryWidget(int x, int y, ItemStack item, TradeEntry entry)
	{
		super(x, y, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, Text.empty());
		BuyButton = ButtonWidget.builder(Text.translatable("rimor.trade.buy"), button -> {}).position(getX() + 106, getY() + 3).size(30, 18).build();
		Item = item;
		Entry = entry;
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
		context.drawItem(Item, getX() + 4, getY() + 4);
		context.drawText(MinecraftClient.getInstance().textRenderer, String.format("%,d", Entry.price()), getX() + 42, getY() + 8, 0, false);
		BuyButton.render(context, mouseX, mouseY, delta);
	}
	
	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder)
	{
	
	}
}
