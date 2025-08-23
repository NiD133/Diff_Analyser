package org.joda.time.convert;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit tests for ConverterSet.
 */
public class TestConverterSet extends TestCase {

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

    private static final Converter INTEGER_CONVERTER = new Converter() {
        public Class<?> getSupportedType() {
            return Integer.class;
        }
    };

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestConverterSet.class);
    }

    //-----------------------------------------------------------------------

    /**
     * Tests that adding a new, unique converter results in a new set with an
     * increased size, and that the original set remains unchanged.
     */
    public void testAdd_whenConverterIsNew_returnsLargerSetAndOriginalIsUnchanged() {
        // Arrange: Create a set with four initial converters.
        Converter[] initialConverters = new Converter[] {
            BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER
        };
        ConverterSet initialSet = new ConverterSet(initialConverters);

        // Act: Add a new, distinct converter to the set.
        // The 'removed' parameter is null because we are not interested in capturing
        // any replaced converter in this test scenario.
        ConverterSet resultSet = initialSet.add(INTEGER_CONVERTER, null);

        // Assert: Verify the original set is immutable and the new set has the correct size.
        assertEquals("Original set should remain unchanged.", 4, initialSet.size());
        assertEquals("Result set should have one more converter.", 5, resultSet.size());
    }
}