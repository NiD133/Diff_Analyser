/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link CharSet} class.
 */
class CharSetTest extends AbstractLangTest {

    @Test
    void testClassModifiers() {
        assertTrue(Modifier.isPublic(CharSet.class.getModifiers()), "CharSet should be public");
        assertFalse(Modifier.isFinal(CharSet.class.getModifiers()), "CharSet should not be final");
    }

    @Test
    void testCharSetCreationWithSimpleStrings() {
        verifyCharSet("abc", new CharRange[] {
            CharRange.is('a'), CharRange.is('b'), CharRange.is('c')
        });

        verifyCharSet("a-ce-f", new CharRange[] {
            CharRange.isIn('a', 'c'), CharRange.isIn('e', 'f')
        });

        verifyCharSet("ae-f", new CharRange[] {
            CharRange.is('a'), CharRange.isIn('e', 'f')
        });

        verifyCharSet("e-fa", new CharRange[] {
            CharRange.is('a'), CharRange.isIn('e', 'f')
        });

        verifyCharSet("ae-fm-pz", new CharRange[] {
            CharRange.is('a'), CharRange.isIn('e', 'f'), CharRange.isIn('m', 'p'), CharRange.is('z')
        });
    }

    @Test
    void testCharSetCreationWithNegatedStrings() {
        verifyCharSet("^abc", new CharRange[] {
            CharRange.isNot('a'), CharRange.is('b'), CharRange.is('c')
        });

        verifyCharSet("b^ac", new CharRange[] {
            CharRange.is('b'), CharRange.isNot('a'), CharRange.is('c')
        });

        verifyCharSet("db^ac", new CharRange[] {
            CharRange.is('d'), CharRange.is('b'), CharRange.isNot('a'), CharRange.is('c')
        });

        verifyCharSet("^b^a", new CharRange[] {
            CharRange.isNot('b'), CharRange.isNot('a')
        });

        verifyCharSet("b^a-c^z", new CharRange[] {
            CharRange.isNotIn('a', 'c'), CharRange.isNot('z'), CharRange.is('b')
        });
    }

    @Test
    void testCharSetCreationWithOddCombinations() {
        verifyOddCombination("a-^c", new char[]{'^', '_', 'c'}, new char[]{'b'});
        verifyOddCombination("^a-^c", new char[]{'b'}, new char[]{'^', '_'});
        verifyOddCombination("a- ^-- ", new char[]{'#', '^', 'a', '*', 'A'}, new char[]{});
        verifyOddCombination("^-b", new char[]{'b', '_', '^'}, new char[]{'A'});
        verifyOddCombination("b-^", new char[]{'b', '^', 'a'}, new char[]{'c'});
    }

    @Test
    void testCharSetCreationWithOddDash() {
        verifyCharSet("-", new CharRange[] { CharRange.is('-') });
        verifyCharSet("--", new CharRange[] { CharRange.is('-') });
        verifyCharSet("---", new CharRange[] { CharRange.is('-') });
        verifyCharSet("----", new CharRange[] { CharRange.is('-') });
        verifyCharSet("-a", new CharRange[] { CharRange.is('-'), CharRange.is('a') });
        verifyCharSet("a-", new CharRange[] { CharRange.is('a'), CharRange.is('-') });
        verifyCharSet("a--", new CharRange[] { CharRange.isIn('a', '-') });
        verifyCharSet("--a", new CharRange[] { CharRange.isIn('-', 'a') });
    }

    @Test
    void testCharSetCreationWithOddNegate() {
        verifyCharSet("^", new CharRange[] { CharRange.is('^') });
        verifyCharSet("^^", new CharRange[] { CharRange.isNot('^') });
        verifyCharSet("^^^", new CharRange[] { CharRange.isNot('^'), CharRange.is('^') });
        verifyCharSet("^^^^", new CharRange[] { CharRange.isNot('^') });
        verifyCharSet("a^", new CharRange[] { CharRange.is('a'), CharRange.is('^') });
        verifyCharSet("^a-", new CharRange[] { CharRange.isNot('a'), CharRange.is('-') });
        verifyCharSet("^^-c", new CharRange[] { CharRange.isNotIn('^', 'c') });
        verifyCharSet("^c-^", new CharRange[] { CharRange.isNotIn('c', '^') });
        verifyCharSet("^c-^d", new CharRange[] { CharRange.isNotIn('c', '^'), CharRange.is('d') });
        verifyCharSet("^^-", new CharRange[] { CharRange.isNot('^'), CharRange.is('-') });
    }

    @Test
    void testCharSetCreationWithSimpleRanges() {
        verifyCharSet(null, new CharRange[] {});
        verifyCharSet("", new CharRange[] {});
        verifyCharSet("a", new CharRange[] { CharRange.is('a') });
        verifyCharSet("^a", new CharRange[] { CharRange.isNot('a') });
        verifyCharSet("a-e", new CharRange[] { CharRange.isIn('a', 'e') });
        verifyCharSet("^a-e", new CharRange[] { CharRange.isNotIn('a', 'e') });
    }

