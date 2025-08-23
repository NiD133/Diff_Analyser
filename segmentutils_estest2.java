package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link SegmentUtils#countArgs(String, int)}.
 */
public class SegmentUtilsTest {

    @Test
    public void countArgs_shouldReturnZero_forMethodWithNoArguments() {
        // Arrange
        final String descriptor = "()V"; // A method with no arguments and void return type

        // Act
        final int argumentCount = SegmentUtils.countArgs(descriptor, 1);

        // Assert
        assertEquals(0, argumentCount);
    }

    @Test
    public void countArgs_shouldCountEachSimpleTypeAsOne() {
        // Arrange
        // A method with one of each simple primitive type (excluding long/double) and one object type.
        final String descriptor = "(BCFISZLjava/lang/String;)V";
        final int expectedCount = 7;

        // Act
        final int argumentCount = SegmentUtils.countArgs(descriptor, 1);

        // Assert
        assertEquals(expectedCount, argumentCount);
    }

    @Test
    public void countArgs_shouldCountArrayTypesAsOne() {
        // Arrange
        // A method with a primitive array and an object array. Each should count as one argument.
        final String descriptor = "([I[[Ljava/lang/Object;)V";
        final int expectedCount = 2;

        // Act
        final int argumentCount = SegmentUtils.countArgs(descriptor, 1);

        // Assert
        assertEquals(expectedCount, argumentCount);
    }

    @Test
    public void countArgs_shouldCountLongAndDoubleAsOne_whenWidthIsOne() {
        // Arrange
        final String descriptor = "(JD)V"; // A method with a long and a double
        final int widthOfLongsAndDoubles = 1;
        final int expectedCount = 2;

        // Act
        final int argumentCount = SegmentUtils.countArgs(descriptor, widthOfLongsAndDoubles);

        // Assert
        assertEquals(expectedCount, argumentCount);
    }

    @Test
    public void countArgs_shouldCountLongAndDoubleAsTwo_whenWidthIsTwo() {
        // Arrange
        // The width parameter is typically 2 when calculating stack frame sizes for invokeinterface.
        final String descriptor = "(JD)V"; // A method with a long and a double
        final int widthOfLongsAndDoubles = 2;
        final int expectedCount = 4; // Each of J and D count as 2

        // Act
        final int argumentCount = SegmentUtils.countArgs(descriptor, widthOfLongsAndDoubles);

        // Assert
        assertEquals(expectedCount, argumentCount);
    }

    @Test
    public void countArgs_shouldCorrectlyCountMixedArgumentTypes() {
        // Arrange
        // A complex descriptor with an Object, an int, a double, and a long array.
        final String descriptor = "(Ljava/lang/String;ID[J)I";
        final int widthOfLongsAndDoubles = 2;
        // Expected: 1 (String) + 1 (int) + 2 (double) + 1 (long array) = 5
        final int expectedCount = 5;

        // Act
        final int argumentCount = SegmentUtils.countArgs(descriptor, widthOfLongsAndDoubles);

        // Assert
        assertEquals(expectedCount, argumentCount);
    }
}