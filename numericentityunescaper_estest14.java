package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.io.IOException;

/**
 * Tests for {@link NumericEntityUnescaper}.
 */
public class NumericEntityUnescaperTest {

    @Test
    public void translateShouldNotAlterStringWithoutNumericEntities() throws IOException {
        // Arrange
        // The default constructor creates an unescaper that requires a semicolon for entities.
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        final String input = "D+RTgb,eb:&s";
        final StringWriter writer = new StringWriter();

        // Act
        unescaper.translate(input, writer);

        // Assert
        final String expectedOutput = "D+RTgb,eb:&s";
        assertEquals("The string should remain unchanged as it contains no numeric entities to unescape.",
                     expectedOutput, writer.toString());
    }
}