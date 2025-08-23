package org.apache.commons.collections4.set;

import org.junit.Test;

/**
 * Tests for {@link CompositeSet}.
 * This class focuses on specific behaviors not covered in a broader test suite.
 */
public class CompositeSetTest {

    /**
     * Tests that calling forEach() with a null action throws a NullPointerException,
     * as expected from the default implementation in java.lang.Iterable.
     */
    @Test(expected = NullPointerException.class)
    public void testForEachWithNullActionShouldThrowException() {
        // Arrange: Create an empty composite set. The content doesn't matter for this test.
        final CompositeSet<Integer> compositeSet = new CompositeSet<>();

        // Act: Call forEach with a null argument.
        // The @Test(expected) annotation will assert that a NullPointerException is thrown.
        compositeSet.forEach(null);
    }
}