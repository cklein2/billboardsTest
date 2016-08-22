package de.sebastiankings.renderengine.entities.types;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import de.sebastiankings.renderengine.entities.Camera;
import de.sebastiankings.renderengine.entities.Model;
import de.sebastiankings.renderengine.shaders.SkyboxShaderProgramm;
import de.sebastiankings.renderengine.texture.Texture;

public class Skybox {

	private Texture texture;
	private Model cube;

	public Skybox(Texture texture, Model model) {
		super();
		this.texture = texture;
		this.cube = model;
	}

	public Texture getTexture() {
		return texture;
	}

	public Model getModel() {
		return cube;
	}
	
	public void render(SkyboxShaderProgramm shader, Camera camera){
		//Kamera --> ViewMatrix
		shader.start();
		shader.loadProjectionMatrix(camera.getProjectionMatrix());
		shader.loadViewMatrix(camera);
		//binden, enable, activate Texture und binden
		glBindVertexArray(cube.getVaoID());
		glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, this.getTexture().getTextureID());
		//rendern
		GL11.glDrawArrays(GL_TRIANGLES,0, cube.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader.stop();
	}

}
