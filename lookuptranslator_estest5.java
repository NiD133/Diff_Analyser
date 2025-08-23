package org.apache.commons.lang3.text.translate;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * This test class contains tests for the LookupTranslator.
 * This particular test focuses on its behavior with invalid input indices.
 */
// The original test class name is preserved for context.
public class LookupTranslator_ESTestTest5 extends LookupTranslator_ESTest_scaffolding {

    /**
     * Verifies that the translate method throws a StringIndexOutOfBoundsException
     * when the provided index is outside the bounds of the input CharSequence.
     */
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void translateShouldThrowExceptionForOutOfBoundsIndex() throws IOException {
        // Arrange: Create a translator with an empty lookup table.
        // The content of the lookup table is irrelevant for this boundary check.
        final CharSequence[][] emptyLookupTable = new CharSequence[0][0];
        final LookupTranslator translator = new LookupTranslator(emptyLookupTable);

        final CharSequence emptyInput = "";
        final Writer writer = new StringWriter();
        // Any index >= 0 is out of bounds for an empty string. We use 1 for clarity.
        final int outOfBoundsIndex = 1;

        // Act: Attempt to translate at an index that does not exist in the input.
        // The @Test(expected=...) annotation handles the assertion.
        translator.translate(emptyInput, outOfBoundsIndex, writer);
    }
}