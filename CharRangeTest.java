You are a professional software engineer. Your task is to improve the understandability of the following test code, which may be either a single test case or a full test suite. Understandability means the ease with which developers can comprehend and maintain the test code.

Here is the original test code:

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

import static org.apache.commons.lang3.LangAssertions.assertNullPointerException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link CharRange}.
 */
class CharRangeTest extends AbstractLangTest {

    @Test
    void testClass() {
        // class changed to non-public in 3.0
        assertFalse(Modifier.isPublic(CharRange.class.getModifiers()));
        assertTrue(Modifier.isFinal(CharRange.class.getModifiers()));
    }

    @Test
    void testConstructorAccessors_is() {
        final CharRange rangea = CharRange.is('a');
        assertEquals('a', rangea.getStart());
        assertEquals('a', rangea.getEnd());
        assertFalse(rangea.isNegated());
        assertEquals("a", rangea.toString());
    }

    @Test
    void testConstructorAccessors_isIn_Normal() {
        final CharRange rangea = CharRange.isIn('a', 'e');
        assertEquals('a', rangea.getStart());
        assertEquals('e', rangea.getEnd());
        assertFalse(rangea.isNegated());
        assertEquals("a-e", rangea.toString());
    }

    @Test
    void testConstructorAccessors_isIn_Reversed() {
        final CharRange rangea = CharRange.isIn('e', 'a');
        assertEquals('a', rangea.getStart());
        assertEquals('e', rangea.getEnd());
        assertFalse(rangea.isNegated());
        assertEquals("a-e", rangea.toString());
    }

    @Test
    void testConstructorAccessors_isIn_Same() {
        final CharRange rangea = CharRange.isIn('a', 'a');
        assertEquals('a', rangea.getStart());
        assertEquals('a', rangea.getEnd());
        assertFalse(rangea.isNegated());
        assertEquals("a", rangea.toString());
    }

    @Test
    void testConstructorAccessors_isNot() {
        final CharRange rangea = CharRange.isNot('a');
        assertEquals('a', rangea.getStart());
        assertEquals('a', rangea.getEnd());
        assertTrue(rangea.isNegated());
        assertEquals("^a", rangea.toString());
    }

    @Test
    void testConstructorAccessors_isNotIn_Normal() {
        final CharRange rangea = CharRange.isNotIn('a', 'e');
        assertEquals('a', rangea.getStart());
        assertEquals('e', rangea.getEnd());
        assertTrue(rangea.isNegated());
        assertEquals("^a-e", rangea.toString());
    }

    @Test
    void testConstructorAccessors_isNotIn_Reversed() {
        final CharRange rangea = CharRange.isNotIn('e', 'a');
        assertEquals('a', rangea.getStart());
        assertEquals('e', rangea.getEnd());
        assertTrue(rangea.isNegated());
        assertEquals("^a-e", rangea.toString());
    }

    @Test
    void testConstructorAccessors_isNotIn_Same() {
        final CharRange rangea = CharRange.isNotIn('a', 'a');
        assertEquals('a', rangea.getStart());
        assertEquals('a', rangea.getEnd());
        assertTrue(rangea.isNegated());
        assertEquals("^a", rangea.toString());
    }

    @Test
    void testContains_Char() {
        CharRange range = CharRange.is('c');
        assertFalse(range.contains('b'));
        assertTrue(range.contains('c'));
        assertFalse(range.contains('d'));
        assertFalse(range.contains('e'));

        range = CharRange.isIn('c', 'd');
        assertFalse(range.contains('b'));
        assertTrue(range.contains('c'));
        assertTrue(range.contains('d'));
        assertFalse(range.contains('e'));

        range = CharRange.isIn('d', 'c');
        assertFalse(range.contains('b'));
        assertTrue(range.contains('c'));
        assertTrue(range.contains('d'));
        assertFalse(range.contains('e'));

        range = CharRange.isNotIn('c', 'd');
        assertTrue(range.contains('b'));
        assertFalse(range.contains('c'));
        assertFalse(range.contains('d'));
        assertTrue(range.contains('e'));
        assertTrue(range.contains((char) 0));
        assertTrue(range.contains(Character.MAX_VALUE));
    }

