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

public class JsonTreeWriterTestTest9 {

    @Test
    public void testTwoNames() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("a");
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> writer.name("a"));
        assertThat(e).hasMessageThat().isEqualTo("Did not expect a name");
    }
}
