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

public class JsonParserTestTest4 {

    @Test
    public void testParseEmptyString() {
        JsonElement e = JsonParser.parseString("\"   \"");
        assertThat(e.isJsonPrimitive()).isTrue();
        assertThat(e.getAsString()).isEqualTo("   ");
    }
}