    @Test
    void testContains_Charrange() {
        final CharRange a = CharRange.is('a');
        final CharRange b = CharRange.is('b');
        final CharRange c = CharRange.is('c');
        final CharRange c2 = CharRange.is('c');
        final CharRange d = CharRange.is('d');
        final CharRange e = CharRange.is('e');
        final CharRange cd = CharRange.isIn('c', 'd');
        final CharRange bd = CharRange.isIn('b', 'd');
        final CharRange bc = CharRange.isIn('b', 'c');
        final CharRange ab = CharRange.isIn('a', 'b');
        final CharRange de = CharRange.isIn('d', 'e');
        final CharRange ef = CharRange.isIn('e', 'f');
        final CharRange ae = CharRange.isIn('a', 'e');

        // normal/normal
        assertFalse(c.contains(b));
        assertTrue(c.contains(c));
        assertTrue(c.contains(c2));
        assertFalse(c.contains(d));

        assertFalse(c.contains(cd));
        assertFalse(c.contains(bd));
        assertFalse(c.contains(bc));
        assertFalse(c.contains(ab));
        assertFalse(c.contains(de));

        assertTrue(cd.contains(c));
        assertTrue(bd.contains(c));
        assertTrue(bc.contains(c));
        assertFalse(ab.contains(c));
        assertFalse(de.contains(c));

        assertTrue(ae.contains(b));
        assertTrue(ae.contains(ab));
        assertTrue(ae.contains(bc));
        assertTrue(ae.contains(cd));
        assertTrue(ae.contains(de));

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
        final CharRange allbutfirst = CharRange.isIn((char) 1, Character.MAX_VALUE);

        // normal/negated
        assertFalse(c.contains(notc));
        assertFalse(c.contains(notbd));
        assertTrue(all.contains(notc));
        assertTrue(all.contains(notbd));
        assertFalse(allbutfirst.contains(notc));
        assertFalse(allbutfirst.contains(notbd));

        // negated/normal
        assertTrue(notc.contains(a));
        assertTrue(notc.contains(b));
        assertFalse(notc.contains(c));
        assertTrue(notc.contains(d));
        assertTrue(notc.contains(e));

        assertTrue(notc.contains(ab));
        assertFalse(notc.contains(bc));
        assertFalse(notc.contains(bd));
        assertFalse(notc.contains(cd));
        assertTrue(notc.contains(de));
        assertFalse(notc.contains(ae));
        assertFalse(notc.contains(all));
        assertFalse(notc.contains(allbutfirst));

        assertTrue(notbd.contains(a));
        assertFalse(notbd.contains(b));
        assertFalse(notbd.contains(c));
        assertFalse(notbd.contains(d));
        assertTrue(notbd.contains(e));

        assertTrue(notcd.contains(ab));
        assertFalse(notcd.contains(bc));
        assertFalse(notcd.contains(bd));
        assertFalse(notcd.contains(cd));
        assertFalse(notcd.contains(de));
        assertFalse(notcd.contains(ae));
        assertTrue(notcd.contains(ef));
        assertFalse(notcd.contains(all));
        assertFalse(notcd.contains(allbutfirst));

        // negated/negated
        assertFalse(notc.contains(notb));
        assertTrue(notc.contains(notc));
        assertFalse(notc.contains(notd));

        assertFalse(notc.contains(notab));
        assertTrue(notc.contains(notbc));
        assertTrue(notc.contains(notbd));
        assertTrue(notc.contains(notcd));
        assertFalse(notc.contains(notde));

        assertFalse(notbd.contains(notb));
        assertFalse(notbd.contains(notc));
        assertFalse(notbd.contains(notd));

        assertFalse(notbd.contains(notab));
        assertFalse(notbd.contains(notbc));
        assertTrue(notbd.contains(notbd));
        assertFalse(notbd.contains(notcd));
        assertFalse(notbd.contains(notde));
        assertTrue(notbd.contains(notae));
    }

