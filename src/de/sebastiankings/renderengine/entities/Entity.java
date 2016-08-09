package de.sebastiankings.renderengine.entities;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import de.sebastiankings.renderengine.shaders.EntityShaderProgram;
import de.sebastiankings.renderengine.texture.Texture;

public class Entity extends BaseEntity {

	protected Texture diffuseTexture;

	public Entity(EntityType type, Model model, EntityDimensions dimensions) {
		super(type, model, new Matrix4f(), dimensions);
	}

	public Entity(EntityType type, Model model, Matrix4f modelMatrix, EntityDimensions dimensions) {
		super(type, model, modelMatrix, dimensions);
	}

	protected Entity(EntityType type, Model model, Texture texture, EntityDimensions dimensions) {
		super(type, model, new Matrix4f(), dimensions);
		this.diffuseTexture = texture;
	}

	public Texture getTexture() {
		return diffuseTexture;
	}

	public void setTexture(Texture texture) {
		this.diffuseTexture = texture;
	}

	public Entity clone() {
		return new Entity(this.type, this.model, this.diffuseTexture, this.dimensions);
	}
	
	@Override
	public void render(EntityShaderProgram shader, Camera camera, PointLight light) {
		shader.start();
		shader.loadTexture();
		shader.loadLight(light);
		shader.loadViewMatrix(camera.getViewMatrix());
		shader.loadProjectionMatrix(camera.getProjectionMatrix());

		// bind VAO and activate VBOs //
		Model model = this.getModel();
		glBindVertexArray(model.getVaoID());
		// Activate VAO Data
		// VERTICES
		glEnableVertexAttribArray(0);
		// NORMALS
		glEnableVertexAttribArray(1);
		// DIFFUSEMAP
		glEnableVertexAttribArray(2);
		// EMISSION
		glEnableVertexAttribArray(3);
		// AMBIENT
		glEnableVertexAttribArray(4);
		// SPECULAR
		glEnableVertexAttribArray(5);
		// SHININESS
		glEnableVertexAttribArray(6);

		glBindTexture(GL_TEXTURE_2D, this.getTexture().getTextureID());
		shader.loadModelMatrix(this.getModelMatrix());

		glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);

		// CLEANUP VERTEX ARRAY ATTRIBUTES
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glDisableVertexAttribArray(4);
		glDisableVertexAttribArray(5);
		glDisableVertexAttribArray(6);
		// CLEANUP VERTEX ARRAY
		glBindVertexArray(0);
		shader.stop();
	}

	public void moveEntityGlobal(Vector3f move) {
		this.entityState.getCurrentPosition().add(move);
		this.entityState.getCurrentNormal().add(move);
		updateModelMatrix();
	}

	public void rotateX(float rotation) {
		this.entityState.incrementRotationX(rotation);
		updateModelMatrix();
	}

	public void rotateY(float rotation) {
		this.entityState.incrementRotationY(rotation);
		updateModelMatrix();
	}

	public void rotateZ(float rotation) {
		this.entityState.incrementRotationZ(rotation);
		updateModelMatrix();
	}

	public void scale(float scale) {
		this.entityState.scale(scale);
		updateModelMatrix();
	}

	protected void updateModelMatrix() {
		Matrix4f mm = new Matrix4f();
		Vector3f position = new Vector3f(entityState.getCurrentPosition());
		mm.translate(position);
		mm.scale(this.entityState.getScaleX(), this.entityState.getScaleY(), this.entityState.getScaleZ());
		mm.rotateXYZ(this.entityState.getRotationX(), this.entityState.getRotationY(), this.entityState.getRotationZ());
		this.modelMatrix = mm;
		
	}

}
