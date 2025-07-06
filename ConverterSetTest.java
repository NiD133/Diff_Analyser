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
 * JUnit test suite for the ConverterSet class.
 * Primarily focused on coverage and basic functionality.
 */
public class TestConverterSet extends TestCase {

    // Define test converters with different supported types
    private static final Converter BOOLEAN_CONVERTER = new Converter() {
        public Class getSupportedType() { return Boolean.class; }
    };
    private static final Converter CHARACTER_CONVERTER = new Converter() {
        public Class getSupportedType() { return Character.class; }
    };
    private static final Converter BYTE_CONVERTER = new Converter() {
        public Class getSupportedType() { return Byte.class; }
    };
    private static final Converter SHORT_CONVERTER = new Converter() {
        public Class getSupportedType() { return Short.class; }
    };
    private static final Converter DUPLICATE_SHORT_CONVERTER = new Converter() {
        public Class getSupportedType() { return Short.class; }
    };
    private static final Converter INTEGER_CONVERTER = new Converter() {
        public Class getSupportedType() { return Integer.class; }
    };

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestConverterSet.class);
    }

    public TestConverterSet(String name) {
        super(name);
    }

    // Test the visibility of the ConverterSet class and its constructor
    public void testClassVisibility() throws Exception {
        Class<?> cls = ConverterSet.class;
        assertFalse("Class should not be public", Modifier.isPublic(cls.getModifiers()));
        assertFalse("Class should not be protected", Modifier.isProtected(cls.getModifiers()));
        assertFalse("Class should not be private", Modifier.isPrivate(cls.getModifiers()));

        Constructor<?> constructor = cls.getDeclaredConstructors()[0];
        assertFalse("Constructor should not be public", Modifier.isPublic(constructor.getModifiers()));
        assertFalse("Constructor should not be protected", Modifier.isProtected(constructor.getModifiers()));
        assertFalse("Constructor should not be private", Modifier.isPrivate(constructor.getModifiers()));
    }

    // Test the selection of converters from a set
    public void testConverterSelection() {
        Converter[] converters = { BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER };
        ConverterSet converterSet = new ConverterSet(converters);

        // Select various types to test the selection mechanism
        converterSet.select(Boolean.class);
        converterSet.select(Character.class);
        converterSet.select(Byte.class);
        converterSet.select(Short.class);
        converterSet.select(Integer.class);
        converterSet.select(Long.class);
        converterSet.select(Float.class);
        converterSet.select(Double.class);
        converterSet.select(null);
        converterSet.select(Calendar.class);
        converterSet.select(GregorianCalendar.class);
        converterSet.select(DateTime.class);
        converterSet.select(DateMidnight.class);
        converterSet.select(ReadableInstant.class);
        converterSet.select(ReadableDateTime.class);
        converterSet.select(ReadWritableInstant.class);
        converterSet.select(ReadWritableDateTime.class);
        converterSet.select(DateTime.class);

        assertEquals("Converter set size should be 4", 4, converterSet.size());
    }

    // Test adding a new converter to the set
    public void testAddConverter() {
        Converter[] converters = { BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER };
        ConverterSet originalSet = new ConverterSet(converters);

        // Add a new converter and verify the size
        ConverterSet newSet = originalSet.add(INTEGER_CONVERTER, null);
        assertEquals("Original set size should remain 4", 4, originalSet.size());
        assertEquals("New set size should be 5", 5, newSet.size());
    }

    // Test adding a duplicate converter to the set
    public void testAddDuplicateConverter() {
        Converter[] converters = { BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER };
        ConverterSet originalSet = new ConverterSet(converters);

        // Add a duplicate converter and verify the set remains unchanged
        ConverterSet newSet = originalSet.add(SHORT_CONVERTER, null);
        assertSame("Set should remain unchanged when adding duplicate", originalSet, newSet);
    }

    // Test adding a converter with the same type but different instance
    public void testAddSameTypeDifferentInstance() {
        Converter[] converters = { BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER };
        ConverterSet originalSet = new ConverterSet(converters);

        // Add a converter with the same type but different instance
        ConverterSet newSet = originalSet.add(DUPLICATE_SHORT_CONVERTER, null);
        assertNotSame("Set should change when adding same type but different instance", originalSet, newSet);
        assertEquals("Original set size should remain 4", 4, originalSet.size());
        assertEquals("New set size should remain 4", 4, newSet.size());
    }

    // Test removing a converter from the set
    public void testRemoveConverter() {
        Converter[] converters = { BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER };
        ConverterSet originalSet = new ConverterSet(converters);

        // Remove a converter and verify the size
        ConverterSet newSet = originalSet.remove(BYTE_CONVERTER, null);
        assertEquals("Original set size should remain 4", 4, originalSet.size());
        assertEquals("New set size should be 3", 3, newSet.size());
    }

    // Test removing a non-existent converter from the set
    public void testRemoveNonExistentConverter() {
        Converter[] converters = { BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER };
        ConverterSet originalSet = new ConverterSet(converters);

        // Attempt to remove a non-existent converter and verify the set remains unchanged
        ConverterSet newSet = originalSet.remove(INTEGER_CONVERTER, null);
        assertSame("Set should remain unchanged when removing non-existent converter", originalSet, newSet);
    }

    // Test removing a converter by an invalid index
    public void testRemoveByInvalidIndex() {
        Converter[] converters = { BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER };
        ConverterSet originalSet = new ConverterSet(converters);

        // Attempt to remove a converter by an invalid index and expect an exception
        try {
            originalSet.remove(200, null);
            fail("Expected IndexOutOfBoundsException for invalid index");
        } catch (IndexOutOfBoundsException expected) {
            // Expected exception
        }
        assertEquals("Set size should remain 4 after invalid index removal", 4, originalSet.size());
    }

    // Test removing a converter by a negative index
    public void testRemoveByNegativeIndex() {
        Converter[] converters = { BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER };
        ConverterSet originalSet = new ConverterSet(converters);

        // Attempt to remove a converter by a negative index and expect an exception
        try {
            originalSet.remove(-1, null);
            fail("Expected IndexOutOfBoundsException for negative index");
        } catch (IndexOutOfBoundsException expected) {
            // Expected exception
        }
        assertEquals("Set size should remain 4 after negative index removal", 4, originalSet.size());
    }
}