    @Test
    void testContainsNullArg() {
        final CharRange range = CharRange.is('a');
        final NullPointerException e = assertNullPointerException(() -> range.contains(null));
        assertEquals("range", e.getMessage());
    }

    @Test
    void testEquals_Object() {
        final CharRange rangea = CharRange.is('a');
        final CharRange rangeae = CharRange.isIn('a', 'e');
        final CharRange rangenotbf = CharRange.isIn('b', 'f');

        assertNotEquals(null, rangea);

        assertEquals(rangea, rangea);
        assertEquals(rangea, CharRange.is('a'));
        assertEquals(rangeae, rangeae);
        assertEquals(rangeae, CharRange.isIn('a', 'e'));
        assertEquals(rangenotbf, rangenotbf);
        assertEquals(rangenotbf, CharRange.isIn('b', 'f'));

        assertNotEquals(rangea, rangeae);
        assertNotEquals(rangea, rangenotbf);
        assertNotEquals(rangeae, rangea);
        assertNotEquals(rangeae, rangenotbf);
        assertNotEquals(rangenotbf, rangea);
        assertNotEquals(rangenotbf, rangeae);
    }

    @Test
    void testHashCode() {
        final CharRange rangea = CharRange.is('a');
        final CharRange rangeae = CharRange.isIn('a', 'e');
        final CharRange rangenotbf = CharRange.isIn('b', 'f');

        assertEquals(rangea.hashCode(), rangea.hashCode());
        assertEquals(rangea.hashCode(), CharRange.is('a').hashCode());
        assertEquals(rangeae.hashCode(), rangeae.hashCode());
        assertEquals(rangeae.hashCode(), CharRange.isIn('a', 'e').hashCode());
        assertEquals(rangenotbf.hashCode(), rangenotbf.hashCode());
        assertEquals(rangenotbf.hashCode(), CharRange.isIn('b', 'f').hashCode());

        assertNotEquals(rangea.hashCode(), rangeae.hashCode());
        assertNotEquals(rangea.hashCode(), rangenotbf.hashCode());
        assertNotEquals(rangeae.hashCode(), rangea.hashCode());
        assertNotEquals(rangeae.hashCode(), rangenotbf.hashCode());
        assertNotEquals(rangenotbf.hashCode(), rangea.hashCode());
        assertNotEquals(rangenotbf.hashCode(), rangeae.hashCode());
    }

    @Test
    void testIterator() {
        final CharRange a = CharRange.is('a');
        final CharRange ad = CharRange.isIn('a', 'd');
        final CharRange nota = CharRange.isNot('a');
        final CharRange emptySet = CharRange.isNotIn((char) 0, Character.MAX_VALUE);
        final CharRange notFirst = CharRange.isNotIn((char) 1, Character.MAX_VALUE);
        final CharRange notLast = CharRange.isNotIn((char) 0, (char) (Character.MAX_VALUE - 1));

        final Iterator<Character> aIt = a.iterator();
        assertNotNull(aIt);
        assertTrue(aIt.hasNext());
        assertEquals(Character.valueOf('a'), aIt.next());
        assertFalse(aIt.hasNext());

        final Iterator<Character> adIt = ad.iterator();
        assertNotNull(adIt);
        assertTrue(adIt.hasNext());
        assertEquals(Character.valueOf('a'), adIt.next());
        assertEquals(Character.valueOf('b'), adIt.next());
        assertEquals(Character.valueOf('c'), adIt.next());
        assertEquals(Character.valueOf('d'), adIt.next());
        assertFalse(adIt.hasNext());

        final Iterator<Character> notaIt = nota.iterator();
        assertNotNull(notaIt);
        assertTrue(notaIt.hasNext());
        while (notaIt.hasNext()) {
            final Character c = notaIt.next();
            assertNotEquals('a', c.charValue());
        }

        final Iterator<Character> emptySetIt = emptySet.iterator();
        assertNotNull(emptySetIt);
        assertFalse(emptySetIt.hasNext());
        assertThrows(NoSuchElementException.class, emptySetIt::next);

        final Iterator<Character> notFirstIt = notFirst.iterator();
        assertNotNull(notFirstIt);
        assertTrue(notFirstIt.hasNext());
        assertEquals(Character.valueOf((char) 0), notFirstIt.next());
        assertFalse(notFirstIt.hasNext());
        assertThrows(NoSuchElementException.class, notFirstIt::next);

        final Iterator<Character> notLastIt = notLast.iterator();
        assertNotNull(notLastIt);
        assertTrue(notLastIt.hasNext());
        assertEquals(Character.valueOf(Character.MAX_VALUE), notLastIt.next());
        assertFalse(notLastIt.hasNext());
        assertThrows(NoSuchElementException.class, notLastIt::next);
    }

