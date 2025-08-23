package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the remove functionality of the {@link ConverterSet}.
 */
public class ConverterSetTest {

    // Descriptive constant names for the test converters
    private static final Converter BOOLEAN_CONVERTER = new TestConverter(Boolean.class);
    private static final Converter CHARACTER_CONVERTER = new TestConverter(Character.class);
    private static final Converter BYTE_CONVERTER = new TestConverter(Byte.class);
    private static final Converter SHORT_CONVERTER = new TestConverter(Short.class);

    @Test
    public void remove_whenConverterExists_shouldReturnSmallerSetAndLeaveOriginalUnchanged() {
        // Arrange: Create a set with four distinct converters.
        Converter[] initialConverters = new Converter[] {
            BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER
        };
        ConverterSet originalSet = new ConverterSet(initialConverters);
        int initialSize = originalSet.size();

        // Act: Remove an existing converter.
        // The second parameter (removed) is null, as we don't need to capture the removed item.
        ConverterSet newSet = originalSet.remove(BYTE_CONVERTER, null);

        // Assert: Verify the original set is immutable and the new set is smaller.
        assertEquals("The original set should be immutable and its size should not change.",
                     initialSize, originalSet.size());
        assertEquals("The new set should contain one less converter.",
                     initialSize - 1, newSet.size());
    }

    /**
     * A simple, reusable implementation of Converter for testing purposes.
     */
    private static class TestConverter implements Converter {
        private final Class<?> type;

        TestConverter(Class<?> type) {
            this.type = type;
        }

        @Override
        public Class<?> getSupportedType() {
            return type;
        }
    }
}