package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.common.MoreAsserts;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class JsonTreeReaderTestTest3 {

    @Test
    public void testSkipValue_name() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("a", "value");
        JsonTreeReader in = new JsonTreeReader(jsonObject);
        in.beginObject();
        in.skipValue();
        assertThat(in.peek()).isEqualTo(JsonToken.STRING);
        assertThat(in.getPath()).isEqualTo("$.<skipped>");
        assertThat(in.nextString()).isEqualTo("value");
    }
}
