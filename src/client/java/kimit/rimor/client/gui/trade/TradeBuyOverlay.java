package kimit.rimor.client.gui.trade;

import kimit.rimor.Rimor;
import kimit.rimor.RimorComponents;
import kimit.rimor.client.gui.InfoOverlay;
import kimit.rimor.client.gui.OverlayContainer;
import kimit.rimor.client.gui.TextRenderHelper;
import kimit.rimor.network.SyncRequestPayload;
import kimit.rimor.player.PlayerData;
import kimit.rimor.trade.TradeEntry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TradeBuyOverlay extends OverlayContainer
{
	public static final Identifier OVERLAY_BUY_TEXTURE = Rimor.id("trade_overlay_buy");
	public static final int OVERLAY_BUY_WIDTH = 100;
	public static final int OVERLAY_BUY_HEIGHT = 100;
	private final TradeScreen Screen;
	private final TradeEntry Entry;
	private final TextFieldWidget AmountWidget;
	private int Amount;
	private OverlayContainer Overlay;
	
	public TradeBuyOverlay(TradeScreen screen, int windowWidth, int windowHeight, TradeEntry entry)
	{
		super((windowWidth - OVERLAY_BUY_WIDTH) / 2, (windowHeight - OVERLAY_BUY_HEIGHT) / 2, OVERLAY_BUY_WIDTH, OVERLAY_BUY_HEIGHT, 1, OVERLAY_BUY_TEXTURE);
		Screen = screen;
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
				else
					Amount = 0;
			}
		});
		addChild(AmountWidget);
		addChild(new TextWidget(getX() + 6, getY() + 6, 88, 9, Text.translatable("rimor.trade.buy"), renderer));
		addChild(ButtonWidget.builder(Text.translatable("rimor.trade.buy"), press ->
		{
			if (Amount != 0)
			{
				PlayerEntity player = Objects.requireNonNull(MinecraftClient.getInstance().player);
				Scoreboard scoreboard = player.getScoreboard();
				PlayerData data = RimorComponents.PLAYER_DATA.get(scoreboard).getData().get(player.getUuid());
				if (Amount * Entry.price() > data.getCash())
				{
					Overlay = new InfoOverlay(windowWidth, windowHeight, 2, Text.translatable("rimor.trade.error.cash"));
					Overlay.setOpened(true);
				}
				else if (data.getStorage().size() == PlayerData.STORAGE_SLOT_PER_LINE * data.getStorageRow())
				{
					Overlay = new InfoOverlay(windowWidth, windowHeight, 2, Text.translatable("rimor.trade.error.storage"));
					Overlay.setOpened(true);
				}
				else
				{
					List<ItemStack> storage = new ArrayList<>(data.getStorage());
					Identifier id = Registries.ITEM.getId(Entry.stack().getItem());
					storage.add(Entry.stack().split(Amount));
					data.setStorage(storage);
					data.setCash(data.getCash() - Amount * Entry.price());
					if (Entry.stack().getCount() == 0)
					{
						List<TradeEntry> entries = RimorComponents.TRADE.get(scoreboard).getTrades().get(id);
						entries.remove(Entry);
						if (entries.isEmpty())
							RimorComponents.TRADE.get(scoreboard).getTrades().remove(id);
					}
					ClientPlayNetworking.send(new SyncRequestPayload(RimorComponents.PLAYER_DATA.get(scoreboard).getData(), RimorComponents.TRADE.get(scoreboard).getTrades()));
					setOpened(false);
					Screen.init();
					OverlayContainer complete = new InfoOverlay(windowWidth, windowHeight, 1, Text.translatable("rimor.trade.complete"));
					Screen.setOverlay(complete);
					complete.setOpened(true);
				}
			}
		}).position(getX() + 6, getY() + 79).size(42, 15).build());
		addChild(ButtonWidget.builder(Text.translatable("rimor.trade.cancel"), press -> setOpened(false)).position(getX() + 52, getY() + 79).size(42, 15).build());
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta)
	{
		super.render(context, mouseX, mouseY, delta);
		context.getMatrices().push();
		context.getMatrices().translate(0, 0, OVERLAY_DEPTH * Depth);
		context.drawGuiTexture(RenderLayer::getGuiTextured, Identifier.ofVanilla("recipe_book/slot_craftable"), getX() + 9, getY() + 18, 25, 25);
		context.drawGuiTexture(RenderLayer::getGuiTextured, Rimor.id("gold"), getX() + 80, getY() + 26, 11, 8);
		context.drawGuiTexture(RenderLayer::getGuiTextured, Rimor.id("gold"), getX() + 80, getY() + 65, 11, 8);
		context.drawGuiTexture(RenderLayer::getGuiTextured, Rimor.id("trade_amount"), getX() + 45, getY() + 46, 10, 13);
		context.drawItem(Entry.stack(), getX() + 13, getY() + 22);
		TextRenderHelper.drawTextCenterInWidth(context, Text.literal(String.valueOf(Entry.stack().getCount())), getX() + 73, getY() + 53, 36, 9, 0);
		TextRenderHelper.drawTextRightInWidth(context, String.format("%,d", Entry.price()), getX() + 76, getY() + 30, 40, 9, 0);
		TextRenderHelper.drawTextRightInWidth(context, String.format("%,d", Amount * Entry.price()), getX() + 76, getY() + 69, 71, 9, 0);
		if (mouseX >= getX() + 9 && mouseX <= getX() + 33 && mouseY >= getY() + 18 && mouseY <= getY() + 42)
			context.drawTooltip(MinecraftClient.getInstance().textRenderer, net.minecraft.client.gui.screen.Screen.getTooltipFromItem(MinecraftClient.getInstance(), Entry.stack()), mouseX, mouseY, Entry.stack().get(DataComponentTypes.TOOLTIP_STYLE));
		context.getMatrices().pop();
		if (Overlay != null && Overlay.isOpened())
			Overlay.render(context, mouseX, mouseY, delta);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers)
	{
		if (Overlay != null && Overlay.isOpened())
			return Overlay.keyPressed(keyCode, scanCode, modifiers);
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean charTyped(char chr, int modifiers)
	{
		if (Overlay != null && Overlay.isOpened())
			return Overlay.charTyped(chr, modifiers);
		return super.charTyped(chr, modifiers);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		if (Overlay != null && Overlay.isOpened())
			return Overlay.mouseClicked(mouseX, mouseY, button);
		if (AmountWidget.mouseClicked(mouseX, mouseY, button))
		{
			AmountWidget.setFocused(true);
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
}
