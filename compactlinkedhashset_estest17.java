package com.google.common.collect;

import static org.junit.Assert.fail;

import org.junit.Test;

// The original test class name and inheritance are preserved to maintain consistency
// with the existing test suite structure.
public class CompactLinkedHashSet_ESTestTest17 extends CompactLinkedHashSet_ESTest_scaffolding {

    /**
     * Tests that calling the internal {@code moveLastEntry} method on a newly created,
     * empty {@code CompactLinkedHashSet} throws a {@code NullPointerException}.
     *
     * <p>This is a white-box test that verifies the behavior of the class's lazy initialization.
     * The internal arrays are only allocated when an element is first added. This test ensures that
     * calling a method that operates on these arrays before initialization fails predictably.
     */
    @Test
    public void moveLastEntry_onUninitializedSet_throwsNullPointerException() {
        // Arrange: Create a set without adding any elements. Due to lazy initialization,
        // its internal data structures (like the 'predecessor' and 'successor' arrays) remain null.
        CompactLinkedHashSet<String> set = new CompactLinkedHashSet<>();

        // Act & Assert: Expect a NullPointerException when an internal method that
        // requires the arrays to be initialized is called.
        try {
            // The arguments (0, 0) are arbitrary; the NPE is thrown by a `requireNonNull`
            // check on an internal array before the arguments are even used.
            set.moveLastEntry(0, 0);
            fail("Expected NullPointerException was not thrown for moveLastEntry on an uninitialized set.");
        } catch (NullPointerException expected) {
            // This is the expected outcome, confirming the safeguard for lazy initialization.
        }
    }
}