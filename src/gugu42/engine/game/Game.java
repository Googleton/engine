package gugu42.engine.game;

import org.lwjgl.input.Keyboard;

public class Game {

	private Mesh mesh;
	private Transform transform;
	private Shader shader;
	private Material material;
	private Camera camera;

	public boolean debugMode = false;

	public Game() {
		mesh = new Mesh();
		shader = new PhongShader().getInstance();
		material = new Material(ResourceLoader.loadTexture("test.png"), new Vector3f(1, 1, 1));
		camera = new Camera();
		
		Vertex[] vertices = new Vertex[] {
				new Vertex(new Vector3f(-1, -1, 0), new Vector2f(0, 0)),
				new Vertex(new Vector3f(0, 1, 0), new Vector2f(0.5f, 0)),
				new Vertex(new Vector3f(1, -1, 0), new Vector2f(1.0f, 0)),
				new Vertex(new Vector3f(0, -1, 1), new Vector2f(0, 0.5f)) };

		int[] indices = new int[] { 3, 1, 0, 2, 1, 3, 0, 1, 2, 0, 2, 3 };

		mesh.addVertices(vertices, indices);

		Transform.setCamera(camera);
		Transform.setProjection(70f, Window.getWidth(), Window.getHeight(),
				0.1f, 1000);
		transform = new Transform();
		
		PhongShader.setAmbientLight(new Vector3f(.1f, .1f, .1f));
	}

	public void input() {
		camera.input();
		if (Input.getKeyDown(Keyboard.KEY_F2)) {
			if (debugMode) {
				System.out.println("Debug off");
				debugMode = false;
			} else {
				System.out.println("Debug on");
				debugMode = true;
			}
		}

	}

	float temp = 0.0f;

	public void update() {
		temp += Time.getDelta();

		float sinTemp = (float) Math.sin(temp);

		transform.setTranslation(sinTemp, 0, 5);
		transform.setRotation(0, sinTemp * 180, 0);
		// transform.setScale(0.7f *sinTemp, 0.7f * sinTemp,0.7f * sinTemp);
	}

	public void render() {
		RenderUtil.setClearColor(Transform.getCamera().getPos().div(2048f).abs());
		shader.bind();
		shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
		mesh.draw();
	}
}
