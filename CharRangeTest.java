package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertNullPointerException;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link CharRange}.
 *
 * Goals:
 * - Use descriptive test names.
 * - Avoid assertion repetition with focused helpers.
 * - Document intent for tricky cases (reversed bounds, negated ranges, iterator edges).
 */
class CharRangeTest extends AbstractLangTest {

    // region Class-level behavior

    @Test
    @DisplayName("Class is package-private and final")
    void classModifiers() {
        assertFalse(Modifier.isPublic(CharRange.class.getModifiers()), "CharRange should be non-public");
        assertTrue(Modifier.isFinal(CharRange.class.getModifiers()), "CharRange should be final");
    }

    // endregion

    // region Factory methods and accessors

    @Test
    @DisplayName("is(ch) creates a single-character non-negated range")
    void factory_is_singleChar() {
        final CharRange range = CharRange.is('a');
        assertRange(range, 'a', 'a', false, "a");
    }

    @Test
    @DisplayName("isIn(start, end) normal order")
    void factory_isIn_normalOrder() {
        final CharRange range = CharRange.isIn('a', 'e');
        assertRange(range, 'a', 'e', false, "a-e");
    }

    @Test
    @DisplayName("isIn(start, end) reversed order gets normalized")
    void factory_isIn_reversedOrder() {
        final CharRange range = CharRange.isIn('e', 'a');
        assertRange(range, 'a', 'e', false, "a-e");
    }

    @Test
    @DisplayName("isIn(start, end) same start and end collapse to single char")
    void factory_isIn_sameBounds() {
        final CharRange range = CharRange.isIn('a', 'a');
        assertRange(range, 'a', 'a', false, "a");
    }

    @Test
    @DisplayName("isNot(ch) creates a negated single-character range")
    void factory_isNot_singleChar() {
        final CharRange range = CharRange.isNot('a');
        assertRange(range, 'a', 'a', true, "^a");
    }

    @Test
    @DisplayName("isNotIn(start, end) normal order")
    void factory_isNotIn_normalOrder() {
        final CharRange range = CharRange.isNotIn('a', 'e');
        assertRange(range, 'a', 'e', true, "^a-e");
    }

    @Test
    @DisplayName("isNotIn(start, end) reversed order gets normalized")
    void factory_isNotIn_reversedOrder() {
        final CharRange range = CharRange.isNotIn('e', 'a');
        assertRange(range, 'a', 'e', true, "^a-e");
    }

    @Test
    @DisplayName("isNotIn(start, end) same start and end collapse to single char")
    void factory_isNotIn_sameBounds() {
        final CharRange range = CharRange.isNotIn('a', 'a');
        assertRange(range, 'a', 'a', true, "^a");
    }

    // endregion

    // region contains(char)

    @Test
    @DisplayName("contains(char) for normal and negated ranges")
    void contains_char() {
        // Single-character range
        CharRange range = CharRange.is('c');
        assertContainsNone(range, 'b', 'd', 'e');
        assertContainsAll(range, 'c');

        // Normal range (in-order)
        range = CharRange.isIn('c', 'd');
        assertContainsAll(range, 'c', 'd');
        assertContainsNone(range, 'b', 'e');

        // Normal range (reversed-order input normalized)
        range = CharRange.isIn('d', 'c');
        assertContainsAll(range, 'c', 'd');
        assertContainsNone(range, 'b', 'e');

        // Negated range
        range = CharRange.isNotIn('c', 'd');
        assertContainsAll(range, 'b', 'e', (char) 0, Character.MAX_VALUE);
        assertContainsNone(range, 'c', 'd');
    }

    // endregion

    // region contains(CharRange)

