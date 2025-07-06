package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.chrono.LenientChronology;
import org.joda.time.chrono.StrictChronology;
import org.joda.time.convert.CalendarConverter;
import org.joda.time.convert.Converter;
import org.joda.time.convert.ConverterSet;
import org.joda.time.convert.DateConverter;
import org.joda.time.convert.LongConverter;
import org.joda.time.convert.NullConverter;
import org.joda.time.convert.ReadableInstantConverter;
import org.joda.time.convert.ReadableIntervalConverter;
import org.joda.time.convert.ReadablePartialConverter;
import org.joda.time.convert.ReadablePeriodConverter;
import org.junit.runner.RunWith;

/**
 * Tests for the ConverterSet class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ConverterSet_ESTest extends ConverterSet_ESTest_scaffolding {

    /**
     * Tests that an IndexOutOfBoundsException is thrown when removing a converter at an invalid index.
     */
    @Test(timeout = 4000)
    public void testRemoveAtInvalidIndex() {
        Converter[] converterArray = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converterArray);
        
        try {
            converterSet.remove(0, converterArray);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    /**
     * Tests that an IllegalArgumentException is thrown when creating a LocalTime with an invalid converter.
     */
    @Test(timeout = 4000)
    public void testLocalTimeWithInvalidConverter() {
        ReadablePeriodConverter readablePeriodConverter = ReadablePeriodConverter.INSTANCE;
        JulianChronology julianChronology = JulianChronology.getInstanceUTC();
        StrictChronology strictChronology = StrictChronology.getInstance(julianChronology);
        LenientChronology lenientChronology = LenientChronology.getInstance(strictChronology);
        
        try {
            new LocalTime(readablePeriodConverter, lenientChronology);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.convert.ConverterManager", e);
        }
    }

    /**
     * Tests that an IllegalArgumentException is thrown when creating a LocalTime with a ConverterSet.
     */
    @Test(timeout = 4000)
    public void testLocalTimeWithConverterSet() {
        Converter[] converterArray = new Converter[5];
        ConverterSet converterSet = new ConverterSet(converterArray);
        JulianChronology julianChronology = JulianChronology.getInstanceUTC();
        
        try {
            new LocalTime(converterSet, julianChronology);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.convert.ConverterManager", e);
        }
    }

    /**
     * Tests that the size of an empty ConverterSet is 0.
     */
    @Test(timeout = 4000)
    public void testEmptyConverterSetSize() {
        Converter[] converterArray = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converterArray);
        assertEquals(0, converterSet.size());
    }

    /**
     * Tests that the size of a non-empty ConverterSet is correct.
     */
    @Test(timeout = 4000)
    public void testNonEmptyConverterSetSize() {
        Converter[] converterArray = new Converter[6];
        ConverterSet converterSet = new ConverterSet(converterArray);
        assertEquals(6, converterSet.size());
    }

    /**
     * Tests that removing a converter from a ConverterSet returns a new ConverterSet.
     */
    @Test(timeout = 4000)
    public void testRemoveConverterFromConverterSet() {
        Converter[] converterArray = new Converter[4];
        ConverterSet converterSet = new ConverterSet(converterArray);
        ConverterSet newConverterSet = converterSet.remove(1, converterArray);
        assertFalse(newConverterSet.equals(converterSet));
    }

    /**
     * Tests that a NullPointerException is thrown when removing a converter from a ConverterSet with a null converter.
     */
    @Test(timeout = 4000)
    public void testRemoveNullConverterFromConverterSet() {
        Converter[] converterArray = new Converter[2];
        ConverterSet converterSet = new ConverterSet(converterArray);
        
        try {
            converterSet.remove(converterArray[1], converterArray);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    /**
     * Tests that an ArrayIndexOutOfBoundsException is thrown when removing a converter from an empty ConverterSet.
     */
    @Test(timeout = 4000)
    public void testRemoveConverterFromEmptyConverterSet() {
        Converter[] converterArray = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converterArray);
        ReadablePeriodConverter readablePeriodConverter = ReadablePeriodConverter.INSTANCE;
        
        try {
            converterSet.remove(readablePeriodConverter, converterArray);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.joda.time.convert.ConverterSet", e);
        }
    }

    // ... rest of the test cases
}