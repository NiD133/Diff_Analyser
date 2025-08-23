package org.joda.time.convert;

import org.joda.time.ReadableInstant;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for ConverterSet.
 *
 * Notes:
 * - Uses real converter implementations shipped with Joda-Time.
 * - Avoids EvoSuite-specific scaffolding and brittle edge cases.
 * - Prefers clear test names and minimal assertions per behavior.
 */
public class ConverterSetTest {

    // ---------- size ----------

    @Test
    public void sizeReflectsNumberOfConverters() {
        ConverterSet set = new ConverterSet(new Converter[] {
            LongConverter.INSTANCE,
            StringConverter.INSTANCE
        });
        assertEquals(2, set.size());
    }

    // ---------- select ----------

    @Test
    public void selectReturnsMatchingConverterByType() {
        ConverterSet set = new ConverterSet(new Converter[] {
            LongConverter.INSTANCE,
            StringConverter.INSTANCE
        });

        assertSame(LongConverter.INSTANCE, set.select(Long.class));
        assertSame(StringConverter.INSTANCE, set.select(String.class));
    }

    @Test
    public void selectReturnsNullWhenNoMatch() {
        ConverterSet set = new ConverterSet(new Converter[] {
            StringConverter.INSTANCE
        });

        assertNull(set.select(Long.class));
    }

    // ---------- add ----------

    @Test(expected = NullPointerException.class)
    public void addNullConverterThrows() {
        ConverterSet set = new ConverterSet(new Converter[] { StringConverter.INSTANCE });
        set.add(null, new Converter[1]);
    }

    @Test
    public void addNewConverterReturnsNewSetAndIncreasesSize() {
        ConverterSet set = new ConverterSet(new Converter[] { LongConverter.INSTANCE });

        ConverterSet updated = set.add(StringConverter.INSTANCE, null);

        assertNotSame(set, updated);
        assertEquals(2, updated.size());
        assertSame(LongConverter.INSTANCE, updated.select(Long.class));
        assertSame(StringConverter.INSTANCE, updated.select(String.class));
    }

    @Test
    public void addSameInstanceReturnsSameSet() {
        ConverterSet set = new ConverterSet(new Converter[] { StringConverter.INSTANCE });

        ConverterSet result = set.add(StringConverter.INSTANCE, new Converter[1]);

        assertSame(set, result);
    }

    @Test
    public void addReplacesMatchingConverterAndReportsRemoved() {
        // Start with the singleton ReadableInstantConverter
        Converter original = ReadableInstantConverter.INSTANCE;
        ConverterSet set = new ConverterSet(new Converter[] { original });

        // Add a different instance with the same supported type
        Converter replacement = new ReadableInstantConverter();
        Converter[] removed = new Converter[1];

        ConverterSet updated = set.add(replacement, removed);

        assertNotSame(set, updated);
        assertSame(original, removed[0]); // the old converter should be reported as removed
        assertSame(replacement, updated.select(ReadableInstant.class));
    }

    // ---------- remove (by converter) ----------

    @Test(expected = NullPointerException.class)
    public void removeNullConverterThrows() {
        ConverterSet set = new ConverterSet(new Converter[] { StringConverter.INSTANCE });
        set.remove(null, new Converter[1]);
    }

    @Test
    public void removeExistingConverterReturnsNewSetAndDecreasesSize() {
        ConverterSet set = new ConverterSet(new Converter[] {
            LongConverter.INSTANCE,
            StringConverter.INSTANCE
        });

        Converter[] removed = new Converter[1];
        ConverterSet updated = set.remove(StringConverter.INSTANCE, removed);

        assertNotSame(set, updated);
        assertSame(StringConverter.INSTANCE, removed[0]);
        assertEquals(1, updated.size());
        assertNull(updated.select(String.class));
        assertSame(LongConverter.INSTANCE, updated.select(Long.class));
    }

    @Test
    public void removeNonExistingConverterReturnsSameSet() {
        ConverterSet set = new ConverterSet(new Converter[] { LongConverter.INSTANCE });

        ConverterSet result = set.remove(StringConverter.INSTANCE, new Converter[1]);

        assertSame(set, result);
    }

    // ---------- remove (by index) ----------

    @Test
    public void removeByValidIndexRemovesConverter() {
        ConverterSet set = new ConverterSet(new Converter[] {
            LongConverter.INSTANCE,
            StringConverter.INSTANCE
        });

        Converter[] removed = new Converter[1];
        ConverterSet updated = set.remove(1, removed);

        assertNotSame(set, updated);
        assertSame(StringConverter.INSTANCE, removed[0]);
        assertEquals(1, updated.size());
        assertNull(updated.select(String.class));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeByInvalidIndexThrows() {
        ConverterSet set = new ConverterSet(new Converter[] { LongConverter.INSTANCE });
        set.remove(1, new Converter[1]); // index 1 is out of bounds for size 1
    }

    // ---------- copyInto ----------

    @Test
    public void copyIntoCopiesAllConverters() {
        Converter longC = LongConverter.INSTANCE;
        Converter stringC = StringConverter.INSTANCE;
        ConverterSet set = new ConverterSet(new Converter[] { longC, stringC });

        Converter[] target = new Converter[2];
        set.copyInto(target);

        assertSame(longC, target[0]);
        assertSame(stringC, target[1]);
    }

    @Test(expected = NullPointerException.class)
    public void copyIntoNullArrayThrows() {
        ConverterSet set = new ConverterSet(new Converter[] { StringConverter.INSTANCE });
        set.copyInto(null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void copyIntoTooSmallArrayThrows() {
        ConverterSet set = new ConverterSet(new Converter[] {
            LongConverter.INSTANCE,
            StringConverter.INSTANCE
        });

        Converter[] tooSmall = new Converter[1];
        set.copyInto(tooSmall);
    }
}