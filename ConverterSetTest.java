package org.joda.time.convert;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.ReadWritableDateTime;
import org.joda.time.ReadWritableInstant;
import org.joda.time.ReadableDateTime;
import org.joda.time.ReadableInstant;
import org.junit.Test;

/**
 * Unit tests for ConverterSet.
 * 
 * Goals:
 * - Make test intent obvious with descriptive names and comments.
 * - Remove duplication by using helper methods and constants.
 * - Prefer clear assertions and explicit expectations.
 */
public class ConverterSetTest {

    // Converter stubs for primitive wrapper types.
    private static final Converter BOOLEAN_CONVERTER = stub(Boolean.class);
    private static final Converter CHAR_CONVERTER = stub(Character.class);
    private static final Converter BYTE_CONVERTER = stub(Byte.class);
    private static final Converter SHORT_CONVERTER = stub(Short.class);

    // Another instance supporting the same type as SHORT_CONVERTER.
    private static final Converter SHORT_CONVERTER_DIFFERENT_INSTANCE = stub(Short.class);

    private static final Converter INT_CONVERTER = stub(Integer.class);

    private static Converter stub(final Class<?> type) {
        return new Converter() {
            @Override
            public Class getSupportedType() { return type; }
        };
    }

    private static ConverterSet newSetWithDefaultConverters() {
        return new ConverterSet(new Converter[] {
            BOOLEAN_CONVERTER, CHAR_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER
        });
    }

    @Test
    public void classAndConstructor_arePackagePrivate() throws Exception {
        // Class should be package-private (no public/protected/private modifier).
        Class<?> cls = ConverterSet.class;
        assertPackagePrivate(cls.getModifiers());

        // There should be a single package-private constructor.
        assertEquals(1, cls.getDeclaredConstructors().length);
        Constructor<?> ctor = cls.getDeclaredConstructors()[0];
        assertPackagePrivate(ctor.getModifiers());
    }

    @Test
    public void select_onVariousTypes_doesNotChangeSetSize() {
        ConverterSet set = newSetWithDefaultConverters();

        // Exercise selection for many different types (including unknowns and null)
        // to populate and reuse the internal select cache (hashtable).
        Class<?>[] typesToSelect = new Class<?>[] {
            Boolean.class,
            Character.class,
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            null,
            Calendar.class,
            GregorianCalendar.class,
            DateTime.class,
            DateMidnight.class,
            ReadableInstant.class,
            ReadableDateTime.class,
            ReadWritableInstant.class,
            ReadWritableDateTime.class,
            DateTime.class, // repeat to ensure cache reuse
        };

        for (Class<?> type : typesToSelect) {
            set.select(type);
        }

        // The set is immutable; select must not modify its contents.
        assertEquals(4, set.size());
    }

    @Test
    public void add_newConverter_returnsNewSet_andLeavesOriginalUnchanged() {
        ConverterSet original = newSetWithDefaultConverters();

        Converter[] removed = new Converter[1];
        ConverterSet updated = original.add(INT_CONVERTER, removed);

        assertEquals("original size unchanged", 4, original.size());
        assertEquals("updated size increased", 5, updated.size());
        assertNull("nothing removed when adding a new supported type", removed[0]);
    }

    @Test
    public void add_sameInstanceAlreadyPresent_returnsSameSet() {
        ConverterSet original = newSetWithDefaultConverters();

        Converter[] removed = new Converter[1];
        ConverterSet updated = original.add(SHORT_CONVERTER, removed);

        assertSame("adding identical instance should return original set", original, updated);
        assertNull("nothing removed when adding identical instance", removed[0]);
    }

    @Test
    public void add_differentInstanceWithSameSupportedType_replacesExisting_butSizeUnchanged() {
        ConverterSet original = newSetWithDefaultConverters();

        Converter[] removed = new Converter[1];
        ConverterSet updated = original.add(SHORT_CONVERTER_DIFFERENT_INSTANCE, removed);

        assertNotSame("adding different instance with same supported type returns a new set", original, updated);
        assertEquals("original size unchanged", 4, original.size());
        assertEquals("updated size unchanged (replacement)", 4, updated.size());
        assertSame("removed should be the previously registered converter instance",
                   SHORT_CONVERTER, removed[0]);
    }

    @Test
    public void remove_existingConverter_returnsNewSet_andLeavesOriginalUnchanged() {
        ConverterSet original = newSetWithDefaultConverters();

        Converter[] removed = new Converter[1];
        ConverterSet updated = original.remove(BYTE_CONVERTER, removed);

        assertEquals("original size unchanged", 4, original.size());
        assertEquals("updated size decreased", 3, updated.size());
        assertSame("removed should be the converter passed in", BYTE_CONVERTER, removed[0]);
    }

    @Test
    public void remove_converterNotInSet_returnsSameSet() {
        ConverterSet original = newSetWithDefaultConverters();

        Converter[] removed = new Converter[1];
        ConverterSet updated = original.remove(INT_CONVERTER, removed);

        assertSame("removing a non-existent converter returns original set", original, updated);
        assertNull("removed should be null when nothing removed", removed[0]);
    }

    @Test
    public void remove_byIndex_withTooLargeIndex_throws_andLeavesSetUnchanged() {
        ConverterSet set = newSetWithDefaultConverters();

        try {
            set.remove(200, null);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // expected
        }

        assertEquals(4, set.size());
    }

    @Test
    public void remove_byIndex_withNegativeIndex_throws_andLeavesSetUnchanged() {
        ConverterSet set = newSetWithDefaultConverters();

        try {
            set.remove(-1, null);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // expected
        }

        assertEquals(4, set.size());
    }

    // Helpers

    private static void assertPackagePrivate(int modifiers) {
        assertFalse(Modifier.isPublic(modifiers));
        assertFalse(Modifier.isProtected(modifiers));
        assertFalse(Modifier.isPrivate(modifiers));
    }
}