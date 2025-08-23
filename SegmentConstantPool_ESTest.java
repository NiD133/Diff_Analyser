package org.apache.commons.compress.harmony.unpack200;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.harmony.unpack200.bytecode.ConstantPoolEntry;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable, focused tests for SegmentConstantPool.
 * 
 * Notes:
 * - Tests avoid relying on internal state of CpBands (we pass null) and focus on
 *   behaviors that do not require fully initialized bands.
 * - For exception cases we assert only on the exception type to reduce brittleness.
 * - Protected methods are tested from the same package.
 */
public class SegmentConstantPoolTest {

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    private SegmentConstantPool newPool() {
        // Passing null bands is fine for the behaviors we test here.
        return new SegmentConstantPool(null);
    }

    // ---------------------------------------------------------------------
    // regexMatches
    // ---------------------------------------------------------------------

    @Test
    public void regexMatches_allPatternMatchesAnyString() {
        assertTrue(SegmentConstantPool.regexMatches(".*", ""));
        assertTrue(SegmentConstantPool.regexMatches(".*", "anything"));
        assertTrue(SegmentConstantPool.regexMatches(".*", ".*")); // literal string still matches ".*"
    }

    @Test
    public void regexMatches_initPatternMatchesOnlyInitNames() {
        String initRegex = SegmentConstantPool.REGEX_MATCH_INIT;

        assertTrue(SegmentConstantPool.regexMatches(initRegex, "<init>"));
        assertTrue(SegmentConstantPool.regexMatches(initRegex, "<init>Something"));
        assertFalse(SegmentConstantPool.regexMatches(initRegex, ""));
        assertFalse(SegmentConstantPool.regexMatches(initRegex, "notInit"));
    }

    @Test(expected = Error.class)
    public void regexMatches_unknownPatternThrowsError() {
        SegmentConstantPool.regexMatches("not-a-supported-pattern", "value");
    }

    // ---------------------------------------------------------------------
    // toIndex / toIntExact
    // ---------------------------------------------------------------------

    @Test
    public void toIndex_withinRangeReturnsSameValue() throws Exception {
        assertEquals(0, SegmentConstantPool.toIndex(0L));
        assertEquals(1, SegmentConstantPool.toIndex(1L));
        assertEquals(123456, SegmentConstantPool.toIndex(123456L));
    }

    @Test(expected = Pack200Exception.class)
    public void toIndex_negativeIndexThrows() throws Exception {
        SegmentConstantPool.toIndex(-1L);
    }

    @Test
    public void toIntExact_withinIntRangeReturnsSameValue() throws Exception {
        assertEquals(0, SegmentConstantPool.toIntExact(0L));
        assertEquals(1, SegmentConstantPool.toIntExact(1L));
        assertEquals(-119, SegmentConstantPool.toIntExact(-119L));
        assertEquals(Integer.MAX_VALUE, SegmentConstantPool.toIntExact((long) Integer.MAX_VALUE));
        assertEquals(Integer.MIN_VALUE, SegmentConstantPool.toIntExact((long) Integer.MIN_VALUE));
    }

    @Test(expected = Pack200Exception.class)
    public void toIntExact_outOfIntRangeThrows() throws Exception {
        SegmentConstantPool.toIntExact(((long) Integer.MAX_VALUE) + 1L);
    }

    // ---------------------------------------------------------------------
    // matchSpecificPoolEntryIndex (single array)
    // ---------------------------------------------------------------------

    @Test
    public void matchSpecificPoolEntryIndex_findsNthOccurrenceByEquals() {
        SegmentConstantPool pool = newPool();
        String[] names = {"A", "B", "B", "A", "A"}; // indices: 0..4

        // Looking for the 0th, 1st, 2nd occurrences of "A"
        assertEquals(0, pool.matchSpecificPoolEntryIndex(names, "A", 0));
        assertEquals(3, pool.matchSpecificPoolEntryIndex(names, "A", 1));
        assertEquals(4, pool.matchSpecificPoolEntryIndex(names, "A", 2));

        // Looking for the 1st occurrence of "B" (i.e., second hit)
        assertEquals(2, pool.matchSpecificPoolEntryIndex(names, "B", 1));
    }

