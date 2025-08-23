package org.joda.time;

import org.joda.time.base.AbstractPartial;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the AbstractPartial class, focusing on the getFieldType() method.
 */
class AbstractPartialTest {

    /**
     * A concrete mock implementation of AbstractPartial for testing.
     * It represents a partial date with two fields: year and monthOfYear.
     * The chronology is fixed to BuddhistChronology in UTC.
     */
    private static class YearMonthPartial extends AbstractPartial {

        private final int[] values = new int[]{2513, 1}; // Buddhist year 2513 is 1970 AD

        @Override
        protected DateTimeField getField(int index, Chronology chrono) {
            switch (index) {
                case 0:
                    return chrono.year();
                case 1:
                    return chrono.monthOfYear();
                default:
                    throw new IndexOutOfBoundsException("Invalid index: " + index);
            }
        }

        @Override
        public int size() {
            return 2;
        }

        @Override
        public int getValue(int index) {
            return values[index];
        }

        @Override
        public Chronology getChronology() {
            // Using a non-ISO chronology to ensure the test correctly uses the partial's chronology.
            return BuddhistChronology.getInstanceUTC();
        }
    }

    @Test
    void getFieldType_shouldReturnCorrectTypeForValidIndex() {
        // Arrange
        ReadablePartial partial = new YearMonthPartial();

        // Act & Assert
        assertEquals(DateTimeFieldType.year(), partial.getFieldType(0));
        assertEquals(DateTimeFieldType.monthOfYear(), partial.getFieldType(1));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 2})
    void getFieldType_shouldThrowExceptionForInvalidIndex(int invalidIndex) {
        // Arrange
        ReadablePartial partial = new YearMonthPartial();

        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> partial.getFieldType(invalidIndex));
    }
}