    @Test
    void testCharSetContainsChar() {
        CharSet btod = CharSet.getInstance("b-d");
        CharSet dtob = CharSet.getInstance("d-b");
        CharSet bcd = CharSet.getInstance("bcd");
        CharSet bd = CharSet.getInstance("bd");
        CharSet notbtod = CharSet.getInstance("^b-d");

        assertContains(btod, new char[]{'b', 'c', 'd'}, new char[]{'a', 'e'});
        assertContains(bcd, new char[]{'b', 'c', 'd'}, new char[]{'a', 'e'});
        assertContains(bd, new char[]{'b', 'd'}, new char[]{'a', 'c', 'e'});
        assertContains(notbtod, new char[]{'a', 'e'}, new char[]{'b', 'c', 'd'});
        assertContains(dtob, new char[]{'b', 'c', 'd'}, new char[]{'a', 'e'});

        assertEquals("[b-d]", dtob.toString());
        assertEquals(1, dtob.getCharRanges().length);
    }

    @Test
    void testCharSetEquality() {
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

    @Test
    void testCharSetGetInstance() {
        assertSame(CharSet.EMPTY, CharSet.getInstance((String) null));
        assertSame(CharSet.EMPTY, CharSet.getInstance(""));
        assertSame(CharSet.ASCII_ALPHA, CharSet.getInstance("a-zA-Z"));
        assertSame(CharSet.ASCII_ALPHA, CharSet.getInstance("A-Za-z"));
        assertSame(CharSet.ASCII_ALPHA_LOWER, CharSet.getInstance("a-z"));
        assertSame(CharSet.ASCII_ALPHA_UPPER, CharSet.getInstance("A-Z"));
        assertSame(CharSet.ASCII_NUMERIC, CharSet.getInstance("0-9"));
    }

    @Test
    void testCharSetGetInstanceWithStringArray() {
        assertNull(CharSet.getInstance((String[]) null));
        assertEquals("[]", CharSet.getInstance().toString());
        assertEquals("[]", CharSet.getInstance(new String[]{null}).toString());
        assertEquals("[a-e]", CharSet.getInstance("a-e").toString());
    }

    @Test
    void testCharSetHashCode() {
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

    @Test
    void testCharSetJavadocExamples() {
        assertFalse(CharSet.getInstance("^a-c").contains('a'));
        assertTrue(CharSet.getInstance("^a-c").contains('d'));
        assertTrue(CharSet.getInstance("^^a-c").contains('a'));
        assertFalse(CharSet.getInstance("^^a-c").contains('^'));
        assertTrue(CharSet.getInstance("^a-cd-f").contains('d'));
        assertTrue(CharSet.getInstance("a-c^").contains('^'));
        assertTrue(CharSet.getInstance("^", "a-c").contains('^'));
    }

    @Test
    void testCharSetSerialization() {
        verifySerialization("a");
        verifySerialization("a-e");
        verifySerialization("be-f^a-z");
    }

    @Test
    void testCharSetStatics() {
        verifyStaticCharSet(CharSet.EMPTY, new CharRange[]{});
        verifyStaticCharSet(CharSet.ASCII_ALPHA, new CharRange[]{
            CharRange.isIn('a', 'z'), CharRange.isIn('A', 'Z')
        });
        verifyStaticCharSet(CharSet.ASCII_ALPHA_LOWER, new CharRange[]{
            CharRange.isIn('a', 'z')
        });
        verifyStaticCharSet(CharSet.ASCII_ALPHA_UPPER, new CharRange[]{
            CharRange.isIn('A', 'Z')
        });
        verifyStaticCharSet(CharSet.ASCII_NUMERIC, new CharRange[]{
            CharRange.isIn('0', '9')
        });
    }

    private void verifyCharSet(String input, CharRange[] expectedRanges) {
        CharSet set = CharSet.getInstance(input);
        CharRange[] actualRanges = set.getCharRanges();
        assertEquals(expectedRanges.length, actualRanges.length, "Unexpected number of ranges");
        for (CharRange expectedRange : expectedRanges) {
            assertTrue(ArrayUtils.contains(actualRanges, expectedRange), "Expected range not found: " + expectedRange);
        }
    }

    private void verifyOddCombination(String input, char[] expectedContains, char[] expectedNotContains) {
        CharSet set = CharSet.getInstance(input);
        for (char ch : expectedContains) {
            assertTrue(set.contains(ch), "Expected set to contain: " + ch);
        }
        for (char ch : expectedNotContains) {
            assertFalse(set.contains(ch), "Expected set to not contain: " + ch);
        }
    }

    private void assertContains(CharSet set, char[] expectedContains, char[] expectedNotContains) {
        for (char ch : expectedContains) {
            assertTrue(set.contains(ch), "Expected set to contain: " + ch);
        }
        for (char ch : expectedNotContains) {
            assertFalse(set.contains(ch), "Expected set to not contain: " + ch);
        }
    }

    private void verifySerialization(String input) {
        CharSet set = CharSet.getInstance(input);
        assertEquals(set, SerializationUtils.clone(set), "Serialized object does not match original");
    }

    private void verifyStaticCharSet(CharSet charSet, CharRange[] expectedRanges) {
        CharRange[] actualRanges = charSet.getCharRanges();
        assertEquals(expectedRanges.length, actualRanges.length, "Unexpected number of ranges");
        for (CharRange expectedRange : expectedRanges) {
            assertTrue(ArrayUtils.contains(actualRanges, expectedRange), "Expected range not found: " + expectedRange);
        }
    }
}