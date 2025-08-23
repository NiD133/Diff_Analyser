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
     * Tests that makeSpatialContext throws a RuntimeException when the legacy
     * 'wktShapeParserClass' property specifies a class that cannot be found.
     */
    @Test
    public void makeSpatialContext_withInvalidWktShapeParserClass_throwsRuntimeException() {
        // Arrange: Create configuration with a non-existent class name for the WKT parser.
        Map<String, String> args = new HashMap<>();
        final String invalidClassName = "org.nonexistent.parser.WktParser";
        args.put("wktShapeParserClass", invalidClassName);

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        // Act & Assert
        try {
            SpatialContextFactory.makeSpatialContext(args, classLoader);
            fail("Expected a RuntimeException because the specified WKT parser class does not exist.");
        } catch (RuntimeException e) {
            // Verify the exception message is informative and correct.
            String expectedMessage = "Unable to find format class: " + invalidClassName;
            assertEquals(expectedMessage, e.getMessage());

            // Verify the underlying cause is a ClassNotFoundException.
            assertNotNull("The exception should have a cause.", e.getCause());
            assertTrue("The cause should be a ClassNotFoundException.", e.getCause() instanceof ClassNotFoundException);
        }
    }
}