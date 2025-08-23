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

public class JsonParserTestTest14 {

    @Test
    public void testStrict() {
        JsonReader reader = new JsonReader(new StringReader("faLsE"));
        Strictness strictness = Strictness.STRICT;
        reader.setStrictness(strictness);
        var e = assertThrows(JsonSyntaxException.class, () -> JsonParser.parseReader(reader));
        assertThat(e).hasCauseThat().hasMessageThat().startsWith("Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON");
        // Original strictness was kept
        assertThat(reader.getStrictness()).isEqualTo(strictness);
    }
}
