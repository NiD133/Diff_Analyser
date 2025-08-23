package com.google.common.io;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link CharStreams#readLines(Readable, LineProcessor)}.
 */
public class CharStreams_ESTestTest36 {

    /**
     * Verifies that readLines returns the result from the LineProcessor
     * after the processor has processed all available lines.
     */
    @Test
    public void readLines_withLineProcessor_returnsProcessorResult() throws IOException {
        // Arrange
        String inputData = "first line\nsecond line";
        StringReader reader = new StringReader(inputData);

        // Create a mock LineProcessor that will process all lines and has a defined result.
        @SuppressWarnings("unchecked") // Necessary for mocking generic types
        LineProcessor<String> mockLineProcessor = mock(LineProcessor.class);

        // Configure the processor to continue processing after each line.
        when(mockLineProcessor.processLine(anyString())).thenReturn(true);

        // Define the final object that the processor should return.
        String expectedResult = "Completed Processing";
        when(mockLineProcessor.getResult()).thenReturn(expectedResult);

        // Act
        String actualResult = CharStreams.readLines(reader, mockLineProcessor);

        // Assert
        // Verify that the method returns the exact object from the processor's getResult().
        assertSame("The result of readLines should be the object returned by the processor's getResult() method.",
                expectedResult, actualResult);

        // Additionally, verify that the processor was called for each line of input.
        verify(mockLineProcessor).processLine("first line");
        verify(mockLineProcessor).processLine("second line");
    }
}