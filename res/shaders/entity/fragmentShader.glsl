#version 330 core

in vec3 pos_eye;
in vec3 n_eye;
uniform samplerCube textureReflection;
uniform mat4 viewMatrix;
out vec4 out_Color;

void main(void){
	vec3 incident_eye=normalize(pos_eye);
	vec3 normal=normalize(n_eye);
	
	//float ratio = 1.0 /2.18;
	//vec3 refracted = refract(incident_eye, normal, ratio);
	//refracted = vec3 (inverse (viewMatrix) * vec4 (refracted, 0.0));
	
	vec3 reflected=reflect(incident_eye, normal);
	reflected=vec3(inverse(viewMatrix)*vec4(reflected, 0.0));
	out_Color=texture(textureReflection, reflected);
    }