package gugu42.engine.game;

public class MainComponent {

	public static final int width = 1280;
	public static final int height = 720;
	public static final String title = "engine";
	public static final double frame_cap = 5000.0;

	private boolean isRunning;
	private Game game;
	
	
	public MainComponent() {
		System.out.println(RenderUtil.getOpenGLVersion());
		RenderUtil.initGraphics();
		isRunning = false;
		game = new Game();
	}

	public void start() {

		if (isRunning)
			return;
		
		run();
	}

	public void stop() {
		if (!isRunning)
			return;

		isRunning = false;
	}

	private void run() {

		isRunning = true;

		int frames = 0;
		long frameCounter = 0;

		final double frameTime = 1.0 / frame_cap;

		long lastTime = Time.getTime();
		double unprocessedTime = 0;

		while (isRunning) {

			boolean render = false;

			long startTime = Time.getTime();
			long passedTime = startTime - lastTime;
			lastTime = startTime;

			unprocessedTime += passedTime / ((double) Time.second);
			frameCounter += passedTime;

			while (unprocessedTime > frameTime) {
				render = true;

				unprocessedTime -= frameTime;

				if (Window.isCloseRequested())
					stop();

				Time.setDelta(frameTime);
				
			
				game.input();
				Input.update();
				game.update();
				
				if (frameCounter >= Time.second) {
					System.out.print(frames +"\n");
					frames = 0;
					frameCounter = 0;
				}
			}
			if (render) {
				render();
				frames++;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		cleanUp();
	}

	private void render() {
		RenderUtil.clearScreen();
		game.render();
		Window.render();
	}

	private void cleanUp() {
		Window.dispose();
	}

	public static void main(String[] args) {
		Window.createWindow(width, height, title);

		MainComponent game = new MainComponent();
		game.start();
	}

}
