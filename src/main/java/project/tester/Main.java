package project.tester;

import engine.Engine;
import engine.window.Window;

public class Main {

    public static void main(String[] args) {
        try {
            Window window = new Window(640, 480, "Testing Window");
            Engine engine = new Engine(window);
            MainScene mainScene = new MainScene();
            engine.run(mainScene);
        }
        catch (IllegalStateException e) {
            System.out.println("OPENGL ERROR:");
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
