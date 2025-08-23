package com.google.gson.internal.bind;

import com.google.gson.JsonElement;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeReader}.
 */
// The test class name and inheritance are preserved from the original.
public class JsonTreeReader_ESTestTest24 extends JsonTreeReader_ESTest_scaffolding {

    /**
     * Verifies that calling nextJsonElement() on a reader initialized with a null
     * JsonElement throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void nextJsonElement_withNullRootElement_throwsNullPointerException() {
        // 1. Arrange: Create a JsonTreeReader with a null root element.
        JsonTreeReader reader = new JsonTreeReader((JsonElement) null);

        // 2. Act: Attempt to read the next JSON element, which should trigger the exception.
        reader.nextJsonElement();

        // 3. Assert: The @Test(expected) annotation handles the exception assertion.
        // The test will pass only if a NullPointerException is thrown.
    }
}