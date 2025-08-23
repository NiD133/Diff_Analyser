package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.io.IOException;

/**
 * Unit tests for {@link NumericEntityUnescaper}.
 */
public class NumericEntityUnescaperTest {

    /**
     * Tests that the translator does not change an input string
     * that does not contain any valid numeric entities.
     */
    @Test
    public void shouldNotModifyStringContainingNoNumericEntities() throws IOException {
        // Arrange
        // The unescaper is created with default options (semiColonRequired).
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        final String input = "This string contains an ampersand & but no numeric entity.";
        final StringWriter writer = new StringWriter();

        // Act
        unescaper.translate(input, writer);

        // Assert
        assertEquals("The output string should be identical to the input string.",
                     input, writer.toString());
    }
}