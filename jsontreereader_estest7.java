package com.google.gson.internal.bind;

import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.IOException;

// The original class name and inheritance are kept to show a focused refactoring
// of the test method. In a real-world scenario, the class might also be renamed
// to something like `JsonTreeReaderTest`.
public class JsonTreeReader_ESTestTest7 extends JsonTreeReader_ESTest_scaffolding {

    /**
     * Verifies that nextDouble() correctly converts a JsonPrimitive holding a long
     * into its equivalent double representation.
     */
    @Test
    public void nextDouble_whenReadingLongPrimitive_returnsEquivalentDouble() throws IOException {
        // Arrange: Create a JsonTreeReader with a JSON primitive containing a long value.
        JsonPrimitive longJsonPrimitive = new JsonPrimitive(-1977L);
        JsonTreeReader jsonTreeReader = new JsonTreeReader(longJsonPrimitive);

        // Act: Call the method under test to read the value as a double.
        double result = jsonTreeReader.nextDouble();

        // Assert: Verify that the returned double is the correct representation of the long.
        assertEquals(-1977.0, result, 0.01);
    }
}