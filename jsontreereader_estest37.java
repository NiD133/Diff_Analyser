package com.google.gson.internal.bind;

import com.google.gson.JsonElement;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

// The original test class name and inheritance are preserved.
// For better maintainability, this class would typically be named JsonTreeReaderTest.
public class JsonTreeReader_ESTestTest37 extends JsonTreeReader_ESTest_scaffolding {

    /**
     * Verifies that calling {@code endArray()} on a {@code JsonTreeReader}
     * initialized with a {@code null} {@code JsonElement} throws a {@code NullPointerException}.
     *
     * <p>This scenario tests the reader's robustness against an invalid initial state.
     * The internal logic attempts to dereference the null element when peeking at the
     * next token, which correctly results in an NPE.
     */
    @Test(timeout = 4000)
    public void endArrayOnReaderInitializedWithNullThrowsNullPointerException() {
        // Arrange: Create a JsonTreeReader with a null JsonElement, representing an
        // invalid state.
        JsonTreeReader reader = new JsonTreeReader((JsonElement) null);

        // Act & Assert: Verify that attempting to call endArray() throws a
        // NullPointerException. The method reference reader::endArray is a concise
        // way to represent the action to be executed.
        assertThrows(NullPointerException.class, reader::endArray);
    }
}