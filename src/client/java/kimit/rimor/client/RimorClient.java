package kimit.rimor.client;

import kimit.rimor.client.gui.trade.TradeOfferScreen;
import kimit.rimor.client.registry.CommandRegistry;
import kimit.rimor.registry.ScreenHandlerRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class RimorClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		CommandRegistry.init();
		HandledScreens.register(ScreenHandlerRegistry.TRADE_OFFER_SCREEN_HANDLER, TradeOfferScreen::new);
	}
}