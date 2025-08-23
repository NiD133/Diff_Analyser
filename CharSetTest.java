package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Tests for CharSet.
 *
 * Goals:
 * - Make intent-driven tests with small helpers to reduce noise.
 * - Group cases by behavior (construction, contains, equality, constants, etc.).
 * - Add short comments where parsing can be surprising (dash '-' and caret '^').
 */
class CharSetTest extends AbstractLangTest {

    // ---------- Test helpers ----------

    private static CharSet set(final String... specs) {
        return CharSet.getInstance(specs);
    }

    private static void assertContainsAll(final CharSet s, final char... chars) {
        for (final char c : chars) {
            assertTrue(s.contains(c), "Expected set " + s + " to contain '" + c + "'");
        }
    }

    private static void assertExcludesAll(final CharSet s, final char... chars) {
        for (final char c : chars) {
            assertFalse(s.contains(c), "Expected set " + s + " to NOT contain '" + c + "'");
        }
    }

    /**
     * Asserts that a set has exactly the given ranges, ignoring order.
     */
    private static void assertRangesUnordered(final CharSet s, final CharRange... expected) {
        final Set<CharRange> expectedSet = new HashSet<>(Arrays.asList(expected));
        final Set<CharRange> actualSet = new HashSet<>(Arrays.asList(s.getCharRanges()));
        assertEquals(expectedSet, actualSet, "Unexpected ranges for " + s);
    }

    // ---------- Class/visibility ----------

    @Test
    void testClassModifiers() {
        assertTrue(Modifier.isPublic(CharSet.class.getModifiers()));
        assertFalse(Modifier.isFinal(CharSet.class.getModifiers()));
    }

    // ---------- Construction: simple cases ----------

    @Test
    void testConstruction_simple() {
        CharSet s;
        CharRange[] ranges;

        s = set((String) null);
        ranges = s.getCharRanges();
        assertEquals("[]", s.toString());
        assertEquals(0, ranges.length);

        s = set("");
        ranges = s.getCharRanges();
        assertEquals("[]", s.toString());
        assertEquals(0, ranges.length);

        s = set("a");
        ranges = s.getCharRanges();
        assertEquals("[a]", s.toString());
        assertEquals(1, ranges.length);
        assertEquals("a", ranges[0].toString());

        s = set("^a");
        ranges = s.getCharRanges();
        assertEquals("[^a]", s.toString());
        assertEquals(1, ranges.length);
        assertEquals("^a", ranges[0].toString());

        s = set("a-e");
        ranges = s.getCharRanges();
        assertEquals("[a-e]", s.toString());
        assertEquals(1, ranges.length);
        assertEquals("a-e", ranges[0].toString());

        s = set("^a-e");
        ranges = s.getCharRanges();
        assertEquals("[^a-e]", s.toString());
        assertEquals(1, ranges.length);
        assertEquals("^a-e", ranges[0].toString());
    }

    // ---------- Construction: combinations ----------

    @Test
    void testConstruction_combinations() {
        // "abc" -> three singletons
        assertRangesUnordered(set("abc"),
                CharRange.is('a'),
                CharRange.is('b'),
                CharRange.is('c'));

        // "a-ce-f" -> two ranges
        assertRangesUnordered(set("a-ce-f"),
                CharRange.isIn('a', 'c'),
                CharRange.isIn('e', 'f'));

        // "ae-f" -> a singleton and a range
        assertRangesUnordered(set("ae-f"),
                CharRange.is('a'),
                CharRange.isIn('e', 'f'));

        // "e-fa" -> order does not matter
        assertRangesUnordered(set("e-fa"),
                CharRange.is('a'),
                CharRange.isIn('e', 'f'));

        // "ae-fm-pz" -> mixture of singletons and ranges
        assertRangesUnordered(set("ae-fm-pz"),
                CharRange.is('a'),
                CharRange.isIn('e', 'f'),
                CharRange.isIn('m', 'p'),
                CharRange.is('z'));
    }

    // ---------- Construction: combinations with negation ----------

    @Test
    void testConstruction_combinations_negated() {
        // Leading negate only applies to the immediately following element when used as "^x" or "^x-y".
        assertRangesUnordered(set("^abc"),
                CharRange.isNot('a'),
                CharRange.is('b'),
                CharRange.is('c'));

        assertRangesUnordered(set("b^ac"),
                CharRange.is('b'),
                CharRange.isNot('a'),
                CharRange.is('c'));

        assertRangesUnordered(set("db^ac"),
                CharRange.is('d'),
                CharRange.is('b'),
                CharRange.isNot('a'),
                CharRange.is('c'));

        // Multiple negations are independent elements
        assertRangesUnordered(set("^b^a"),
                CharRange.isNot('b'),
                CharRange.isNot('a'));

        assertRangesUnordered(set("b^a-c^z"),
                CharRange.isNotIn('a', 'c'),
                CharRange.isNot('z'),
                CharRange.is('b'));
    }

    // ---------- Construction: odd combinations around '^' and '-' ----------

