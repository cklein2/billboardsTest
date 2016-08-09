package de.sebastiankings.renderengine.entities;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import de.sebastiankings.renderengine.shaders.BillboardShaderProgram;

public class Billboards extends Entity{

	private Vector3f normaleBillboard;
	
	public Vector3f getNormaleBillboard() {
		return normaleBillboard;
	}
	public Billboards clone() {
		Entity newEntity=new Entity(this.type, this.model, this.diffuseTexture, this.dimensions);
		return new Billboards(newEntity);
	}

	public Billboards(Entity entitiy) {
		super(entitiy.getType(), entitiy.getModel(), entitiy.getDimensions());
		this.normaleBillboard = new Vector3f(0,0,1);
	}
	
	//hier wo es aufgerufen wird vorher die Matrix mit der Kamera berechnen
	
	public void render(BillboardShaderProgram shader, Camera camera, PointLight light) {
		shader.start();
		shader.loadBillboardVector(this.getNormaleBillboard());
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
		// NormaleBillboards
		glEnableVertexAttribArray(7);

		//glBindTexture(GL_TEXTURE_2D, this.getTexture().getTextureID());
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
		glDisableVertexAttribArray(7);
		// CLEANUP VERTEX ARRAY
		glBindVertexArray(0);
		shader.stop();
	}
	
}
