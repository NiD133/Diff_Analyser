package org.apache.commons.io.function;

import org.junit.Test;
import java.util.function.Supplier;

/**
 * Tests for the {@link Uncheck} utility class.
 */
public class UncheckTest {

    /**
     * Tests that calling Uncheck.run() with a null IORunnable argument
     * throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void runWithNullRunnableShouldThrowNullPointerException() {
        // Arrange: Create a dummy message supplier. This supplier is for wrapping
        // IOExceptions and will not be used in this test case, as the
        // NullPointerException is thrown before the runnable is executed.
        final Supplier<String> dummyMessageSupplier = () -> "This message should not be used";

        // Act & Assert: Call the method under test with a null runnable.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        Uncheck.run(null, dummyMessageSupplier);
    }
}