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

public class JsonTreeWriterTestTest22 {

    /**
     * {@link JsonTreeWriter} effectively replaces the complete writing logic of {@link JsonWriter} to
     * create a {@link JsonElement} tree instead of writing to a {@link Writer}. Therefore all
     * relevant methods of {@code JsonWriter} must be overridden.
     */
    @Test
    public void testOverrides() {
        List<String> ignoredMethods = Arrays.asList("setLenient(boolean)", "isLenient()", "setStrictness(com.google.gson.Strictness)", "getStrictness()", "setIndent(java.lang.String)", "setHtmlSafe(boolean)", "isHtmlSafe()", "setFormattingStyle(com.google.gson.FormattingStyle)", "getFormattingStyle()", "setSerializeNulls(boolean)", "getSerializeNulls()");
        MoreAsserts.assertOverridesMethods(JsonWriter.class, JsonTreeWriter.class, ignoredMethods);
    }
}
