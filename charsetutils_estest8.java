package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test suite focuses on the squeeze method of the CharSetUtils class.
 * The original test class name, CharSetUtils_ESTestTest8, is retained to provide context,
 * though a more descriptive name like CharSetUtilsSqueezeTest would be preferable in a real-world scenario.
 */
public class CharSetUtils_ESTestTest8 {

    /**
     * Tests that {@link CharSetUtils#squeeze(String, String...)} does not modify the
     * input string when its repeated characters are not present in the specified character set.
     *
     * <p>This test ensures that only characters explicitly listed in the set are squeezed.
     * For instance, in the string "bookkeeper," the repeated 'o' and 'k' should remain
     * untouched if they are not in the character set. The test also confirms that a
     * {@code null} value within the set array is handled without errors.</p>
     */
    @Test
    public void squeezeShouldNotAlterStringWhenSetLacksRepeatedChars() {
        // Arrange: Define an input string with repeated characters ('o', 'k') and a
        // character set that does not include them. A null element is included in the
        // set to ensure it is handled gracefully.
        final String input = "bookkeeper";
        final String[] characterSet = new String[]{"b", "p", "r", null};
        final String expected = "bookkeeper";

        // Act: Execute the squeeze method with the prepared inputs.
        final String result = CharSetUtils.squeeze(input, characterSet);

        // Assert: Verify that the output string is identical to the input, as none of
        // the repeated characters were in the specified set to be squeezed.
        assertEquals(expected, result);
    }
}