package org.apache.commons.collections4.set;

import org.junit.Test;

/**
 * This test class contains an improved version of a test case for CompositeSet.
 * The original test was auto-generated, making it complex and difficult to understand.
 * The refactored test focuses on a single, clear behavior.
 */
public class CompositeSet_ESTestTest55 extends CompositeSet_ESTest_scaffolding {

    /**
     * Tests that calling toArray() with a null array argument throws a NullPointerException.
     * This is the expected behavior as specified by the java.util.Collection interface.
     */
    @Test(expected = NullPointerException.class)
    public void toArrayWithNullArrayArgumentShouldThrowNullPointerException() {
        // Arrange: Create an empty CompositeSet.
        // The behavior of toArray(null) is the same for empty and non-empty sets.
        final CompositeSet<Object> compositeSet = new CompositeSet<>();

        // Act: Call the toArray method with a null argument.
        // Assert: A NullPointerException is expected, as declared by the @Test annotation.
        compositeSet.toArray((Object[]) null);
    }
}