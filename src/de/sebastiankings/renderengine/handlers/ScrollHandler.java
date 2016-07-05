package de.sebastiankings.renderengine.handlers;

import org.lwjgl.glfw.GLFWScrollCallback;

public class ScrollHandler extends GLFWScrollCallback {

	private double scrollAmount;

	@Override
	public void invoke(long arg0, double arg1, double arg2) {
		this.scrollAmount = arg2;
	}

	public double getScrollAmount() {
		return scrollAmount;
	}

}
