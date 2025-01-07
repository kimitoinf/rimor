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
		builder.add("rimor.info", "확인");
		builder.add("rimor.trade.offer", "등록");
		builder.add("rimor.trade.lowest", "최저가");
		builder.add("rimor.trade.total", "총액");
		builder.add("rimor.trade.fee", "수수료");
		builder.add("rimor.trade.item", "아이템");
		builder.add("rimor.trade.seller", "판매자");
		builder.add("rimor.trade.amount", "수량");
		builder.add("rimor.trade.price", "가격");
		builder.add("rimor.trade.buy", "구매");
		builder.add("rimor.trade.cancel", "취소");
		builder.add("rimor.trade.complete", "거래가 완료되었습니다.");
		builder.add("rimor.trade.offer.complete", "등록이 완료되었습니다.");
		builder.add("rimor.trade.error.cash", "현금이 부족합니다.");
		builder.add("rimor.trade.error.storage", "창고 공간이 부족합니다.");
		builder.add("rimor.storage", "창고");
	}
}
