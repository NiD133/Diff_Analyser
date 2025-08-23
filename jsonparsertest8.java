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

public class JsonParserTestTest8 {

    @Test
    public void testParseMixedArray() {
        String json = "[{},13,\"stringValue\"]";
        JsonElement e = JsonParser.parseString(json);
        assertThat(e.isJsonArray()).isTrue();
        JsonArray array = e.getAsJsonArray();
        assertThat(array.get(0).toString()).isEqualTo("{}");
        assertThat(array.get(1).getAsInt()).isEqualTo(13);
        assertThat(array.get(2).getAsString()).isEqualTo("stringValue");
    }
}
