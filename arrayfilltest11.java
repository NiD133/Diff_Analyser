package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.lang3.function.FailableIntFunction;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArrayFill#fill(Object[], FailableIntFunction)}.
 */
public class ArrayFillTest extends AbstractLangTest {

    @Test
    void testFillWithGenerator_shouldPopulateArray() throws Exception {
        // Arrange
        final Integer[] actualArray = new Integer[5];
        final Integer[] expectedArray = {0, 1, 2, 3, 4};

        // Act
        final Integer[] resultArray = ArrayFill.fill(actualArray, Integer::valueOf);

        // Assert
        assertSame(actualArray, resultArray, "The method should modify the array in-place and return it.");
        assertArrayEquals(expectedArray, resultArray, "The array should be filled with values from the generator.");
    }

    @Test
    void testFillWithGenerator_whenArrayIsNull_shouldReturnNull() throws Exception {
        // The generator function is irrelevant when the array is null.
        assertNull(ArrayFill.fill(null, Integer::valueOf), "Filling a null array should return null.");
    }

    @Test
    void testFillWithGenerator_whenArrayIsEmpty_shouldReturnSameInstance() throws Exception {
        // Arrange
        final String[] emptyArray = ArrayUtils.EMPTY_STRING_ARRAY;

        // Act
        final String[] resultArray = ArrayFill.fill(emptyArray, i -> "Value" + i);

        // Assert
        assertSame(emptyArray, resultArray, "Filling an empty array should return the same instance.");
    }

    @Test
    void testFillWithGenerator_whenGeneratorIsNullAndArrayIsEmpty_shouldDoNothing() throws Exception {
        // Arrange
        final FailableIntFunction<Object, ?> nullGenerator = null;
        final Object[] emptyArray = ArrayUtils.EMPTY_OBJECT_ARRAY;

        // Act
        final Object[] resultArray = ArrayFill.fill(emptyArray, nullGenerator);

        // Assert
        assertSame(emptyArray, resultArray, "Should return the same instance for an empty array, even with a null generator.");
    }

    @Test
    void testFillWithGenerator_whenGeneratorIsNullAndArrayIsNotEmpty_shouldThrowException() {
        // Arrange
        final Integer[] array = new Integer[3];
        final FailableIntFunction<Integer, ?> nullGenerator = null;

        // Act & Assert
        // The underlying java.util.Arrays.setAll throws a NullPointerException for a null generator.
        assertThrows(NullPointerException.class, () -> {
            ArrayFill.fill(array, nullGenerator);
        });
    }
}