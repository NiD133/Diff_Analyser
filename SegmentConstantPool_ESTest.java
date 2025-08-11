package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import org.apache.commons.compress.harmony.unpack200.CpBands;
import org.apache.commons.compress.harmony.unpack200.SegmentConstantPool;
import org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry;
import org.apache.commons.compress.harmony.unpack200.bytecode.ConstantPoolEntry;

/**
 * Tests for SegmentConstantPool functionality including constant pool entry retrieval,
 * regex matching, index conversion, and pool entry matching.
 */
public class SegmentConstantPool_ESTest {

    private static final String INIT_REGEX = "^<init>.*";
    private static final String MATCH_ALL_REGEX = ".*";
    
    // Test fixture setup
    private SegmentConstantPool createPoolWithNullBands() {
        return new SegmentConstantPool(null);
    }

    // ========== Pool Entry Matching Tests ==========
    
    @Test
    public void testMatchSpecificPoolEntry_WithNullValue_ReturnsMinusOne() {
        SegmentConstantPool pool = createPoolWithNullBands();
        String[] stringArray = new String[6];
        
        int result = pool.matchSpecificPoolEntryIndex(stringArray, stringArray[0], -182);
        
        assertEquals(-1, result);
    }

    @Test
    public void testMatchSpecificPoolEntry_WithNullValueAtIndex_ReturnsMinusOne() {
        SegmentConstantPool pool = createPoolWithNullBands();
        String[] stringArray = new String[4];
        
        int result = pool.matchSpecificPoolEntryIndex(stringArray, stringArray[2], 5);
        
        assertEquals(-1, result);
    }

    @Test
    public void testMatchSpecificPoolEntry_WithNullValueAtZeroIndex_ReturnsZero() {
        SegmentConstantPool pool = createPoolWithNullBands();
        String[] stringArray = new String[8];
        
        int result = pool.matchSpecificPoolEntryIndex(stringArray, stringArray[2], 0);
        
        assertEquals(0, result);
    }

    @Test
    public void testMatchSpecificPoolEntry_WithMatchingValue_ReturnsCorrectIndex() {
        SegmentConstantPool pool = createPoolWithNullBands();
        String[] stringArray = new String[8];
        stringArray[1] = INIT_REGEX;
        
        int result = pool.matchSpecificPoolEntryIndex(stringArray, INIT_REGEX, 0);
        
        assertEquals(1, result);
    }

    @Test(expected = NullPointerException.class)
    public void testMatchSpecificPoolEntry_WithNullArray_ThrowsNullPointerException() {
        SegmentConstantPool pool = createPoolWithNullBands();
        
        pool.matchSpecificPoolEntryIndex(null, null, 5);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testMatchSpecificPoolEntry_WithEmptySecondaryArray_ThrowsArrayIndexOutOfBoundsException() {
        SegmentConstantPool pool = createPoolWithNullBands();
        String[] emptyArray = new String[0];
        String[] primaryArray = new String[2];
        
        pool.matchSpecificPoolEntryIndex(primaryArray, emptyArray, primaryArray[1], ":M", 2501);
    }

    @Test(expected = NullPointerException.class)
    public void testMatchSpecificPoolEntry_WithNullCompareString_ThrowsNullPointerException() {
        SegmentConstantPool pool = createPoolWithNullBands();
        String[] stringArray = new String[6];
        stringArray[0] = "((";
        
        pool.matchSpecificPoolEntryIndex(stringArray, stringArray, null, INIT_REGEX, 0);
    }

    @Test
    public void testMatchSpecificPoolEntry_WithNonMatchingRegex_ReturnsMinusOne() {
        SegmentConstantPool pool = createPoolWithNullBands();
        String[] stringArray = new String[11];
        stringArray[2] = "^]LzTC)tW7*]J5tWdvD$";
        
        int result = pool.matchSpecificPoolEntryIndex(stringArray, stringArray, "^]LzTC)tW7*]J5tWdvD$", INIT_REGEX, -1311);
        
        assertEquals(-1, result);
    }

    @Test(expected = Error.class)
    public void testMatchSpecificPoolEntry_WithUnknownRegexPattern_ThrowsError() {
        SegmentConstantPool pool = createPoolWithNullBands();
        String[] stringArray = new String[1];
        stringArray[0] = "v^<iPit>.*";
        
        pool.matchSpecificPoolEntryIndex(stringArray, stringArray, "v^<iPit>.*", "v^<iPit>.*", -1089);
    }

    // ========== Regex Matching Tests ==========
    
    @Test
    public void testRegexMatches_InitRegexWithNonMatchingString_ReturnsFalse() {
        boolean result = SegmentConstantPool.regexMatches(INIT_REGEX, "v_g:,");
        
        assertFalse(result);
    }

    @Test
    public void testRegexMatches_MatchAllRegexWithAnyString_ReturnsTrue() {
        boolean result = SegmentConstantPool.regexMatches(MATCH_ALL_REGEX, MATCH_ALL_REGEX);
        
        assertTrue(result);
    }

    @Test
    public void testRegexMatches_InitRegexWithEmptyString_ReturnsFalse() {
        boolean result = SegmentConstantPool.regexMatches(INIT_REGEX, "");
        
        assertFalse(result);
    }

    @Test(expected = NullPointerException.class)
    public void testRegexMatches_WithNullCompareString_ThrowsNullPointerException() {
        SegmentConstantPool.regexMatches(INIT_REGEX, null);
    }

    @Test(expected = Error.class)
    public void testRegexMatches_WithUnknownPattern_ThrowsError() {
        SegmentConstantPool.regexMatches("gml`F", "6x,WAe3");
    }

    // ========== Index Conversion Tests ==========
    
    @Test
    public void testToIntExact_WithZero_ReturnsZero() throws Exception {
        int result = SegmentConstantPool.toIntExact(0L);
        
        assertEquals(0, result);
    }

    @Test
    public void testToIntExact_WithNegativeValue_ReturnsNegativeInt() throws Exception {
        int result = SegmentConstantPool.toIntExact(-119L);
        
        assertEquals(-119, result);
    }

    @Test
    public void testToIntExact_WithPositiveValue_ReturnsPositiveInt() throws Exception {
        int result = SegmentConstantPool.toIntExact(1);
        
        assertEquals(1, result);
    }

    @Test(expected = IOException.class)
    public void testToIntExact_WithValueTooLarge_ThrowsIOException() throws Exception {
        SegmentConstantPool.toIntExact(4294967294L);
    }

    @Test
    public void testToIndex_WithZero_ReturnsZero() throws Exception {
        int result = SegmentConstantPool.toIndex(0L);
        
        assertEquals(0, result);
    }

    @Test
    public void testToIndex_WithPositiveValue_ReturnsPositiveInt() throws Exception {
        int result = SegmentConstantPool.toIndex(1L);
        
        assertEquals(1, result);
    }

    @Test(expected = IOException.class)
    public void testToIndex_WithNegativeValue_ThrowsIOException() throws Exception {
        SegmentConstantPool.toIndex(-1975L);
    }

    // ========== Constant Pool Entry Retrieval Tests ==========
    
    @Test
    public void testGetConstantPoolEntry_WithNegativeType_ReturnsNull() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        
        ConstantPoolEntry result = pool.getConstantPoolEntry(-1, -1);
        
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testGetConstantPoolEntry_WithUnsupportedType_ThrowsIOException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        
        pool.getConstantPoolEntry(-13, 1152L);
    }

