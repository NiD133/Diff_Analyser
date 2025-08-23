package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import org.junit.Test;

/**
 * Tests for {@link LoopingListIterator}.
 * This class focuses on the constructor's behavior with invalid input.
 */
// The original test class name and inheritance are kept to match the request format.
// In a real-world scenario, these would likely be renamed (e.g., to LoopingListIteratorTest).
public class LoopingListIterator_ESTestTest15 extends LoopingListIterator_ESTest_scaffolding {

    /**
     * Tests that the constructor throws a NullPointerException when initialized with a null list,
     * as per its contract.
     */
    @Test
    public void constructorShouldThrowNullPointerExceptionForNullList() {
        try {
            // Act: Attempt to create an iterator with a null list.
            new LoopingListIterator<Object>((List<Object>) null);
            
            // Assert: The test should fail if no exception is thrown.
            fail("Expected a NullPointerException to be thrown for a null list.");
        } catch (final NullPointerException e) {
            // Assert: Verify that the correct exception was thrown with the expected message.
            // The message "collection" is specified by Objects.requireNonNull in the constructor.
            assertEquals("collection", e.getMessage());
        }
    }
}