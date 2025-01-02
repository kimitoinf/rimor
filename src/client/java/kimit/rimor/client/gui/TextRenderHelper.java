package kimit.rimor.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class TextRenderHelper
{
	public static void drawTextLeft(DrawContext context, String text, int left, int centerY, int fontHeight, int color)
	{
		drawTextLeft(context, Text.literal(text), left, centerY, fontHeight, color);
	}
	
	public static void drawTextLeft(DrawContext context, Text text, int left, int centerY, int fontHeight, int color)
	{
		TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
		float scale = (float) fontHeight / renderer.fontHeight;
		context.getMatrices().push();
		context.getMatrices().scale(scale, scale, 1.0f);
		context.drawText(renderer, text, (int) ((left + 1) / scale), (int) ((centerY - (float) fontHeight / 2 + 1) / scale), color, false);
		context.getMatrices().pop();
	}
	
	public static void drawTextRight(DrawContext context, String text, int right, int centerY, int fontHeight, int color)
	{
		drawTextRight(context, Text.literal(text), right, centerY, fontHeight, color);
	}
	
	public static void drawTextRight(DrawContext context, Text text, int right, int centerY, int fontHeight, int color)
	{
		TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
		float scale = (float) fontHeight / renderer.fontHeight;
		context.getMatrices().push();
		context.getMatrices().scale(scale, scale, 1.0f);
		context.drawText(renderer, text, (int) ((right - renderer.getWidth(text) + 1) / scale), (int) ((centerY - (float) fontHeight / 2 + 1) / scale), color, false);
		context.getMatrices().pop();
	}
	
	public static void drawTextCenter(DrawContext context, String text, int centerX, int centerY, int fontHeight, int color)
	{
		drawTextCenter(context, Text.literal(text), centerX, centerY, fontHeight, color);
	}
	
	public static void drawTextCenter(DrawContext context, Text text, int centerX, int centerY, int fontHeight, int color)
	{
		TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
		float scale = (float) fontHeight / renderer.fontHeight;
		context.getMatrices().push();
		context.getMatrices().scale(scale, scale, 1.0f);
		context.drawText(renderer, text, (int) ((centerX - (float) renderer.getWidth(text) / 2 + 1) / scale), (int) ((centerY - (float) fontHeight / 2 + 1) / scale), color, false);
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
		context.drawText(renderer, text, (int) ((x + 1) / scale), (int) ((centerY - (float) renderer.fontHeight * scale / 2 + 1) / scale), color, false);
		context.getMatrices().pop();
	}
	
	public static void drawTextCenterInWidth(DrawContext context, String text, int centerX, int centerY, int width, int fontHeight, int color)
	{
		drawTextCenterInWidth(context, Text.literal(text), centerX, centerY, width, fontHeight, color);
	}
	
	public static void drawTextCenterInWidth(DrawContext context, Text text, int centerX, int centerY, int width, int fontHeight, int color)
	{
		TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
		float scale = (float) fontHeight / renderer.fontHeight;
		float textWidth = renderer.getWidth(text) * scale;
		context.getMatrices().push();
		if (textWidth <= width)
			context.getMatrices().scale(scale, scale, 1.0f);
		else
		{
			scale = (float) width / renderer.getWidth(text);
			context.getMatrices().scale(scale, scale, 1.0f);
		}
		context.drawText(renderer, text, (int) ((centerX - textWidth / 2 + 1) / scale), (int) ((centerY - (float) fontHeight / 2 + 1) / scale), color, false);
		context.getMatrices().pop();
	}
	
	public static void drawTextRightInWidth(DrawContext context, String text, int right, int centerY, int width, int fontHeight, int color)
	{
		drawTextRightInWidth(context, Text.literal(text), right, centerY, width, fontHeight, color);
	}
	
	public static void drawTextRightInWidth(DrawContext context, Text text, int right, int centerY, int width, int fontHeight, int color)
	{
		TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
		float scale = (float) fontHeight / renderer.fontHeight;
		float textWidth = renderer.getWidth(text) * scale;
		context.getMatrices().push();
		if (textWidth <= width)
			context.getMatrices().scale(scale, scale, 1.0f);
		else
		{
			scale = (float) width / renderer.getWidth(text);
			context.getMatrices().scale(scale, scale, 1.0f);
		}
		context.drawText(renderer, text, (int) ((right - renderer.getWidth(text) + 1) / scale), (int) ((centerY - (float) fontHeight / 2 + 1) / scale), color, false);
		context.getMatrices().pop();
	}
}
