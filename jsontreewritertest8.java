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

public class JsonTreeWriterTestTest8 {

    @Test
    public void testNameInArray() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> writer.name("hello"));
        assertThat(e).hasMessageThat().isEqualTo("Please begin an object before writing a name.");
        writer.value(12);
        e = assertThrows(IllegalStateException.class, () -> writer.name("hello"));
        assertThat(e).hasMessageThat().isEqualTo("Please begin an object before writing a name.");
        writer.endArray();
        assertThat(writer.get().toString()).isEqualTo("[12]");
    }
}
