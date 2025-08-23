package org.mockito.internal.util.collections;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSet_ESTestTest38 {

    /**
     * This test documents an unexpected behavior of the removeAll method.
     * When the input collection contains duplicate elements, the current implementation
     * throws an AssertionError. This test captures that specific, and likely incorrect,
     * behavior. A more robust implementation should handle duplicates gracefully
     * without throwing an error.
     */
    @Test(timeout = 4000)
    public void removeAll_whenCollectionContainsDuplicates_throwsAssertionError() {
        // Arrange
        HashCodeAndEqualsSafeSet safeSet = new HashCodeAndEqualsSafeSet();
        Object objectInstance = new Object();
        HashCodeAndEqualsMockWrapper wrappedObject = HashCodeAndEqualsMockWrapper.of(objectInstance);

        // Create a list containing multiple references to the same wrapped object.
        // The presence of duplicates is the trigger for the bug being tested.
        List<HashCodeAndEqualsMockWrapper> collectionWithDuplicates = List.of(
            wrappedObject,
            wrappedObject,
            wrappedObject,
            wrappedObject
        );

        // Act & Assert
        try {
            safeSet.removeAll(collectionWithDuplicates);
            fail("Expected an AssertionError because the current implementation mishandles duplicate elements.");
        } catch (AssertionError e) {
            // Verify that the thrown error has the expected (though unhelpful) message.
            // This confirms we've caught the specific issue this test targets.
            assertEquals("WRONG", e.getMessage());
        }
    }
}