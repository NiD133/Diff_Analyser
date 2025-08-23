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

public class JsonTreeReaderTestTest8 {

    @Test
    public void testCustomJsonElementSubclass() throws IOException {
        // superclass constructor
        @SuppressWarnings("deprecation")
        class CustomSubclass extends JsonElement {

            @Override
            public JsonElement deepCopy() {
                return this;
            }
        }
        JsonArray array = new JsonArray();
        array.add(new CustomSubclass());
        JsonTreeReader reader = new JsonTreeReader(array);
        reader.beginArray();
        // Should fail due to custom JsonElement subclass
        var e = assertThrows(MalformedJsonException.class, () -> reader.peek());
        assertThat(e).hasMessageThat().isEqualTo("Custom JsonElement subclass " + CustomSubclass.class.getName() + " is not supported");
    }
}
