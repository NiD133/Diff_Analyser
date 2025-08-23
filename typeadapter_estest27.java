package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.EOFException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for the {@link TypeAdapter} class.
 */
public class TypeAdapterTest {

    /**
     * Verifies that calling fromJson() with an empty Reader results in an EOFException.
     * This is the expected behavior because the underlying JsonReader cannot find a
     * JSON token to parse.
     */
    @Test
    public void fromJson_withEmptyReader_shouldThrowEOFException() throws Exception {
        // Arrange: Create a TypeAdapter and an empty input reader.
        // The specific TypeAdapter implementation is not critical here; the behavior
        // is handled by the base fromJson(Reader) method.
        TypeAdapter<Object> typeAdapter = new Gson.FutureTypeAdapter<>().nullSafe();
        Reader emptyReader = new StringReader("");

        // Act & Assert
        try {
            typeAdapter.fromJson(emptyReader);
            fail("Expected an EOFException to be thrown for an empty reader, but it completed successfully.");
        } catch (EOFException expected) {
            // Verify that the exception message is correct, confirming the source of the error.
            assertEquals("End of input at line 1 column 1 path $", expected.getMessage());
        }
    }
}