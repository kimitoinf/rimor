package kimit.rimor.client.gui.trade;

import kimit.rimor.Rimor;
import kimit.rimor.client.gui.OverlayContainer;
import kimit.rimor.client.gui.TextRenderHelper;
import kimit.rimor.trade.TradeEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TradeBuyOverlay extends OverlayContainer
{
	public static final Identifier OVERLAY_BUY_TEXTURE = Rimor.id("trade_overlay_buy");
	public static final int OVERLAY_BUY_WIDTH = 100;
	public static final int OVERLAY_BUY_HEIGHT = 100;
	private final TradeEntry Entry;
	private final TextFieldWidget AmountWidget;
	private int Amount;
	
	public TradeBuyOverlay(int parentWidth, int parentHeight, TradeEntry entry)
	{
		super((parentWidth - OVERLAY_BUY_WIDTH) / 2, (parentHeight - OVERLAY_BUY_HEIGHT) / 2, OVERLAY_BUY_WIDTH, OVERLAY_BUY_HEIGHT, OVERLAY_BUY_TEXTURE);
		Entry = entry;
		TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
		AmountWidget = new TextFieldWidget(renderer, getX() + 9, getY() + 46, 36, 13, Text.empty());
		AmountWidget.setChangedListener(text ->
		{
			try
			{
				int amount = Integer.parseInt(AmountWidget.getText());
				if (amount > Entry.stack().getCount())
					throw new NumberFormatException();
				Amount = amount;
			}
			catch (NumberFormatException e)
			{
				if (!text.isEmpty())
					AmountWidget.setText(text.substring(0, text.length() - 1));
			}
		});
		addChild(AmountWidget);
		addChild(new TextWidget(getX() + 6, getY() + 6, 88, 9, Text.translatable("rimor.trade.buy"), renderer));
		addChild(ButtonWidget.builder(Text.translatable("rimor.trade.buy"), press -> {}).position(getX() + 6, getY() + 79).size(42, 15).build());
		addChild(ButtonWidget.builder(Text.translatable("rimor.trade.cancel"), press -> setOpened(false)).position(getX() + 52, getY() + 79).size(42, 15).build());
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta)
	{
		super.render(context, mouseX, mouseY, delta);
		context.getMatrices().push();
		context.getMatrices().translate(0, 0, 1000);
		context.drawGuiTexture(RenderLayer::getGuiTextured, Identifier.ofVanilla("recipe_book/slot_craftable"), getX() + 9, getY() + 18, 25, 25);
		context.drawGuiTexture(RenderLayer::getGuiTextured, Rimor.id("gold"), getX() + 80, getY() + 26, 11, 8);
		context.drawGuiTexture(RenderLayer::getGuiTextured, Rimor.id("gold"), getX() + 80, getY() + 65, 11, 8);
		context.drawGuiTexture(RenderLayer::getGuiTextured, Rimor.id("trade_amount"), getX() + 45, getY() + 46, 10, 13);
		context.drawItem(Entry.stack(), getX() + 13, getY() + 22);
		TextRenderHelper.drawTextCenterInWidth(context, Text.literal(String.valueOf(Entry.stack().getCount())), getX() + 73, getY() + 53, 36, 9, 0);
		TextRenderHelper.drawTextRightInWidth(context, String.format("%,d", Entry.price()), getX() + 76, getY() + 30, 40, 9, 0);
		TextRenderHelper.drawTextRightInWidth(context, String.format("%,d", Amount * Entry.price()), getX() + 76, getY() + 69, 71, 9, 0);
		if (mouseX >= getX() + 9 && mouseX <= getX() + 33 && mouseY >= getY() + 18 && mouseY <= getY() + 42)
			context.drawTooltip(MinecraftClient.getInstance().textRenderer, Screen.getTooltipFromItem(MinecraftClient.getInstance(), Entry.stack()), mouseX, mouseY, Entry.stack().get(DataComponentTypes.TOOLTIP_STYLE));
		context.getMatrices().pop();
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		if (AmountWidget.mouseClicked(mouseX, mouseY, button))
		{
			AmountWidget.setFocused(true);
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
}
