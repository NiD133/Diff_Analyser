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

public class JsonParserTestTest2 {

    @Test
    public void testParseUnquotedStringArrayFails() {
        JsonElement element = JsonParser.parseString("[a,b,c]");
        assertThat(element.getAsJsonArray().get(0).getAsString()).isEqualTo("a");
        assertThat(element.getAsJsonArray().get(1).getAsString()).isEqualTo("b");
        assertThat(element.getAsJsonArray().get(2).getAsString()).isEqualTo("c");
        assertThat(element.getAsJsonArray()).hasSize(3);
    }
}
