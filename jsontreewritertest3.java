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

public class JsonTreeWriterTestTest3 {

    @Test
    public void testObject() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("A").value(1);
        writer.name("B").value(2);
        writer.endObject();
        assertThat(writer.get().toString()).isEqualTo("{\"A\":1,\"B\":2}");
    }
}
