package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.gson.common.TestTypes.BagOfPrimitives;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

public class JsonParserTestTest10 {

    /**
     * Deeply nested JSON objects should not cause {@link StackOverflowError}
     */
    @Test
    public void testParseDeeplyNestedObjects() throws IOException {
        int times = 10000;
        // {"a":{"a": ... {"a":null} ... }}
        String json = "{\"a\":".repeat(times) + "null" + "}".repeat(times);
        JsonReader jsonReader = new JsonReader(new StringReader(json));
        jsonReader.setNestingLimit(Integer.MAX_VALUE);
        int actualTimes = 0;
        JsonObject current = JsonParser.parseReader(jsonReader).getAsJsonObject();
        while (true) {
            assertThat(current.size()).isEqualTo(1);
            actualTimes++;
            JsonElement next = current.get("a");
            if (next.isJsonNull()) {
                break;
            } else {
                current = next.getAsJsonObject();
            }
        }
        assertThat(actualTimes).isEqualTo(times);
    }
}
