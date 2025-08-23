package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.stream.JsonToken;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class JsonTreeReader_ESTestTest95 extends JsonTreeReader_ESTest_scaffolding {

    /**
     * Tests the behavior of {@link JsonTreeReader#skipValue()} when called
     * immediately after {@link JsonTreeReader#beginArray()} on an empty array.
     *
     * <p>The expected behavior for reading an array is to call {@code endArray()}
     * when {@code hasNext()} is false. This test verifies the unconventional
     * behavior of calling {@code skipValue()} instead, which results in the
     * reader's position being reset to the start of the array.
     */
    @Test(timeout = 4000)
    public void skipValue_whenAtEndOfEmptyArray_resetsReaderToBeginningOfArray() throws IOException {
        // Arrange
        JsonArray emptyArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(emptyArray);

        // Position the reader inside the empty array.
        // The next token for an empty array is END_ARRAY.
        reader.beginArray();
        assertEquals("Precondition: Reader should be at the end of the array",
                JsonToken.END_ARRAY, reader.peek());

        // Act
        // Instead of calling the expected endArray(), we call skipValue().
        // The implementation of skipValue() consumes the END_ARRAY token by
        // popping the array's internal iterator, but not the array itself.
        reader.skipValue();

        // Assert
        // Because only the iterator was popped, the reader's state is effectively
        // reset. Peeking again shows the reader is positioned at the beginning
        // of the array once more.
        assertEquals("Reader should be reset to the beginning of the array",
                JsonToken.BEGIN_ARRAY, reader.peek());
    }
}