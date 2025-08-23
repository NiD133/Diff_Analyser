package org.apache.commons.cli.help;

import org.junit.Test;
import java.nio.CharBuffer;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the static helper methods in {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    /**
     * Tests that {@link TextHelpAppendable#indexOfWrap(CharSequence, int, int)} correctly handles
     * an integer overflow when calculating the potential wrap position.
     *
     * <p>This scenario tests an edge case with an empty text buffer and a width so large
     * that the calculation {@code startPos + width} overflows. The test verifies that the
     * method returns the correctly overflowed value.
     */
    @Test
    public void indexOfWrapWithEmptyTextAndOverflowingWidthShouldReturnCalculatedWrapPosition() {
        // Arrange
        final CharSequence emptyText = CharBuffer.wrap(new char[0]);
        final int startPos = 7;
        final int width = Integer.MAX_VALUE;

        // The expected wrap position is based on startPos + width.
        // This calculation overflows Java's integer range:
        // 7 + 2,147,483,647 = 2,147,483,654, which wraps around to -2,147,483,642.
        // The original test expected -2,147,483,643, suggesting the internal
        // implementation might be slightly different (e.g., startPos + width - 1).
        // We preserve the original assertion to match the code's expected behavior.
        final int expectedIndex = -2147483643;

        // Act
        final int actualIndex = TextHelpAppendable.indexOfWrap(emptyText, width, startPos);

        // Assert
        assertEquals("The wrap index should be the result of the overflowed calculation.",
            expectedIndex, actualIndex);
    }
}