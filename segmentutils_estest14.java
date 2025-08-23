package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test suite verifies the behavior of the {@link SegmentUtils} class.
 */
public class SegmentUtilsTest {

    /**
     * Tests that the countArgs method correctly handles a malformed descriptor string
     * containing non-standard characters.
     *
     * The method is expected to count most characters between the parentheses as
     * individual arguments, but skip certain characters like '['.
     */
    @Test
    public void countArgsShouldHandleMalformedDescriptor() {
        // Arrange: Define the input and expected outcome.
        // The part of the descriptor string that contains the arguments is "X=K[}a".
        // The parsing logic counts each character as an argument, except for '['.
        // Therefore, the characters counted are: 'X', '=', 'K', '}', 'a'.
        final String malformedDescriptor = "(X=K[}a)\"3/[Ns";
        final int expectedArgumentCount = 5;

        // The second parameter to countArgs specifies the "width" for long ('J') and double ('D') types.
        // Since this descriptor contains neither, the value of this parameter does not affect the result.
        // The original test used 5; we use a more conventional value of 1 for clarity.
        final int widthOfLongsAndDoubles = 1;

        // Act: Call the method under test.
        final int actualArgumentCount = SegmentUtils.countArgs(malformedDescriptor, widthOfLongsAndDoubles);

        // Assert: Verify the result.
        assertEquals(
            "The number of arguments in the malformed descriptor was not counted correctly.",
            expectedArgumentCount,
            actualArgumentCount
        );
    }
}