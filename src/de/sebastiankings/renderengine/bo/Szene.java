package de.sebastiankings.renderengine.bo;

import java.util.List;

import de.sebastiankings.renderengine.entities.BaseEntity;
import de.sebastiankings.renderengine.entities.Billboards;
import de.sebastiankings.renderengine.entities.Camera;
import de.sebastiankings.renderengine.entities.Entity;
import de.sebastiankings.renderengine.entities.PointLight;
import de.sebastiankings.renderengine.entities.types.Skybox;
import de.sebastiankings.renderengine.shaders.BillboardShaderProgram;
import de.sebastiankings.renderengine.shaders.EntityShaderProgram;
import de.sebastiankings.renderengine.shaders.SkyboxShaderProgramm;

public class Szene {

	private Inputs inputs;
	private Camera camera;

	private List<PointLight> lights;
	private List<Entity> entities;
	private List<Billboards> billboards;
	
	
	private EntityShaderProgram entityShader;
	private BillboardShaderProgram billboardShader;
	private SkyboxShaderProgramm skyboxShader;
	private Skybox skybox;
	
	public Szene(List<Entity> entities, List<Billboards> billboards, List<PointLight> lights, Camera camera, Inputs inputs) {
		this.setEntities(entities);
		this.setBillboards(billboards);
		this.setLights(lights);
		this.setCamera(camera);
		this.setInputs(inputs);
	}

	
public BillboardShaderProgram getBillboardShader(){
	return billboardShader;
}
	
	public EntityShaderProgram getEntityShader() {
		return entityShader;
	}

	public void setBillboardShader(BillboardShaderProgram billboardShader) {
		this.billboardShader=billboardShader;
	}
	public void setEntityShader(EntityShaderProgram entityShader) {
		this.entityShader = entityShader;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public Inputs getInputs() {
		return inputs;
	}

	public void setInputs(Inputs inputs) {
		this.inputs = inputs;
	}

	public List<PointLight> getLights() {
		return lights;
	}

	public void setLights(List<PointLight> lights) {
		this.lights = lights;
	}

	public List<Entity> getEntities() {
		return entities;
	}
	public List<Billboards> getBillboards() {
		return billboards;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
	public void setBillboards(List<Billboards> billboards) {
		this.billboards = billboards;
	}

	public SkyboxShaderProgramm getSkyboxShader(){	
		return skyboxShader;
	}

	public void setSkyboxShader(SkyboxShaderProgramm skyboxShader){	
		this.skyboxShader=skyboxShader;
	}
	
	public Skybox getSkybox(){
		return skybox;
	}
	public void setSkybox(Skybox skybox){
		this.skybox=skybox;
	}
	
}
