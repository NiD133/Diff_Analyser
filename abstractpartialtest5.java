package org.joda.time;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.base.AbstractPartial;
import org.joda.time.chrono.BuddhistChronology;

/**
 * Unit tests for the base implementation of partial date/time classes,
 * {@link org.joda.time.base.AbstractPartial}.
 *
 * <p>This test class verifies the behavior of concrete methods in AbstractPartial
 * by using a minimal mock implementation of the abstract methods.
 */
public class TestAbstractPartial extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestAbstractPartial.class);
    }

    //-----------------------------------------------------------------------

    /**
     * A mock implementation of AbstractPartial for testing purposes.
     * It defines a partial with two fields: year and monthOfYear,
     * using the Buddhist chronology.
     */
    private static class MockPartial extends AbstractPartial {

        /**
         * Returns the field at the specified index.
         *
         * @param index the index of the field (0 for year, 1 for monthOfYear)
         * @param chrono the chronology to use
         * @return the corresponding DateTimeField
         */
        @Override
        protected DateTimeField getField(int index, Chronology chrono) {
            switch (index) {
                case 0:
                    return chrono.year();
                case 1:
                    return chrono.monthOfYear();
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        /**
         * Returns the number of fields in this partial.
         *
         * @return the size, which is fixed at 2
         */
        @Override
        public int size() {
            return 2;
        }

        /**
         * Gets the value of the field at the specified index.
         * This mock is not concerned with actual values, so it returns a dummy value.
         *
         * @param index the index of the field
         * @return a dummy value of 0
         */
        @Override
        public int getValue(int index) {
            // The actual values are not relevant for the getFieldTypes() test.
            return 0;
        }

        /**
         * Returns the chronology of this partial.
         *
         * @return the BuddhistChronology in UTC
         */
        @Override
        public Chronology getChronology() {
            return BuddhistChronology.getInstanceUTC();
        }
    }

    //-----------------------------------------------------------------------

    /**
     * Tests that getFieldTypes() correctly derives the field types
     * from the fields defined in the partial.
     */
    public void testGetFieldTypes() {
        // Arrange
        ReadablePartial partial = new MockPartial();

        // Act
        DateTimeFieldType[] fieldTypes = partial.getFieldTypes();

        // Assert
        assertNotNull(fieldTypes);
        assertEquals("Should have 2 field types", 2, fieldTypes.length);
        assertEquals("First field type should be 'year'", DateTimeFieldType.year(), fieldTypes[0]);
        assertEquals("Second field type should be 'monthOfYear'", DateTimeFieldType.monthOfYear(), fieldTypes[1]);
    }
}