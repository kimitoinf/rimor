package kimit.rimor.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class KoreanLangProvider extends FabricLanguageProvider
{
	public KoreanLangProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registry)
	{
		super(output, "ko_kr", registry);
	}
	
	@Override
	public void generateTranslations(RegistryWrapper.WrapperLookup registry, TranslationBuilder builder)
	{
		builder.add("rimor.trade.add", "추가");
		builder.add("rimor.trade.item", "아이템");
		builder.add("rimor.trade.seller", "판매자");
		builder.add("rimor.trade.amount", "수량");
		builder.add("rimor.trade.price", "가격");
		builder.add("rimor.trade.buy", "구매");
		builder.add("rimor.trade.cancel", "취소");
	}
}
