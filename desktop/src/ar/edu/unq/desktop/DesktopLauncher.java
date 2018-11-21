package ar.edu.unq.desktop;

import ar.edu.unq.OptimizationExampleLocalVariable;
import ar.edu.unq.OptimizationExamplesPartialBatch;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ar.edu.unq.OptimizationExamples;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 900;
        config.width = 1200;
        new LwjglApplication(new OptimizationExamples(), config);
    }
}
