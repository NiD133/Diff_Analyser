package org.jsoup.internal;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link StringUtil} class.
 */
public class StringUtilTest {

    @Test
    public void joinOnEmptyCollectionReturnsEmptyString() {
        // Arrange
        // An empty collection, the type of elements does not matter.
        Iterable<?> emptyCollection = Collections.emptyList();
        String separator = ", ";

        // Act
        // The join method is called with the empty collection.
        String result = StringUtil.join(emptyCollection, separator);

        // Assert
        // The result should be an empty string, regardless of the separator.
        assertEquals("", result);
    }
}