package org.joda.time;

import org.joda.time.base.AbstractPartial;
import org.joda.time.chrono.BuddhistChronology;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Tests the functionality of the {@link AbstractPartial} base class.
 *
 * <p>This test suite uses a concrete mock implementation of {@link AbstractPartial}
 * to verify the behavior of its implemented methods.
 */
public class AbstractPartialTest {

    /**
     * A concrete implementation of AbstractPartial for testing purposes.
     *
     * <p>It represents a partial date with two fields: {@code year} and {@code monthOfYear}.
     * The chronology is fixed to Buddhist UTC, and the initial values are {1970, 1}.
     */
    private static class MockPartialForTest extends AbstractPartial {

        private final int[] values = new int[]{1970, 1};

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
            // A specific, non-ISO chronology is used to ensure the SUT correctly
            // uses the chronology provided by the subclass.
            return BuddhistChronology.getInstanceUTC();
        }
    }

    //-----------------------------------------------------------------------

    @Test
    public void getValues_shouldReturnAnArrayOfAllFieldValues() {
        // Arrange
        ReadablePartial partial = new MockPartialForTest();
        int[] expectedValues = {1970, 1};

        // Act
        int[] actualValues = partial.getValues();

        // Assert
        assertArrayEquals("The returned array should contain the values of all fields",
                expectedValues, actualValues);
    }
}