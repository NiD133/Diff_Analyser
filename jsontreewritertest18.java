package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.Strictness;
import com.google.gson.common.MoreAsserts;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class JsonTreeWriterTestTest18 {

    @Test
    public void testLenientNansAndInfinities() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.setStrictness(Strictness.LENIENT);
        writer.beginArray();
        writer.value(Float.NaN);
        writer.value(Float.NEGATIVE_INFINITY);
        writer.value(Float.POSITIVE_INFINITY);
        writer.value(Double.NaN);
        writer.value(Double.NEGATIVE_INFINITY);
        writer.value(Double.POSITIVE_INFINITY);
        writer.endArray();
        assertThat(writer.get().toString()).isEqualTo("[NaN,-Infinity,Infinity,NaN,-Infinity,Infinity]");
    }
}
