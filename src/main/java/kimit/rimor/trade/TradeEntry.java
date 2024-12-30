package kimit.rimor.trade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Uuids;

import java.util.UUID;

public record TradeEntry(UUID seller, int amount, int price)
{
	public static final Codec<TradeEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Uuids.CODEC.fieldOf("seller").forGetter(TradeEntry::seller),
			Codec.INT.fieldOf("amount").forGetter(TradeEntry::amount),
			Codec.INT.fieldOf("price").forGetter(TradeEntry::price)
	).apply(instance, TradeEntry::new));
	
	@Override
	public String toString()
	{
		return "TradeEntry{" +
				"seller=" + seller +
				", amount=" + amount +
				", price=" + price +
				'}';
	}
}
