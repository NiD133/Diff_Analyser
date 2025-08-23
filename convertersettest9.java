package org.joda.time.convert;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test class for the remove-by-index functionality of {@link ConverterSet}.
 */
public class ConverterSetTest {

    // Descriptive names for test data make the test's purpose clearer.
    private static final Converter BOOLEAN_CONVERTER = new Converter() {
        public Class<?> getSupportedType() {
            return Boolean.class;
        }
    };
    private static final Converter CHARACTER_CONVERTER = new Converter() {
        public Class<?> getSupportedType() {
            return Character.class;
        }
    };
    private static final Converter BYTE_CONVERTER = new Converter() {
        public Class<?> getSupportedType() {
            return Byte.class;
        }
    };
    private static final Converter SHORT_CONVERTER = new Converter() {
        public Class<?> getSupportedType() {
            return Short.class;
        }
    };

    @Test
    public void removeByIndex_withNegativeIndex_throwsExceptionAndLeavesSetUnchanged() {
        // Arrange: Create a well-defined initial state for the test.
        Converter[] initialConverters = {
            BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER
        };
        ConverterSet set = new ConverterSet(initialConverters);
        int expectedSize = initialConverters.length;
        assertEquals(expectedSize, set.size(), "Pre-condition: Initial set size should be correct.");

        // Act & Assert: Verify that calling the method with an invalid index throws the correct exception.
        // Using assertThrows is a modern, clear way to test for exceptions.
        assertThrows(IndexOutOfBoundsException.class, () -> {
            set.remove(-1, null);
        });

        // Assert: Verify that the state of the object remains unchanged after the failed operation.
        assertEquals(expectedSize, set.size(), "The set size should not change after a failed removal attempt.");
    }
}