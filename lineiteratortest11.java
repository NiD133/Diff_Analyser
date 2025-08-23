package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LineIterator}.
 */
public class LineIteratorTest {

    /**
     * Creates a list of strings for testing, e.g., ["LINE 0", "LINE 1", ...].
     *
     * @param lineCount The number of lines to create.
     * @return A new list of strings.
     */
    private List<String> createStringLines(final int lineCount) {
        final List<String> lines = new ArrayList<>();
        for (int i = 0; i < lineCount; i++) {
            lines.add("LINE " + i);
        }
        return lines;
    }

    /**
     * Tests that {@link LineIterator#hasNext()} throws an {@link IllegalStateException}
     * when the underlying reader fails with an {@link IOException}.
     */
    @Test
    void hasNextShouldThrowIllegalStateExceptionWhenReaderFails() {
        // Arrange: Create a mock reader that always throws an IOException.
        final Reader mockReader = new BufferedReader(new StringReader("")) {
            @Override
            public String readLine() throws IOException {
                throw new IOException("Test Exception: Simulating a read error.");
            }
        };

        // Act & Assert: The call to hasNext() should trigger the IOException,
        // which is wrapped in an IllegalStateException.
        try (final LineIterator lineIterator = new LineIterator(mockReader)) {
            assertThrows(IllegalStateException.class, lineIterator::hasNext,
                "hasNext() should throw IllegalStateException on reader error.");
        }
    }

    /**
     * Tests that a custom {@link LineIterator} subclass can filter lines
     * by overriding the {@link LineIterator#isValidLine(String)} method.
     */
    @Test
    void iteratorShouldFilterLinesBasedOnIsValidLineOverride() {
        // Arrange: Define the full set of lines and the expected result after filtering.
        final List<String> allLines = createStringLines(9); // "LINE 0" through "LINE 8"

        // The filter logic will remove lines where the last digit % 3 == 1.
        // This means lines ending in 1, 4, and 7 will be removed.
        final List<String> expectedLines = allLines.stream()
            .filter(line -> {
                final char lastChar = line.charAt(line.length() - 1);
                final int lastDigit = Character.getNumericValue(lastChar);
                return lastDigit % 3 != 1;
            })
            .collect(Collectors.toList());

        final String fileContent = String.join(System.lineSeparator(), allLines);
        final Reader reader = new StringReader(fileContent);

        // Act: Create a custom LineIterator with the filtering logic and collect the results.
        try (final LineIterator iterator = new LineIterator(reader) {
            @Override
            protected boolean isValidLine(final String line) {
                final char lastChar = line.charAt(line.length() - 1);
                final int lastDigit = Character.getNumericValue(lastChar);
                return lastDigit % 3 != 1;
            }
        }) {
            // Assert that remove() is unsupported at any time.
            assertThrows(UnsupportedOperationException.class, iterator::remove);

            final List<String> actualLines = new ArrayList<>();
            iterator.forEachRemaining(actualLines::add);

            // Assert that the filtered lines match the expected output.
            assertEquals(expectedLines, actualLines);

            // Assert the behavior of an exhausted iterator.
            assertFalse(iterator.hasNext(), "Iterator should be exhausted.");
            assertThrows(NoSuchElementException.class, iterator::next,
                "Calling next() on an exhausted iterator should throw.");
        }
    }
}