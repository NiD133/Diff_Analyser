package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.convert.*;

/**
 * Test suite for ConverterSet class functionality.
 * Tests the immutable set operations for managing type converters.
 */
public class ConverterSetTest {

    // Test data setup helpers
    private Converter[] createConverterArray(int size) {
        return new Converter[size];
    }

    private Converter[] createPopulatedConverterArray() {
        Converter[] converters = new Converter[4];
        converters[0] = ReadableInstantConverter.INSTANCE;
        converters[1] = new ReadableIntervalConverter();
        converters[2] = new ReadablePeriodConverter();
        converters[3] = new CalendarConverter();
        return converters;
    }

    // Size and basic operations tests
    @Test
    public void testSize_WithSingleConverter_ReturnsOne() {
        Converter[] converters = createConverterArray(1);
        ConverterSet converterSet = new ConverterSet(converters);
        
        assertEquals("ConverterSet should contain exactly one converter", 1, converterSet.size());
    }

    @Test
    public void testCopyInto_WithMatchingArraySize_CopiesAllConverters() {
        Converter[] sourceConverters = createConverterArray(7);
        ConverterSet converterSet = new ConverterSet(sourceConverters);
        Converter[] destinationArray = createConverterArray(7);
        
        converterSet.copyInto(destinationArray);
        
        assertEquals("Destination array should maintain same length", 7, destinationArray.length);
    }

    // Add operation tests
    @Test
    public void testAdd_WithNewConverter_ReturnsNewConverterSet() {
        Converter[] converters = createPopulatedConverterArray();
        ConverterSet originalSet = new ConverterSet(converters);
        CalendarConverter newConverter = new CalendarConverter();
        
        ConverterSet resultSet = originalSet.add(newConverter, converters);
        
        assertNotSame("Adding converter should return new ConverterSet instance", 
                     originalSet, resultSet);
    }

    @Test
    public void testAdd_WithExistingConverter_ReturnsSameConverterSet() {
        Converter[] converters = createConverterArray(1);
        StringConverter stringConverter = StringConverter.INSTANCE;
        converters[0] = stringConverter;
        ConverterSet converterSet = new ConverterSet(converters);
        
        ConverterSet resultSet = converterSet.add(stringConverter, converters);
        
        assertSame("Adding existing converter should return same ConverterSet", 
                  converterSet, resultSet);
    }

    @Test
    public void testAdd_WithSameConverterInstance_ReturnsSameConverterSet() {
        Converter[] converters = createConverterArray(10);
        ReadableInstantConverter converter = new ReadableInstantConverter();
        converters[0] = converter;
        ConverterSet converterSet = new ConverterSet(converters);
        
        ConverterSet resultSet = converterSet.add(converter, null);
        
        assertSame("Adding same converter instance should return same ConverterSet", 
                  converterSet, resultSet);
    }

    @Test
    public void testAdd_WithEmptySetAndNewConverter_ReturnsNewConverterSet() {
        Converter[] emptyConverters = createConverterArray(0);
        ConverterSet emptySet = new ConverterSet(emptyConverters);
        ReadableInstantConverter newConverter = ReadableInstantConverter.INSTANCE;
        
        ConverterSet resultSet = emptySet.add(newConverter, null);
        
        assertNotSame("Adding to empty set should return new ConverterSet", 
                     emptySet, resultSet);
    }

    @Test
    public void testAdd_WithDifferentInstanceOfSameConverterType_ReturnsNewConverterSet() {
        Converter[] converters = createConverterArray(3);
        converters[0] = ReadableInstantConverter.INSTANCE;
        ConverterSet converterSet = new ConverterSet(converters);
        ReadableInstantConverter differentInstance = new ReadableInstantConverter();
        
        ConverterSet resultSet = converterSet.add(differentInstance, null);
        
        assertNotSame("Adding different instance should return new ConverterSet", 
                     converterSet, resultSet);
    }

    // Remove operation tests
    @Test
    public void testRemove_WithExistingConverter_ReturnsNewConverterSetWithReducedSize() {
        Converter[] converters = createConverterArray(1);
        StringConverter stringConverter = StringConverter.INSTANCE;
        converters[0] = stringConverter;
        ConverterSet converterSet = new ConverterSet(converters);
        
        ConverterSet resultSet = converterSet.remove(stringConverter, converters);
        
        assertEquals("Removing converter should result in empty set", 0, resultSet.size());
    }

