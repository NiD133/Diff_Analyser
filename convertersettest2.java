package org.joda.time.convert;

import java.util.Calendar;
import java.util.GregorianCalendar;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.ReadWritableDateTime;
import org.joda.time.ReadWritableInstant;
import org.joda.time.ReadableDateTime;
import org.joda.time.ReadableInstant;

/**
 * Tests the behavior of the ConverterSet, focusing on the purity of the select() method.
 */
public class ConverterSetTestTest2 extends TestCase {

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
     * Verifies that the select() method does not modify the size of the ConverterSet.
     * <p>
     * The ConverterSet uses an internal fixed-size cache (a hashtable of size 16)
     * for the select() operation. This test calls select() more than 16 times with
     * various types to ensure that the set's external state (its size) remains
     * unchanged, even when the internal cache is filled and must handle collisions
     * or lookups for uncached types. This confirms that select() is a pure query
     * method.
     */
    public void testSelectDoesNotAlterSetSize() {
        // Arrange: Create a set with a known number of converters.
        Converter[] initialConverters = new Converter[] {
            BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER
        };
        ConverterSet set = new ConverterSet(initialConverters);
        final int expectedSize = 4;
        assertEquals("Pre-condition: Initial set size should be correct.", expectedSize, set.size());

        // Act: Call select() numerous times with a diverse set of types.
        // This includes types present in the set, types not in the set, null,
        // classes, subclasses, and interfaces to thoroughly exercise the caching mechanism.
        set.select(Boolean.class);            // In set
        set.select(Character.class);          // In set
        set.select(Byte.class);               // In set
        set.select(Short.class);              // In set

        set.select(Integer.class);            // Not in set
        set.select(Long.class);               // Not in set
        set.select(Float.class);              // Not in set
        set.select(Double.class);             // Not in set

        set.select(null);                     // Null type
        set.select(Calendar.class);           // Standard library class
        set.select(GregorianCalendar.class);  // Subclass
        set.select(DateTime.class);           // Joda-Time class
        set.select(DateMidnight.class);       // Joda-Time subclass
        set.select(ReadableInstant.class);    // Joda-Time interface
        set.select(ReadableDateTime.class);   // Joda-Time interface
        set.select(ReadWritableInstant.class);// Joda-Time interface
        set.select(ReadWritableDateTime.class);// Joda-Time interface
        
        set.select(DateTime.class);           // Repeated call

        // Assert: The size of the set should not have changed.
        assertEquals("The set size must remain unchanged after multiple select() calls.", expectedSize, set.size());
    }
}