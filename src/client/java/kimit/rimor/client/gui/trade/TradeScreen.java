package kimit.rimor.client.gui.trade;

import kimit.rimor.Rimor;
import kimit.rimor.RimorComponents;
import kimit.rimor.client.gui.OverlayContainer;
import kimit.rimor.client.gui.ScrollableContainer;
import kimit.rimor.client.gui.TextRenderHelper;
import kimit.rimor.network.TradeOfferPayload;
import kimit.rimor.trade.TradeEntry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextIconButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TradeScreen extends Screen
{
	public static final Identifier BACKGROUND_TEXTURE = Rimor.id("trade");
	public static final int BACKGROUND_WIDTH = 400;
	public static final int BACKGROUND_HEIGHT = 220;
	private final Scoreboard Provider;
	private ScrollableContainer ItemContainer;
	private ScrollableContainer TradeContainer;
	private OverlayContainer Overlay;
	private TextFieldWidget SearchField;
	private TextIconButtonWidget OfferButton;
	private TextIconButtonWidget RefreshButton;
	private Identifier SelectedItem = null;
	private long PlayerCash;
	private String Search = "";
	
	public TradeScreen()
	{
		super(Text.empty());
		Provider = Objects.requireNonNull(MinecraftClient.getInstance().player).getScoreboard();
	}
	
	@Override
	protected void init()
	{
		super.init();
		ItemContainer = new ScrollableContainer(getLeft() + 8, getTop() + 47, 106, 165, getLeft() + 116, getTop() + 47, 165, 16.0f);
		TradeContainer = new ScrollableContainer(getLeft() + 134, getTop() + 47, 246, 165, getLeft() + 381, getTop() + 47, 165, 16.0f);
		SearchField = new TextFieldWidget(textRenderer, getLeft() + 26, getTop() + 30, 99, 14, Text.empty());
		OfferButton = TextIconButtonWidget.builder(Text.translatable("rimor.trade.offer"), press -> ClientPlayNetworking.send(TradeOfferPayload.INSTANCE), false).dimension(50, 18).texture(Rimor.id("trade_offer_button"), 16, 16).build();
		OfferButton.setPosition(getLeft() + 323, getTop() + 6);
		RefreshButton = TextIconButtonWidget.builder(Text.literal("Refresh"), press -> init(), true).dimension(18, 18).texture(Rimor.id("trade_refresh_button"), 16, 16).build();
		RefreshButton.setPosition(getLeft() + 376, getTop() + 6);
		search();
		if (SelectedItem != null)
			selectItem(SelectedItem);
		assert MinecraftClient.getInstance().player != null;
		PlayerCash = RimorComponents.PLAYER_DATA.get(Provider).getData().get(MinecraftClient.getInstance().player.getUuid()).getCash();
		if (Overlay != null && Overlay.isOpened())
			Overlay = null;
	}
	
	public void selectItem(Identifier item)
	{
		SelectedItem = item;
		List<ClickableWidget> results = new ArrayList<>();
		List<TradeEntry> entries = RimorComponents.TRADE.get(Provider).getTrades().get(item);
		if (entries != null)
		{
			for (int loop = 0; loop < entries.size(); loop++)
				results.add(new TradeEntryWidget(this, TradeContainer.getX() + 4, TradeContainer.getY() + 4 + loop * 25, entries.get(loop)));
			TradeContainer.clearChildren();
			TradeContainer.addChildren(results);
		}
		else
		{
			SelectedItem = null;
			TradeContainer.clearChildren();
		}
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta)
	{
		super.render(context, mouseX, mouseY, delta);
		context.drawGuiTexture(RenderLayer::getGuiTextured, BACKGROUND_TEXTURE, getLeft(), getTop(), BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
		TextRenderHelper.drawTextCenter(context, Text.translatable("rimor.trade.item"), getLeft() + 149, getTop() + 37, 9, 0);
		TextRenderHelper.drawTextCenter(context, Text.translatable("rimor.trade.seller"), getLeft() + 188, getTop() + 37, 9, 0);
		TextRenderHelper.drawTextCenter(context, Text.translatable("rimor.trade.amount"), getLeft() + 233, getTop() + 37, 9, 0);
		TextRenderHelper.drawTextCenter(context, Text.translatable("rimor.trade.price"), getLeft() + 295, getTop() + 37, 9, 0);
		TextRenderHelper.drawTextCenter(context, Text.translatable("rimor.trade.buy"), getLeft() + 358, getTop() + 37, 9, 0);
		TextRenderHelper.drawTextLeft(context, String.format("%,d", PlayerCash), getLeft() + 25, getTop() + 15, 9, 0);
		OfferButton.render(context, mouseX, mouseY, delta);
		RefreshButton.render(context, mouseX, mouseY, delta);
		ItemContainer.render(context, mouseX, mouseY, delta);
		TradeContainer.render(context, mouseX, mouseY, delta);
		SearchField.render(context, mouseX, mouseY, delta);
		if (Overlay != null && Overlay.isOpened())
			Overlay.render(context, mouseX, mouseY, delta);
	}
	
	public void search()
	{
		List<ClickableWidget> results = new ArrayList<>();
		List<Identifier> keys = RimorComponents.TRADE.get(Provider).getTrades().keySet().stream().toList();
		int pos = 0;
		for (Identifier key : keys)
		{
			Item item = Registries.ITEM.get(key);
			if (item.getName().getString().toLowerCase(Locale.ROOT).contains(Search.toLowerCase(Locale.ROOT)))
			{
				results.add(new TradeItemButton(this, ItemContainer.getX() + 3 + pos % 4 * 25, ItemContainer.getY() + 3 + pos / 4 * 25, new ItemStack(item)));
				pos++;
			}
		}
		ItemContainer.clearChildren();
		ItemContainer.addChildren(results);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers)
	{
		if (Overlay != null && Overlay.isOpened())
			return Overlay.keyPressed(keyCode, scanCode, modifiers);
		if (SearchField.keyPressed(keyCode, scanCode, modifiers))
		{
			Search = SearchField.getText();
			search();
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean charTyped(char chr, int modifiers)
	{
		if (Overlay != null && Overlay.isOpened())
			return Overlay.charTyped(chr, modifiers);
		if (SearchField.charTyped(chr, modifiers))
		{
			Search = SearchField.getText();
			search();
			return true;
		}
		return super.charTyped(chr, modifiers);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		if (Overlay != null && Overlay.isOpened())
			return Overlay.mouseClicked(mouseX, mouseY, button);
		if (ItemContainer.mouseClicked(mouseX, mouseY, button) || TradeContainer.mouseClicked(mouseX, mouseY, button) || OfferButton.mouseClicked(mouseX, mouseY, button) || RefreshButton.mouseClicked(mouseX, mouseY, button))
			return true;
		if (SearchField.mouseClicked(mouseX, mouseY, button))
		{
			SearchField.setFocused(true);
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount)
	{
		if (Overlay != null && Overlay.isOpened())
			return Overlay.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
		if (ItemContainer.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount) || TradeContainer.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount))
			return true;
		return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}
	
	public void setOverlay(OverlayContainer overlay)
	{
		Overlay = overlay;
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
