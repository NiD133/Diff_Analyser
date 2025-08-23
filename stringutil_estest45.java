package org.jsoup.internal;

import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for {@link StringUtil}.
 */
public class StringUtilTest {

    /**
     * Verifies that the join(Iterator, String) method fully consumes the provided iterator.
     * After the join operation is complete, the iterator should be exhausted.
     */
    @Test
    public void joinWithIteratorConsumesTheIterator() {
        // Arrange: Create a list of strings and get an iterator for it.
        List<String> items = new LinkedList<>(Arrays.asList("first", "second"));
        Iterator<String> iterator = items.iterator();

        // Act: Join the elements using the iterator.
        String result = StringUtil.join(iterator, ",");

        // Assert: Check that the resulting string is correct and the iterator is now empty.
        assertEquals("first,second", result);
        assertFalse("The iterator should be fully consumed after the join operation.", iterator.hasNext());
    }
}