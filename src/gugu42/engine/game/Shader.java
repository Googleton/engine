package gugu42.engine.game;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;

import java.util.HashMap;

public class Shader {

	private int program;
	private HashMap<String, Integer> uniforms;

	/**
	 * Constructor, no parameters
	 */
	public Shader() {
		program = glCreateProgram();
		uniforms = new HashMap<String, Integer>();

		if (program == 0) {
			System.err
					.println("Shader creation failed : Could not find valid memory location in constructor");
			System.exit(1);
		}
	}

	/**
	 * Binds the program, like an init method, no parameters
	 */
	public void bind() {
		glUseProgram(program);
	}

	/**
	 * Used in child class
	 * 
	 * @param worldMatrix
	 * @param projectedMatrix
	 * @param material
	 */
	public void updateUniforms(Matrix4f worldMatrix, Matrix4f projectedMatrix, Material material){
		
	}
	
	/**
	 * Register uniforms in shader file
	 * @param uniform Uniform name in shader file
	 */
	public void addUniform(String uniform) {
		int uniformLocation = glGetUniformLocation(program, uniform);

		if (uniformLocation == 0xFFFFFFFF) {
			System.err.println("Error : Could not find uniform: " + uniform);
			new Exception().printStackTrace();
			System.exit(1);
		}

		uniforms.put(uniform, uniformLocation);
	}

	/**
	 * Register vertex shader
	 * @param text ResourceLoader.addShader("filename");
	 */
	public void addVertexShader(String text) {
		addProgram(text, GL_VERTEX_SHADER);
	}

	/**
	 * Register geometry shader
	 * @param text ResourceLoader.addShader("filename");
	 */
	public void addGeometryShader(String text) {
		addProgram(text, GL_GEOMETRY_SHADER);
	}

	/**
	 * Register fragment shader
	 * @param text ResourceLoader.addShader("filename");
	 */
	public void addFragmentShader(String text) {
		addProgram(text, GL_FRAGMENT_SHADER);
	}

	
	/**
	 * "Inits" the shaders
	 */
	@SuppressWarnings("deprecation")
	public void compileShader() {
		glLinkProgram(program);

		if (glGetProgram(program, GL_LINK_STATUS) == 0) {
			System.err.println(glGetProgramInfoLog(program, 1024));
			System.exit(1);
		}

		glValidateProgram(program);

		if (glGetProgram(program, GL_VALIDATE_STATUS) == 0) {
			System.err.println(glGetProgramInfoLog(program, 1024));
			System.exit(1);
		}
	}

	/**
	 * Register program in GPU memory
	 * 
	 * @param text Program's name
	 * @param type Program's type
	 */
	@SuppressWarnings("deprecation")
	private void addProgram(String text, int type) {
		int shader = glCreateShader(type);

		if (shader == 0) {
			System.err
					.println("Shader creation failed : Could not find valid memory location when adding shapes");
			System.exit(1);
		}

		glShaderSource(shader, text);
		glCompileShader(shader);

		if (glGetShader(shader, GL_COMPILE_STATUS) == 0) {
			System.err.println(glGetShaderInfoLog(shader, 1024));
			System.exit(1);
		}

		glAttachShader(program, shader);
	}
	
	public void setUniformi(String uniformName, int value){
		glUniform1i(uniforms.get(uniformName), value);
	}
	
	public void setUniformf(String uniformName, float value){
		glUniform1f(uniforms.get(uniformName), value);
	}
	
	public void setUniform(String uniformName, Vector3f value){
		glUniform3f(uniforms.get(uniformName), value.getX(), value.getY(), value.getZ());
	}
	
	public void setUniform(String uniformName, Matrix4f value){
		glUniformMatrix4(uniforms.get(uniformName), true, Util.createFlippedBuffer(value));
	}

}
