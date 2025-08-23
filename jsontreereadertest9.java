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

public class JsonTreeReaderTestTest9 {

    /**
     * {@link JsonTreeReader} ignores nesting limit because:
     *
     * <ul>
     *   <li>It is an internal class and often created implicitly without the user having access to it
     *       (as {@link JsonReader}), so they cannot easily adjust the limit
     *   <li>{@link JsonTreeReader} may be created based on an existing {@link JsonReader}; in that
     *       case it would be necessary to propagate settings to account for a custom nesting limit,
     *       see also related https://github.com/google/gson/pull/2151
     *   <li>Nesting limit as protection against {@link StackOverflowError} is not that relevant for
     *       {@link JsonTreeReader} because a deeply nested {@link JsonElement} tree would first have
     *       to be constructed; and if it is constructed from a regular {@link JsonReader}, then its
     *       nesting limit would already apply
     * </ul>
     */
    @Test
    public void testNestingLimitIgnored() throws IOException {
        int limit = 10;
        JsonArray json = new JsonArray();
        JsonArray current = json;
        // This adds additional `limit` nested arrays, so in total there are `limit + 1` arrays
        for (int i = 0; i < limit; i++) {
            JsonArray nested = new JsonArray();
            current.add(nested);
            current = nested;
        }
        JsonTreeReader reader = new JsonTreeReader(json);
        reader.setNestingLimit(limit);
        assertThat(reader.getNestingLimit()).isEqualTo(limit);
        for (int i = 0; i < limit; i++) {
            reader.beginArray();
        }
        // Does not throw exception; limit is ignored
        reader.beginArray();
        reader.endArray();
        for (int i = 0; i < limit; i++) {
            reader.endArray();
        }
        assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
        reader.close();
    }
}