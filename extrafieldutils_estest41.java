package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link ExtraFieldUtils} class.
 */
public class ExtraFieldUtilsTest {

    /**
     * Tests that the ExtraFieldUtils class can be instantiated.
     * This is primarily for code coverage, as it is a utility class
     * with static methods.
     */
    @Test
    public void testInstantiation() {
        // Act: Create an instance of the utility class.
        final ExtraFieldUtils extraFieldUtils = new ExtraFieldUtils();

        // Assert: The instance should be successfully created.
        assertNotNull("A new instance of ExtraFieldUtils should not be null.", extraFieldUtils);
    }
}