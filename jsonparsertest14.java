package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.stream.JsonReader;
import java.io.StringReader;
import org.junit.Test;

public class JsonParserTestTest14 {

    /**
     * Tests that {@link JsonParser#parseReader(JsonReader)} respects the {@link Strictness#STRICT}
     * setting of the provided reader. When the reader is strict, parsing should fail for
     * malformed JSON. The test also verifies that the reader's strictness setting is not
     * modified by the call.
     */
    @Test
    public void testParseReaderWithStrictReaderFailsForMalformedJson() {
        // Arrange
        // In strict mode, the boolean literal "false" must be all lowercase.
        String malformedJson = "faLsE";
        JsonReader reader = new JsonReader(new StringReader(malformedJson));
        reader.setStrictness(Strictness.STRICT);

        // Act & Assert
        // Verify that parsing fails with a specific error message for the malformed JSON.
        JsonSyntaxException exception = assertThrows(
            JsonSyntaxException.class,
            () -> JsonParser.parseReader(reader)
        );
        assertThat(exception)
            .hasCauseThat()
            .hasMessageThat()
            .startsWith("Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON");

        // Further assert that the original strictness of the reader was preserved.
        assertThat(reader.getStrictness()).isEqualTo(Strictness.STRICT);
    }
}