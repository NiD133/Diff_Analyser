package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.io.IOException;

// The original test class name and runner are kept for context,
// though in a real-world scenario, they might also be simplified.
import org.evosuite.runtime.EvoRunner;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
public class LookupTranslator_ESTestTest4 extends LookupTranslator_ESTest_scaffolding {

    /**
     * Tests that the translator correctly finds a matching CharSequence in its lookup table,
     * writes the corresponding value to the output, and returns the length of the matched key.
     */
    @Test(timeout = 4000)
    public void shouldTranslateMatchingCharSequenceAndReturnConsumedLength() throws IOException {
        // --- Arrange ---

        // Define a clear key-value pair for the lookup table.
        // Using simple strings makes the test's purpose easy to understand.
        final CharSequence keyToBeFound = "alpha";
        final CharSequence replacementValue = "α";

        // The lookup table should be an array of key-value pairs.
        final CharSequence[][] lookupTable = {
            { keyToBeFound, replacementValue }
        };

        final LookupTranslator translator = new LookupTranslator(lookupTable);
        final StringWriter writer = new StringWriter();

        // The input to be translated is the key we expect to find.
        final CharSequence input = keyToBeFound;

        // --- Act ---

        // Call the method under test. The return value is the number of characters consumed.
        final int consumedChars = translator.translate(input, 0, writer);

        // --- Assert ---

        // 1. Verify that the correct replacement value was written to the output.
        assertEquals("The translated output should be the replacement value from the lookup table.",
                     replacementValue.toString(), writer.toString());

        // 2. Verify that the number of characters consumed from the input
        //    is equal to the length of the key that was found.
        assertEquals("The number of consumed characters should match the length of the key.",
                     keyToBeFound.length(), consumedChars);
    }
}