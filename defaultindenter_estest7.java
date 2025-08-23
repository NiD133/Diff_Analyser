package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;

import java.io.IOException;

/**
 * This test verifies the behavior of the {@link DefaultIndenter#writeIndentation} method.
 */
public class DefaultIndenterTest {

    /**
     * Verifies that calling writeIndentation with a null JsonGenerator
     * results in a NullPointerException, as the method cannot operate
     * without a generator to write to.
     */
    @Test(expected = NullPointerException.class)
    public void writeIndentation_whenGeneratorIsNull_shouldThrowNullPointerException() throws IOException {
        // Arrange: Get a standard indenter instance. The indentation level is arbitrary
        // as the null generator will cause the exception before the level is used.
        DefaultIndenter indenter = DefaultIndenter.SYSTEM_LINEFEED_INSTANCE;
        int anyIndentLevel = 1;

        // Act & Assert: This call is expected to throw a NullPointerException.
        indenter.writeIndentation(null, anyIndentLevel);
    }
}