    @Test
    @DisplayName("contains(CharRange) across normal and negated ranges")
    void contains_range() {
        // Single-character ranges
        final CharRange a = CharRange.is('a');
        final CharRange b = CharRange.is('b');
        final CharRange c = CharRange.is('c');
        final CharRange c2 = CharRange.is('c');
        final CharRange d = CharRange.is('d');
        final CharRange e = CharRange.is('e');

        // Normal ranges
        final CharRange cd = CharRange.isIn('c', 'd');
        final CharRange bd = CharRange.isIn('b', 'd');
        final CharRange bc = CharRange.isIn('b', 'c');
        final CharRange ab = CharRange.isIn('a', 'b');
        final CharRange de = CharRange.isIn('d', 'e');
        final CharRange ef = CharRange.isIn('e', 'f');
        final CharRange ae = CharRange.isIn('a', 'e');

        // Normal vs normal
        assertNotContains(c, b);
        assertContains(c, c);
        assertContains(c, c2);
        assertNotContains(c, d);

        assertNotContains(c, cd);
        assertNotContains(c, bd);
        assertNotContains(c, bc);
        assertNotContains(c, ab);
        assertNotContains(c, de);

        assertContains(cd, c);
        assertContains(bd, c);
        assertContains(bc, c);
        assertNotContains(ab, c);
        assertNotContains(de, c);

        assertContains(ae, b);
        assertContains(ae, ab);
        assertContains(ae, bc);
        assertContains(ae, cd);
        assertContains(ae, de);

        // Negated ranges
        final CharRange notb = CharRange.isNot('b');
        final CharRange notc = CharRange.isNot('c');
        final CharRange notd = CharRange.isNot('d');
        final CharRange notab = CharRange.isNotIn('a', 'b');
        final CharRange notbc = CharRange.isNotIn('b', 'c');
        final CharRange notbd = CharRange.isNotIn('b', 'd');
        final CharRange notcd = CharRange.isNotIn('c', 'd');
        final CharRange notde = CharRange.isNotIn('d', 'e');
        final CharRange notae = CharRange.isNotIn('a', 'e');

        final CharRange all = CharRange.isIn((char) 0, Character.MAX_VALUE);
        final CharRange allButFirst = CharRange.isIn((char) 1, Character.MAX_VALUE);

        // Normal vs negated
        assertNotContains(c, notc);
        assertNotContains(c, notbd);
        assertContains(all, notc);
        assertContains(all, notbd);
        assertNotContains(allButFirst, notc);
        assertNotContains(allButFirst, notbd);

        // Negated vs normal
        assertContains(notc, a);
        assertContains(notc, b);
        assertNotContains(notc, c);
        assertContains(notc, d);
        assertContains(notc, e);

        assertContains(notc, ab);
        assertNotContains(notc, bc);
        assertNotContains(notc, bd);
        assertNotContains(notc, cd);
        assertContains(notc, de);
        assertNotContains(notc, ae);
        assertNotContains(notc, all);
        assertNotContains(notc, allButFirst);

        assertContains(notbd, a);
        assertNotContains(notbd, b);
        assertNotContains(notbd, c);
        assertNotContains(notbd, d);
        assertContains(notbd, e);

        assertContains(notcd, ab);
        assertNotContains(notcd, bc);
        assertNotContains(notcd, bd);
        assertNotContains(notcd, cd);
        assertNotContains(notcd, de);
        assertNotContains(notcd, ae);
        assertContains(notcd, ef);
        assertNotContains(notcd, all);
        assertNotContains(notcd, allButFirst);

        // Negated vs negated
        assertNotContains(notc, notb);
        assertContains(notc, notc);
        assertNotContains(notc, notd);

        assertNotContains(notc, notab);
        assertContains(notc, notbc);
        assertContains(notc, notbd);
        assertContains(notc, notcd);
        assertNotContains(notc, notde);

        assertNotContains(notbd, notb);
        assertNotContains(notbd, notc);
        assertNotContains(notbd, notd);

        assertNotContains(notbd, notab);
        assertNotContains(notbd, notbc);
        assertContains(notbd, notbd);
        assertNotContains(notbd, notcd);
        assertNotContains(notbd, notde);
        assertContains(notbd, notae);
    }

    @Test
    @DisplayName("contains(null) throws NPE with parameter name")
    void contains_nullArg() {
        final CharRange range = CharRange.is('a');
        final NullPointerException npe = assertNullPointerException(() -> range.contains(null));
        assertEquals("range", npe.getMessage());
    }

    // endregion

    // region equals and hashCode

    @Test
    @DisplayName("equals: reflexive, symmetric for equal factory results, and unequal across differing ranges")
    void equals_contract() {
        final CharRange a = CharRange.is('a');
        final CharRange ae = CharRange.isIn('a', 'e');
        final CharRange bf = CharRange.isIn('b', 'f');

        assertNotEquals(null, a);

        assertEquals(a, a);
        assertEquals(a, CharRange.is('a'));

        assertEquals(ae, ae);
        assertEquals(ae, CharRange.isIn('a', 'e'));

        assertEquals(bf, bf);
        assertEquals(bf, CharRange.isIn('b', 'f'));

        assertNotEquals(a, ae);
        assertNotEquals(a, bf);
        assertNotEquals(ae, a);
        assertNotEquals(ae, bf);
        assertNotEquals(bf, a);
        assertNotEquals(bf, ae);
    }

    @Test
    @DisplayName("hashCode consistent with equals for equal factory results and different for unequal ranges")
    void hashCode_contract() {
        final CharRange a = CharRange.is('a');
        final CharRange ae = CharRange.isIn('a', 'e');
        final CharRange bf = CharRange.isIn('b', 'f');

        assertEquals(a.hashCode(), a.hashCode());
        assertEquals(a.hashCode(), CharRange.is('a').hashCode());

        assertEquals(ae.hashCode(), ae.hashCode());
        assertEquals(ae.hashCode(), CharRange.isIn('a', 'e').hashCode());

        assertEquals(bf.hashCode(), bf.hashCode());
        assertEquals(bf.hashCode(), CharRange.isIn('b', 'f').hashCode());

        assertNotEquals(a.hashCode(), ae.hashCode());
        assertNotEquals(a.hashCode(), bf.hashCode());
        assertNotEquals(ae.hashCode(), a.hashCode());
        assertNotEquals(ae.hashCode(), bf.hashCode());
        assertNotEquals(bf.hashCode(), a.hashCode());
        assertNotEquals(bf.hashCode(), ae.hashCode());
    }

