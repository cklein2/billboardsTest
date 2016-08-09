package de.sebastiankings.renderengine.entities;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class EntityState {

	private float rotationX;
	private float rotationY;
	private float rotationZ;

	private Vector3f currentPosition;
	private Vector3f currentNormal;

	private float scaleX = 1;
	private float scaleY = 1;
	private float scaleZ = 1;

	public EntityState() {
		// SET DEFAULT VALUES;
		this.setRotationX(0.0f);
		this.setRotationY(0.0f);
		this.rotationZ = 0.0f;
		this.currentPosition = new Vector3f(2.0f, 0.0f, 2.0f);
		this.currentNormal = new Vector3f(0.0f, 0.0f, 1.0f);
	}

	public Vector3f getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(Vector3f currentPosition) {
		this.currentPosition = currentPosition;
	}
	

	public float getRotationZ() {
		return rotationZ;
	}

	public void setRotationZ(float roationZ) {
		this.rotationZ = roationZ;
	}

	public void incrementRotationX(float rotationX) {
		this.setRotationX(this.getRotationX() + rotationX);
	}

	public void incrementRotationY(float rotationY) {
		this.setRotationY(this.getRotationY() + rotationY);
	}

	public void incrementRotationZ(float rotationZ) {
		this.rotationZ += rotationZ;
	}

	public float getRotationX() {
		return rotationX;
	}

	public void setRotationX(float rotationX) {
		this.rotationX = rotationX;
	}

	public float getRotationY() {
		return rotationY;
	}

	public void setRotationY(float rotationY) {
		this.rotationY = rotationY;
	}

	public void scale(float scale) {
		this.scaleX = scale;
		this.scaleY = scale;
		this.scaleZ = scale;
	}

	public float getScaleX() {
		return scaleX;
	}

	public float getScaleY() {
		return scaleY;
	}

	public float getScaleZ() {
		return scaleZ;
	}

	public Vector3f getCurrentNormal() {
		return currentNormal;
	}
	
	public void setCurrentNormal(Vector3f currentNormal) {
		this.currentNormal=currentNormal;
	}


}
