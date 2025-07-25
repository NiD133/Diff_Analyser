package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CharSet}.
 */
class CharSetTest extends AbstractLangTest {

    /**
     * Tests the visibility and finality of the CharSet class.
     */
    @Test
    void testClassModifiers() {
        assertTrue(Modifier.isPublic(CharSet.class.getModifiers()), "CharSet should be public");
        assertFalse(Modifier.isFinal(CharSet.class.getModifiers()), "CharSet should not be final");
    }

    /**
     * Tests CharSet creation with simple character combinations.
     */
    @Test
    void testConstructorWithSimpleCombinations() {
        assertCharSet("abc", new CharRange[] { CharRange.is('a'), CharRange.is('b'), CharRange.is('c') });
        assertCharSet("a-ce-f", new CharRange[] { CharRange.isIn('a', 'c'), CharRange.isIn('e', 'f') });
        assertCharSet("ae-f", new CharRange[] { CharRange.is('a'), CharRange.isIn('e', 'f') });
        assertCharSet("e-fa", new CharRange[] { CharRange.is('a'), CharRange.isIn('e', 'f') });
        assertCharSet("ae-fm-pz", new CharRange[] { CharRange.is('a'), CharRange.isIn('e', 'f'), CharRange.isIn('m', 'p'), CharRange.is('z') });
    }

    /**
     * Tests CharSet creation with negated character combinations.
     */
    @Test
    void testConstructorWithNegatedCombinations() {
        assertCharSet("^abc", new CharRange[] { CharRange.isNot('a'), CharRange.is('b'), CharRange.is('c') });
        assertCharSet("b^ac", new CharRange[] { CharRange.is('b'), CharRange.isNot('a'), CharRange.is('c') });
        assertCharSet("db^ac", new CharRange[] { CharRange.is('d'), CharRange.is('b'), CharRange.isNot('a'), CharRange.is('c') });
        assertCharSet("^b^a", new CharRange[] { CharRange.isNot('b'), CharRange.isNot('a') });
        assertCharSet("b^a-c^z", new CharRange[] { CharRange.isNotIn('a', 'c'), CharRange.isNot('z'), CharRange.is('b') });
    }

    /**
     * Tests CharSet creation with odd character combinations.
     */
    @Test
    void testConstructorWithOddCombinations() {
        assertCharSet("a-^c", new CharRange[] { CharRange.isIn('a', '^'), CharRange.is('c') }, 'b', false, '^', true, '_', true, 'c', true);
        assertCharSet("^a-^c", new CharRange[] { CharRange.isNotIn('a', '^'), CharRange.is('c') }, 'b', true, '^', false, '_', false);
        assertCharSet("a- ^-- ", new CharRange[] { CharRange.isIn('a', ' '), CharRange.isNotIn('-', ' ') }, '#', true, '^', true, 'a', true, '*', true, 'A', true);
        assertCharSet("^-b", new CharRange[] { CharRange.isIn('^', 'b') }, 'b', true, '_', true, 'A', false, '^', true);
        assertCharSet("b-^", new CharRange[] { CharRange.isIn('^', 'b') }, 'b', true, '^', true, 'a', true, 'c', false);
    }

    /**
     * Tests CharSet creation with odd dash combinations.
     */
    @Test
    void testConstructorWithOddDash() {
        assertCharSet("-", new CharRange[] { CharRange.is('-') });
        assertCharSet("--", new CharRange[] { CharRange.is('-') });
        assertCharSet("---", new CharRange[] { CharRange.is('-') });
        assertCharSet("----", new CharRange[] { CharRange.is('-') });
        assertCharSet("-a", new CharRange[] { CharRange.is('-'), CharRange.is('a') });
        assertCharSet("a-", new CharRange[] { CharRange.is('a'), CharRange.is('-') });
        assertCharSet("a--", new CharRange[] { CharRange.isIn('a', '-') });
        assertCharSet("--a", new CharRange[] { CharRange.isIn('-', 'a') });
    }