    // endregion

    // region Iterator

    @Test
    @DisplayName("iterator() yields expected characters for normal, negated, and edge cases")
    void iterator_behavior() {
        final CharRange singleA = CharRange.is('a');
        final CharRange aToD = CharRange.isIn('a', 'd');
        final CharRange notA = CharRange.isNot('a');

        // Edge cases that produce an empty or single-element iterator:
        final CharRange emptySet = CharRange.isNotIn((char) 0, Character.MAX_VALUE); // excludes all chars
        final CharRange onlyFirst = CharRange.isNotIn((char) 1, Character.MAX_VALUE); // only '\u0000' allowed
        final CharRange onlyLast = CharRange.isNotIn((char) 0, (char) (Character.MAX_VALUE - 1)); // only MAX_VALUE allowed

        // Single element
        assertIteratorYields(singleA, 'a');

        // Multiple elements in order
        assertIteratorYields(aToD, 'a', 'b', 'c', 'd');

        // Negated single excludes that one element; iterate and ensure 'a' never appears
        final Iterator<Character> notAIt = notA.iterator();
        assertNotNull(notAIt);
        assertTrue(notAIt.hasNext());
        while (notAIt.hasNext()) {
            assertNotEquals('a', notAIt.next().charValue(), "Negated range must not include excluded char");
        }

        // Empty iterator throws on next
        final Iterator<Character> emptyIt = emptySet.iterator();
        assertNotNull(emptyIt);
        assertFalse(emptyIt.hasNext());
        assertThrows(NoSuchElementException.class, emptyIt::next);

        // Single allowed values for negated ranges
        final Iterator<Character> onlyFirstIt = onlyFirst.iterator();
        assertNotNull(onlyFirstIt);
        assertTrue(onlyFirstIt.hasNext());
        assertEquals(Character.valueOf((char) 0), onlyFirstIt.next());
        assertFalse(onlyFirstIt.hasNext());
        assertThrows(NoSuchElementException.class, onlyFirstIt::next);

        final Iterator<Character> onlyLastIt = onlyLast.iterator();
        assertNotNull(onlyLastIt);
        assertTrue(onlyLastIt.hasNext());
        assertEquals(Character.valueOf(Character.MAX_VALUE), onlyLastIt.next());
        assertFalse(onlyLastIt.hasNext());
        assertThrows(NoSuchElementException.class, onlyLastIt::next);
    }

    @Test
    @DisplayName("iterator().remove() is unsupported")
    void iterator_remove_isUnsupported() {
        final Iterator<Character> it = CharRange.is('a').iterator();
        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    // endregion

    // region Serialization

    @Test
    @DisplayName("Serialization preserves value semantics")
    void serialization_roundTrip() {
        CharRange range = CharRange.is('a');
        assertEquals(range, SerializationUtils.clone(range));

        range = CharRange.isIn('a', 'e');
        assertEquals(range, SerializationUtils.clone(range));

        range = CharRange.isNotIn('a', 'e');
        assertEquals(range, SerializationUtils.clone(range));
    }

    // endregion

    // region Test helpers

    private static void assertRange(final CharRange range, final char start, final char end, final boolean negated, final String expectedToString) {
        assertEquals(start, range.getStart(), "start");
        assertEquals(end, range.getEnd(), "end");
        assertEquals(negated, range.isNegated(), "negated");
        assertEquals(expectedToString, range.toString(), "toString");
    }

    private static void assertContainsAll(final CharRange range, final char... chars) {
        for (final char c : chars) {
            assertTrue(range.contains(c), () -> "Expected to contain '" + printable(c) + "'");
        }
    }

    private static void assertContainsNone(final CharRange range, final char... chars) {
        for (final char c : chars) {
            assertFalse(range.contains(c), () -> "Expected NOT to contain '" + printable(c) + "'");
        }
    }

    private static void assertContains(final CharRange container, final CharRange containee) {
        assertTrue(container.contains(containee),
                () -> container + " should contain " + containee);
    }

    private static void assertNotContains(final CharRange container, final CharRange containee) {
        assertFalse(container.contains(containee),
                () -> container + " should NOT contain " + containee);
    }

    private static void assertIteratorYields(final CharRange range, final char... expected) {
        final Iterator<Character> it = range.iterator();
        assertNotNull(it);
        for (final char exp : expected) {
            assertTrue(it.hasNext(), "Expected more elements");
            assertEquals(Character.valueOf(exp), it.next(), "Unexpected element");
        }
        assertFalse(it.hasNext(), "Iterator should be exhausted");
    }

    private static String printable(final char c) {
        if (Character.isISOControl(c)) {
            return String.format("\\u%04x", (int) c);
        }
        return Character.toString(c);
    }

    // endregion
}