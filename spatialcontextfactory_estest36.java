package org.locationtech.spatial4j.context;

import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link SpatialContextFactory}.
 */
public class SpatialContextFactoryTest {

    /**
     * Tests that makeSpatialContext throws a RuntimeException when provided with an
     * unknown distance calculator name in the configuration.
     */
    @Test
    public void makeSpatialContext_withUnknownCalculator_shouldThrowRuntimeException() {
        // --- Arrange ---
        // Create configuration arguments for the SpatialContextFactory.
        Map<String, String> args = new HashMap<>();

        // Define an invalid value for the 'distCalculator' argument to trigger the error.
        final String unknownCalculatorName = "invalidCalculatorName";
        args.put("distCalculator", unknownCalculatorName);

        // Use the current thread's context class loader, which is standard practice.
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // --- Act & Assert ---
        try {
            SpatialContextFactory.makeSpatialContext(args, classLoader);
            // The test should fail if no exception is thrown as expected.
            fail("Expected a RuntimeException for an unknown distance calculator, but none was thrown.");
        } catch (RuntimeException e) {
            // Verify that the exception message is correct and informative.
            String expectedMessage = "Unknown calculator: " + unknownCalculatorName;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}