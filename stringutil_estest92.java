package org.jsoup.internal;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class StringUtilTest {

    @Test
    public void joinWithSingleElementCollectionReturnsElementWithoutSeparator() {
        // Arrange
        List<String> items = List.of("one");
        String separator = ",";
        String expected = "one";

        // Act
        String actual = StringUtil.join(items, separator);

        // Assert
        assertEquals(expected, actual);
    }
}