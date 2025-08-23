package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Test suite for {@link LoopingListIterator}.
 * This class contains a specific test case from a larger, generated test suite.
 */
public class LoopingListIteratorTest {

    /**
     * Tests that calling previousIndex() on an iterator created from an empty list
     * throws a NoSuchElementException.
     */
    @Test
    public void previousIndex_onEmptyList_throwsNoSuchElementException() {
        // Arrange: Create a LoopingListIterator with an empty list.
        final List<Object> emptyList = Collections.emptyList();
        final LoopingListIterator<Object> iterator = new LoopingListIterator<>(emptyList);

        // Act & Assert: Verify that calling previousIndex() throws the expected exception.
        // The assertThrows method is a standard and readable way to test for exceptions.
        final NoSuchElementException thrown = assertThrows(
            NoSuchElementException.class,
            iterator::previousIndex // The action that is expected to throw
        );

        // Assert on the exception message for more precise validation.
        assertEquals("There are no elements for this iterator to loop on", thrown.getMessage());
    }
}