package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.Collection;
import org.junit.Test;

/**
 * Tests for {@link SequenceReader}.
 * This class contains an improved version of a test originally from SequenceReader_ESTestTest16.
 */
public class SequenceReaderTest {

    /**
     * Tests that the read() method correctly returns the first character 
     * from a sequence containing a single reader.
     */
    @Test
    public void readShouldReturnFirstCharacterFromSingleReader() throws IOException {
        // Arrange
        final String inputData = "directoryFilter";
        final StringReader singleReader = new StringReader(inputData);

        final Collection<StringReader> readers = new ArrayDeque<>();
        readers.add(singleReader);

        final SequenceReader sequenceReader = new SequenceReader(readers);

        // Act
        final int firstCharRead = sequenceReader.read();

        // Assert
        assertEquals("The first character should be 'd'", 'd', firstCharRead);
    }
}