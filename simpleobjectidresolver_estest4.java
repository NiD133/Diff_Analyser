package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * This test suite focuses on the {@link SimpleObjectIdResolver} class.
 */
public class SimpleObjectIdResolverTest {

    /**
     * Tests that an instance of {@code SimpleObjectIdResolver} is compatible
     * with another resolver of the exact same type. The {@code canUseFor} method
     * is expected to return true in this scenario.
     */
    @Test
    public void canUseFor_shouldReturnTrue_whenResolverIsOfSameType() {
        // Arrange: Create two separate instances of the resolver.
        SimpleObjectIdResolver resolver1 = new SimpleObjectIdResolver();
        SimpleObjectIdResolver resolver2 = new SimpleObjectIdResolver();

        // Act: Check if the first resolver can be used as a blueprint for the second.
        boolean isCompatible = resolver1.canUseFor(resolver2);

        // Assert: The result should be true as they are of the same class.
        assertTrue("A resolver should be compatible with another resolver of the same type.", isCompatible);
    }
}