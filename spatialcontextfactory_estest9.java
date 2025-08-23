package org.locationtech.spatial4j.context;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link SpatialContextFactory}.
 */
public class SpatialContextFactoryTest {

    /**
     * Tests that makeSpatialContext throws a RuntimeException when the
     * 'spatialContextFactory' argument specifies a class that does not exist.
     */
    @Test
    public void makeSpatialContext_withNonExistentFactoryClass_shouldThrowRuntimeException() {
        // Arrange: Set up arguments with a factory class name that cannot be found.
        final String nonExistentClassName = "com.example.NonExistentFactory";
        Map<String, String> args = new HashMap<>();
        args.put("spatialContextFactory", nonExistentClassName);

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // Act & Assert: Expect a RuntimeException that is caused by a ClassNotFoundException.
        try {
            SpatialContextFactory.makeSpatialContext(args, classLoader);
            fail("Expected a RuntimeException because the specified factory class does not exist.");
        } catch (RuntimeException e) {
            // The factory is expected to wrap the underlying ClassNotFoundException.
            Throwable cause = e.getCause();
            assertNotNull("The RuntimeException should have a cause.", cause);
            assertTrue("The cause should be a ClassNotFoundException.", cause instanceof ClassNotFoundException);

            // Verify the exception message clearly indicates which class could not be found.
            String expectedMessage = "java.lang.ClassNotFoundException: " + nonExistentClassName;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}