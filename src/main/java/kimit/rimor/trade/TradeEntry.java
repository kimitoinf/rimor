package kimit.rimor.trade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Uuids;

import java.util.UUID;

public record TradeEntry(ItemStack stack, UUID seller, int price)
{
	public static final Codec<TradeEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ItemStack.CODEC.fieldOf("stack").forGetter(TradeEntry::stack),
			Uuids.CODEC.fieldOf("seller").forGetter(TradeEntry::seller),
			Codec.INT.fieldOf("price").forGetter(TradeEntry::price)
	).apply(instance, TradeEntry::new));
}
