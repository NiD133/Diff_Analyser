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

public class JsonParserTestTest3 {

    @Test
    public void testParseString() {
        String json = "{a:10,b:'c'}";
        JsonElement e = JsonParser.parseString(json);
        assertThat(e.isJsonObject()).isTrue();
        assertThat(e.getAsJsonObject().get("a").getAsInt()).isEqualTo(10);
        assertThat(e.getAsJsonObject().get("b").getAsString()).isEqualTo("c");
    }
}
