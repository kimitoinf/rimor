package kimit.rimor.network;

import kimit.rimor.Rimor;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public class TradeOfferPayload implements CustomPayload
{
	public static final TradeOfferPayload INSTANCE = new TradeOfferPayload();
	public static final CustomPayload.Id<TradeOfferPayload> ID = new Id<>(Rimor.id("trade_offer"));
	public static final PacketCodec<PacketByteBuf, TradeOfferPayload> CODEC = PacketCodec.unit(INSTANCE);
	
	@Override
	public Id<? extends CustomPayload> getId()
	{
		return ID;
	}
}
