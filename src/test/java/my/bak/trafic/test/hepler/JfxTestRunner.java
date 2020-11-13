/**
 * © 2015 isp-insoft all rights reserved.
 */
package my.bak.trafic.test.hepler;

import com.sun.javafx.application.PlatformImpl;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;


/**
 * This runner can be used to run JUnit-Tests on the JavaFx-Thread. This class can be used as a parameter to the
 */

public class JfxTestRunner extends BlockJUnit4ClassRunner {
    /**
     * Creates a test runner, that initializes the JavaFx runtime.
     *
     * @param klass The class under test.
     * @throws InitializationError if the test class is malformed.
     */
    public JfxTestRunner(final Class<?> klass) throws InitializationError {
        super(klass);
        try {
            setupJavaFX();
        } catch (final InterruptedException e) {
            throw new InitializationError("Could not initialize the JavaFx platform.");
        }
    }


    private static void setupJavaFX() throws InterruptedException {
        // initializes JavaFX environment
        PlatformImpl.startup(() -> {
            /* No need to do anything here */
        });
    }
}