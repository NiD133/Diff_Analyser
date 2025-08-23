package org.jsoup.nodes;

import org.jsoup.internal.QuietAppendable;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Entities} class, focusing on the character escaping functionality.
 */
public class Entities_ESTestTest2 extends Entities_ESTest_scaffolding {

    /**
     * Verifies that the escape method correctly encodes special HTML characters,
     * such as the greater-than sign ('>').
     */
    @Test(timeout = 4000)
    public void escapeShouldCorrectlyEncodeSpecialHtmlCharacter() {
        // Arrange
        // The input string contains a '>' character, which requires HTML escaping.
        String inputString = "sup1$w1b#6@>wd6L";
        String expectedOutput = "sup1$w1b#6@&gt;wd6L";

        // A StringBuilder is used to capture the output from the escape method.
        StringBuilder stringBuilder = new StringBuilder();
        QuietAppendable appendable = new QuietAppendable(stringBuilder);

        // Default output settings are sufficient for this test.
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        // The 'options' parameter is set to 0, indicating no special handling
        // like trimming or attribute-specific escaping.
        int noOptions = 0;

        // Act
        // Execute the method under test, which will write the escaped string into the appendable.
        Entities.escape(appendable, inputString, outputSettings, noOptions);
        String actualOutput = stringBuilder.toString();

        // Assert
        // Verify that the resulting string matches the expected escaped output.
        assertEquals(expectedOutput, actualOutput);
    }
}