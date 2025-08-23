package org.jsoup.internal;

import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link StringUtil#join(Iterator, String)} method.
 */
public class StringUtilTest {

    /**
     * Verifies that joining elements from an empty iterator results in an empty string,
     * regardless of the separator provided.
     */
    @Test
    public void joinWithEmptyIteratorShouldReturnEmptyString() {
        // Arrange: Create an empty iterator and define a separator.
        Iterator<?> emptyIterator = Collections.emptyIterator();
        String separator = ", ";

        // Act: Call the join method with the empty iterator.
        String result = StringUtil.join(emptyIterator, separator);

        // Assert: The resulting string should be empty.
        assertEquals("", result);
    }
}