    @Test
    void testConstruction_oddCombinations() {
        // "a-^c" -> "a-^" then "c". '-' treats the next char as range end even if it's '^'.
        CharSet s = set("a-^c");
        assertRangesUnordered(s,
                CharRange.isIn('a', '^'),
                CharRange.is('c'));
        assertExcludesAll(s, 'b');
        assertContainsAll(s, '^', '_', 'c'); // '_' is between '^' and 'a' in ASCII

        // "^a-^c" -> "^a-^" (negated range 'a'..'^') then 'c'
        s = set("^a-^c");
        assertRangesUnordered(s,
                CharRange.isNotIn('a', '^'),
                CharRange.is('c'));
        assertContainsAll(s, 'b');
        assertExcludesAll(s, '^', '_');

        // "a- ^-- " -> "a- " then negated "-- " i.e., everything except '-'..' '
        s = set("a- ^-- ");
        assertRangesUnordered(s,
                CharRange.isIn('a', ' '),
                CharRange.isNotIn('-', ' '));
        assertContainsAll(s, '#', '^', 'a', '*', 'A');

        // "^-b" -> range '^'..'b'
        s = set("^-b");
        assertRangesUnordered(s, CharRange.isIn('^', 'b'));
        assertContainsAll(s, 'b', '_', '^');
        assertExcludesAll(s, 'A');

        // "b-^" -> reversed range handled as '^'..'b'
        s = set("b-^");
        assertRangesUnordered(s, CharRange.isIn('^', 'b'));
        assertContainsAll(s, 'b', '^', 'a'); // 'a' is between '^' and 'b'
        assertExcludesAll(s, 'c');
    }

    // ---------- Construction: tricky dashes ----------

    @Test
    void testConstruction_dashEdgeCases() {
        // A single or stacked '-' can be a literal, unless it forms a valid range "x-y".
        assertRangesUnordered(set("-"), CharRange.is('-'));
        assertRangesUnordered(set("--"), CharRange.is('-'));
        assertRangesUnordered(set("---"), CharRange.is('-'));
        assertRangesUnordered(set("----"), CharRange.is('-'));

        assertRangesUnordered(set("-a"),
                CharRange.is('-'),
                CharRange.is('a'));

        assertRangesUnordered(set("a-"),
                CharRange.is('a'),
                CharRange.is('-'));

        // "a--" and "--a" produce ranges (a..'-') or ('-'..a) respectively
        assertRangesUnordered(set("a--"), CharRange.isIn('a', '-'));
        assertRangesUnordered(set("--a"), CharRange.isIn('-', 'a'));
    }

    // ---------- Construction: tricky '^' (negation) ----------

    @Test
    void testConstruction_negationEdgeCases() {
        CharSet s;
        CharRange[] ranges;

        // Bare "^" is a literal if alone
        s = set("^");
        ranges = s.getCharRanges();
        assertEquals(1, ranges.length);
        assertTrue(Arrays.asList(ranges).contains(CharRange.is('^')));

        // "^^" -> a single negated '^'
        s = set("^^");
        ranges = s.getCharRanges();
        assertEquals(1, ranges.length);
        assertTrue(Arrays.asList(ranges).contains(CharRange.isNot('^')));

        // "^^^" -> negation of '^' plus a literal '^'
        s = set("^^^");
        assertRangesUnordered(s,
                CharRange.isNot('^'),
                CharRange.is('^'));

        // "^^^^" -> two negations of '^' (duplication de-duplicated by set)
        s = set("^^^^");
        assertRangesUnordered(s, CharRange.isNot('^'));

        // "a^" -> 'a' and literal '^'
        s = set("a^");
        assertRangesUnordered(s, CharRange.is('a'), CharRange.is('^'));

        // "^a-" -> negate 'a' and a literal '-'
        s = set("^a-");
        assertRangesUnordered(s, CharRange.isNot('a'), CharRange.is('-'));

        // "^^-c" -> negate '^'..'c'
        s = set("^^-c");
        assertRangesUnordered(s, CharRange.isNotIn('^', 'c'));

        // "^c-^" -> negate 'c'..'^'
        s = set("^c-^");
        assertRangesUnordered(s, CharRange.isNotIn('c', '^'));

        // "^c-^d" -> negate 'c'..'^' and a literal 'd'
        s = set("^c-^d");
        assertRangesUnordered(s, CharRange.isNotIn('c', '^'), CharRange.is('d'));

        // "^^-" -> negate '^' and a literal '-'
        s = set("^^-");
        assertRangesUnordered(s, CharRange.isNot('^'), CharRange.is('-'));
    }

    // ---------- Contains semantics ----------

