package kimit.rimor;

import kimit.rimor.trade.TradeComponent;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentInitializer;

public class RimorComponents implements ScoreboardComponentInitializer
{
	public static final ComponentKey<TradeComponent> TRADE = ComponentRegistry.getOrCreate(Identifier.of(Rimor.MOD_ID, "trade"), TradeComponent.class);
	
	@Override
	public void registerScoreboardComponentFactories(ScoreboardComponentFactoryRegistry registry)
	{
		registry.registerScoreboardComponent(TRADE, TradeComponent::new);
	}
}
