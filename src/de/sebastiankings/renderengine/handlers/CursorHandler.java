package de.sebastiankings.renderengine.handlers;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class CursorHandler extends GLFWCursorPosCallback {

	private double x;
	private double y;

	private double deltaX;
	private double deltaY;

	private boolean updated = false;

	@Override
	public void invoke(long window, double xpos, double ypos) {
		deltaX = x - xpos;
		deltaY = y - ypos;
		x = xpos;
		y = ypos;
		updated = true;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public float getDeltaX() {
		return (float) deltaX;
	}

	public float getDeltaY() {
		return (float) deltaY;
	}

	public boolean hasNewValues() {
		return updated;
	}

	public void markRead() {
		updated = false;
	}
}