    @Test
    void testIteratorRemove() {
        final CharRange a = CharRange.is('a');
        final Iterator<Character> aIt = a.iterator();
        assertThrows(UnsupportedOperationException.class, aIt::remove);
    }

    @Test
    void testSerialization() {
        CharRange range = CharRange.is('a');
        assertEquals(range, SerializationUtils.clone(range));
        range = CharRange.isIn('a', 'e');
        assertEquals(range, SerializationUtils.clone(range));
        range = CharRange.isNotIn('a', 'e');
        assertEquals(range, SerializationUtils.clone(range));
    }
}

Here is the source class under test:

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang3;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * A contiguous range of characters, optionally negated.
 *
 * <p>Instances are immutable.</p>
 *
 * <p>#ThreadSafe#</p>
 * @since 1.0
 */
// TODO: This is no longer public and will be removed later as CharSet is moved
// to depend on Range.
final class CharRange implements Iterable<Character>, Serializable {

    /**
     * Character {@link Iterator}.
     * <p>#NotThreadSafe#</p>
     */
    private static final class CharacterIterator implements Iterator<Character> {

        /**
         * The current character
         */
        private char current;

        private final CharRange range;

        private boolean hasNext;

        /**
         * Constructs a new iterator for the character range.
         *
         * @param r The character range
         */
        private CharacterIterator(final CharRange r) {
            range = r;
            hasNext = true;
            if (range.negated) {
                if (range.start == 0) {
                    if (range.end == Character.MAX_VALUE) {
                        // This range is an empty set
                        hasNext = false;
                    } else {
                        current = (char) (range.end + 1);
                    }
                } else {
                    current = 0;
                }
            } else {
                current = range.start;
            }
        }

        /**
         * Has the iterator not reached the end character yet?
         *
         * @return {@code true} if the iterator has yet to reach the character date
         */
        @Override
        public boolean hasNext();

        /**
         * Returns the next character in the iteration
         *
         * @return {@link Character} for the next character
         */
        @Override
        public Character next();

        /**
         * Prepares the next character in the range.
         */
        private void prepareNext();

        /**
         * Always throws UnsupportedOperationException.
         *
         * @throws UnsupportedOperationException Always thrown.
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove();
    }

    /**
     * Required for serialization support. Lang version 2.0.
     *
     * @see java.io.Serializable
     */
    private static final long serialVersionUID = 8270183163158333422L;

    /**
     * Empty array.
     */
    static final CharRange[] EMPTY_ARRAY = {};

    /**
     * Constructs a {@link CharRange} over a single character.
     *
     * @param ch  only character in this range
     * @return the new CharRange object
     * @since 2.5
     */
    public static CharRange is(final char ch);

