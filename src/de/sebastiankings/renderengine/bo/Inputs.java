package de.sebastiankings.renderengine.bo;

import static org.lwjgl.glfw.GLFW.*;

import org.apache.log4j.Logger;

import de.sebastiankings.renderengine.handlers.CursorHandler;
import de.sebastiankings.renderengine.handlers.KeyboardHandler;
import de.sebastiankings.renderengine.handlers.ScrollHandler;

public class Inputs {

	private static final Logger LOGGER = Logger.getLogger(Inputs.class);

	private KeyboardHandler keyboard;
	private CursorHandler mouse;
	private ScrollHandler scroll;

	public Inputs() {
		this.keyboard = new KeyboardHandler();
		this.mouse = new CursorHandler();
		this.scroll = new ScrollHandler();
	}

	public boolean keyPressed(int key) {
		return keyboard.iskeyPressed(key);
	}

	public CursorHandler getMouse() {
		return mouse;
	}

	public double getDeltaX() {
		return mouse.getDeltaX();
	}

	public double getDeltaY() {
		return mouse.getDeltaY();
	}

	public double getScrollAmount() {
		return scroll.getScrollAmount();
	}

	public boolean registerInputs(long windowId) {
		LOGGER.debug("Registering Inputs");
		try {
			glfwSetKeyCallback(windowId, keyboard);
			glfwSetCursorPosCallback(windowId, mouse);
			glfwSetScrollCallback(windowId, scroll);
			LOGGER.debug("Done");
		} catch (Exception e) {
			LOGGER.error("Cant register inputhandlers", e);
			return false;
		}

		return true;
	}

	public void cleanUp() {
		keyboard.release();
		mouse.release();
		scroll.release();
	}

}