    @Test
    public void matchSpecificPoolEntryIndex_returnsMinusOneWhenNotFound() {
        SegmentConstantPool pool = newPool();
        String[] names = {"X", "Y"};

        assertEquals(-1, pool.matchSpecificPoolEntryIndex(names, "Z", 0)); // no Z at all
        assertEquals(-1, pool.matchSpecificPoolEntryIndex(names, "X", 1)); // only one X
    }

    // ---------------------------------------------------------------------
    // matchSpecificPoolEntryIndex (paired arrays)
    // ---------------------------------------------------------------------

    @Test
    public void matchSpecificPoolEntryIndex_pairArrays_matchesEqualsAndInitRegex() {
        SegmentConstantPool pool = newPool();

        String[] primary =   {"C", "M", "M", "C", "M"};
        String[] secondary = {"<init>", "foo", "<init>bar", "noop", "<init>baz"};

        // Find the 0th and 1st occurrences where:
        //   primary equals "M"
        //   secondary matches ^<init>.*
        int regexIndex0 = pool.matchSpecificPoolEntryIndex(
            primary, secondary, "M", SegmentConstantPool.REGEX_MATCH_INIT, 0);
        int regexIndex1 = pool.matchSpecificPoolEntryIndex(
            primary, secondary, "M", SegmentConstantPool.REGEX_MATCH_INIT, 1);

        assertEquals(2, regexIndex0); // "M" + "<init>bar"
        assertEquals(4, regexIndex1); // "M" + "<init>baz"
    }

    @Test
    public void matchSpecificPoolEntryIndex_pairArrays_returnsMinusOneWhenNotFound() {
        SegmentConstantPool pool = newPool();

        String[] primary =   {"C", "M", "M"};
        String[] secondary = {"<init>", "foo", "bar"};

        int index = pool.matchSpecificPoolEntryIndex(
            primary, secondary, "M", SegmentConstantPool.REGEX_MATCH_INIT, 0);

        assertEquals(-1, index); // none of the "M" entries match the init regex
    }

    // ---------------------------------------------------------------------
    // High-level API: minimal, stable exception behavior
    // ---------------------------------------------------------------------

    @Test(expected = Pack200Exception.class)
    public void getInitMethodPoolEntry_wrongCpThrows() throws Exception {
        // Only CP_METHOD is allowed for <init>
        newPool().getInitMethodPoolEntry(SegmentConstantPool.CP_CLASS, 0L, "java/lang/Object");
    }

    @Test(expected = Pack200Exception.class)
    public void getConstantPoolEntry_signatureTypeIsNotSupported() throws Exception {
        // SIGNATURE is documented as not supported in some implementations
        newPool().getConstantPoolEntry(SegmentConstantPool.SIGNATURE, 1L);
    }

    @Test(expected = Pack200Exception.class)
    public void getConstantPoolEntry_descriptorTypeIsNotSupported() throws Exception {
        // CP_DESCR is documented as not supported in some implementations
        newPool().getConstantPoolEntry(SegmentConstantPool.CP_DESCR, 1L);
    }

    @Test(expected = Pack200Exception.class)
    public void getClassSpecificPoolEntry_unsupportedTypeThrows() throws Exception {
        newPool().getClassSpecificPoolEntry(-999, 0L, "java/lang/Object");
    }

    // ---------------------------------------------------------------------
    // getConstantPoolEntry: null-response contract for negative type/index
    // (kept very minimal; behavior may vary across implementations)
    // ---------------------------------------------------------------------

    @Test
    public void getConstantPoolEntry_returnsNullForNegativeTypeOrIndex() throws Exception {
        ConstantPoolEntry e1 = newPool().getConstantPoolEntry(-1, -1);
        assertNull(e1);
    }
}