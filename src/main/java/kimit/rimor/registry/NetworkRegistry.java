package kimit.rimor.registry;

import kimit.rimor.RimorComponents;
import kimit.rimor.network.SyncRequestPayload;
import kimit.rimor.network.TradeOfferPayload;
import kimit.rimor.screen.TradeOfferScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;

public class NetworkRegistry
{
	public static void init()
	{
		PayloadTypeRegistry.playC2S().register(SyncRequestPayload.ID, SyncRequestPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(SyncRequestPayload.ID, (payload, context) ->
		{
			Scoreboard scoreboard = context.player().getScoreboard();
			RimorComponents.PLAYER_DATA.get(scoreboard).setData(payload.getPlayerData());
			RimorComponents.PLAYER_DATA.sync(scoreboard);
			RimorComponents.TRADE.get(scoreboard).setTrades(payload.getTrades());
			RimorComponents.TRADE.sync(scoreboard);
		});
		
		PayloadTypeRegistry.playC2S().register(TradeOfferPayload.ID, TradeOfferPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(TradeOfferPayload.ID, (payload, context) ->
		{
			context.player().openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, playerEntity) ->
					new TradeOfferScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(context.player().getWorld(), context.player().getBlockPos())), Text.translatable("rimor.trade.offer")));
		});
	}
}
