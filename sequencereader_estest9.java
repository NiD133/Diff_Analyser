package org.apache.commons.io.input;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import org.junit.Test;

/**
 * Tests for {@link SequenceReader}.
 */
public class SequenceReaderTest {

    /**
     * Tests that the SequenceReader constructor throws a ConcurrentModificationException
     * if it is initialized with a sublist whose backing list has been modified.
     *
     * This is expected behavior because the constructor iterates over the provided
     * collection, and the sublist's iterator is fail-fast.
     */
    @Test
    public void constructorShouldThrowExceptionWhenBackingListOfSubListIsModified() {
        // Arrange: Create a list and a sublist view of it.
        final ArrayList<StringReader> backingList = new ArrayList<>();
        final List<StringReader> subList = backingList.subList(0, 0);

        // Act: Structurally modify the backing list *after* the sublist has been created.
        // This action invalidates the sublist and its iterator.
        backingList.add(new StringReader("some data"));

        // Assert: Attempting to create a SequenceReader with the invalidated sublist
        // should trigger a ConcurrentModificationException.
        try {
            new SequenceReader(subList);
            fail("Expected a ConcurrentModificationException to be thrown.");
        } catch (final ConcurrentModificationException e) {
            // This is the expected outcome.
            // For this specific case in ArrayList$SubList, the exception message is null.
            assertNull("The exception message should be null.", e.getMessage());
        }
    }
}