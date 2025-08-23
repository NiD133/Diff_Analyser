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

public class JsonParserTestTest13 {

    @Test
    public void testLegacyStrict() {
        JsonReader reader = new JsonReader(new StringReader("unquoted"));
        Strictness strictness = Strictness.LEGACY_STRICT;
        // LEGACY_STRICT is ignored by JsonParser later; parses in lenient mode instead
        reader.setStrictness(strictness);
        assertThat(JsonParser.parseReader(reader)).isEqualTo(new JsonPrimitive("unquoted"));
        // Original strictness was restored
        assertThat(reader.getStrictness()).isEqualTo(strictness);
    }
}
