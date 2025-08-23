package com.google.gson.internal.bind;

import com.google.gson.JsonElement;
import java.io.IOException;
import org.junit.Test;

// The test runner and scaffolding dependencies from the original test are preserved.
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonTreeReader_ESTestTest17 extends JsonTreeReader_ESTest_scaffolding {

    /**
     * Verifies that calling nextString() on a JsonTreeReader initialized with a null
     * JsonElement throws a NullPointerException. This is the expected behavior as the
     * reader has no element to process.
     */
    @Test(expected = NullPointerException.class, timeout = 4000)
    public void nextStringShouldThrowNullPointerExceptionWhenReaderIsCreatedWithNull() throws IOException {
        // Arrange: Create a reader with a null JsonElement, which is an invalid state.
        JsonTreeReader jsonTreeReader = new JsonTreeReader((JsonElement) null);

        // Act & Assert: Attempting to read a string should fail with a NullPointerException.
        jsonTreeReader.nextString();
    }
}