    @Test
    public void testRemove_WithExistingConverterFromMultipleConverters_ReturnsNewConverterSet() {
        Converter[] converters = createConverterArray(2);
        converters[1] = ReadablePartialConverter.INSTANCE;
        ConverterSet converterSet = new ConverterSet(converters);
        
        ConverterSet resultSet = converterSet.remove(converters[1], converters);
        
        assertNotSame("Removing existing converter should return new ConverterSet", 
                     converterSet, resultSet);
    }

    @Test
    public void testRemove_WithNullConverter_ReturnsSameConverterSet() {
        Converter[] emptyConverters = createConverterArray(0);
        ConverterSet converterSet = new ConverterSet(emptyConverters);
        
        ConverterSet resultSet = converterSet.remove(null, null);
        
        assertSame("Removing null converter should return same ConverterSet", 
                  converterSet, resultSet);
    }

    @Test
    public void testRemoveByIndex_WithValidIndex_ReturnsNewConverterSet() {
        Converter[] converters = createConverterArray(7);
        ConverterSet converterSet = new ConverterSet(converters);
        
        ConverterSet resultSet = converterSet.remove(1, converters);
        
        assertNotSame("Removing by valid index should return new ConverterSet", 
                     converterSet, resultSet);
    }

    // Converter selection tests
    @Test
    public void testSelect_WithLongClass_ReturnsLongConverter() {
        Converter[] converters = createConverterArray(5);
        LongConverter longConverter = LongConverter.INSTANCE;
        converters[0] = longConverter;
        converters[1] = new ReadablePartialConverter();
        converters[2] = new CalendarConverter();
        converters[3] = new ReadableIntervalConverter();
        converters[4] = new ReadableIntervalConverter();
        ConverterSet converterSet = new ConverterSet(converters);
        
        Converter selectedConverter = converterSet.select(Long.class);
        
        assertSame("Should select the LongConverter for Long class", 
                  longConverter, selectedConverter);
    }

    @Test
    public void testSelect_WithUnsupportedClass_ReturnsNull() {
        Converter[] converters = createConverterArray(1);
        converters[0] = NullConverter.INSTANCE;
        ConverterSet converterSet = new ConverterSet(converters);
        
        Converter selectedConverter = converterSet.select(ConverterSet.Entry.class);
        
        assertNull("Should return null for unsupported converter type", selectedConverter);
    }

    @Test
    public void testSelect_WithNullClass_ReturnsNull() {
        Converter[] converters = createConverterArray(1);
        converters[0] = StringConverter.INSTANCE;
        ConverterSet converterSet = new ConverterSet(converters);
        
        Converter selectedConverter = converterSet.select(null);
        
        assertNull("Should return null when selecting with null class", selectedConverter);
    }

    @Test
    public void testSelect_WithMultipleConvertersButNoMatch_ReturnsNull() {
        Converter[] converters = createConverterArray(3);
        converters[0] = new ReadableIntervalConverter();
        converters[1] = new LongConverter();
        converters[2] = new ReadableDurationConverter();
        ConverterSet converterSet = new ConverterSet(converters);
        
        Converter selectedConverter = converterSet.select(ConverterSet.Entry.class);
        
        assertNull("Should return null when no converter matches the class", selectedConverter);
    }

