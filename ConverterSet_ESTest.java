package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.chrono.LenientChronology;
import org.joda.time.chrono.StrictChronology;
import org.joda.time.convert.*;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ConverterSetTest extends ConverterSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testRemoveWithInvalidIndexThrowsIndexOutOfBoundsException() {
        Converter[] converters = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converters);

        try {
            converterSet.remove(0, converters);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testLocalTimeCreationWithInvalidConverterThrowsIllegalArgumentException() {
        ReadablePeriodConverter periodConverter = ReadablePeriodConverter.INSTANCE;
        JulianChronology julianChronology = JulianChronology.getInstanceUTC();
        StrictChronology strictChronology = StrictChronology.getInstance(julianChronology);
        LenientChronology lenientChronology = LenientChronology.getInstance(strictChronology);

        try {
            new LocalTime(periodConverter, lenientChronology);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.convert.ConverterManager", e);
        }
    }

    @Test(timeout = 4000)
    public void testLocalTimeCreationWithConverterSetThrowsIllegalArgumentException() {
        Converter[] converters = new Converter[5];
        ConverterSet converterSet = new ConverterSet(converters);
        JulianChronology julianChronology = JulianChronology.getInstanceUTC();

        try {
            new LocalTime(converterSet, julianChronology);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.convert.ConverterManager", e);
        }
    }

    @Test(timeout = 4000)
    public void testConverterSetSizeWithEmptyArray() {
        Converter[] converters = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converters);
        assertEquals(0, converterSet.size());
    }

    @Test(timeout = 4000)
    public void testConverterSetSizeWithNonEmptyArray() {
        Converter[] converters = new Converter[6];
        ConverterSet converterSet = new ConverterSet(converters);
        assertEquals(6, converterSet.size());
    }

    @Test(timeout = 4000)
    public void testRemoveConverterByIndex() {
        Converter[] converters = new Converter[4];
        ConverterSet converterSet = new ConverterSet(converters);
        ConverterSet newConverterSet = converterSet.remove(1, converters);
        assertFalse(newConverterSet.equals(converterSet));
    }

    @Test(timeout = 4000)
    public void testRemoveNullConverterThrowsNullPointerException() {
        Converter[] converters = new Converter[2];
        ConverterSet converterSet = new ConverterSet(converters);

        try {
            converterSet.remove(converters[1], converters);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveNonExistentConverterThrowsArrayIndexOutOfBoundsException() {
        Converter[] converters = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converters);
        ReadablePeriodConverter periodConverter = ReadablePeriodConverter.INSTANCE;

        try {
            converterSet.remove(periodConverter, converters);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveWithNullArrayThrowsNullPointerException() {
        ConverterSet converterSet = new ConverterSet(null);

        try {
            converterSet.remove(-275, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveWithNegativeIndexThrowsNegativeArraySizeException() {
        Converter[] converters = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converters);

        try {
            converterSet.remove(-1721, null);
            fail("Expecting exception: NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveWithNegativeIndexThrowsArrayIndexOutOfBoundsException() {
        Converter[] converters = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converters);

        try {
            converterSet.remove(-1238, converters);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testCopyIntoWithNullArrayThrowsNullPointerException() {
        Converter[] converters = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converters);

        try {
            converterSet.copyInto(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testCopyIntoWithSmallerArrayThrowsArrayIndexOutOfBoundsException() {
        Converter[] converters = new Converter[5];
        ConverterSet converterSet = new ConverterSet(converters);
        Converter[] smallerArray = new Converter[4];

        try {
            converterSet.copyInto(smallerArray);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testAddNullConverterThrowsNullPointerException() {
        Converter[] converters = new Converter[14];
        ConverterSet converterSet = new ConverterSet(converters);

        try {
            converterSet.add(converters[1], converters);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddConverterToEmptyArrayThrowsArrayIndexOutOfBoundsException() {
        Converter[] converters = new Converter[0];
        ReadablePeriodConverter periodConverter = new ReadablePeriodConverter();
        ConverterSet converterSet = new ConverterSet(converters);

        try {
            converterSet.add(periodConverter, converters);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testSelectWithNullClassReturnsNull() {
        Converter[] converters = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converters);
        Converter selectedConverter = converterSet.select(null);
        assertNull(selectedConverter);
    }

    @Test(timeout = 4000)
    public void testAddAndSelectConverter() {
        Converter[] converters = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converters);
        ReadableIntervalConverter intervalConverter = ReadableIntervalConverter.INSTANCE;
        Converter[] newConverters = new Converter[3];
        ConverterSet newConverterSet = converterSet.add(intervalConverter, newConverters);
        Class<Object> objectClass = Object.class;
        Converter selectedConverter = newConverterSet.select(objectClass);
        assertNull(selectedConverter);
    }

    @Test(timeout = 4000)
    public void testRemoveWithInvalidIndexThrowsIndexOutOfBoundsException() {
        Converter[] converters = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converters);

        try {
            converterSet.remove(365, converters);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveNonExistentConverterReturnsSameSet() {
        Converter[] converters = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converters);
        CalendarConverter calendarConverter = CalendarConverter.INSTANCE;
        ConverterSet newConverterSet = converterSet.remove(calendarConverter, null);
        assertSame(converterSet, newConverterSet);
    }

    @Test(timeout = 4000)
    public void testRemoveNullConverterReturnsSameSet() {
        Converter[] converters = new Converter[5];
        NullConverter nullConverter = NullConverter.INSTANCE;
        ConverterSet converterSet = new ConverterSet(converters);
        ConverterSet newConverterSet = converterSet.remove(nullConverter, converters);
        assertSame(converterSet, newConverterSet);
    }

    @Test(timeout = 4000)
    public void testAddConverterReturnsDifferentSet() {
        Converter[] converters = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converters);
        ReadableIntervalConverter intervalConverter = ReadableIntervalConverter.INSTANCE;
        ConverterSet newConverterSet = converterSet.add(intervalConverter, null);
        assertFalse(newConverterSet.equals(converterSet));
    }

    @Test(timeout = 4000)
    public void testAddExistingConverterReturnsSameSet() {
        Converter[] converters = new Converter[1];
        LongConverter longConverter = LongConverter.INSTANCE;
        converters[0] = longConverter;
        ConverterSet converterSet = new ConverterSet(converters);
        LongConverter newLongConverter = new LongConverter();
        ConverterSet newConverterSet = converterSet.add(newLongConverter, null);
        assertNotSame(newConverterSet, converterSet);
    }

    @Test(timeout = 4000)
    public void testAddDifferentConverterReturnsDifferentSet() {
        Converter[] converters = new Converter[5];
        ReadablePartialConverter partialConverter = ReadablePartialConverter.INSTANCE;
        converters[0] = partialConverter;
        LongConverter longConverter = LongConverter.INSTANCE;
        converters[1] = longConverter;
        NullConverter nullConverter = NullConverter.INSTANCE;
        converters[2] = nullConverter;
        DateConverter dateConverter = new DateConverter();
        converters[3] = dateConverter;
        ConverterSet converterSet = new ConverterSet(converters);
        ConverterSet newConverterSet = converterSet.add(dateConverter, converters);
        assertNotSame(newConverterSet, converterSet);
    }

    @Test(timeout = 4000)
    public void testAddSameConverterReturnsSameSet() {
        Converter[] converters = new Converter[7];
        LongConverter longConverter = LongConverter.INSTANCE;
        converters[0] = longConverter;
        ConverterSet converterSet = new ConverterSet(converters);
        ConverterSet newConverterSet = converterSet.add(longConverter, null);
        assertSame(newConverterSet, converterSet);
    }

    @Test(timeout = 4000)
    public void testRemoveAndAddConverterReturnsDifferentSet() {
        Converter[] converters = new Converter[5];
        ReadablePartialConverter partialConverter = ReadablePartialConverter.INSTANCE;
        LongConverter longConverter = LongConverter.INSTANCE;
        converters[1] = longConverter;
        NullConverter nullConverter = NullConverter.INSTANCE;
        converters[2] = nullConverter;
        DateConverter dateConverter = new DateConverter();
        converters[3] = dateConverter;
        converters[4] = longConverter;
        ConverterSet converterSet = new ConverterSet(converters);
        ConverterSet newConverterSet = converterSet.remove(nullConverter, converters);
        assertNotSame(newConverterSet, converterSet);

        ConverterSet anotherNewConverterSet = converterSet.add(partialConverter, converters);
        assertNotSame(anotherNewConverterSet, converterSet);
    }

    @Test(timeout = 4000)
    public void testSelectWithNullClassReturnsNullConverter() {
        Converter[] converters = new Converter[5];
        LongConverter longConverter = LongConverter.INSTANCE;
        NullConverter nullConverter = NullConverter.INSTANCE;
        converters[2] = nullConverter;
        DateConverter dateConverter = new DateConverter();
        converters[3] = dateConverter;
        converters[4] = longConverter;
        ConverterSet converterSet = new ConverterSet(converters);
        Converter selectedConverter = converterSet.select(null);
        assertSame(selectedConverter, nullConverter);
    }

    @Test(timeout = 4000)
    public void testSelectWithNullClassThrowsNullPointerException() {
        Converter[] converters = new Converter[5];
        ConverterSet converterSet = new ConverterSet(converters);

        try {
            converterSet.select(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testSizeWithNullArrayThrowsNullPointerException() {
        ConverterSet converterSet = new ConverterSet(null);

        try {
            converterSet.size();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testCopyIntoWithValidArray() {
        Converter[] converters = new Converter[5];
        ConverterSet converterSet = new ConverterSet(converters);
        converterSet.copyInto(converters);
        assertEquals(5, converters.length);
    }
}