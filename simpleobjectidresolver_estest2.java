package com.fasterxml.jackson.annotation;

import org.junit.Test;

/**
 * Unit tests for the {@link SimpleObjectIdResolver} class.
 */
public class SimpleObjectIdResolverTest {

    /**
     * Verifies that the canUseFor() method throws a NullPointerException
     * when passed a null argument, as this is considered invalid input.
     */
    @Test(expected = NullPointerException.class, timeout = 4000)
    public void canUseForShouldThrowNullPointerExceptionWhenResolverIsNull() {
        // Arrange: Create an instance of the class under test.
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();

        // Act: Call the method with a null argument.
        // The test framework will assert that a NullPointerException is thrown.
        resolver.canUseFor(null);
    }
}