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

public class JsonTreeWriterTestTest20 {

    @Test
    public void testStrictBoxedNansAndInfinities() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.setStrictness(Strictness.LEGACY_STRICT);
        writer.beginArray();
        assertThrows(IllegalArgumentException.class, () -> writer.value(Float.valueOf(Float.NaN)));
        assertThrows(IllegalArgumentException.class, () -> writer.value(Float.valueOf(Float.NEGATIVE_INFINITY)));
        assertThrows(IllegalArgumentException.class, () -> writer.value(Float.valueOf(Float.POSITIVE_INFINITY)));
        assertThrows(IllegalArgumentException.class, () -> writer.value(Double.valueOf(Double.NaN)));
        assertThrows(IllegalArgumentException.class, () -> writer.value(Double.valueOf(Double.NEGATIVE_INFINITY)));
        assertThrows(IllegalArgumentException.class, () -> writer.value(Double.valueOf(Double.POSITIVE_INFINITY)));
    }
}
