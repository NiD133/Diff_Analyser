package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.DupDetector;
import com.fasterxml.jackson.core.JsonStreamContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for the extensibility of {@link JsonReadContext}.
 * <p>
 * This test verifies that {@link JsonReadContext} can be subclassed, a feature
 * enabled by making the class non-final in jackson-core version 2.19.
 *
 * @see <a href="https://github.com/FasterXML/jackson-core/issues/1421">jackson-core#1421</a>
 */
class JsonReadContextExtensionTest {

    /**
     * A simple subclass of {@link JsonReadContext} used solely to verify
     * that the superclass is extensible and its protected constructor is accessible.
     */
    private static class CustomJsonReadContext extends JsonReadContext {
        public CustomJsonReadContext(JsonReadContext parent, int nestingDepth, DupDetector dups,
                                     int type, int lineNr, int colNr) {
            super(parent, nestingDepth, dups, type, lineNr, colNr);
        }
    }

    @Test
    @DisplayName("Should allow subclassing of JsonReadContext")
    void canBeExtended() {
        // This test's primary purpose is to ensure that instantiating a subclass does not fail.
        // This confirms that JsonReadContext is non-final and its constructor is accessible.
        CustomJsonReadContext context = assertDoesNotThrow(() ->
            new CustomJsonReadContext(
                    /* parent */ null,
                    /* nestingDepth */ 0,
                    /* dups */ null,
                    /* type */ JsonStreamContext.TYPE_ROOT,
                    /* lineNr */ 0,
                    /* colNr */ 0
            ),
            "Instantiation of a JsonReadContext subclass should be possible."
        );

        // Further verify that the object was created successfully.
        assertNotNull(context, "The context instance should not be null after creation.");
    }
}