package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry;
import org.apache.commons.compress.harmony.unpack200.bytecode.ConstantPoolEntry;

/**
 * This test suite verifies the behavior of the {@link SegmentConstantPool} class.
 * The tests cover exception handling, edge cases, and the class's public API.
 * Many tests are run with a null CpBands object to ensure proper NullPointerException
 * handling in methods that rely on it.
 */
public class SegmentConstantPoolTest {

    private final SegmentConstantPool constantPoolWithNullBands = new SegmentConstantPool(null);

    // --- Tests for static helper methods ---

    @Test
    public void toIntExactShouldConvertLongToInt() {
        assertEquals(0, SegmentConstantPool.toIntExact(0L));
        assertEquals(1, SegmentConstantPool.toIntExact(1L));
        assertEquals(-119, SegmentConstantPool.toIntExact(-119L));
    }

    @Test(expected = IOException.class)
    public void toIntExactShouldThrowIOExceptionForValueExceedingIntMax() throws IOException {
        SegmentConstantPool.toIntExact(Integer.MAX_VALUE + 1L);
    }

    @Test
    public void toIndexShouldConvertPositiveLongToIndex() throws IOException {
        assertEquals(0, SegmentConstantPool.toIndex(0L));
        assertEquals(1, SegmentConstantPool.toIndex(1L));
    }

    @Test(expected = IOException.class)
    public void toIndexShouldThrowIOExceptionForNegativeValue() throws IOException {
        SegmentConstantPool.toIndex(-1975L);
    }

    @Test
    public void regexMatchesShouldCorrectlyMatchKnownPatterns() {
        assertTrue("Regex '.*' should match any string", SegmentConstantPool.regexMatches(".*", "any string"));
        assertFalse("Regex '^<init>.*' should not match an empty string", SegmentConstantPool.regexMatches("^<init>.*", ""));
        assertFalse("Regex '^<init>.*' should not match a non-matching string", SegmentConstantPool.regexMatches("^<init>.*", "v_ g:,"));
    }

    @Test(expected = NullPointerException.class)
    public void regexMatchesShouldThrowNPEForNullStringToCompare() {
        SegmentConstantPool.regexMatches("^<init>.*", null);
    }

    @Test
    public void regexMatchesShouldThrowErrorForUnknownPattern() {
        try {
            SegmentConstantPool.regexMatches("gml`F", "some string");
            fail("Expected an Error for an unknown regex pattern, but none was thrown.");
        } catch (Error e) {
            assertEquals("regex trying to match a pattern I don't know: gml`F", e.getMessage());
        }
    }

    // --- Tests for getValue() ---

    @Test(expected = NullPointerException.class)
    public void getValueForUtf8WithNullBandsShouldThrowNPE() throws IOException {
        constantPoolWithNullBands.getValue(SegmentConstantPool.UTF_8, 1L);
    }

    @Test(expected = NullPointerException.class)
    public void getValueForIntWithNullBandsShouldThrowNPE() throws IOException {
        constantPoolWithNullBands.getValue(SegmentConstantPool.CP_INT, 2L);
    }

    @Test(expected = NullPointerException.class)
    public void getValueForFloatWithNullBandsShouldThrowNPE() throws IOException {
        constantPoolWithNullBands.getValue(SegmentConstantPool.CP_FLOAT, 3L);
    }

    @Test(expected = NullPointerException.class)
    public void getValueForLongWithNullBandsShouldThrowNPE() throws IOException {
        constantPoolWithNullBands.getValue(SegmentConstantPool.CP_LONG, 4L);
    }

    @Test(expected = NullPointerException.class)
    public void getValueForDoubleWithNullBandsShouldThrowNPE() throws IOException {
        constantPoolWithNullBands.getValue(SegmentConstantPool.CP_DOUBLE, 5L);
    }

    @Test(expected = NullPointerException.class)
    public void getValueForStringWithNullBandsShouldThrowNPE() throws IOException {
        constantPoolWithNullBands.getValue(SegmentConstantPool.CP_STRING, 6L);
    }

    @Test(expected = NullPointerException.class)
    public void getValueForClassWithNullBandsShouldThrowNPE() throws IOException {
        constantPoolWithNullBands.getValue(SegmentConstantPool.CP_CLASS, 7L);
    }

    @Test(expected = NullPointerException.class)
    public void getValueForSignatureWithNullBandsShouldThrowNPE() throws IOException {
        constantPoolWithNullBands.getValue(SegmentConstantPool.SIGNATURE, 8L);
    }

    @Test(expected = NullPointerException.class)
    public void getValueForDescrWithNullBandsShouldThrowNPE() throws IOException {
        constantPoolWithNullBands.getValue(SegmentConstantPool.CP_DESCR, 9L);
    }

    @Test
    public void getValueForIMethodWithNegativeIndexShouldReturnNull() throws IOException {
        ClassFileEntry value = constantPoolWithNullBands.getValue(SegmentConstantPool.CP_IMETHOD, -1L);
        assertNull(value);
    }


    @Test(expected = IOException.class)
    public void getValueWithNegativeRangeShouldThrowIOException() throws IOException {
        constantPoolWithNullBands.getValue(SegmentConstantPool.CP_CLASS, -460L);
    }

    @Test
    public void getValueWithInvalidCpTypeShouldThrowError() {
        try {
            constantPoolWithNullBands.getValue(-282, 0L);
            fail("Expected an Error for an invalid CP type, but none was thrown.");
        } catch (Error e) {
            assertEquals("Tried to get a value I don't know about: -282", e.getMessage());
        } catch (IOException e) {
            fail("Threw an unexpected IOException: " + e.getMessage());
        }
    }