    @Test(expected = IOException.class)
    public void testGetConstantPoolEntry_WithNegativeIndex_ThrowsIOException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        
        pool.getConstantPoolEntry(0, -1988L);
    }

    @Test(expected = IOException.class)
    public void testGetConstantPoolEntry_WithDescriptorType_ThrowsIOException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        
        pool.getConstantPoolEntry(SegmentConstantPool.CP_DESCR, 4);
    }

    @Test(expected = IOException.class)
    public void testGetConstantPoolEntry_WithSignatureType_ThrowsIOException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        
        pool.getConstantPoolEntry(SegmentConstantPool.SIGNATURE, 8);
    }

    // Tests for various constant pool types with null bands (expecting NullPointerException)
    @Test(expected = NullPointerException.class)
    public void testGetConstantPoolEntry_Utf8WithNullBands_ThrowsNullPointerException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        pool.getConstantPoolEntry(SegmentConstantPool.UTF_8, 2586L);
    }

    @Test(expected = NullPointerException.class)
    public void testGetConstantPoolEntry_IntWithNullBands_ThrowsNullPointerException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        pool.getConstantPoolEntry(SegmentConstantPool.CP_INT, 1137L);
    }

    @Test(expected = NullPointerException.class)
    public void testGetConstantPoolEntry_FloatWithNullBands_ThrowsNullPointerException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        pool.getConstantPoolEntry(SegmentConstantPool.CP_FLOAT, 1152L);
    }

    @Test(expected = NullPointerException.class)
    public void testGetConstantPoolEntry_LongWithNullBands_ThrowsNullPointerException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        pool.getConstantPoolEntry(SegmentConstantPool.CP_LONG, 2L);
    }

    @Test(expected = NullPointerException.class)
    public void testGetConstantPoolEntry_DoubleWithNullBands_ThrowsNullPointerException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        pool.getConstantPoolEntry(SegmentConstantPool.CP_DOUBLE, 2009L);
    }

    @Test(expected = NullPointerException.class)
    public void testGetConstantPoolEntry_StringWithNullBands_ThrowsNullPointerException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        pool.getConstantPoolEntry(SegmentConstantPool.CP_STRING, 1152L);
    }

    @Test(expected = NullPointerException.class)
    public void testGetConstantPoolEntry_ClassWithNullBands_ThrowsNullPointerException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        pool.getConstantPoolEntry(SegmentConstantPool.CP_CLASS, 1152L);
    }

    @Test(expected = NullPointerException.class)
    public void testGetConstantPoolEntry_FieldWithNullBands_ThrowsNullPointerException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        pool.getConstantPoolEntry(SegmentConstantPool.CP_FIELD, 139L);
    }

    @Test(expected = NullPointerException.class)
    public void testGetConstantPoolEntry_MethodWithNullBands_ThrowsNullPointerException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        pool.getConstantPoolEntry(SegmentConstantPool.CP_METHOD, 10);
    }

    @Test(expected = NullPointerException.class)
    public void testGetConstantPoolEntry_InterfaceMethodWithNullBands_ThrowsNullPointerException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        pool.getConstantPoolEntry(SegmentConstantPool.CP_IMETHOD, 139L);
    }

    // ========== Class-Specific Pool Entry Tests ==========
    
    @Test(expected = IOException.class)
    public void testGetClassSpecificPoolEntry_WithUnsupportedType_ThrowsIOException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        
        pool.getClassSpecificPoolEntry(-1734632697, -1734632697, INIT_REGEX);
    }

    @Test(expected = NullPointerException.class)
    public void testGetClassSpecificPoolEntry_WithNullBands_ThrowsNullPointerException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        
        pool.getClassSpecificPoolEntry(SegmentConstantPool.CP_IMETHOD, 12, "TestClass");
    }

    // ========== Init Method Pool Entry Tests ==========
    
    @Test(expected = IOException.class)
    public void testGetInitMethodPoolEntry_WithNonMethodType_ThrowsIOException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        
        pool.getInitMethodPoolEntry(3120, 3120, "TestMessage");
    }

    @Test(expected = IOException.class)
    public void testGetInitMethodPoolEntry_WithSignatureType_ThrowsIOException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        
        pool.getInitMethodPoolEntry(SegmentConstantPool.SIGNATURE, 8, "TestClass");
    }

    @Test(expected = NullPointerException.class)
    public void testGetInitMethodPoolEntry_WithMethodTypeAndNullBands_ThrowsNullPointerException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        
        pool.getInitMethodPoolEntry(SegmentConstantPool.CP_METHOD, 11, "TestClass");
    }

    // ========== Get Value Tests ==========
    
    @Test
    public void testGetValue_WithInterfaceMethodTypeAndNegativeIndex_ReturnsNull() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        
        ClassFileEntry result = pool.getValue(SegmentConstantPool.CP_IMETHOD, -1L);
        
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testGetValue_WithNegativeRange_ThrowsIOException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        
        pool.getValue(SegmentConstantPool.CP_CLASS, -460L);
    }

    @Test(expected = Error.class)
    public void testGetValue_WithUnknownType_ThrowsError() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        
        pool.getValue(-282, 4294967296L);
    }

    // Tests for getValue with various types and null bands (expecting NullPointerException)
    @Test(expected = NullPointerException.class)
    public void testGetValue_Utf8WithNullBands_ThrowsNullPointerException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        pool.getValue(SegmentConstantPool.UTF_8, 1);
    }

    @Test(expected = NullPointerException.class)
    public void testGetValue_IntWithNullBands_ThrowsNullPointerException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        pool.getValue(SegmentConstantPool.CP_INT, 2);
    }

    @Test(expected = NullPointerException.class)
    public void testGetValue_FloatWithNullBands_ThrowsNullPointerException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        pool.getValue(SegmentConstantPool.CP_FLOAT, 1622L);
    }

    @Test(expected = NullPointerException.class)
    public void testGetValue_LongWithNullBands_ThrowsNullPointerException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        pool.getValue(SegmentConstantPool.CP_LONG, 1970L);
    }

    @Test(expected = NullPointerException.class)
    public void testGetValue_DoubleWithNullBands_ThrowsNullPointerException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        pool.getValue(SegmentConstantPool.CP_DOUBLE, 4);
    }

    @Test(expected = NullPointerException.class)
    public void testGetValue_StringWithNullBands_ThrowsNullPointerException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        pool.getValue(SegmentConstantPool.CP_STRING, 5L);
    }

    @Test(expected = NullPointerException.class)
    public void testGetValue_ClassWithNullBands_ThrowsNullPointerException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        pool.getValue(SegmentConstantPool.CP_CLASS, 5);
    }

    @Test(expected = NullPointerException.class)
    public void testGetValue_SignatureWithNullBands_ThrowsNullPointerException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        pool.getValue(SegmentConstantPool.SIGNATURE, 8);
    }

    @Test(expected = NullPointerException.class)
    public void testGetValue_DescriptorWithNullBands_ThrowsNullPointerException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        pool.getValue(SegmentConstantPool.CP_DESCR, 9);
    }

    // ========== Class Pool Entry Tests ==========
    
    @Test(expected = NullPointerException.class)
    public void testGetClassPoolEntry_WithNullBands_ThrowsNullPointerException() throws Exception {
        SegmentConstantPool pool = createPoolWithNullBands();
        
        pool.getClassPoolEntry("java/lang/Object");
    }
}