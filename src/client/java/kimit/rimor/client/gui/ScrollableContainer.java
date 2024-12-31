package kimit.rimor.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class ScrollableContainer extends EmptyWidget implements Drawable, Element
{
	public static final Identifier SCROLLER_TEXTURE = Identifier.ofVanilla("container/creative_inventory/scroller");
	public static final int SCROLLER_WIDTH = 12;
	public static final int SCROLLER_HEIGHT = 15;
	private boolean Focused;
	private int ScrollerX;
	private int ScrollerY;
	private int CurrentScroller;
	private int ScrollHeight;
	private int Scroll = 0;
	private final float ScrollSpeed;
	private final List<ClickableWidget> Children;
	private int ChildrenHeight;
	
	public ScrollableContainer(int x, int y, int width, int height, int scrollerX, int scrollerY, int scrollHeight, float scrollSpeed)
	{
		super(x, y, width, height);
		ScrollerX = scrollerX;
		ScrollerY = scrollerY;
		CurrentScroller = 0;
		ScrollHeight = scrollHeight;
		ScrollSpeed = scrollSpeed;
		Children = new ArrayList<>();
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta)
	{
		context.enableScissor(getX(), getY(), getX() + getWidth(), getY() + getHeight());
		context.getMatrices().push();
		context.getMatrices().translate(0, -Scroll, 0);
		
		for (ClickableWidget child : Children)
			child.render(context, mouseX, mouseY + Scroll, delta);
		
		context.getMatrices().pop();
		context.disableScissor();
		
		for (ClickableWidget child : Children)
		{
			if (child instanceof ClickableWidget)
			{
				TooltipWidget tooltip = (TooltipWidget) child;
				if (tooltip.isMouseTooltipOver(mouseX, mouseY + Scroll) && mouseY >= getY() && mouseY <= getY() + getHeight())
					context.drawTooltip(MinecraftClient.getInstance().textRenderer, tooltip.getTooltip(tooltip.getStack()), mouseX, mouseY, tooltip.getStack().get(DataComponentTypes.TOOLTIP_STYLE));
			}
		}
		
		context.drawGuiTexture(RenderLayer::getGuiTextured, SCROLLER_TEXTURE, ScrollerX, ScrollerY + CurrentScroller, SCROLLER_WIDTH, SCROLLER_HEIGHT);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		for (ClickableWidget child : Children)
			if (isMouseOver(mouseX, mouseY) && child.mouseClicked(mouseX, mouseY + Scroll, button))
				return true;
		return false;
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount)
	{
		if (isMouseOver(mouseX, mouseY) || isMouseOverOnScroller(mouseX, mouseY))
		{
			Scroll = (int) MathHelper.clamp(Scroll - ScrollSpeed * verticalAmount, 0, ChildrenHeight >= getHeight() ? ChildrenHeight - getHeight() : 0);
			CurrentScroller = (int) ((double) Scroll / (ChildrenHeight - getHeight()) * (ScrollHeight - SCROLLER_HEIGHT));
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean isMouseOver(double mouseX, double mouseY)
	{
		return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight();
	}
	
	public boolean isMouseOverOnScroller(double mouseX, double mouseY)
	{
		return mouseX >= ScrollerX && mouseX <= ScrollerX + SCROLLER_WIDTH && mouseY >= ScrollerY && mouseY <= ScrollerY + SCROLLER_HEIGHT;
	}
	
	@Override
	public void setFocused(boolean focused)
	{
		Focused = focused;
	}
	
	@Override
	public boolean isFocused()
	{
		return Focused;
	}
	
	@Override
	public ScreenRect getNavigationFocus()
	{
		return new ScreenRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	
	private void caculateChildrenHeight()
	{
		int minHeight = Children.isEmpty() ? 0 : Children.getFirst().getY();
		int maxHeight = 0;
		for (ClickableWidget loop : Children)
		{
			if (loop.getY() < minHeight)
				minHeight = loop.getY();
			if (loop.getY() + loop.getHeight() > maxHeight)
				maxHeight = loop.getY() + loop.getHeight();
		}
		ChildrenHeight = maxHeight + minHeight - 2 * getY();
	}
	
	public List<ClickableWidget> getChildren()
	{
		return Children;
	}
	
	public void addChild(ClickableWidget child)
	{
		Children.add(child);
		caculateChildrenHeight();
	}
	
	public void addChildren(List<ClickableWidget> children)
	{
		Children.addAll(children);
		caculateChildrenHeight();
	}
	
	public void clearChildren()
	{
		Children.clear();
		Scroll = 0;
		CurrentScroller = 0;
	}
	
	public int getScrollerX()
	{
		return ScrollerX;
	}
	
	public void setScrollerX(int scrollerX)
	{
		ScrollerX = scrollerX;
	}
	
	public int getScrollerY()
	{
		return ScrollerY;
	}
	
	public void setScrollerY(int scrollerY)
	{
		ScrollerY = scrollerY;
	}
	
	public int getScrollHeight()
	{
		return ScrollHeight;
	}
	
	public void setScrollHeight(int scrollHeight)
	{
		ScrollHeight = scrollHeight;
	}
}
