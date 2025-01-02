package kimit.rimor;

import kimit.rimor.player.PlayerDataComponent;
import kimit.rimor.trade.TradeComponent;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentInitializer;

public class RimorComponents implements ScoreboardComponentInitializer
{
	public static final ComponentKey<PlayerDataComponent> PLAYER_DATA = ComponentRegistry.getOrCreate(Rimor.id("player_data"), PlayerDataComponent.class);
	public static final ComponentKey<TradeComponent> TRADE = ComponentRegistry.getOrCreate(Rimor.id("trade"), TradeComponent.class);
	
	@Override
	public void registerScoreboardComponentFactories(ScoreboardComponentFactoryRegistry registry)
	{
		registry.registerScoreboardComponent(PLAYER_DATA, PlayerDataComponent::new);
		registry.registerScoreboardComponent(TRADE, TradeComponent::new);
	}
}
