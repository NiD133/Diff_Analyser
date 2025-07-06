package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.chrono.LenientChronology;
import org.joda.time.chrono.StrictChronology;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link ConverterSet} class.  This focuses on the
 * core functionality of adding, removing, and selecting converters from the set.
 */
@RunWith(JUnit4.class)
public class ConverterSetTest {

    @Test
    public void testRemove_withEmptyArray_throwsIndexOutOfBoundsException() {
        Converter[] emptyArray = new Converter[0];
        ConverterSet converterSet = new ConverterSet(emptyArray);

        try {
            converterSet.remove(0, emptyArray);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test
    public void testLocalTimeConstructor_withInvalidConverter_throwsIllegalArgumentException() {
        // Demonstrates a scenario where ConverterSet is indirectly used through LocalTime constructor.
        // This tests the integration of ConverterSet with other Joda-Time classes.
        ReadablePeriodConverter readablePeriodConverter = ReadablePeriodConverter.INSTANCE;
        JulianChronology julianChronology = JulianChronology.getInstanceUTC();
        StrictChronology strictChronology = StrictChronology.getInstance(julianChronology);
        LenientChronology lenientChronology = LenientChronology.getInstance(strictChronology);

        try {
            new LocalTime(readablePeriodConverter, lenientChronology);
            fail("Expected IllegalArgumentException: No partial converter found");
        } catch (IllegalArgumentException e) {
            assertEquals("No partial converter found for type: org.joda.time.convert.ReadablePeriodConverter", e.getMessage());
        }
    }

    @Test
    public void testLocalTimeConstructor_withConverterSet_throwsIllegalArgumentException() {
        // Similar to the above test, but directly using ConverterSet.
        Converter[] converterArray = new Converter[5];
        ConverterSet converterSet = new ConverterSet(converterArray);
        JulianChronology julianChronology = JulianChronology.getInstanceUTC();

        try {
            new LocalTime(converterSet, julianChronology);
            fail("Expected IllegalArgumentException: No partial converter found");
        } catch (IllegalArgumentException e) {
            assertEquals("No partial converter found for type: org.joda.time.convert.ConverterSet", e.getMessage());
        }
    }

    @Test
    public void testSize_withEmptyArray_returnsZero() {
        Converter[] emptyArray = new Converter[0];
        ConverterSet converterSet = new ConverterSet(emptyArray);
        assertEquals(0, converterSet.size());
    }

    @Test
    public void testSize_withNonEmptyArray_returnsCorrectSize() {
        Converter[] converterArray = new Converter[6];
        ConverterSet converterSet = new ConverterSet(converterArray);
        assertEquals(6, converterSet.size());
    }

    @Test
    public void testRemove_validIndex_returnsNewConverterSet() {
        Converter[] converterArray = new Converter[4];
        ConverterSet converterSet = new ConverterSet(converterArray);
        ConverterSet newConverterSet = converterSet.remove(1, converterArray);
        assertFalse(converterSet.equals(newConverterSet)); // Verify that a new object is created
    }

    @Test(expected = NullPointerException.class)
    public void testRemove_nullConverterInArray_throwsNullPointerException() {
        Converter[] converterArray = new Converter[2];
        ConverterSet converterSet = new ConverterSet(converterArray);
        converterSet.remove(converterArray[1], converterArray);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRemove_converterNotInSet_throwsArrayIndexOutOfBoundsException() {
        Converter[] emptyArray = new Converter[0];
        ConverterSet converterSet = new ConverterSet(emptyArray);
        ReadablePeriodConverter readablePeriodConverter = ReadablePeriodConverter.INSTANCE;
        converterSet.remove(readablePeriodConverter, emptyArray);
    }

    @Test(expected = NullPointerException.class)
    public void testRemove_nullArray_throwsNullPointerException() {
        ConverterSet converterSet = new ConverterSet((Converter[]) null);
        converterSet.remove(-275, (Converter[]) null);
    }

    @Test(expected = NegativeArraySizeException.class)
    public void testRemove_negativeIndexWithNullArray_throwsNegativeArraySizeException() {
        Converter[] emptyArray = new Converter[0];
        ConverterSet converterSet = new ConverterSet(emptyArray);
        converterSet.remove(-1721, (Converter[]) null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRemove_negativeIndex_throwsArrayIndexOutOfBoundsException() {
        Converter[] emptyArray = new Converter[0];
        ConverterSet converterSet = new ConverterSet(emptyArray);
        converterSet.remove(-1238, emptyArray);
    }

    @Test(expected = NullPointerException.class)
    public void testCopyInto_nullArray_throwsNullPointerException() {
        Converter[] converterArray = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converterArray);
        converterSet.copyInto((Converter[]) null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCopyInto_smallerArray_throwsArrayIndexOutOfBoundsException() {
        Converter[] converterArray = new Converter[5];
        ConverterSet converterSet = new ConverterSet(converterArray);
        Converter[] smallerArray = new Converter[4];
        converterSet.copyInto(smallerArray);
    }

    @Test(expected = NullPointerException.class)
    public void testAdd_nullConverterInArray_throwsNullPointerException() {
        Converter[] converterArray = new Converter[14];
        ConverterSet converterSet = new ConverterSet(converterArray);
        converterSet.add(converterArray[1], converterArray);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testAdd_converterNotInSet_throwsArrayIndexOutOfBoundsException() {
        Converter[] emptyArray = new Converter[0];
        ReadablePeriodConverter readablePeriodConverter = new ReadablePeriodConverter();
        ConverterSet converterSet = new ConverterSet(emptyArray);
        converterSet.add(readablePeriodConverter, emptyArray);
    }

    @Test
    public void testSelect_nullClass_returnsNull() {
        Converter[] converterArray = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converterArray);
        Converter converter = converterSet.select((Class<?>) null);
        assertNull(converter);
    }

    @Test
    public void testSelect_classNotPresent_returnsNull() {
        Converter[] converterArray = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converterArray);
        ReadableIntervalConverter readableIntervalConverter = ReadableIntervalConverter.INSTANCE;
        Converter[] converterArray1 = new Converter[3];
        ConverterSet converterSet1 = converterSet.add(readableIntervalConverter, converterArray1);
        Class<Object> objectClass = Object.class;
        Converter converter = converterSet1.select(objectClass);
        assertNull(converter);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemove_indexOutOfBounds_throwsIndexOutOfBoundsException() {
        Converter[] converterArray = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converterArray);
        converterSet.remove(365, converterArray);
    }

    @Test
    public void testRemove_converterNotInSet_returnsSameSet() {
        Converter[] converterArray = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converterArray);
        CalendarConverter calendarConverter = CalendarConverter.INSTANCE;
        ConverterSet newConverterSet = converterSet.remove(calendarConverter, (Converter[]) null);
        assertSame(converterSet, newConverterSet);
    }

    @Test
    public void testRemove_nullConverter_returnsSameSet() {
        Converter[] converterArray = new Converter[5];
        NullConverter nullConverter = NullConverter.INSTANCE;
        ConverterSet converterSet = new ConverterSet(converterArray);
        ConverterSet newConverterSet = converterSet.remove(nullConverter, converterArray);
        assertSame(converterSet, newConverterSet);
    }

    @Test
    public void testAdd_converterNotInSet_returnsNewSet() {
        Converter[] converterArray = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converterArray);
        ReadableIntervalConverter readableIntervalConverter = ReadableIntervalConverter.INSTANCE;
        ConverterSet newConverterSet = converterSet.add(readableIntervalConverter, (Converter[]) null);
        assertFalse(converterSet.equals(newConverterSet));
    }

    @Test
    public void testAdd_sameConverterType_returnsNewSet() {
        Converter[] converterArray = new Converter[1];
        LongConverter longConverter0 = LongConverter.INSTANCE;
        converterArray[0] = (Converter) longConverter0;
        ConverterSet converterSet = new ConverterSet(converterArray);
        LongConverter longConverter1 = new LongConverter();
        ConverterSet newConverterSet = converterSet.add(longConverter1, (Converter[]) null);
        assertNotSame(newConverterSet, converterSet);
    }

    @Test
    public void testAdd_existingConverter_returnsNewSet() {
        Converter[] converterArray = new Converter[5];
        ReadablePartialConverter readablePartialConverter = ReadablePartialConverter.INSTANCE;
        converterArray[0] = (Converter) readablePartialConverter;
        LongConverter longConverter0 = LongConverter.INSTANCE;
        converterArray[1] = (Converter) longConverter0;
        NullConverter nullConverter = NullConverter.INSTANCE;
        converterArray[2] = (Converter) nullConverter;
        DateConverter dateConverter0 = new DateConverter();
        DateConverter dateConverter1 = new DateConverter();
        converterArray[3] = (Converter) dateConverter1;
        ConverterSet converterSet = new ConverterSet(converterArray);
        ConverterSet newConverterSet = converterSet.add(dateConverter0, converterArray);
        assertNotSame(newConverterSet, converterSet);
    }

    @Test
    public void testAdd_sameConverter_returnsSameSet() {
        Converter[] converterArray = new Converter[7];
        LongConverter longConverter0 = LongConverter.INSTANCE;
        converterArray[0] = (Converter) longConverter0;
        ConverterSet converterSet = new ConverterSet(converterArray);
        ConverterSet newConverterSet = converterSet.add(longConverter0, (Converter[]) null);
        assertSame(newConverterSet, converterSet);
    }

    @Test
    public void testAdd_dateConverter_returnsSameSet() {
        Converter[] converterArray = new Converter[5];
        ReadablePartialConverter readablePartialConverter = ReadablePartialConverter.INSTANCE;
        converterArray[0] = (Converter) readablePartialConverter;
        LongConverter longConverter0 = LongConverter.INSTANCE;
        converterArray[1] = (Converter) longConverter0;
        NullConverter nullConverter = NullConverter.INSTANCE;
        converterArray[2] = (Converter) nullConverter;
        DateConverter dateConverter0 = new DateConverter();
        converterArray[3] = (Converter) dateConverter0;
        ConverterSet converterSet = new ConverterSet(converterArray);
        ConverterSet newConverterSet = converterSet.add(dateConverter0, converterArray);
        assertSame(newConverterSet, converterSet);
    }

    @Test
    public void testRemove_nullConverter_returnsNewSet() {
        Converter[] converterArray = new Converter[5];
        ReadablePartialConverter readablePartialConverter = ReadablePartialConverter.INSTANCE;
        LongConverter longConverter0 = LongConverter.INSTANCE;
        converterArray[1] = (Converter) longConverter0;
        NullConverter nullConverter = NullConverter.INSTANCE;
        converterArray[2] = (Converter) nullConverter;
        DateConverter dateConverter0 = new DateConverter();
        converterArray[3] = (Converter) dateConverter0;
        converterArray[4] = (Converter) longConverter0;
        ConverterSet converterSet = new ConverterSet(converterArray);
        ConverterSet newConverterSet = converterSet.remove(nullConverter, converterArray);
        assertNotSame(newConverterSet, converterSet);
        ConverterSet converterSet2 = converterSet.add(readablePartialConverter, converterArray);
        assertNotSame(converterSet2, converterSet);
    }

    @Test
    public void testSelect_nullConverterInArray_returnsNullConverter() {
        Converter[] converterArray = new Converter[5];
        LongConverter longConverter0 = LongConverter.INSTANCE;
        NullConverter nullConverter = NullConverter.INSTANCE;
        converterArray[2] = (Converter) nullConverter;
        DateConverter dateConverter0 = new DateConverter();
        converterArray[3] = (Converter) dateConverter0;
        converterArray[4] = (Converter) longConverter0;
        ConverterSet converterSet = new ConverterSet(converterArray);
        Converter converter = converterSet.select((Class<?>) null);
        assertSame(converter, nullConverter);
    }

    @Test
    public void testMutablePeriodConstructor_withInvalidConverter_throwsIllegalArgumentException() {
        ReadablePartialConverter readablePartialConverter = new ReadablePartialConverter();
        PeriodType periodType = PeriodType.standard();
        ReadableInstantConverter readableInstantConverter = new ReadableInstantConverter();
        DateConverter dateConverter = new DateConverter();
        Class<ConverterSet.Entry> entryClass = ConverterSet.Entry.class;
        ConverterSet.Entry converterSetEntry = new ConverterSet.Entry(entryClass, readableInstantConverter);
        DateTimeZone dateTimeZone = DateTimeZone.getDefault();
        Chronology chronology = dateConverter.getChronology((Object) converterSetEntry, dateTimeZone);

        try {
            new MutablePeriod(readablePartialConverter, periodType, chronology);
            fail("Expected IllegalArgumentException: No period converter found");
        } catch (IllegalArgumentException e) {
            assertEquals("No period converter found for type: org.joda.time.convert.ReadablePartialConverter", e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testSelect_nullConverterArray_throwsNullPointerException() {
        Converter[] converterArray = new Converter[5];
        ConverterSet converterSet = new ConverterSet(converterArray);
        converterSet.select((Class<?>) null);
    }

    @Test(expected = NullPointerException.class)
    public void testSize_nullConverterArray_throwsNullPointerException() {
        ConverterSet converterSet = new ConverterSet((Converter[]) null);
        converterSet.size();
    }

    @Test
    public void testCopyInto_validArray_copiesElements() {
        Converter[] converterArray = new Converter[5];
        ConverterSet converterSet = new ConverterSet(converterArray);
        converterSet.copyInto(converterArray);
        assertEquals(5, converterArray.length);
    }
}