package kimit.rimor.client.gui.trade;

import kimit.rimor.Rimor;
import kimit.rimor.RimorComponents;
import kimit.rimor.client.gui.InfoOverlay;
import kimit.rimor.client.gui.OverlayContainer;
import kimit.rimor.client.gui.TextRenderHelper;
import kimit.rimor.network.SyncRequestPayload;
import kimit.rimor.player.PlayerData;
import kimit.rimor.screen.TradeOfferScreenHandler;
import kimit.rimor.trade.TradeEntry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.*;

public class TradeOfferScreen extends HandledScreen<TradeOfferScreenHandler>
{
	public static final Identifier BACKGROUND_TEXTURE = Rimor.id("trade_offer");
	public static final int BACKGROUND_WIDTH = 176;
	public static final int BACKGROUND_HEIGHT = 154;
	public static final int FEE_RATE = 10;
	public static final int PRICE_LIMIT = 12;
	private TextFieldWidget PriceField;
	private ItemStack Stack;
	private long Price;
	private OverlayContainer Overlay;
	
	public TradeOfferScreen(TradeOfferScreenHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
		Stack = ItemStack.EMPTY;
	}
	
	@Override
	protected void handledScreenTick()
	{
		super.handledScreenTick();
		ItemStack stack = handler.slots.getFirst().getStack();
		if (!ItemStack.areEqual(Stack, stack))
			Stack = stack;
	}
	
	@Override
	protected void init()
	{
		backgroundWidth = BACKGROUND_WIDTH;
		backgroundHeight = BACKGROUND_HEIGHT;
		super.init();
		assert client != null;
		ButtonWidget offer = ButtonWidget.builder(Text.translatable("rimor.trade.offer"), press ->
		{
			if (Price != 0 && !Stack.isEmpty())
			{
				PlayerEntity player = client.player;
				assert player != null;
				Scoreboard scoreboard = player.getScoreboard();
				PlayerData data = RimorComponents.PLAYER_DATA.get(scoreboard).getData().get(player.getUuid());
				if (getFee() > data.getCash())
				{
					Overlay = new InfoOverlay(width, height, 1, Text.translatable("rimor.trade.error.cash"));
					Overlay.setOpened(true);
				}
				else
				{
					RimorComponents.TRADE.get(scoreboard).addEntry(player.getUuid(), Stack.copy(), Price);
					ClientPlayNetworking.send(new SyncRequestPayload(RimorComponents.PLAYER_DATA.get(scoreboard).getData(), RimorComponents.TRADE.get(scoreboard).getTrades()));
					data.setCash(data.getCash() - getFee());
					assert client.interactionManager != null;
					client.interactionManager.clickButton(handler.syncId, 0);
					Overlay = new InfoOverlay(width, height, 1, Text.translatable("rimor.trade.offer.complete"));
					Overlay.setOpened(true);
				}
			}
		}).position(x + 16, y + 48).size(36, 15).build();
		PriceField = new TextFieldWidget(client.textRenderer, x + 97, y + 24, 54, 13, Text.empty());
		PriceField.setChangedListener(text ->
		{
			if (!text.matches("[0-9]*"))
				PriceField.setText(text.replaceAll("[^[0-9]+]", ""));
			else if (text.length() == PRICE_LIMIT)
				PriceField.setText(text.substring(0, PRICE_LIMIT - 1));
			try
			{
				Price = Long.parseLong(PriceField.getText());
			}
			catch (NumberFormatException e)
			{
				Price = 0;
			}
		});
		addDrawableChild(offer);
		addDrawableChild(PriceField);
	}
	
	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY)
	{
		context.drawGuiTexture(RenderLayer::getGuiTextured, BACKGROUND_TEXTURE, x, y, backgroundWidth, backgroundHeight);
	}
	
	@Override
	protected void drawForeground(DrawContext context, int mouseX, int mouseY)
	{
		context.drawText(this.textRenderer, this.title, this.titleX, this.titleY, 4210752, false);
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta)
	{
		drawBackground(context, delta, mouseX, mouseY);
		super.render(context, mouseX, mouseY, delta);
		drawMouseoverTooltip(context, mouseX, mouseY);
		
		TextRenderHelper.drawTextLeft(context, Text.translatable("rimor.trade.lowest"), x + 61, y + 15, 9, 0);
		TextRenderHelper.drawTextLeft(context, Text.translatable("rimor.trade.price"), x + 61, y + 31, 9, 0);
		TextRenderHelper.drawTextLeft(context, Text.translatable("rimor.trade.total"), x + 61, y + 47, 9, 0);
		TextRenderHelper.drawTextLeft(context, Text.translatable("rimor.trade.fee"), x + 61, y + 59, 9, 0);
		if (!Stack.isEmpty())
		{
			assert Objects.requireNonNull(client).player != null;
			assert client.player != null;
			List<TradeEntry> entries = RimorComponents.TRADE.get(client.player.getScoreboard()).getTrades().get(Registries.ITEM.getId(Stack.getItem()));
			TextRenderHelper.drawTextRightInWidth(context, entries == null ? "N/A" : String.format("%,d", entries.getFirst().price()), x + 150, y + 15, 54, 9, 0);
			
			long total = Stack.getCount() * Price;
			TextRenderHelper.drawTextRightInWidth(context, String.format("%,d", total), x + 150, y + 47, 54, 9, 0);
			TextRenderHelper.drawTextRightInWidth(context, String.format("%,d", getFee()), x + 150, y + 59, 54, 9, 0);
		}
		else
			TextRenderHelper.drawTextRightInWidth(context, "N/A", x + 150, y + 15, 54, 9, 0);
		
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
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	private long getFee()
	{
		return Stack.isEmpty() ? 0 : Stack.getCount() * Price * FEE_RATE / 100;
	}
}