    // --- Tests for getConstantPoolEntry() ---

    @Test
    public void getConstantPoolEntryWithNegativeIndexShouldReturnNull() throws IOException {
        assertNull(constantPoolWithNullBands.getConstantPoolEntry(-1, -1));
    }

    @Test(expected = IOException.class)
    public void getConstantPoolEntryWithNegativeIndexAndValidTypeShouldThrowIOException() throws IOException {
        constantPoolWithNullBands.getConstantPoolEntry(SegmentConstantPool.ALL, -1988L);
    }

    @Test
    public void getConstantPoolEntryWithUnsupportedTypeShouldThrowIOException() {
        try {
            constantPoolWithNullBands.getConstantPoolEntry(SegmentConstantPool.SIGNATURE, 8);
            fail("Expected IOException for unsupported type SIGNATURE");
        } catch (IOException e) {
            assertEquals("Type SIGNATURE is not supported yet: 8", e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void getConstantPoolEntryForMethodWithNullBandsShouldThrowNPE() throws IOException {
        constantPoolWithNullBands.getConstantPoolEntry(SegmentConstantPool.CP_METHOD, 10L);
    }

    @Test(expected = NullPointerException.class)
    public void getConstantPoolEntryForIMethodWithNullBandsShouldThrowNPE() throws IOException {
        constantPoolWithNullBands.getConstantPoolEntry(SegmentConstantPool.CP_IMETHOD, 11L);
    }

    // --- Tests for getInitMethodPoolEntry() ---

    @Test
    public void getInitMethodPoolEntryWithNonMethodTypeShouldThrowIOException() {
        try {
            constantPoolWithNullBands.getInitMethodPoolEntry(SegmentConstantPool.CP_FIELD, 1L, "SomeClass");
            fail("Expected an IOException for non-method type, but none was thrown.");
        } catch (IOException e) {
            assertEquals("Nothing but CP_METHOD can be an <init>", e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void getInitMethodPoolEntryWithNullBandsShouldThrowNPE() throws IOException {
        constantPoolWithNullBands.getInitMethodPoolEntry(SegmentConstantPool.CP_METHOD, 1L, "SomeClass");
    }

    // --- Tests for getClassPoolEntry() ---

    @Test(expected = NullPointerException.class)
    public void getClassPoolEntryWithNullBandsShouldThrowNPE() throws IOException {
        constantPoolWithNullBands.getClassPoolEntry("java/lang/String");
    }

    // --- Tests for getClassSpecificPoolEntry() ---

    @Test(expected = NullPointerException.class)
    public void getClassSpecificPoolEntryForMethodWithNullBandsShouldThrowNPE() throws IOException {
        constantPoolWithNullBands.getClassSpecificPoolEntry(SegmentConstantPool.CP_METHOD, 1L, "SomeClass");
    }

    @Test
    public void getClassSpecificPoolEntryWithInvalidTypeShouldThrowIOException() {
        try {
            constantPoolWithNullBands.getClassSpecificPoolEntry(-1, 1L, "SomeClass");
            fail("Expected an IOException for invalid type, but none was thrown.");
        } catch (IOException e) {
            assertEquals("Type is not supported yet: -1", e.getMessage());
        }
    }

    // --- Tests for matchSpecificPoolEntryIndex() ---

    @Test
    public void matchSpecificPoolEntryIndexShouldReturnCorrectIndexForMatch() {
        String[] names = {"Object", "String", "String", "Object", "Object"};
        // The 0th "String" is at index 1
        assertEquals(1, constantPoolWithNullBands.matchSpecificPoolEntryIndex(names, "String", 0));
        // The 2nd "Object" (0-indexed) is at index 4
        assertEquals(4, constantPoolWithNullBands.matchSpecificPoolEntryIndex(names, "Object", 2));
    }

    @Test
    public void matchSpecificPoolEntryIndexShouldReturnCorrectIndexForFirstNullMatch() {
        String[] names = {null, "a", "b"};
        assertEquals(0, constantPoolWithNullBands.matchSpecificPoolEntryIndex(names, null, 0));
    }

    @Test
    public void matchSpecificPoolEntryIndexShouldReturnNegativeOneForNoMatch() {
        String[] names = {"a", "b", "c"};
        assertEquals(-1, constantPoolWithNullBands.matchSpecificPoolEntryIndex(names, "d", 0));
    }

    @Test
    public void matchSpecificPoolEntryIndexShouldReturnNegativeOneForIndexOutOfBounds() {
        String[] names = {"a", "b", "a"};
        // There is no 2nd 'b'
        assertEquals(-1, constantPoolWithNullBands.matchSpecificPoolEntryIndex(names, "b", 1));
        // Negative desired index is not found
        assertEquals(-1, constantPoolWithNullBands.matchSpecificPoolEntryIndex(names, "a", -1));
    }

    @Test
    public void matchSpecificPoolEntryIndexWithTwoArraysShouldReturnNegativeOneForNoMatch() {
        String[] primary =   {"A", "B", "A"};
        String[] secondary = {"X", "Y", "Z"};
        // Looking for A that has a secondary value matching "Y", which doesn't exist.
        int result = constantPoolWithNullBands.matchSpecificPoolEntryIndex(primary, secondary, "A", "Y", 0);
        assertEquals(-1, result);
    }

    @Test(expected = NullPointerException.class)
    public void matchSpecificPoolEntryIndexWithTwoArraysAndNullRegexShouldThrowNPE() {
        String[] primary = {"A"};
        String[] secondary = {"X"};
        constantPoolWithNullBands.matchSpecificPoolEntryIndex(primary, secondary, "A", null, 0);
    }
}