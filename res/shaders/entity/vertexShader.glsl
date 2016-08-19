#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 normal;

out vec3 reflectionCoords;
out vec3 pos_eye;
out vec3 n_eye;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

uniform vec3 lightPos;

void main(){
    vec4 worldPosition = modelMatrix * vec4(position, 1.0);
    pos_eye=vec3(viewMatrix*modelMatrix*vec4(position, 1.0));
    n_eye=vec3(viewMatrix*modelMatrix*vec4(normal, 1.0));
    
	gl_Position = projectionMatrix * viewMatrix  * worldPosition;
	
    
}