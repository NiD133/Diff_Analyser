package com.google.common.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CharStreams#readLines(Readable)}.
 */
@DisplayName("CharStreams.readLines()")
class CharStreamsReadLinesTest {

    @Test
    void readLines_withNewlineSeparatedString_returnsListOfLines() throws IOException {
        // Arrange
        String inputWithNewlines = "a\nb\nc";
        StringReader reader = new StringReader(inputWithNewlines);
        List<String> expectedLines = ImmutableList.of("a", "b", "c");

        // Act
        List<String> actualLines = CharStreams.readLines(reader);

        // Assert
        assertEquals(expectedLines, actualLines);
    }

    @Test
    void readLines_withTrailingNewline_doesNotIncludeEmptyStringAtEnd() throws IOException {
        // Arrange
        String inputWithTrailingNewline = "a\nb\n";
        StringReader reader = new StringReader(inputWithTrailingNewline);
        List<String> expectedLines = ImmutableList.of("a", "b");

        // Act
        List<String> actualLines = CharStreams.readLines(reader);

        // Assert
        assertEquals(expectedLines, actualLines);
    }

    @Test
    void readLines_withEmptyInput_returnsEmptyList() throws IOException {
        // Arrange
        StringReader emptyReader = new StringReader("");
        List<String> expectedLines = ImmutableList.of();

        // Act
        List<String> actualLines = CharStreams.readLines(emptyReader);

        // Assert
        assertEquals(expectedLines, actualLines);
    }

    @Test
    void readLines_withCarriageReturnLineFeed_returnsListOfLines() throws IOException {
        // Arrange
        String inputWithCrLf = "a\r\nb\r\nc";
        StringReader reader = new StringReader(inputWithCrLf);
        List<String> expectedLines = ImmutableList.of("a", "b", "c");

        // Act
        List<String> actualLines = CharStreams.readLines(reader);

        // Assert
        assertEquals(expectedLines, actualLines);
    }
}