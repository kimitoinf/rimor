package kimit.rimor.client.gui;

import kimit.rimor.Rimor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class InfoOverlay extends OverlayContainer
{
	public static final Identifier BACKGROUND_TEXTURE = Rimor.id("info");
	public static final int BACKGROUND_WIDTH = 100;
	public static final int BACKGROUND_HEIGHT = 50;
	private final Text Message;
	
	public InfoOverlay(int windowWidth, int windowHeight, int depth, Text message)
	{
		super((windowWidth - BACKGROUND_WIDTH) / 2, (windowHeight - BACKGROUND_HEIGHT) / 2, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, depth, BACKGROUND_TEXTURE);
		Message = message;
		addChild(ButtonWidget.builder(Text.translatable("rimor.info"), press -> setOpened(false)).position(getX() + 30, getY() + 31).size(40, 13).build());
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta)
	{
		super.render(context, mouseX, mouseY, delta);
		context.getMatrices().push();
		context.getMatrices().translate(0, 0, OVERLAY_DEPTH * Depth);
		TextRenderHelper.drawTextCenterInWidth(context, Message, getX() + 50, getY() + 17, 88, 9, 0);
		context.getMatrices().pop();
	}
}
