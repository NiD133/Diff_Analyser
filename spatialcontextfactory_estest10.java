package org.locationtech.spatial4j.context;

import org.junit.Test;
import java.lang.NoSuchFieldException;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link SpatialContextFactory}.
 */
public class SpatialContextFactoryTest {

    /**
     * Tests that calling initField() with a field name that does not exist (like an empty string)
     * throws an Error that wraps the underlying NoSuchFieldException from reflection.
     */
    @Test
    public void initFieldWithNonExistentNameShouldThrowError() {
        // Arrange: Create an instance of the factory and define an invalid field name.
        SpatialContextFactory factory = new SpatialContextFactory();
        String nonExistentFieldName = "";

        // Act & Assert: Verify that the expected Error is thrown.
        try {
            factory.initField(nonExistentFieldName);
            fail("Expected an Error to be thrown because the field name does not exist.");
        } catch (Error e) {
            // Assert that the thrown error is not null.
            assertNotNull("The caught error should not be null.", e);
            
            // Assert that the cause of the Error is the expected reflection exception.
            // This confirms the method failed for the right reason.
            Throwable cause = e.getCause();
            assertTrue(
                "The cause of the error should be a NoSuchFieldException.",
                cause instanceof NoSuchFieldException
            );
        }
    }
}