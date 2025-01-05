package kimit.rimor.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class OverlayContainer extends EmptyWidget implements Drawable, Element
{
	protected final int OVERLAY_DEPTH = 1000;
	private final Identifier BackgroundTexture;
	private boolean Opened;
	private boolean Focused;
	private final List<ClickableWidget> Children;
	protected final int Depth;
	
	public OverlayContainer(int x, int y, int width, int height, int depth, Identifier texture)
	{
		super(x, y, width, height);
		Depth = depth;
		BackgroundTexture = texture;
		Children = new ArrayList<>();
		Opened = false;
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta)
	{
		if (Opened)
		{
			context.getMatrices().push();
			context.getMatrices().translate(0, 0, OVERLAY_DEPTH * Depth);
			context.drawGuiTexture(RenderLayer::getGuiTextured, BackgroundTexture, getX(), getY(), getWidth(), getHeight());
			for (ClickableWidget child : Children)
				child.render(context, mouseX, mouseY, delta);
			context.getMatrices().pop();
		}
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers)
	{
		if (Opened)
		{
			if (keyCode == GLFW.GLFW_KEY_ESCAPE)
			{
				Opened = false;
				return true;
			}
			for (ClickableWidget child : Children)
				if (child.keyPressed(keyCode, scanCode, modifiers))
					return true;
		}
		return false;
	}
	
	@Override
	public boolean charTyped(char chr, int modifiers)
	{
		if (Opened)
			for (ClickableWidget child : Children)
				if (child.charTyped(chr, modifiers))
					return true;
		return false;
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		if (Opened)
			for (ClickableWidget child : Children)
				if (child.mouseClicked(mouseX, mouseY, button))
					return true;
		return false;
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount)
	{
		if (Opened)
			for (ClickableWidget child : Children)
				if (child.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount))
					return true;
		return false;
	}
	
	public boolean isOpened()
	{
		return Opened;
	}
	
	public void setOpened(boolean opened)
	{
		Opened = opened;
	}
	
	@Override
	public boolean isFocused()
	{
		return Focused;
	}
	
	@Override
	public void setFocused(boolean focused)
	{
		Focused = focused;
	}
	
	@Override
	public ScreenRect getNavigationFocus()
	{
		return new ScreenRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	
	public List<ClickableWidget> getChildren()
	{
		return Children;
	}
	
	public void addChild(ClickableWidget child)
	{
		Children.add(child);
	}
	
	public void addChildren(List<ClickableWidget> children)
	{
		Children.addAll(children);
	}
	
	public void clearChildren()
	{
		Children.clear();
	}
}
