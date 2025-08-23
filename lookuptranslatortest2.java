package org.apache.commons.lang3.text.translate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link LookupTranslator}.
 */
// Renamed from LookupTranslatorTestTest2 for clarity and convention.
class LookupTranslatorTest extends AbstractLangTest {

    /**
     * Tests that the translator works correctly when the lookup table is populated
     * with CharSequence implementations that are not Strings (e.g., StringBuffer).
     * This is a regression test for LANG-882.
     *
     * @see <a href="https://issues.apache.org/jira/browse/LANG-882">LANG-882</a>
     */
    @Test
    void shouldTranslateWhenLookupKeyIsStringBuffer() throws IOException {
        // Arrange
        final CharSequence key = new StringBuffer("one");
        final CharSequence value = new StringBuffer("two");

        // The bug (LANG-882) occurred because HashMap requires keys to have a stable
        // equals/hashCode implementation, which StringBuffer lacks. The fix was to
        // convert lookup keys to String internally. This test verifies that fix.
        final LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { key, value } });

        final CharSequence inputToTranslate = new StringBuffer("one");
        final StringWriter writer = new StringWriter();

        // Act
        final int consumedChars = translator.translate(inputToTranslate, 0, writer);

        // Assert
        assertEquals(3, consumedChars, "Should consume the length of the matched key");
        assertEquals("two", writer.toString(), "Should translate to the correct value from the lookup table");
    }
}