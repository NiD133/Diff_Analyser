package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CharSetUtils}.
 * This class focuses on the squeeze method under specific edge case conditions.
 */
public class CharSetUtilsTest {

    /**
     * Tests that CharSetUtils.squeeze() returns the original string unchanged
     * when the set of characters to squeeze is an array containing only null elements.
     * An effectively empty set should not cause any modifications.
     */
    @Test
    public void squeezeWithSetContainingOnlyNullsShouldReturnOriginalString() {
        // Arrange
        // An input string with repeated characters that could be squeezed.
        final String input = "helllo wooorld";
        // A set that is effectively empty because it only contains nulls.
        final String[] setWithNulls = {null, null};

        // Act
        final String result = CharSetUtils.squeeze(input, setWithNulls);

        // Assert
        // The result should be identical to the input string because the set was empty.
        assertEquals(input, result);
    }
}