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

public class JsonTreeWriterTestTest7 {

    @Test
    public void testNameAsTopLevelValue() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> writer.name("hello"));
        assertThat(e).hasMessageThat().isEqualTo("Did not expect a name");
        writer.value(12);
        writer.close();
        e = assertThrows(IllegalStateException.class, () -> writer.name("hello"));
        assertThat(e).hasMessageThat().isEqualTo("Please begin an object before writing a name.");
    }
}
