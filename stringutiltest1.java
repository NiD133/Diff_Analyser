package org.jsoup.internal;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the StringUtil#join methods.
 */
public class StringUtilTest {

    @Test
    void joinWithMultipleItemsReturnsConcatenatedString() {
        // Arrange
        List<String> items = Arrays.asList("one", "two", "three");
        String separator = " ";
        String expected = "one two three";

        // Act
        String actual = StringUtil.join(items, separator);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void joinWithSingleItemReturnsTheItemItself() {
        // Arrange
        List<String> items = Collections.singletonList("one");
        String separator = " ";
        String expected = "one";

        // Act
        String actual = StringUtil.join(items, separator);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void joinWithEmptyListReturnsEmptyString() {
        // Arrange
        List<String> items = Collections.emptyList();
        String separator = " ";
        String expected = "";

        // Act
        String actual = StringUtil.join(items, separator);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void joinWithSingleEmptyStringReturnsEmptyString() {
        // Arrange
        List<String> items = Collections.singletonList("");
        String separator = " ";
        String expected = "";

        // Act
        String actual = StringUtil.join(items, separator);

        // Assert
        assertEquals(expected, actual);
    }
}