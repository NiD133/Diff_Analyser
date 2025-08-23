package com.google.gson.internal.bind;

import com.google.gson.common.MoreAsserts;
import com.google.gson.stream.JsonWriter;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    /**
     * JsonTreeWriter is a custom JsonWriter that builds a JsonElement tree in memory
     * instead of writing to an output stream. To achieve this, it must override all
     * core writing methods from the base {@link JsonWriter}.
     *
     * <p>This test verifies that all relevant methods are indeed overridden. This ensures
     * that no writing operations accidentally fall back to the base JsonWriter implementation,
     * which would fail because JsonTreeWriter is initialized with an unusable Writer.
     */
    @Test
    public void allWriterMethodsAreOverridden() {
        // These methods from JsonWriter are not relevant to JsonTreeWriter because they
        // configure the output format (e.g., indentation, HTML safety), which does not
        // apply when building an in-memory JSON tree.
        List<String> nonOverriddenMethods = Arrays.asList(
            // Leniency and Strictness settings
            "setLenient(boolean)",
            "isLenient()",
            "setStrictness(com.google.gson.Strictness)",
            "getStrictness()",

            // Output formatting settings
            "setIndent(java.lang.String)",
            "setHtmlSafe(boolean)",
            "isHtmlSafe()",
            "setFormattingStyle(com.google.gson.FormattingStyle)",
            "getFormattingStyle()",

            // Null serialization setting
            "setSerializeNulls(boolean)",
            "getSerializeNulls()"
        );

        // Verify that JsonTreeWriter overrides all methods from JsonWriter,
        // except for the configuration methods listed above.
        MoreAsserts.assertOverridesMethods(JsonWriter.class, JsonTreeWriter.class, nonOverriddenMethods);
    }
}