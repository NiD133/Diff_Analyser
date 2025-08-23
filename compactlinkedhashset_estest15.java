package com.google.common.collect;

import org.junit.Test;
import java.util.Collections;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains a specific test case for {@link CompactLinkedHashSet},
 * focusing on its behavior under inconsistent internal state.
 */
public class CompactLinkedHashSetRefactoredTest {

    /**
     * Verifies that toArray() throws an ArrayIndexOutOfBoundsException if the internal
     * 'elements' array is smaller than the set's reported size.
     *
     * <p>This is a white-box test that intentionally creates a corrupted state to ensure
     * the method fails predictably rather than causing undefined behavior. Such a state
     * should not occur in normal operation.
     */
    @Test
    public void toArray_whenInternalElementsArrayIsSmallerThanSize_throwsArrayIndexOutOfBounds() {
        // Arrange: Create a set that reports having one element.
        CompactLinkedHashSet<String> set = CompactLinkedHashSet.create(Collections.singleton("one"));
        assertEquals("Pre-condition: set size should be 1", 1, set.size());

        // Act: Manually corrupt the internal state by replacing the backing array
        // with an empty one. This creates an inconsistency: size() is 1, but the
        // elements array has a length of 0.
        set.elements = new Object[0];

        // Assert: toArray() should now fail when it tries to access elements[0].
        try {
            set.toArray();
            fail("Expected an ArrayIndexOutOfBoundsException due to the inconsistent internal state.");
        } catch (ArrayIndexOutOfBoundsException expected) {
            // The exception is expected because toArray() iterates up to size(),
            // and accessing index 0 of an empty array is out of bounds.
            assertEquals("The exception message should indicate the failing index.", "0", expected.getMessage());
        }
    }
}