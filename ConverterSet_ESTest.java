package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Hours;
import org.joda.time.Interval;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.Seconds;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.convert.CalendarConverter;
import org.joda.time.convert.Converter;
import org.joda.time.convert.ConverterSet;
import org.joda.time.convert.LongConverter;
import org.joda.time.convert.NullConverter;
import org.joda.time.convert.ReadableDurationConverter;
import org.joda.time.convert.ReadableInstantConverter;
import org.joda.time.convert.ReadableIntervalConverter;
import org.joda.time.convert.ReadablePartialConverter;
import org.joda.time.convert.ReadablePeriodConverter;
import org.joda.time.convert.StringConverter;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ConverterSet_ESTest extends ConverterSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testRemoveWithInvalidIndexThrowsIndexOutOfBoundsException() {
        Converter[] converters = new Converter[1];
        ConverterSet converterSet = new ConverterSet(converters);

        try {
            converterSet.remove(1, converters);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddDuplicateConverterCreatesNewSet() {
        Converter[] converters = new Converter[8];
        converters[0] = new ReadableInstantConverter();
        converters[1] = new ReadableIntervalConverter();
        converters[2] = new ReadablePeriodConverter();
        converters[3] = new CalendarConverter();
        CalendarConverter duplicateConverter = new CalendarConverter();
        ConverterSet converterSet = new ConverterSet(converters);

        ConverterSet newConverterSet = converterSet.add(duplicateConverter, converters);

        assertNotSame(newConverterSet, converterSet);
    }

    @Test(timeout = 4000)
    public void testConvertHoursToSeconds() {
        Hours hours = Hours.ONE;
        Seconds seconds = hours.toStandardSeconds();
        PeriodType periodType = seconds.getPeriodType();
        MutablePeriod mutablePeriod = new MutablePeriod((Object) null, periodType);
    }

    @Test(timeout = 4000)
    public void testRemoveConverterReducesSize() {
        Converter[] converters = new Converter[1];
        converters[0] = StringConverter.INSTANCE;
        ConverterSet converterSet = new ConverterSet(converters);

        ConverterSet newConverterSet = converterSet.remove(converters[0], converters);
        int newSize = newConverterSet.size();

        assertEquals(0, newSize);
    }

    @Test(timeout = 4000)
    public void testInitialSizeOfConverterSet() {
        Converter[] converters = new Converter[1];
        ConverterSet converterSet = new ConverterSet(converters);

        int size = converterSet.size();

        assertEquals(1, size);
    }

    @Test(timeout = 4000)
    public void testRemoveByIndexCreatesNewSet() {
        Converter[] converters = new Converter[7];
        ConverterSet converterSet = new ConverterSet(converters);

        ConverterSet newConverterSet = converterSet.remove(1, converters);

        assertFalse(newConverterSet.equals(converterSet));
    }

    @Test(timeout = 4000)
    public void testRemoveNullConverterThrowsNullPointerException() {
        Converter[] converters = new Converter[1];
        ConverterSet converterSet = new ConverterSet(converters);

        try {
            converterSet.remove(converters[0], converters);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveFromEmptyArrayThrowsArrayIndexOutOfBoundsException() {
        StringConverter stringConverter = StringConverter.INSTANCE;
        Converter[] converters = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converters);

        try {
            converterSet.remove(stringConverter, converters);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveFromNullArrayThrowsNullPointerException() {
        ConverterSet converterSet = new ConverterSet((Converter[]) null);

        try {
            converterSet.remove(291, (Converter[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveNegativeIndexThrowsNegativeArraySizeException() {
        Converter[] converters = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converters);

        try {
            converterSet.remove(-3378, (Converter[]) null);
            fail("Expecting exception: NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveNegativeIndexThrowsArrayIndexOutOfBoundsException() {
        Converter[] converters = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converters);

        try {
            converterSet.remove(-785, converters);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testCopyIntoNullArrayThrowsNullPointerException() {
        ConverterSet converterSet = new ConverterSet((Converter[]) null);

        try {
            converterSet.copyInto((Converter[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testCopyIntoSmallerArrayThrowsArrayIndexOutOfBoundsException() {
        Converter[] converters = new Converter[7];
        ConverterSet converterSet = new ConverterSet(converters);
        Converter[] smallerArray = new Converter[1];

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
            converterSet.add(converters[0], converters);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddToEmptyArrayThrowsArrayIndexOutOfBoundsException() {
        StringConverter stringConverter = StringConverter.INSTANCE;
        Converter[] converters = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converters);

        try {
            converterSet.add(stringConverter, converters);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testSelectReturnsNullForNonExistentConverter() {
        Converter[] converters = new Converter[1];
        converters[0] = NullConverter.INSTANCE;
        ConverterSet converterSet = new ConverterSet(converters);

        Converter selectedConverter = converterSet.select(ConverterSet.Entry.class);

        assertNull(selectedConverter);
    }

    @Test(timeout = 4000)
    public void testCreateMutablePeriodWithInvalidTypeThrowsIllegalArgumentException() {
        Hours hours = Hours.ONE;
        Seconds seconds = hours.toStandardSeconds();
        PeriodType periodType = seconds.getPeriodType();

        try {
            new MutablePeriod(periodType, periodType);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.convert.ConverterManager", e);
        }
    }

    @Test(timeout = 4000)
    public void testSelectReturnsNullForNonMatchingType() {
        Converter[] converters = new Converter[3];
        converters[0] = new ReadableIntervalConverter();
        converters[1] = new LongConverter();
        converters[2] = new ReadableDurationConverter();
        ConverterSet converterSet = new ConverterSet(converters);

        Converter selectedConverter = converterSet.select(ConverterSet.Entry.class);

        assertNull(selectedConverter);
    }

    @Test(timeout = 4000)
    public void testSelectReturnsNullForNullType() {
        Converter[] converters = new Converter[1];
        converters[0] = StringConverter.INSTANCE;
        ConverterSet converterSet = new ConverterSet(converters);

        Converter selectedConverter = converterSet.select(null);

        assertNull(selectedConverter);
    }

    @Test(timeout = 4000)
    public void testSelectReturnsMatchingConverter() {
        Converter[] converters = new Converter[5];
        converters[0] = LongConverter.INSTANCE;
        converters[1] = new ReadablePartialConverter();
        converters[2] = new CalendarConverter();
        converters[3] = new ReadableIntervalConverter();
        converters[4] = new ReadableIntervalConverter();
        ConverterSet converterSet = new ConverterSet(converters);

        Converter selectedConverter = converterSet.select(Long.class);

        assertSame(selectedConverter, LongConverter.INSTANCE);
    }

    @Test(timeout = 4000)
    public void testRemoveInvalidIndexThrowsIndexOutOfBoundsException() {
        Converter[] converters = new Converter[1];
        ConverterSet converterSet = new ConverterSet(converters);

        try {
            converterSet.remove(1968, converters);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveNullConverterReturnsSameSet() {
        Converter[] converters = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converters);

        ConverterSet newConverterSet = converterSet.remove(null, null);

        assertSame(newConverterSet, converterSet);
    }

    @Test(timeout = 4000)
    public void testRemoveExistingConverterCreatesNewSet() {
        Converter[] converters = new Converter[2];
        converters[1] = ReadablePartialConverter.INSTANCE;
        ConverterSet converterSet = new ConverterSet(converters);

        ConverterSet newConverterSet = converterSet.remove(converters[1], converters);

        assertNotSame(newConverterSet, converterSet);
    }

    @Test(timeout = 4000)
    public void testAddNewConverterCreatesNewSet() {
        Converter[] converters = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converters);
        ReadableInstantConverter newConverter = ReadableInstantConverter.INSTANCE;

        ConverterSet newConverterSet = converterSet.add(newConverter, null);

        assertNotSame(converterSet, newConverterSet);
    }

    @Test(timeout = 4000)
    public void testAddDuplicateConverterReturnsSameSet() {
        Converter[] converters = new Converter[1];
        converters[0] = StringConverter.INSTANCE;
        ConverterSet converterSet = new ConverterSet(converters);

        ConverterSet newConverterSet = converterSet.add(StringConverter.INSTANCE, converters);

        assertSame(newConverterSet, converterSet);
    }

    @Test(timeout = 4000)
    public void testAddExistingConverterReturnsSameSet() {
        Converter[] converters = new Converter[10];
        converters[0] = ReadableInstantConverter.INSTANCE;
        ConverterSet converterSet = new ConverterSet(converters);

        ConverterSet newConverterSet = converterSet.add(converters[0], null);

        assertSame(newConverterSet, converterSet);
    }

    @Test(timeout = 4000)
    public void testIntervalCreationWithInvalidTypeThrowsIllegalArgumentException() {
        Class<ConverterSet.Entry> entryClass = ConverterSet.Entry.class;
        ReadablePartialConverter partialConverter = new ReadablePartialConverter();
        ConverterSet.Entry entry = new ConverterSet.Entry(entryClass, partialConverter);
        CopticChronology chronology = CopticChronology.getInstance();

        try {
            new Interval(entry, chronology);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.convert.ConverterManager", e);
        }
    }

    @Test(timeout = 4000)
    public void testSelectNullTypeThrowsNullPointerException() {
        Converter[] converters = new Converter[1];
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
        ConverterSet converterSet = new ConverterSet((Converter[]) null);

        try {
            converterSet.size();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testCopyIntoArray() {
        Converter[] converters = new Converter[7];
        ConverterSet converterSet = new ConverterSet(converters);

        converterSet.copyInto(converters);

        assertEquals(7, converters.length);
    }
}