    /**
     * Tests CharSet creation with odd negation combinations.
     */
    @Test
    void testConstructorWithOddNegate() {
        assertCharSet("^", new CharRange[] { CharRange.is('^') });
        assertCharSet("^^", new CharRange[] { CharRange.isNot('^') });
        assertCharSet("^^^", new CharRange[] { CharRange.isNot('^'), CharRange.is('^') });
        assertCharSet("^^^^", new CharRange[] { CharRange.isNot('^') });
        assertCharSet("a^", new CharRange[] { CharRange.is('a'), CharRange.is('^') });
        assertCharSet("^a-", new CharRange[] { CharRange.isNot('a'), CharRange.is('-') });
        assertCharSet("^^-c", new CharRange[] { CharRange.isNotIn('^', 'c') });
        assertCharSet("^c-^", new CharRange[] { CharRange.isNotIn('c', '^') });
        assertCharSet("^c-^d", new CharRange[] { CharRange.isNotIn('c', '^'), CharRange.is('d') });
        assertCharSet("^^-", new CharRange[] { CharRange.isNot('^'), CharRange.is('-') });
    }

    /**
     * Tests CharSet creation with simple strings.
     */
    @Test
    void testConstructorWithSimpleStrings() {
        assertCharSet((String) null, new CharRange[0]);
        assertCharSet("", new CharRange[0]);
        assertCharSet("a", new CharRange[] { CharRange.is('a') });
        assertCharSet("^a", new CharRange[] { CharRange.isNot('a') });
        assertCharSet("a-e", new CharRange[] { CharRange.isIn('a', 'e') });
        assertCharSet("^a-e", new CharRange[] { CharRange.isNotIn('a', 'e') });
    }

    /**
     * Tests the contains method of CharSet.
     */
    @Test
    void testContainsChar() {
        CharSet btod = CharSet.getInstance("b-d");
        CharSet dtob = CharSet.getInstance("d-b");
        CharSet bcd = CharSet.getInstance("bcd");
        CharSet bd = CharSet.getInstance("bd");
        CharSet notbtod = CharSet.getInstance("^b-d");

        assertContains(btod, 'a', false, 'b', true, 'c', true, 'd', true, 'e', false);
        assertContains(bcd, 'a', false, 'b', true, 'c', true, 'd', true, 'e', false);
        assertContains(bd, 'a', false, 'b', true, 'c', false, 'd', true, 'e', false);
        assertContains(notbtod, 'a', true, 'b', false, 'c', false, 'd', false, 'e', true);
        assertContains(dtob, 'a', false, 'b', true, 'c', true, 'd', true, 'e', false);

        CharRange[] array = dtob.getCharRanges();
        assertEquals("[b-d]", dtob.toString());
        assertEquals(1, array.length);
    }

    /**
     * Tests the equals method of CharSet.
     */
    @Test
    void testEquals() {
        CharSet abc = CharSet.getInstance("abc");
        CharSet abc2 = CharSet.getInstance("abc");
        CharSet atoc = CharSet.getInstance("a-c");
        CharSet atoc2 = CharSet.getInstance("a-c");
        CharSet notatoc = CharSet.getInstance("^a-c");
        CharSet notatoc2 = CharSet.getInstance("^a-c");

        assertNotEquals(null, abc);

        assertEquals(abc, abc);
        assertEquals(abc, abc2);
        assertNotEquals(abc, atoc);
        assertNotEquals(abc, notatoc);

        assertNotEquals(atoc, abc);
        assertEquals(atoc, atoc);
        assertEquals(atoc, atoc2);
        assertNotEquals(atoc, notatoc);

        assertNotEquals(notatoc, abc);
        assertNotEquals(notatoc, atoc);
        assertEquals(notatoc, notatoc);
        assertEquals(notatoc, notatoc2);
    }

    /**
     * Tests the getInstance method of CharSet.
     */
    @Test
    void testGetInstance() {
        assertSame(CharSet.EMPTY, CharSet.getInstance((String) null));
        assertSame(CharSet.EMPTY, CharSet.getInstance(""));
        assertSame(CharSet.ASCII_ALPHA, CharSet.getInstance("a-zA-Z"));
        assertSame(CharSet.ASCII_ALPHA, CharSet.getInstance("A-Za-z"));
        assertSame(CharSet.ASCII_ALPHA_LOWER, CharSet.getInstance("a-z"));
        assertSame(CharSet.ASCII_ALPHA_UPPER, CharSet.getInstance("A-Z"));
        assertSame(CharSet.ASCII_NUMERIC, CharSet.getInstance("0-9"));
    }

