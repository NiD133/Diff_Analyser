package org.jsoup.internal;

import org.junit.Test;
import java.util.Collections;
import java.util.Iterator;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the join methods in {@link StringUtil}.
 */
public class StringUtilJoinTest {

    @Test
    public void joinWithSingleElementIteratorShouldReturnElementWithoutSeparator() {
        // Arrange: Create an iterator that will yield a single element.
        Iterator<String> singleElementIterator = Collections.singletonList("one").iterator();
        String separator = ",";

        // Act: Call the method under test.
        String result = StringUtil.join(singleElementIterator, separator);

        // Assert: The result should be the single element itself, with no separator applied.
        assertEquals("one", result);
    }
}