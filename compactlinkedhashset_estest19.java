package com.google.common.collect;

import static org.junit.Assert.fail;

import java.util.Locale;
import org.junit.Test;

/**
 * This class contains the refactored test case.
 * The original test class name suggests it was generated, so the scaffolding is preserved.
 */
public class CompactLinkedHashSet_ESTestTest19 extends CompactLinkedHashSet_ESTest_scaffolding {

    /**
     * Tests that calling the internal {@code insertEntry} method with an index far beyond the
     * set's allocated capacity throws a NullPointerException.
     *
     * <p>This is a white-box test that targets an internal implementation detail. It ensures that
     * attempting to write to an unallocated slot in the backing arrays fails fast, as was
     * asserted by the original generated test.
     */
    @Test
    public void insertEntry_withIndexBeyondCapacity_throwsNullPointerException() {
        // Arrange: Create an empty set with a small default capacity.
        CompactLinkedHashSet<Locale.Category> set = CompactLinkedHashSet.create();
        Locale.Category element = Locale.Category.DISPLAY;

        int indexBeyondCapacity = 91;
        int elementHash = 91;
        // A mask of 0 implies a backing hash table of size 1, an edge case.
        int mask = 0;

        // Act & Assert: Expect an exception when calling an internal method with an invalid index.
        try {
            set.insertEntry(indexBeyondCapacity, element, elementHash, mask);
            fail("Expected a NullPointerException when calling insertEntry() with an index "
                + "that is out of bounds for the internal arrays.");
        } catch (NullPointerException expected) {
            // This is the expected outcome. The test verifies that the method fails predictably
            // when its preconditions (a valid index) are not met.
        }
    }
}