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

public class JsonTreeReaderTestTest10 {

    /**
     * {@link JsonTreeReader} effectively replaces the complete reading logic of {@link JsonReader} to
     * read from a {@link JsonElement} instead of a {@link Reader}. Therefore all relevant methods of
     * {@code JsonReader} must be overridden.
     */
    @Test
    public void testOverrides() {
        List<String> ignoredMethods = Arrays.asList("setLenient(boolean)", "isLenient()", "setStrictness(com.google.gson.Strictness)", "getStrictness()", "setNestingLimit(int)", "getNestingLimit()");
        MoreAsserts.assertOverridesMethods(JsonReader.class, JsonTreeReader.class, ignoredMethods);
    }
}
