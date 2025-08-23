package org.joda.time.convert;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit tests for the ConverterSet class, focusing on removal operations.
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

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestConverterSet.class);
    }

    //-----------------------------------------------------------------------

    /**
     * Tests that calling remove() with an out-of-bounds index throws
     * an IndexOutOfBoundsException and does not alter the original set.
     */
    public void testRemoveByIndex_throwsException_forInvalidIndex() {
        // Arrange: Create a set with a known size.
        Converter[] initialConverters = new Converter[] {
            BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER
        };
        ConverterSet set = new ConverterSet(initialConverters);
        final int originalSize = set.size();
        assertEquals(4, originalSize);

        // Act & Assert: Attempt to remove with an index that is too high.
        try {
            set.remove(200, null);
            fail("Expected IndexOutOfBoundsException for a large positive index.");
        } catch (IndexOutOfBoundsException ex) {
            // This is the expected behavior.
        }

        // Act & Assert: Attempt to remove with a negative index.
        try {
            set.remove(-1, null);
            fail("Expected IndexOutOfBoundsException for a negative index.");
        } catch (IndexOutOfBoundsException ex) {
            // This is the expected behavior.
        }

        // Final Assertion: Verify the original set remains unchanged, confirming immutability.
        assertEquals("The original set should not be modified after a failed removal.",
                     originalSize, set.size());
    }
}