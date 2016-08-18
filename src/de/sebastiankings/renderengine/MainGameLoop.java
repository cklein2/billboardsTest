package de.sebastiankings.renderengine;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.system.libffi.Closure;

import de.sebastiankings.renderengine.bo.Inputs;
import de.sebastiankings.renderengine.bo.Szene;
import de.sebastiankings.renderengine.engine.DisplayManager;
import de.sebastiankings.renderengine.entities.BillboardFactory;
import de.sebastiankings.renderengine.entities.Billboards;
import de.sebastiankings.renderengine.entities.Camera;
import de.sebastiankings.renderengine.entities.Entity;
import de.sebastiankings.renderengine.entities.EntityFactory;
import de.sebastiankings.renderengine.entities.EntityType;
import de.sebastiankings.renderengine.entities.PointLight;
import de.sebastiankings.renderengine.shaders.BillboardShaderProgram;
import de.sebastiankings.renderengine.shaders.EntityShaderProgram;
import de.sebastiankings.renderengine.shaders.SkyboxShaderProgramm;
import de.sebastiankings.renderengine.texture.Texture;
import de.sebastiankings.renderengine.utils.LoaderUtils;
import de.sebastiankings.renderengine.utils.SkyboxUtils;

public class MainGameLoop {
	private static final Logger LOGGER = Logger.getLogger(MainGameLoop.class);
	@SuppressWarnings("unused")
	private static GLFWErrorCallback errorCallback;
	@SuppressWarnings("unused")
	private static GLFWKeyCallback keyCallback;
	@SuppressWarnings("unused")
	private static Closure debug;

	private static long windowId;
	private static Szene szene;

	public static List<Billboards> billboards = new ArrayList<Billboards>();

	public static void main(String[] args) {
		try {
			// Setup window
			init();

			// Create Szene f�r Entities
			List<Entity> entities = new ArrayList<Entity>();
			for (int i = 1; i < 5; i++) {
				Entity gumba = EntityFactory.createEntity(EntityType.GUMBA);
				Texture reflectionTexture = LoaderUtils.loadCubeMapTexture("res/skybox/landscape");
				gumba.setReflectionTexture(reflectionTexture);
				gumba.moveEntityGlobal(new Vector3f(4.0f * i, 0.0f * i, 4.0f * i));
				gumba.rotateY(30 * i);
				entities.add(gumba);
			}

			// List<Billboards> billboards = new ArrayList<Billboards>();
			// for (int i = 1; i < 5; i++) {
			// Billboards gumba =
			// BillboardFactory.createBillboard(EntityType.GUMBA);
			//
			// gumba.moveEntityGlobal(new Vector3f(3.8f*i, 0.0f * i, 4.0f ));
			// //gumba.rotateY(30);
			// billboards.add(gumba);
			// }

			List<PointLight> lights = new ArrayList<PointLight>();
			lights.add(
					new PointLight(new Vector3f(100.0f), new Vector3f(1.0f), new Vector3f(1.0f), new Vector3f(1.0f)));

			Inputs inputs = new Inputs();
			inputs.registerInputs(windowId);
			szene = new Szene(entities, billboards, lights, new Camera(), inputs);
			szene.setSkybox(SkyboxUtils.loadSkybox("res/skybox/test"));
			initShaderProgramms();

			LOGGER.info("Start GameLoop");
			long lastStartTime = System.currentTimeMillis() - 10;
			while (glfwWindowShouldClose(windowId) == GL_FALSE) {
				// setDeltatime
				long deltaTime = System.currentTimeMillis() - lastStartTime;
				lastStartTime = System.currentTimeMillis();
				// H
				handleInputs(deltaTime);
				render(deltaTime);

				DisplayManager.updateDisplay();
			}
			LOGGER.info("Ending Gameloop! Cleaning Up");
			cleanUp();
			LOGGER.info("Finished cleaning! Goodbye!");

		} catch (Exception e) {
			LOGGER.error("There was an error!", e);
			e.printStackTrace();
		} finally {

		}
	}

	private static void init() {
		LOGGER.info("Initialize Game");
		// Alle OGL-Settings laden
		windowId = DisplayManager.createDisplay();
		loadOpenGlSettings();
	}

	private static void initShaderProgramms() {
		szene.setEntityShader(new EntityShaderProgram("res/shaders/entity/vertexShader.glsl",
				"res/shaders/entity/fragmentShader.glsl"));
		// hier
		szene.setSkyboxShader(new SkyboxShaderProgramm("res/shaders/skybox/vertexShader.glsl",
				"res/shaders/skybox/fragmentShader.glsl"));
		// ende
		// szene.setBillboardShader(new
		// BillboardShaderProgram("res/shaders/billboard/vertexShader.glsl",
		// "res/shaders/billboard/fragmentShader.glsl"));
	}

	/**
	 * Method to Handle all user Inputs,
	 * 
	 * @param deltaTime
	 *            Timedifference to lastFrame
	 */
	private static void handleInputs(long deltaTime) {
		Inputs inputs = szene.getInputs();
		Camera cam = szene.getCamera();

		// Mousemovement
		if (inputs.getMouse().hasNewValues()) {
			cam.incrementPhi((float) inputs.getDeltaX() * -0.0003f);
			cam.incrementTheta((float) inputs.getDeltaY() * -0.0003f);
			inputs.getMouse().markRead();
		}

		if (inputs.keyPressed(GLFW_KEY_ESCAPE)) {
			glfwSetWindowShouldClose(DisplayManager.getWindow(), GL_TRUE);
		}

		if (inputs.keyPressed(GLFW_KEY_A)) {
			cam.moveLeft();
		}

		if (inputs.keyPressed(GLFW_KEY_D)) {
			cam.moveRight();

		}
		// CLEAR ROTATION IF NOT LEFT OR RIGHT
		if (inputs.keyPressed(GLFW_KEY_W)) {
			cam.moveForward();
		}
		if (inputs.keyPressed(GLFW_KEY_S)) {
			cam.moveBackward();
		}
		if (inputs.keyPressed(GLFW_KEY_SPACE)) {
		}
	}

	private static void render(long deltaTime) {

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		szene.getCamera().updateViewMatrix();
		szene.getSkybox().render(szene.getSkyboxShader(), szene.getCamera());
		for (Entity entity : szene.getEntities()) {
			entity.render(szene.getEntityShader(), szene.getCamera(), szene.getLights().get(0));
		}
		// for (Billboards billboard : szene.getBillboards()) {
		// testen f�r drehung von einem Billboard:

		// Vector3f cam=szene.getCamera().getCamPos();
		// Vector3f
		// billi=szene.getBillboards().get(0).getEntityState().getCurrentPosition();
		// //Vector3f up=new Vector3f(0,1,0);
		// float winkel=cam.angle(billi);
		// szene.getBillboards().get(0).getEntityState().setRotationY(winkel);
		// szene.getBillboards().get(0).render(szene.getBillboardShader(),
		// szene.getCamera(), szene.getLights().get(0));

	}

	private static void loadOpenGlSettings() {
		LOGGER.trace("Loading OGL-Settings");
		glClearColor(0.4f, 0.4f, 0.4f, 1);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_DEPTH_TEST);
	}

	private static void cleanUp() {
		szene.getEntityShader().cleanUp();
		DisplayManager.closeDisplay();
	}

}
