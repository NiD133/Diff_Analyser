package org.joda.time;

import org.joda.time.base.AbstractPartial;
import org.joda.time.chrono.BuddhistChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the abstract base class {@link AbstractPartial}.
 * This test focuses on the concrete implementation of the getField(int) method.
 */
public class AbstractPartialTest {

    /**
     * A minimal, concrete implementation of AbstractPartial for testing purposes.
     * It represents a partial date with two fields (year and monthOfYear)
     * using the Buddhist chronology in UTC.
     */
    private static class MockPartialForFieldTest extends AbstractPartial {
        private static final Chronology CHRONOLOGY = BuddhistChronology.getInstanceUTC();
        
        // The values are not relevant for testing getField(int) but are required by the contract.
        private final int[] values = new int[]{2562, 1}; // Example: Buddhist year 2562, Month 1

        @Override
        protected DateTimeField getField(int index, Chronology chrono) {
            switch (index) {
                case 0:
                    return chrono.year();
                case 1:
                    return chrono.monthOfYear();
                default:
                    // This is the exception that the test expects to be propagated.
                    throw new IndexOutOfBoundsException("Invalid index: " + index);
            }
        }

        @Override
        public int size() {
            return 2;
        }
        
        @Override
        public Chronology getChronology() {
            return CHRONOLOGY;
        }

        @Override
        public int getValue(int index) {
            return values[index];
        }
    }

    @Test
    public void getField_withValidIndex_returnsCorrectField() {
        // Arrange
        ReadablePartial partial = new MockPartialForFieldTest();
        Chronology chronology = BuddhistChronology.getInstanceUTC();

        // Act & Assert
        // The getField(int) method in AbstractPartial should delegate to our mock's
        // getField(int, Chronology) implementation.
        assertEquals("Field at index 0 should be the year field", chronology.year(), partial.getField(0));
        assertEquals("Field at index 1 should be the monthOfYear field", chronology.monthOfYear(), partial.getField(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getField_withNegativeIndex_throwsException() {
        // Arrange
        ReadablePartial partial = new MockPartialForFieldTest();

        // Act: This should throw IndexOutOfBoundsException
        partial.getField(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getField_withIndexEqualToSize_throwsException() {
        // Arrange
        ReadablePartial partial = new MockPartialForFieldTest();

        // Act: This should throw IndexOutOfBoundsException as valid indices are 0 and 1.
        partial.getField(2);
    }
}