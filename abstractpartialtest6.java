package org.joda.time;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.base.AbstractPartial;
import org.joda.time.field.AbstractPartialFieldProperty;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;

/**
 * Unit test for the equals() method in AbstractPartialFieldProperty.
 *
 * This test verifies the contract of the equals() method by comparing a property
 * against itself, other equal instances, and instances that differ by field,
 * value, chronology, or type.
 */
public class TestAbstractPartialFieldPropertyEquals extends TestCase {

    public static TestSuite suite() {
        return new TestSuite(TestAbstractPartialFieldPropertyEquals.class);
    }

    // A base property for testing, representing the 'year' field of our mock partial.
    // It is used as the reference object in the equality checks.
    static class YearPropertyOfBuddhistPartial extends AbstractPartialFieldProperty {
        private final YearMonthBuddhistPartial partial = new YearMonthBuddhistPartial();

        @Override
        public DateTimeField getField() {
            return partial.getField(0); // Year field
        }

        @Override
        public ReadablePartial getReadablePartial() {
            return partial;
        }

        @Override
        public int get() {
            return partial.getValue(0); // Year value
        }
    }

    // A property representing a different field ('month') of the same partial.
    static class MonthPropertyOfBuddhistPartial extends AbstractPartialFieldProperty {
        private final YearMonthBuddhistPartial partial = new YearMonthBuddhistPartial();

        @Override
        public DateTimeField getField() {
            return partial.getField(1); // Month field
        }

        @Override
        public ReadablePartial getReadablePartial() {
            return partial;
        }

        @Override
        public int get() {
            return partial.getValue(1); // Month value
        }
    }

    // A property that has the same partial and field but reports a different value.
    static class PropertyWithDifferentValue extends YearPropertyOfBuddhistPartial {
        @Override
        public int get() {
            return 2023; // Different value
        }
    }

    // A property that has the same partial but reports a different DateTimeField.
    static class PropertyWithDifferentFieldType extends YearPropertyOfBuddhistPartial {
        @Override
        public DateTimeField getField() {
            return BuddhistChronology.getInstanceUTC().hourOfDay(); // Different field
        }
    }

    // A property that has a partial with a different Chronology (ISO instead of Buddhist).
    static class PropertyWithDifferentChronology extends YearPropertyOfBuddhistPartial {
        @Override
        public ReadablePartial getReadablePartial() {
            return new YearMonthBuddhistPartial() {
                @Override
                public Chronology getChronology() {
                    return ISOChronology.getInstanceUTC(); // Different chronology
                }
            };
        }
    }

    public void testEquals_contract() {
        AbstractPartialFieldProperty baseProperty = new YearPropertyOfBuddhistPartial();

        // 1. Test reflexivity: an object must be equal to itself.
        assertTrue("Property should be equal to itself.", baseProperty.equals(baseProperty));

        // 2. Test for equality with another instance having the same state.
        assertTrue("Property should be equal to a new instance with the same state.",
                baseProperty.equals(new YearPropertyOfBuddhistPartial()));

        // 3. Test for inequality with a property of a different field.
        AbstractPartialFieldProperty differentFieldProperty = new MonthPropertyOfBuddhistPartial();
        assertFalse("Property should not be equal to a property with a different field.",
                baseProperty.equals(differentFieldProperty));

        // 4. Test for inequality with a property having a different value.
        AbstractPartialFieldProperty differentValueProperty = new PropertyWithDifferentValue();
        assertFalse("Property should not be equal to a property with a different value.",
                baseProperty.equals(differentValueProperty));

        // 5. Test for inequality with a property having a different DateTimeField type.
        AbstractPartialFieldProperty differentDateTimeFieldTypeProperty = new PropertyWithDifferentFieldType();
        assertFalse("Property should not be equal to a property with a different DateTimeField type.",
                baseProperty.equals(differentDateTimeFieldTypeProperty));

        // 6. Test for inequality with a property having a different chronology.
        AbstractPartialFieldProperty differentChronologyProperty = new PropertyWithDifferentChronology();
        assertFalse("Property should not be equal to a property with a different chronology.",
                baseProperty.equals(differentChronologyProperty));

        // 7. Test for inequality with different object types.
        assertFalse("Property should not be equal to a String.", baseProperty.equals("a string"));
        assertFalse("Property should not be equal to null.", baseProperty.equals(null));
    }

    // A mock partial for testing, representing year and month in Buddhist chronology.
    private static class YearMonthBuddhistPartial extends AbstractPartial {
        // Corresponds to year=1970, month=1
        private final int[] values = new int[] { 1970, 1 };

        @Override
        protected DateTimeField getField(int index, Chronology chrono) {
            switch(index) {
                case 0:
                    return chrono.year();
                case 1:
                    return chrono.monthOfYear();
                default:
                    throw new IndexOutOfBoundsException();
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
            return BuddhistChronology.getInstanceUTC();
        }
    }
}