package org.joda.time.convert;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
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
 * Tests for the {@link ConverterSet} class.
 * <p>
 * This class focuses on testing the internal structure and behavior of
 * {@code ConverterSet}, particularly its ability to add, remove, and select
 * converters. The tests cover edge cases like adding/removing null converters
 * and handling duplicate converters.
 */
public class TestConverterSet extends TestCase {

    // Constants for creating Converter instances with specific supported types.
    private static final Converter BOOLEAN_CONVERTER = new MockConverter(Boolean.class);
    private static final Converter CHARACTER_CONVERTER = new MockConverter(Character.class);
    private static final Converter BYTE_CONVERTER = new MockConverter(Byte.class);
    private static final Converter SHORT_CONVERTER = new MockConverter(Short.class);
    private static final Converter ANOTHER_SHORT_CONVERTER = new MockConverter(Short.class); // distinct instance
    private static final Converter INTEGER_CONVERTER = new MockConverter(Integer.class);

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestConverterSet.class);
    }

    public TestConverterSet(String name) {
        super(name);
    }

    /**
     * Helper class to create anonymous Converter instances easily.
     */
    private static class MockConverter implements Converter {
        private final Class<?> supportedType;

        public MockConverter(Class<?> supportedType) {
            this.supportedType = supportedType;
        }

        @Override
        public Class<?> getSupportedType() {
            return supportedType;
        }
    }

    //-----------------------------------------------------------------------
    public void testClassDetails() throws Exception {
        Class<?> cls = ConverterSet.class;

        // Verify that the class is package-private (not public, protected, or private).
        assertFalse("ConverterSet should not be public", Modifier.isPublic(cls.getModifiers()));
        assertFalse("ConverterSet should not be protected", Modifier.isProtected(cls.getModifiers()));
        assertFalse("ConverterSet should not be private", Modifier.isPrivate(cls.getModifiers()));

        // Verify the constructor is package-private
        assertEquals(1, cls.getDeclaredConstructors().length);
        Constructor<?> con = cls.getDeclaredConstructors()[0];
        assertFalse("Constructor should not be public", Modifier.isPublic(con.getModifiers()));
        assertFalse("Constructor should not be protected", Modifier.isProtected(con.getModifiers()));
        assertFalse("Constructor should not be private", Modifier.isPrivate(con.getModifiers()));
    }

    //-----------------------------------------------------------------------
    public void testBigHashtableResizingNotTriggered() {
        Converter[] initialConverters = new Converter[] {
            BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER,
        };
        ConverterSet set = new ConverterSet(initialConverters);

        // Simulate multiple calls to 'select' with different types to verify that
        // hash table resizing is not incorrectly triggered (as the select method is not implemented).
        // The original code seemed to hint at a concern regarding hashtable resizing.
        set.select(Boolean.class);
        set.select(Character.class);
        set.select(Byte.class);
        set.select(Short.class);
        set.select(Integer.class);
        set.select(Long.class);
        set.select(Float.class);
        set.select(Double.class);
        set.select(null);
        set.select(Calendar.class);
        set.select(GregorianCalendar.class);
        set.select(DateTime.class);
        set.select(DateMidnight.class);
        set.select(ReadableInstant.class);
        set.select(ReadableDateTime.class);
        set.select(ReadWritableInstant.class);
        set.select(ReadWritableDateTime.class);
        set.select(DateTime.class);

        assertEquals(4, set.size()); // Should remain the initial size
    }

    //-----------------------------------------------------------------------
    public void testAddConverterWhenRemovedArrayIsNull() {
        Converter[] initialConverters = new Converter[] {
            BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER,
        };
        ConverterSet set = new ConverterSet(initialConverters);
        ConverterSet result = set.add(INTEGER_CONVERTER, null);

        assertEquals(4, set.size()); // Original set should be unchanged
        assertEquals(5, result.size()); // New set should have the added converter
    }

    public void testAddExistingConverterWhenRemovedArrayIsNull() {
        Converter[] initialConverters = new Converter[] {
            BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER,
        };
        ConverterSet set = new ConverterSet(initialConverters);
        ConverterSet result = set.add(SHORT_CONVERTER, null);

        assertSame(set, result); // Should return the original set (no change)
    }

    public void testAddEquivalentConverterWhenRemovedArrayIsNull() {
        Converter[] initialConverters = new Converter[] {
            BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER,
        };
        ConverterSet set = new ConverterSet(initialConverters);
        ConverterSet result = set.add(ANOTHER_SHORT_CONVERTER, null);

        assertTrue(set != result); // A new set should be created
        assertEquals(4, set.size()); // Original set unchanged
        assertEquals(4, result.size()); // new set the same size as the original because one converter has been replaced.
    }

    //-----------------------------------------------------------------------
    public void testRemoveConverterWhenRemovedArrayIsNull() {
        Converter[] initialConverters = new Converter[] {
            BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER,
        };
        ConverterSet set = new ConverterSet(initialConverters);
        ConverterSet result = set.remove(BYTE_CONVERTER, null);

        assertEquals(4, set.size()); // Original set unchanged
        assertEquals(3, result.size()); // New set should have one less converter
    }

    public void testRemoveNonExistingConverterWhenRemovedArrayIsNull() {
        Converter[] initialConverters = new Converter[] {
            BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER,
        };
        ConverterSet set = new ConverterSet(initialConverters);
        ConverterSet result = set.remove(INTEGER_CONVERTER, null);

        assertSame(set, result); // Should return the original set (no change)
    }

    //-----------------------------------------------------------------------
    public void testRemoveConverterWithInvalidIndexWhenRemovedArrayIsNull() {
        Converter[] initialConverters = new Converter[] {
            BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER,
        };
        ConverterSet set = new ConverterSet(initialConverters);

        try {
            set.remove(200, null);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Expected
        }
        assertEquals(4, set.size()); // Original set unchanged
    }

    public void testRemoveConverterWithNegativeIndexWhenRemovedArrayIsNull() {
        Converter[] initialConverters = new Converter[] {
            BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER,
        };
        ConverterSet set = new ConverterSet(initialConverters);
        try {
            set.remove(-1, null);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Expected
        }
        assertEquals(4, set.size()); // Original set unchanged
    }
}