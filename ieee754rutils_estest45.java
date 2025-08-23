package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Test case for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that the public constructor of the utility class can be instantiated.
     *
     * <p>
     * Although {@link IEEE754rUtils} is a utility class with only static methods,
     * its public constructor is maintained for backward compatibility. This test ensures
     * the constructor can be called without error, primarily for code coverage purposes.
     * </p>
     */
    @Test
    public void constructorShouldCreateInstanceForCoverage() {
        // Act: Instantiate the utility class
        final IEEE754rUtils instance = new IEEE754rUtils();

        // Assert: The instance should be successfully created and not null.
        assertNotNull("The constructor should create a non-null instance.", instance);
    }
}