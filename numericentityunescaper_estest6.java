package org.apache.commons.lang3.text.translate;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * This test class contains tests for the {@link NumericEntityUnescaper} class,
 * focusing on boundary conditions of the translate method.
 */
// The original test extended a scaffolding class. We'll keep it for compatibility,
// but note that in a real-world scenario, we'd question its necessity.
public class NumericEntityUnescaper_ESTestTest6 extends NumericEntityUnescaper_ESTest_scaffolding {

    /**
     * Tests that translate() throws a StringIndexOutOfBoundsException when the
     * provided index is outside the bounds of the input character sequence.
     */
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void translateShouldThrowExceptionForOutOfBoundsIndex() throws IOException {
        // Arrange
        // Create an unescaper with default options. The specific options are not relevant for this test.
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        final CharSequence emptyInput = "";
        final Writer writer = new StringWriter();
        
        // Any index greater than or equal to the input's length (0) is out of bounds.
        // We use 1 for clarity.
        final int outOfBoundsIndex = 1;

        // Act & Assert
        // This call is expected to throw a StringIndexOutOfBoundsException,
        // which is declared by the @Test annotation.
        unescaper.translate(emptyInput, outOfBoundsIndex, writer);
    }
}