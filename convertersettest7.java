package org.joda.time.convert;

import junit.framework.TestCase;
import org.joda.time.convert.ConverterSet.Converter;

/**
 * Unit tests for the ConverterSet class, focusing on the remove() method.
 */
public class ConverterSetTest extends TestCase {

    // A converter for Boolean types.
    private static final Converter BOOLEAN_CONVERTER = new Converter() {
        public Class<?> getSupportedType() {
            return Boolean.class;
        }
    };

    // A converter for Character types.
    private static final Converter CHARACTER_CONVERTER = new Converter() {
        public Class<?> getSupportedType() {
            return Character.class;
        }
    };

    // A converter for Byte types.
    private static final Converter BYTE_CONVERTER = new Converter() {
        public Class<?> getSupportedType() {
            return Byte.class;
        }
    };

    // A converter for Short types.
    private static final Converter SHORT_CONVERTER = new Converter() {
        public Class<?> getSupportedType() {
            return Short.class;
        }
    };

    // A converter for Integer types, used for removal attempts.
    private static final Converter INTEGER_CONVERTER = new Converter() {
        public Class<?> getSupportedType() {
            return Integer.class;
        }
    };

    /**
     * Tests that calling remove() with a converter that is not in the set
     * returns the original set instance, confirming immutability in this scenario.
     */
    public void testRemove_whenConverterIsNotInSet_returnsSameInstance() {
        // Arrange: Create a set with a few converters.
        Converter[] initialConverters = new Converter[]{
                BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER
        };
        ConverterSet initialSet = new ConverterSet(initialConverters);

        // The converter to be removed is intentionally not present in the initial set.
        Converter converterToRemove = INTEGER_CONVERTER;

        // Act: Attempt to remove the non-existent converter.
        ConverterSet resultSet = initialSet.remove(converterToRemove, null);

        // Assert: The returned set should be the exact same instance as the original.
        // This is because the set is immutable and no change was made.
        assertSame("Expected the original set instance when removing a non-existent converter",
                initialSet, resultSet);
    }
}