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

public class JsonParserTestTest9 {

    /**
     * Deeply nested JSON arrays should not cause {@link StackOverflowError}
     */
    @Test
    public void testParseDeeplyNestedArrays() throws IOException {
        int times = 10000;
        // [[[ ... ]]]
        String json = "[".repeat(times) + "]".repeat(times);
        JsonReader jsonReader = new JsonReader(new StringReader(json));
        jsonReader.setNestingLimit(Integer.MAX_VALUE);
        int actualTimes = 0;
        JsonArray current = JsonParser.parseReader(jsonReader).getAsJsonArray();
        while (true) {
            actualTimes++;
            if (current.isEmpty()) {
                break;
            }
            assertThat(current.size()).isEqualTo(1);
            current = current.get(0).getAsJsonArray();
        }
        assertThat(actualTimes).isEqualTo(times);
    }
}