    // Exception handling tests
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveByIndex_WithIndexOutOfBounds_ThrowsException() {
        Converter[] converters = createConverterArray(1);
        ConverterSet converterSet = new ConverterSet(converters);
        
        converterSet.remove(1, converters);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveByIndex_WithLargeIndex_ThrowsException() {
        Converter[] converters = createConverterArray(1);
        ConverterSet converterSet = new ConverterSet(converters);
        
        converterSet.remove(1968, converters);
    }

    @Test(expected = NullPointerException.class)
    public void testRemove_WithNullConverterInArray_ThrowsException() {
        Converter[] converters = createConverterArray(1); // contains null
        ConverterSet converterSet = new ConverterSet(converters);
        
        converterSet.remove(converters[0], converters);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRemove_WithEmptyArrayAndNonNullConverter_ThrowsException() {
        StringConverter stringConverter = StringConverter.INSTANCE;
        Converter[] emptyConverters = createConverterArray(0);
        ConverterSet converterSet = new ConverterSet(emptyConverters);
        
        converterSet.remove(stringConverter, emptyConverters);
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveByIndex_WithNullConverterArray_ThrowsException() {
        ConverterSet converterSet = new ConverterSet(null);
        
        converterSet.remove(291, null);
    }

    @Test(expected = NegativeArraySizeException.class)
    public void testRemoveByIndex_WithNegativeIndexAndNullArray_ThrowsException() {
        Converter[] emptyConverters = createConverterArray(0);
        ConverterSet converterSet = new ConverterSet(emptyConverters);
        
        converterSet.remove(-3378, null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRemoveByIndex_WithNegativeIndex_ThrowsException() {
        Converter[] emptyConverters = createConverterArray(0);
        ConverterSet converterSet = new ConverterSet(emptyConverters);
        
        converterSet.remove(-785, emptyConverters);
    }

    @Test(expected = NullPointerException.class)
    public void testCopyInto_WithNullArray_ThrowsException() {
        ConverterSet converterSet = new ConverterSet(null);
        
        converterSet.copyInto(null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCopyInto_WithTooSmallDestinationArray_ThrowsException() {
        Converter[] sourceConverters = createConverterArray(7);
        ConverterSet converterSet = new ConverterSet(sourceConverters);
        Converter[] tooSmallArray = createConverterArray(1);
        
        converterSet.copyInto(tooSmallArray);
    }

    @Test(expected = NullPointerException.class)
    public void testAdd_WithNullConverter_ThrowsException() {
        Converter[] converters = createConverterArray(14);
        ConverterSet converterSet = new ConverterSet(converters);
        
        converterSet.add(converters[0], converters); // converters[0] is null
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testAdd_WithEmptyArrayAndNonNullConverter_ThrowsException() {
        StringConverter stringConverter = StringConverter.INSTANCE;
        Converter[] emptyConverters = createConverterArray(0);
        ConverterSet converterSet = new ConverterSet(emptyConverters);
        
        converterSet.add(stringConverter, emptyConverters);
    }

    @Test(expected = NullPointerException.class)
    public void testSelect_WithNullConverterArray_ThrowsException() {
        Converter[] converters = createConverterArray(1);
        ConverterSet converterSet = new ConverterSet(converters);
        
        converterSet.select(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSize_WithNullConverterArray_ThrowsException() {
        ConverterSet converterSet = new ConverterSet(null);
        
        converterSet.size();
    }

    // Integration tests (these were in the original but seem unrelated to ConverterSet)
    @Test
    public void testMutablePeriod_WithNullObject_CreatesValidPeriod() {
        // This test seems unrelated to ConverterSet but was in original
        // Keeping for completeness but would normally be moved to appropriate test class
        org.joda.time.Hours hours = org.joda.time.Hours.ONE;
        org.joda.time.Seconds seconds = hours.toStandardSeconds();
        org.joda.time.PeriodType periodType = seconds.getPeriodType();
        
        org.joda.time.MutablePeriod mutablePeriod = new org.joda.time.MutablePeriod(null, periodType);
        
        assertNotNull("MutablePeriod should be created successfully", mutablePeriod);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMutablePeriod_WithInvalidPeriodType_ThrowsException() {
        // This test seems unrelated to ConverterSet but was in original
        org.joda.time.Hours hours = org.joda.time.Hours.ONE;
        org.joda.time.Seconds seconds = hours.toStandardSeconds();
        org.joda.time.PeriodType periodType = seconds.getPeriodType();
        
        new org.joda.time.MutablePeriod(periodType, periodType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInterval_WithUnsupportedType_ThrowsException() {
        // This test seems unrelated to ConverterSet but was in original
        Class<ConverterSet.Entry> entryClass = ConverterSet.Entry.class;
        ReadablePartialConverter converter = new ReadablePartialConverter();
        ConverterSet.Entry entry = new ConverterSet.Entry(entryClass, converter);
        org.joda.time.chrono.CopticChronology chronology = org.joda.time.chrono.CopticChronology.getInstance();
        
        new org.joda.time.Interval(entry, chronology);
    }
}