    @Test
    void testContains_char() {
        final CharSet bToD = set("b-d");
        final CharSet dToB = set("d-b"); // reversed should normalize to "b-d"
        final CharSet bcd = set("bcd");
        final CharSet bd = set("bd");
        final CharSet notBToD = set("^b-d");

        assertExcludesAll(bToD, 'a', 'e');
        assertContainsAll(bToD, 'b', 'c', 'd');

        assertExcludesAll(bcd, 'a', 'e');
        assertContainsAll(bcd, 'b', 'c', 'd');

        assertExcludesAll(bd, 'a', 'c', 'e');
        assertContainsAll(bd, 'b', 'd');

        assertContainsAll(notBToD, 'a', 'e');
        assertExcludesAll(notBToD, 'b', 'c', 'd');

        assertExcludesAll(dToB, 'a', 'e');
        assertContainsAll(dToB, 'b', 'c', 'd');

        // Normalization check: "d-b" becomes [b-d] as a single range
        assertEquals("[b-d]", dToB.toString());
        assertEquals(1, dToB.getCharRanges().length);
    }

    // ---------- Equality and hashCode ----------

    @Test
    void testEquals_and_hashCode() {
        final CharSet abc = set("abc");
        final CharSet abc2 = set("abc");
        final CharSet aToC = set("a-c");
        final CharSet aToC2 = set("a-c");
        final CharSet notAToC = set("^a-c");
        final CharSet notAToC2 = set("^a-c");

        assertNotEquals(null, abc);

        // equals
        assertEquals(abc, abc);
        assertEquals(abc, abc2);
        assertNotEquals(abc, aToC);
        assertNotEquals(abc, notAToC);

        assertNotEquals(aToC, abc);
        assertEquals(aToC, aToC);
        assertEquals(aToC, aToC2);
        assertNotEquals(aToC, notAToC);

        assertNotEquals(notAToC, abc);
        assertNotEquals(notAToC, aToC);
        assertEquals(notAToC, notAToC);
        assertEquals(notAToC, notAToC2);

        // hashCode contract
        assertEquals(abc.hashCode(), abc.hashCode());
        assertEquals(abc.hashCode(), abc2.hashCode());
        assertEquals(aToC.hashCode(), aToC.hashCode());
        assertEquals(aToC.hashCode(), aToC2.hashCode());
        assertEquals(notAToC.hashCode(), notAToC.hashCode());
        assertEquals(notAToC.hashCode(), notAToC2.hashCode());
    }

    // ---------- Factory: common instances and caching ----------

    @Test
    void testGetInstance_commonAndCaching() {
        assertSame(CharSet.EMPTY, set((String) null));
        assertSame(CharSet.EMPTY, set(""));
        assertSame(CharSet.ASCII_ALPHA, set("a-zA-Z"));
        assertSame(CharSet.ASCII_ALPHA, set("A-Za-z"));
        assertSame(CharSet.ASCII_ALPHA_LOWER, set("a-z"));
        assertSame(CharSet.ASCII_ALPHA_UPPER, set("A-Z"));
        assertSame(CharSet.ASCII_NUMERIC, set("0-9"));
    }

    @Test
    void testGetInstance_varargs() {
        assertNull(CharSet.getInstance((String[]) null));
        assertEquals("[]", set().toString());
        assertEquals("[]", set((String) null).toString());
        assertEquals("[a-e]", set("a-e").toString());
    }

    // ---------- Predefined constants ----------

    @Test
    void testConstants() {
        CharRange[] ranges;

        ranges = CharSet.EMPTY.getCharRanges();
        assertEquals(0, ranges.length);

        ranges = CharSet.ASCII_ALPHA.getCharRanges();
        assertEquals(2, ranges.length);
        assertTrue(Arrays.asList(ranges).contains(CharRange.isIn('a', 'z')));
        assertTrue(Arrays.asList(ranges).contains(CharRange.isIn('A', 'Z')));

        ranges = CharSet.ASCII_ALPHA_LOWER.getCharRanges();
        assertEquals(1, ranges.length);
        assertTrue(Arrays.asList(ranges).contains(CharRange.isIn('a', 'z')));

        ranges = CharSet.ASCII_ALPHA_UPPER.getCharRanges();
        assertEquals(1, ranges.length);
        assertTrue(Arrays.asList(ranges).contains(CharRange.isIn('A', 'Z')));

        ranges = CharSet.ASCII_NUMERIC.getCharRanges();
        assertEquals(1, ranges.length);
        assertTrue(Arrays.asList(ranges).contains(CharRange.isIn('0', '9')));
    }

    // ---------- Serialization ----------

    @Test
    void testSerialization_roundtrip() {
        CharSet s = set("a");
        assertEquals(s, SerializationUtils.clone(s));

        s = set("a-e");
        assertEquals(s, SerializationUtils.clone(s));

        s = set("be-f^a-z");
        assertEquals(s, SerializationUtils.clone(s));
    }

    // ---------- Javadoc examples (documentation contract) ----------

    @Test
    void testJavadocExamples() {
        assertFalse(set("^a-c").contains('a'));
        assertTrue(set("^a-c").contains('d'));
        assertTrue(set("^^a-c").contains('a'));
        assertFalse(set("^^a-c").contains('^'));
        assertTrue(set("^a-cd-f").contains('d'));
        assertTrue(set("a-c^").contains('^'));
        assertTrue(set("^", "a-c").contains('^'));
    }
}