package kimit.rimor.registry;

import kimit.rimor.Rimor;
import kimit.rimor.screen.TradeOfferScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenHandlerRegistry
{
	public static final ScreenHandlerType<TradeOfferScreenHandler> TRADE_OFFER_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, Rimor.id("trade_offer"), new ScreenHandlerType<>(TradeOfferScreenHandler::new, FeatureSet.empty()));
	
	public static void init()
	{
	
	}
}
