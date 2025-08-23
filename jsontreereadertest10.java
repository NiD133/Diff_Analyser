package com.google.gson.internal.bind;

import com.google.gson.common.MoreAsserts;
import com.google.gson.stream.JsonReader;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * Verifies that {@link JsonTreeReader} correctly overrides the necessary methods
 * from its superclass, {@link JsonReader}.
 */
public class JsonTreeReaderOverridesTest {

    /**
     * A list of method signatures from {@link JsonReader} that should NOT be overridden
     * by {@link JsonTreeReader}.
     * <p>
     * These methods are related to configuring a stream-based parser (e.g., setting
     * leniency or resource limits), which is not applicable when reading from an
     * in-memory {@link com.google.gson.JsonElement} tree.
     */
    private static final List<String> IGNORED_STREAM_CONFIG_METHODS = Arrays.asList(
        "setLenient(boolean)",
        "isLenient()",
        "setStrictness(com.google.gson.Strictness)",
        "getStrictness()",
        "setNestingLimit(int)",
        "getNestingLimit()"
    );

    @Test
    public void shouldOverrideAllRelevantJsonReaderMethods() {
        /*
         * The JsonTreeReader class provides a JsonReader API over an in-memory JsonElement,
         * completely replacing the stream-based parsing logic of its superclass. To ensure
         * it functions as a valid substitute, it is crucial that it overrides all relevant
         * methods from JsonReader.
         *
         * This test asserts that all public and protected methods are overridden,
         * excluding those that are specific to stream configuration.
         */
        MoreAsserts.assertOverridesMethods(
            JsonReader.class,
            JsonTreeReader.class,
            IGNORED_STREAM_CONFIG_METHODS
        );
    }
}