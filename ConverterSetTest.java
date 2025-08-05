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
 * Primarily for coverage and validation of ConverterSet functionality.
 */
public class TestConverterSet extends TestCase {

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

    public void testConverterSelection() {
        Converter[] converters = { BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER };
        ConverterSet converterSet = new ConverterSet(converters);

        // Test selection of various types
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

    public void testAddConverter() {
        Converter[] converters = { BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER };
        ConverterSet originalSet = new ConverterSet(converters);

        ConverterSet newSet = originalSet.add(INTEGER_CONVERTER, null);
        assertEquals("Original set size should remain 4", 4, originalSet.size());
        assertEquals("New set size should be 5", 5, newSet.size());

        ConverterSet sameSet = originalSet.add(SHORT_CONVERTER, null);
        assertSame("Adding existing converter should return the same set", originalSet, sameSet);

        ConverterSet duplicateSet = originalSet.add(DUPLICATE_SHORT_CONVERTER, null);
        assertNotSame("Adding duplicate converter should return a different set", originalSet, duplicateSet);
        assertEquals("Original set size should remain 4", 4, originalSet.size());
        assertEquals("Duplicate set size should be 4", 4, duplicateSet.size());
    }

    public void testRemoveConverter() {
        Converter[] converters = { BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER };
        ConverterSet originalSet = new ConverterSet(converters);

        ConverterSet reducedSet = originalSet.remove(BYTE_CONVERTER, null);
        assertEquals("Original set size should remain 4", 4, originalSet.size());
        assertEquals("Reduced set size should be 3", 3, reducedSet.size());

        ConverterSet sameSet = originalSet.remove(INTEGER_CONVERTER, null);
        assertSame("Removing non-existent converter should return the same set", originalSet, sameSet);
    }

    public void testRemoveByIndex() {
        Converter[] converters = { BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER };
        ConverterSet converterSet = new ConverterSet(converters);

        try {
            converterSet.remove(200, null);
            fail("Expected IndexOutOfBoundsException for invalid index");
        } catch (IndexOutOfBoundsException expected) {
            // Expected exception
        }
        assertEquals("Converter set size should remain 4", 4, converterSet.size());

        try {
            converterSet.remove(-1, null);
            fail("Expected IndexOutOfBoundsException for negative index");
        } catch (IndexOutOfBoundsException expected) {
            // Expected exception
        }
        assertEquals("Converter set size should remain 4", 4, converterSet.size());
    }
}