package de.sebastiankings.renderengine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.system.libffi.Closure;

import de.sebastiankings.renderengine.bo.Inputs;
import de.sebastiankings.renderengine.bo.Szene;
import de.sebastiankings.renderengine.engine.DisplayManager;
import de.sebastiankings.renderengine.entities.Camera;
import de.sebastiankings.renderengine.entities.Entity;
import de.sebastiankings.renderengine.entities.BaseEntity;
import de.sebastiankings.renderengine.entities.BillboardFactory;
import de.sebastiankings.renderengine.entities.Billboards;
import de.sebastiankings.renderengine.entities.EntityFactory;
import de.sebastiankings.renderengine.entities.EntityType;
import de.sebastiankings.renderengine.entities.PointLight;
import de.sebastiankings.renderengine.shaders.EntityShaderProgram;
import de.sebastiankings.renderengine.shaders.BillboardShaderProgram;


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

			// Create Szene für Entities
			List<Entity> entities = new ArrayList<Entity>();
//			for (int i = 1; i < 5; i++) {
//				Entity gumba = EntityFactory.createEntity(EntityType.GUMBA);
//				gumba.moveEntityGlobal(new Vector3f(4.0f * i, 0.0f * i, 4.0f * i));
//				gumba.rotateY(30 * i);
//				entities.add(gumba);
//			}
			
			//List<Billboards> billboards = new ArrayList<Billboards>();
			for (int i = 1; i < 5; i++) {
				Billboards gumba = BillboardFactory.createBillboard(EntityType.GUMBA);
				gumba.moveEntityGlobal(new Vector3f(3.8f*i, 0.0f * i, 4.0f ));
				//gumba.rotateY(30); 
				billboards.add(gumba);
			}

			List<PointLight> lights = new ArrayList<PointLight>();
			lights.add(new PointLight(new Vector3f(100.0f), new Vector3f(1.0f), new Vector3f(1.0f), new Vector3f(1.0f)));

			Inputs inputs = new Inputs();
			inputs.registerInputs(windowId);
			szene = new Szene(entities, billboards, lights, new Camera(), inputs);
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
		szene.setEntityShader(new EntityShaderProgram("res/shaders/entity/vertexShader.glsl", "res/shaders/entity/fragmentShader.glsl"));
		szene.setBillboardShader(new BillboardShaderProgram("res/shaders/billboard/vertexShader.glsl", "res/shaders/billboard/fragmentShader.glsl"));
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
		for (Entity entity : szene.getEntities()) {
			entity.render(szene.getEntityShader(), szene.getCamera(), szene.getLights().get(0));
		}
		//for (Billboards billboard : szene.getBillboards()) {
			//Berechnung der neuen Matrix --> Drehung anhand BillboardNormale	

//			Vector3f look = szene.getCamera().getCamPos().sub(billboard.getEntityState().getCurrentPosition());
//			look.normalize();
//			Vector3f right = new Vector3f();
//			Vector3f cameraUp=new Vector3f(0.0f,1.0f,0.0f);
//					cameraUp.cross(look, right);
//			Vector3f up2 =new Vector3f(0.0f,0.0f,0.0f);
//					look.cross(right, up2);
//			Matrix4f transform = new Matrix4f();
//			transform.m00 = right.x;
//			transform.m01 = right.y;
//			transform.m02 = right.z;
//			transform.m03 = 0;
//			transform.m10 = up2.x;
//			transform.m11 = up2.y;
//			transform.m12 = up2.z;
//			transform.m13 = 0;
//			transform.m20 = look.x;
//			transform.m21 = look.y;
//			transform.m22 = look.z;
//			transform.m23 = 0;
//			transform.m30 = billboard.getEntityState().getCurrentPosition().x;
//			transform.m31 = billboard.getEntityState().getCurrentPosition().y;
//			transform.m32 = billboard.getEntityState().getCurrentPosition().z;
//			transform.m33 = 1;
//
//			billboard.setModelMatrix(transform);
			
			
			//billboard.render(szene.getBillboardShader(), szene.getCamera(), szene.getLights().get(0));
		//}
			//testen für drehung von einem Billboard:
			//szene.getBillboards().get(1).render(szene.getBillboardShader(), szene.getCamera(), szene.getLights().get(0));
			Vector3f cam=szene.getCamera().getCamPos();
			Billboards billi=szene.getBillboards().get(0);
			Vector3f up=new Vector3f(0,1,0);
			Vector3f view = new Vector3f(cam.x - billi.getEntityState().getCurrentPosition().x, cam.y - billi.getEntityState().getCurrentPosition().y, cam.z - billi.getEntityState().getCurrentPosition().z);
			  Vector3f xzp = new Vector3f(view.x, 0, view.z);
			  xzp.normalize();
			  view.normalize();
			  float cosp = view.dot(xzp);
			  Matrix4f neue= new Matrix4f(xzp.z, xzp.x * -view.y, xzp.x * cosp, 0, 0, cosp, view.y, 0, -xzp.x, xzp.z * -view.y, xzp.z * cosp,0,0,0,0,1);
			Matrix4f totalNeue=szene.getBillboards().get(0).getModelMatrix().lookAlong(cam, up, neue);
			
			szene.getBillboards().get(0).setModelMatrix(totalNeue);
			szene.getBillboards().get(0).render(szene.getBillboardShader(), szene.getCamera(), szene.getLights().get(0));
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
