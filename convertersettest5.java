package org.joda.time.convert;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for ConverterSet.
 * This test focuses on the add() method.
 */
public class ConverterSetTest extends TestCase {

    // Descriptive names for converters, using a helper class to reduce boilerplate.
    private static final Converter BOOLEAN_CONVERTER = new FixedTypeConverter(Boolean.class);
    private static final Converter CHARACTER_CONVERTER = new FixedTypeConverter(Character.class);
    private static final Converter BYTE_CONVERTER = new FixedTypeConverter(Byte.class);
    private static final Converter ORIGINAL_SHORT_CONVERTER = new FixedTypeConverter(Short.class);
    private static final Converter REPLACEMENT_SHORT_CONVERTER = new FixedTypeConverter(Short.class);

    /**
     * Tests that adding a converter for a type that already exists in the set
     * replaces the original converter and returns a new set instance.
     */
    public void testAdd_whenConverterForTypeExists_replacesOriginalConverter() {
        // Arrange
        Converter[] initialConverters = new Converter[]{
            BOOLEAN_CONVERTER,
            CHARACTER_CONVERTER,
            BYTE_CONVERTER,
            ORIGINAL_SHORT_CONVERTER
        };
        ConverterSet initialSet = new ConverterSet(initialConverters);
        int expectedSize = initialConverters.length;

        // Act: Add a new converter for Short.class, which is already present.
        // The 'removed' parameter is null, as we are not testing that output parameter here.
        ConverterSet newSet = initialSet.add(REPLACEMENT_SHORT_CONVERTER, null);

        // Assert
        assertNotNull("A new set should be returned.", newSet);
        assertNotSame("A new set instance should be returned on modification to preserve immutability.", initialSet, newSet);

        assertEquals("The original set's size should not change.", expectedSize, initialSet.size());
        assertEquals("The new set should have the same size, as the operation was a replacement.", expectedSize, newSet.size());

        // Verify the content of the new set to confirm the replacement was successful.
        Converter[] finalConverters = new Converter[newSet.size()];
        newSet.copyInto(finalConverters);
        List<Converter> finalConverterList = Arrays.asList(finalConverters);

        assertTrue("The new set should contain the replacement converter.", finalConverterList.contains(REPLACEMENT_SHORT_CONVERTER));
        assertFalse("The new set should not contain the original, replaced converter.", finalConverterList.contains(ORIGINAL_SHORT_CONVERTER));
    }

    /**
     * A simple, reusable Converter implementation for tests.
     */
    private static class FixedTypeConverter implements Converter {
        private final Class<?> iType;

        FixedTypeConverter(Class<?> type) {
            this.iType = type;
        }

        @Override
        public Class<?> getSupportedType() {
            return iType;
        }

        @Override
        public String toString() {
            return "Converter<" + iType.getSimpleName() + ">";
        }
    }
}