    /**
     * Constructs a {@link CharRange} over a set of characters.
     *
     * <p>If start and end are in the wrong order, they are reversed.
     * Thus {@code a-e} is the same as {@code e-a}.</p>
     *
     * @param start  first character, inclusive, in this range
     * @param end  last character, inclusive, in this range
     * @return the new CharRange object
     * @since 2.5
     */
    public static CharRange isIn(final char start, final char end);

    /**
     * Constructs a negated {@link CharRange} over a single character.
     *
     * <p>A negated range includes everything except that defined by the
     * single character.</p>
     *
     * @param ch  only character in this range
     * @return the new CharRange object
     * @since 2.5
     */
    public static CharRange isNot(final char ch);

    /**
     * Constructs a negated {@link CharRange} over a set of characters.
     *
     * <p>A negated range includes everything except that defined by the
     * start and end characters.</p>
     *
     * <p>If start and end are in the wrong order, they are reversed.
     * Thus {@code a-e} is the same as {@code e-a}.</p>
     *
     * @param start  first character, inclusive, in this range
     * @param end  last character, inclusive, in this range
     * @return the new CharRange object
     * @since 2.5
     */
    public static CharRange isNotIn(final char start, final char end);

    /**
     * The first character, inclusive, in the range.
     */
    private final char start;

    /**
     * The last character, inclusive, in the range.
     */
    private final char end;

    /**
     * True if the range is everything except the characters specified.
     */
    private final boolean negated;

    /**
     * Cached toString.
     */
    private transient String iToString;

    /**
     * Constructs a {@link CharRange} over a set of characters,
     * optionally negating the range.
     *
     * <p>A negated range includes everything except that defined by the
     * start and end characters.</p>
     *
     * <p>If start and end are in the wrong order, they are reversed.
     * Thus {@code a-e} is the same as {@code e-a}.</p>
     *
     * @param start  first character, inclusive, in this range
     * @param end  last character, inclusive, in this range
     * @param negated  true to express everything except the range
     */
    private CharRange(char start, char end, final boolean negated) {
        if (start > end) {
            final char temp = start;
            start = end;
            end = temp;
        }
        this.start = start;
        this.end = end;
        this.negated = negated;
    }

    // Contains
    /**
     * Is the character specified contained in this range.
     *
     * @param ch  the character to check
     * @return {@code true} if this range contains the input character
     */
    public boolean contains(final char ch);

    /**
     * Are all the characters of the passed in range contained in
     * this range.
     *
     * @param range  the range to check against
     * @return {@code true} if this range entirely contains the input range
     * @throws NullPointerException if {@code null} input
     */
    public boolean contains(final CharRange range);

    // Basics
    /**
     * Compares two CharRange objects, returning true if they represent
     * exactly the same range of characters defined in the same way.
     *
     * @param obj  the object to compare to
     * @return true if equal
     */
    @Override
    public boolean equals(final Object obj);

    /**
     * Gets the end character for this character range.
     *
     * @return the end char (inclusive)
     */
    public char getEnd();

    // Accessors
    /**
     * Gets the start character for this character range.
     *
     * @return the start char (inclusive)
     */
    public char getStart();

    /**
     * Gets a hashCode compatible with the equals method.
     *
     * @return a suitable hashCode
     */
    @Override
    public int hashCode();

    /**
     * Is this {@link CharRange} negated.
     *
     * <p>A negated range includes everything except that defined by the
     * start and end characters.</p>
     *
     * @return {@code true} if negated
     */
    public boolean isNegated();

    /**
     * Returns an iterator which can be used to walk through the characters described by this range.
     *
     * <p>#NotThreadSafe# the iterator is not thread-safe</p>
     * @return an iterator to the chars represented by this range
     * @since 2.5
     */
    @Override
    public Iterator<Character> iterator();

    /**
     * Gets a string representation of the character range.
     *
     * @return string representation of this range
     */
    @Override
    public String toString();
}

Please generate a more understandable version of the test code using the above guidelines.