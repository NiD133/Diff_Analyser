package org.apache.commons.collections4.bag;

import org.junit.Test;
import java.util.Collections;

/**
 * This class contains tests for TreeBag, focusing on specific edge cases.
 * The original test was auto-generated and has been rewritten for clarity.
 */
public class TreeBag_ESTestTest10 extends TreeBag_ESTest_scaffolding {

    /**
     * Tests that adding an element of an incompatible type to a non-empty TreeBag
     * throws a ClassCastException.
     * <p>
     * A TreeBag without a custom comparator relies on the natural ordering of its
     * elements. When the first element is added (e.g., an Integer), it establishes
     * the type that all subsequent elements must be comparable to. Attempting to add
     * an element of a different, non-comparable type (like a String) should fail.
     */
    @Test(expected = ClassCastException.class)
    public void add_whenIncompatibleTypeIsAdded_thenThrowsClassCastException() {
        // Arrange: Create a TreeBag and add an Integer. This sets the
        // bag's internal comparison logic to expect Integers or
        // other types comparable to Integer.
        // We use TreeBag<Object> to satisfy the compiler, allowing us to attempt
        // to add a different type at runtime.
        final TreeBag<Object> treeBag = new TreeBag<>(Collections.singleton(1));

        // Act: Attempt to add a String, which cannot be compared to an Integer.
        // This action is expected to throw a ClassCastException from the underlying TreeMap.
        treeBag.add("incompatible string type");

        // Assert: The test succeeds if a ClassCastException is thrown, as declared
        // in the @Test annotation. No explicit fail() is needed.
    }
}