package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Tests for {@link CharStreams#readLines(Readable, LineProcessor)}.
 *
 * <p>This test class focuses on verifying the behavior of the LineProcessor, specifically its
 * ability to control the reading process by returning true or false.
 */
public class CharStreamsReadLinesWithLineProcessorTest extends IoTestCase {

    private static final String MULTILINE_TEXT = "a\nb\nc";

    /**
     * A LineProcessor that counts processed lines and can be configured to stop after a certain
     * number of lines. It can also accumulate the processed lines into a StringBuilder.
     */
    private static class TestLineProcessor implements LineProcessor<Integer> {
        private final int stopAfterLineCount;
        private final StringBuilder processedLinesBuilder;
        private int linesSeen = 0;

        /**
         * Creates a processor that stops after a specified number of lines.
         *
         * @param stopAfterLineCount The number of lines to process before returning false.
         *     Processing continues as long as {@code linesSeen < stopAfterLineCount}.
         */
        TestLineProcessor(int stopAfterLineCount) {
            this(stopAfterLineCount, null);
        }

        /**
         * Creates a processor that stops after a specified number of lines and appends processed
         * lines to a StringBuilder.
         *
         * @param stopAfterLineCount The number of lines to process before returning false.
         * @param processedLinesBuilder The builder to append lines to, or null if not needed.
         */
        TestLineProcessor(int stopAfterLineCount, StringBuilder processedLinesBuilder) {
            this.stopAfterLineCount = stopAfterLineCount;
            this.processedLinesBuilder = processedLinesBuilder;
        }

        @Override
        public boolean processLine(String line) {
            linesSeen++;
            if (processedLinesBuilder != null) {
                processedLinesBuilder.append(line);
            }
            // Return true to continue processing, false to stop.
            return linesSeen < stopAfterLineCount;
        }

        @Override
        public Integer getResult() {
            return linesSeen;
        }
    }

    public void testReadLines_processesAllLinesWhenProcessorAlwaysReturnsTrue() throws IOException {
        // Arrange
        Reader reader = new StringReader(MULTILINE_TEXT);
        // A processor that will see 3 lines and stop after 3, effectively processing all lines.
        LineProcessor<Integer> allLinesProcessor = new TestLineProcessor(3);

        // Act
        Integer linesProcessed = CharStreams.readLines(reader, allLinesProcessor);

        // Assert
        assertEquals("Should have processed all 3 lines", 3, linesProcessed.intValue());
    }

    public void testReadLines_stopsAfterFirstLineWhenProcessorReturnsFalse() throws IOException {
        // Arrange
        Reader reader = new StringReader(MULTILINE_TEXT);
        // A processor configured to stop after processing the first line.
        LineProcessor<Integer> stopEarlyProcessor = new TestLineProcessor(1);

        // Act
        Integer linesProcessed = CharStreams.readLines(reader, stopEarlyProcessor);

        // Assert
        assertEquals("Should have stopped processing after the first line", 1, linesProcessed.intValue());
    }

    public void testReadLines_stopsConditionallyAndAccumulatesResult() throws IOException {
        // Arrange
        Reader reader = new StringReader(MULTILINE_TEXT);
        StringBuilder processedContent = new StringBuilder();
        // A processor configured to stop after the second line and accumulate the content.
        LineProcessor<Integer> conditionalProcessor = new TestLineProcessor(2, processedContent);

        // Act
        Integer linesProcessed = CharStreams.readLines(reader, conditionalProcessor);

        // Assert
        assertEquals("Should have stopped processing after the second line", 2, linesProcessed.intValue());
        assertEquals(
            "Should have accumulated the content of the first two lines",
            "ab",
            processedContent.toString());
    }
}