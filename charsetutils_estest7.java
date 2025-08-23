package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

// The original test class name and inheritance are preserved as the full test suite context is not available.
public class CharSetUtils_ESTestTest7 extends CharSetUtils_ESTest_scaffolding {

    /**
     * Tests that CharSetUtils.squeeze correctly handles a set defined by a String array
     * that contains a null element.
     *
     * <p>The squeeze operation should ignore the null element and use the characters from the
     * other non-null elements in the set. In this case, 'f' and 'n' are in the set,
     * so consecutive occurrences ("ff" and "nn") in the input string are squeezed to a
     * single character.</p>
     */
    @Test
    public void testSqueezeWithSetContainingNullElement() {
        // Arrange
        final String input = "offset cannot be negative";
        final String expected = "ofset canot be negative";

        // The set contains the characters to be squeezed ('f' and 'n') and a null element,
        // which the method should gracefully ignore.
        final String[] characterSet = {"fn", null};

        // Act
        final String result = CharSetUtils.squeeze(input, characterSet);

        // Assert
        assertEquals(expected, result);
    }
}