package kimit.rimor.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

public class TextRenderHelper
{
	public static void drawTextFromRight(DrawContext context, String text, int right, int centerY, int fontHeight, int color)
	{
		drawTextFromRight(context, Text.literal(text), right, centerY, fontHeight, color);
	}
	
	public static void drawTextFromRight(DrawContext context, Text text, int right, int centerY, int fontHeight, int color)
	{
		TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
		float scale = (float) fontHeight / renderer.fontHeight;
		context.getMatrices().push();
		context.getMatrices().scale(scale, scale, 1.0f);
		context.drawText(renderer, text, (int) ((right - renderer.getWidth(text)) / scale), (int) ((centerY - (float) fontHeight / 2) / scale), color, false);
		context.getMatrices().pop();
	}
	
	public static void drawTextCenterAlign(DrawContext context, String text, int centerX, int centerY, int fontHeight, int color)
	{
		drawTextCenterAlign(context, Text.literal(text), centerX, centerY, fontHeight, color);
	}
	
	public static void drawTextCenterAlign(DrawContext context, Text text, int centerX, int centerY, int fontHeight, int color)
	{
		TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
		float scale = (float) fontHeight / renderer.fontHeight;
		context.getMatrices().push();
		context.getMatrices().scale(scale, scale, 1.0f);
		context.drawText(renderer, (OrderedText) text, (int) ((centerX - (float) renderer.getWidth(text) / 2) / scale), (int) ((centerY - (float) fontHeight / 2) / scale), color, false);
		context.getMatrices().pop();
	}
	
	public static void drawTextFitWidth(DrawContext context, String text, int x, int centerY, int width, int color)
	{
		drawTextFitWidth(context, Text.literal(text), x, centerY, width, color);
	}
	
	public static void drawTextFitWidth(DrawContext context, Text text, int x, int centerY, int width, int color)
	{
		TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
		float scale = (float) width / renderer.getWidth(text);
		context.getMatrices().push();
		context.getMatrices().scale(scale, scale, 1.0f);
		context.drawText(renderer, text, (int) (x / scale), (int) ((centerY - (float) renderer.fontHeight * scale / 2) / scale), color, false);
		context.getMatrices().pop();
	}
}