    /**
     * Tests the getInstance method with a string array.
     */
    @Test
    void testGetInstanceWithStringArray() {
        assertNull(CharSet.getInstance((String[]) null));
        assertEquals("[]", CharSet.getInstance().toString());
        assertEquals("[]", CharSet.getInstance(new String[] { null }).toString());
        assertEquals("[a-e]", CharSet.getInstance("a-e").toString());
    }

    /**
     * Tests the hashCode method of CharSet.
     */
    @Test
    void testHashCode() {
        CharSet abc = CharSet.getInstance("abc");
        CharSet abc2 = CharSet.getInstance("abc");
        CharSet atoc = CharSet.getInstance("a-c");
        CharSet atoc2 = CharSet.getInstance("a-c");
        CharSet notatoc = CharSet.getInstance("^a-c");
        CharSet notatoc2 = CharSet.getInstance("^a-c");

        assertEquals(abc.hashCode(), abc.hashCode());
        assertEquals(abc.hashCode(), abc2.hashCode());
        assertEquals(atoc.hashCode(), atoc.hashCode());
        assertEquals(atoc.hashCode(), atoc2.hashCode());
        assertEquals(notatoc.hashCode(), notatoc.hashCode());
        assertEquals(notatoc.hashCode(), notatoc2.hashCode());
    }

    /**
     * Tests examples from the CharSet Javadoc.
     */
    @Test
    void testJavadocExamples() {
        assertFalse(CharSet.getInstance("^a-c").contains('a'));
        assertTrue(CharSet.getInstance("^a-c").contains('d'));
        assertTrue(CharSet.getInstance("^^a-c").contains('a'));
        assertFalse(CharSet.getInstance("^^a-c").contains('^'));
        assertTrue(CharSet.getInstance("^a-cd-f").contains('d'));
        assertTrue(CharSet.getInstance("a-c^").contains('^'));
        assertTrue(CharSet.getInstance("^", "a-c").contains('^'));
    }

    /**
     * Tests the serialization of CharSet.
     */
    @Test
    void testSerialization() {
        CharSet set = CharSet.getInstance("a");
        assertEquals(set, SerializationUtils.clone(set));
        set = CharSet.getInstance("a-e");
        assertEquals(set, SerializationUtils.clone(set));
        set = CharSet.getInstance("be-f^a-z");
        assertEquals(set, SerializationUtils.clone(set));
    }

    /**
     * Tests static CharSet instances.
     */
    @Test
    void testStaticCharSets() {
        assertCharSet(CharSet.EMPTY, new CharRange[0]);
        assertCharSet(CharSet.ASCII_ALPHA, new CharRange[] { CharRange.isIn('a', 'z'), CharRange.isIn('A', 'Z') });
        assertCharSet(CharSet.ASCII_ALPHA_LOWER, new CharRange[] { CharRange.isIn('a', 'z') });
        assertCharSet(CharSet.ASCII_ALPHA_UPPER, new CharRange[] { CharRange.isIn('A', 'Z') });
        assertCharSet(CharSet.ASCII_NUMERIC, new CharRange[] { CharRange.isIn('0', '9') });
    }

    /**
     * Helper method to assert CharSet creation and contents.
     */
    private void assertCharSet(String input, CharRange[] expectedRanges, Object... containsChecks) {
        CharSet set = CharSet.getInstance(input);
        CharRange[] array = set.getCharRanges();
        assertEquals(expectedRanges.length, array.length, "Unexpected number of ranges for input: " + input);
        for (CharRange expectedRange : expectedRanges) {
            assertTrue(ArrayUtils.contains(array, expectedRange), "Expected range not found: " + expectedRange);
        }
        for (int i = 0; i < containsChecks.length; i += 2) {
            char ch = (char) containsChecks[i];
            boolean expected = (boolean) containsChecks[i + 1];
            assertEquals(expected, set.contains(ch), "Unexpected contains result for char: " + ch);
        }
    }

    /**
     * Helper method to assert CharSet contains checks.
     */
    private void assertContains(CharSet set, Object... containsChecks) {
        for (int i = 0; i < containsChecks.length; i += 2) {
            char ch = (char) containsChecks[i];
            boolean expected = (boolean) containsChecks[i + 1];
            assertEquals(expected, set.contains(ch), "Unexpected contains result for char: " + ch);
        }
    }
}