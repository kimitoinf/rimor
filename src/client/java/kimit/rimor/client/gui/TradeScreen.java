package kimit.rimor.client.gui;

import kimit.rimor.Rimor;
import kimit.rimor.RimorComponents;
import kimit.rimor.trade.TradeEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TradeScreen extends Screen
{
	public static final Identifier BACKGROUND_TEXTURE = Rimor.id("trade");
	public static final int BACKGROUND_WIDTH = 300;
	public static final int BACKGROUND_HEIGHT = 200;
	private ScrollableContainer ItemContainer;
	private ScrollableContainer TradeContainer;
	private TextFieldWidget SearchWidget;
	
	public TradeScreen()
	{
		super(Text.empty());
	}
	
	@Override
	protected void init()
	{
		super.init();
		ItemContainer = new ScrollableContainer(getLeft() + 8, getTop() + 27, 106, 165, getLeft() + 116, getTop() + 27, 165, 16.0f);
		List<ClickableWidget> trades = new ArrayList<>();
		if (TradeContainer != null)
			trades = TradeContainer.getChildren();
		TradeContainer = new ScrollableContainer(getLeft() + 134, getTop() + 8, 146, 184, getLeft() + 281, getTop() + 8, 184, 16.0f);
		TradeContainer.addChildren(trades);
		SearchWidget = new TextFieldWidget(textRenderer, getLeft() + 26, getTop() + 10, 99, 14, Text.empty());
		search();
	}
	
	public void selectItem(Identifier item)
	{
		List<ClickableWidget> results = new ArrayList<>();
		List<TradeEntry> entries = RimorComponents.TRADE.get(Objects.requireNonNull(MinecraftClient.getInstance().getServer()).getScoreboard()).getTrades().get(item);
		for (int loop = 0; loop < entries.size(); loop++)
			results.add(new TradeEntryWidget(4, 4 + loop * 25, new ItemStack(Registries.ITEM.get(item)), entries.get(loop)));
		TradeContainer.clearChildren();
		TradeContainer.addChildren(results);
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta)
	{
		super.render(context, mouseX, mouseY, delta);
		context.drawGuiTexture(RenderLayer::getGuiTextured, BACKGROUND_TEXTURE, getLeft(), getTop(), BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
		ItemContainer.render(context, mouseX, mouseY, delta);
		TradeContainer.render(context, mouseX, mouseY, delta);
		SearchWidget.render(context, mouseX, mouseY, delta);
	}
	
	public void search()
	{
		List<ClickableWidget> results = new ArrayList<>();
		List<Identifier> keys = RimorComponents.TRADE.get(Objects.requireNonNull(MinecraftClient.getInstance().getServer()).getScoreboard()).getTrades().keySet().stream().toList();
		int pos = 0;
		for (Identifier key : keys)
		{
			Item item = Registries.ITEM.get(key);
			if (item.getName().getString().toLowerCase(Locale.ROOT).contains(SearchWidget.getText().toLowerCase(Locale.ROOT)))
			{
				results.add(new TradeItemButton(this, 3 + pos % 4 * 25, 3 + pos / 4 * 25, new ItemStack(item)));
				pos++;
			}
		}
		ItemContainer.clearChildren();
		ItemContainer.addChildren(results);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers)
	{
		if (SearchWidget.keyPressed(keyCode, scanCode, modifiers))
		{
			search();
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean charTyped(char chr, int modifiers)
	{
		if (SearchWidget.charTyped(chr, modifiers))
		{
			search();
			return true;
		}
		return super.charTyped(chr, modifiers);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		if (ItemContainer.mouseClicked(mouseX, mouseY, button) || TradeContainer.mouseClicked(mouseX, mouseY, button))
			return true;
		if (SearchWidget.mouseClicked(mouseX, mouseY, button))
		{
			SearchWidget.setFocused(true);
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount)
	{
		if (ItemContainer.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount) || TradeContainer.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount))
			return true;
		return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}
	
	private int getLeft()
	{
		return (this.width - BACKGROUND_WIDTH) / 2;
	}
	
	private int getTop()
	{
		return (this.height - BACKGROUND_HEIGHT) / 2;
	}
}
