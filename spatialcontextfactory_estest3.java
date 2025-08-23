package org.locationtech.spatial4j.context;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link SpatialContextFactory}.
 */
public class SpatialContextFactoryTest {

    /**
     * Tests that makeSpatialContext throws a RuntimeException when the 'readers'
     * configuration property contains a class name that does not exist.
     */
    @Test
    public void makeSpatialContext_withInvalidReaderClassName_throwsRuntimeException() {
        // Arrange: Create configuration with a non-existent class name for the 'readers' property.
        // Using a descriptive, fully-qualified name makes the test's intent clear.
        final String invalidClassName = "com.example.NonExistentShapeReader";
        Map<String, String> config = new HashMap<>();
        config.put("readers", invalidClassName);

        // Act & Assert
        try {
            SpatialContextFactory.makeSpatialContext(config, null);
            fail("Expected a RuntimeException because the specified reader class cannot be found.");
        } catch (RuntimeException e) {
            // Verify that the exception message clearly indicates which class failed to load.
            String expectedMessage = "Unable to find format class: " + invalidClassName;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}