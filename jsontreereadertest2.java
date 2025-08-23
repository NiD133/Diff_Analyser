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

public class JsonTreeReaderTestTest2 {

    @Test
    public void testSkipValue_filledJsonObject() throws IOException {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        jsonArray.add('c');
        jsonArray.add("text");
        jsonObject.add("a", jsonArray);
        jsonObject.addProperty("b", true);
        jsonObject.addProperty("i", 1);
        jsonObject.add("n", JsonNull.INSTANCE);
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("n", 2L);
        jsonObject.add("o", jsonObject2);
        jsonObject.addProperty("s", "text");
        JsonTreeReader in = new JsonTreeReader(jsonObject);
        in.skipValue();
        assertThat(in.peek()).isEqualTo(JsonToken.END_DOCUMENT);
        assertThat(in.getPath()).isEqualTo("$");
    }
}
