package org.apache.commons.lang3.text.translate;

import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;

/**
 * Tests for {@link LookupTranslator}.
 */
public class LookupTranslatorTest {

    /**
     * Verifies that the translate method throws a NullPointerException when the input CharSequence is null.
     * The behavior is expected regardless of the translator's internal state (e.g., even with a null lookup map).
     */
    @Test(expected = NullPointerException.class)
    public void translateShouldThrowNullPointerExceptionForNullInput() {
        // Arrange: Create a translator. Its internal lookup table can be null for this test case,
        // as we are focused on how the method handles null input.
        final LookupTranslator translator = new LookupTranslator((CharSequence[][]) null);
        final Writer writer = new StringWriter();
        final int anyIndex = 0; // The index value is irrelevant as the null check on the input should occur first.

        // Act & Assert: Calling translate with a null input CharSequence should throw a NullPointerException.
        translator.translate(null, anyIndex, writer);
    }
}