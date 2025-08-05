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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CharRange}.
 */
@DisplayName("Tests for org.apache.commons.lang3.CharRange")
class CharRangeTest extends AbstractLangTest {

    @Test
    @DisplayName("CharRange class should be final and not public")
    void class_shouldBeFinalAndNotPublic() {
        assertFalse(Modifier.isPublic(CharRange.class.getModifiers()));
        assertTrue(Modifier.isFinal(CharRange.class.getModifiers()));
    }

    @Nested
    @DisplayName("Factory Method Tests")
    class FactoryMethodTests {

        @Test
        @DisplayName("is() should create a correct range for a single character")
        void is_shouldCreateCorrectRangeForSingleCharacter() {
            // Arrange
            final char testChar = 'a';

            // Act
            final CharRange range = CharRange.is(testChar);

            // Assert
            assertEquals(testChar, range.getStart());
            assertEquals(testChar, range.getEnd());
            assertFalse(range.isNegated());
            assertEquals("a", range.toString());
        }

        @Test
        @DisplayName("isNot() should create a correct negated range for a single character")
        void isNot_shouldCreateCorrectNegatedRangeForSingleCharacter() {
            // Arrange
            final char testChar = 'a';

            // Act
            final CharRange range = CharRange.isNot(testChar);

            // Assert
            assertEquals(testChar, range.getStart());
            assertEquals(testChar, range.getEnd());
            assertTrue(range.isNegated());
            assertEquals("^a", range.toString());
        }

        @Test
        @DisplayName("isIn() should create a correct range when start is less than end")
        void isIn_shouldCreateCorrectRangeForNormalOrder() {
            // Arrange
            final char start = 'a';
            final char end = 'e';

            // Act
            final CharRange range = CharRange.isIn(start, end);

            // Assert
            assertEquals(start, range.getStart());
            assertEquals(end, range.getEnd());
            assertFalse(range.isNegated());
            assertEquals("a-e", range.toString());
        }

        @Test
        @DisplayName("isIn() should create a correct range when start is greater than end")
        void isIn_shouldCreateCorrectRangeForReversedOrder() {
            // Arrange
            final char start = 'e';
            final char end = 'a';

            // Act
            final CharRange range = CharRange.isIn(start, end);

            // Assert
            assertEquals(end, range.getStart(), "Start should be the smaller character");
            assertEquals(start, range.getEnd(), "End should be the larger character");
            assertFalse(range.isNegated());
            assertEquals("a-e", range.toString());
        }

        @Test
        @DisplayName("isIn() should create a single character range when start equals end")
        void isIn_shouldCreateSingleCharRangeWhenStartEqualsEnd() {
            // Arrange
            final char testChar = 'a';

            // Act
            final CharRange range = CharRange.isIn(testChar, testChar);

            // Assert
            assertEquals(testChar, range.getStart());
            assertEquals(testChar, range.getEnd());
            assertFalse(range.isNegated());
            assertEquals("a", range.toString());
        }

        @Test
        @DisplayName("isNotIn() should create a correct negated range when start is less than end")
        void isNotIn_shouldCreateCorrectNegatedRangeForNormalOrder() {
            // Arrange
            final char start = 'a';
            final char end = 'e';

            // Act
            final CharRange range = CharRange.isNotIn(start, end);

            // Assert
            assertEquals(start, range.getStart());
            assertEquals(end, range.getEnd());
            assertTrue(range.isNegated());
            assertEquals("^a-e", range.toString());
        }

        @Test
        @DisplayName("isNotIn() should create a correct negated range when start is greater than end")
        void isNotIn_shouldCreateCorrectNegatedRangeForReversedOrder() {
            // Arrange
            final char start = 'e';
            final char end = 'a';

            // Act
            final CharRange range = CharRange.isNotIn(start, end);

            // Assert
            assertEquals(end, range.getStart(), "Start should be the smaller character");
            assertEquals(start, range.getEnd(), "End should be the larger character");
            assertTrue(range.isNegated());
            assertEquals("^a-e", range.toString());
        }

        @Test
        @DisplayName("isNotIn() should create a negated single character range when start equals end")
        void isNotIn_shouldCreateNegatedSingleCharRangeWhenStartEqualsEnd() {
            // Arrange
            final char testChar = 'a';

            // Act
            final CharRange range = CharRange.isNotIn(testChar, testChar);

            // Assert
            assertEquals(testChar, range.getStart());
            assertEquals(testChar, range.getEnd());
            assertTrue(range.isNegated());
            assertEquals("^a", range.toString());
        }
    }

    @Nested
    @DisplayName("contains(char) Tests")
    class ContainsCharTests {

        @Test
        @DisplayName("Given a single-char range, contains() should return true only for that char")
        void givenSingleCharRange_whenContainsChar_thenReturnsTrueForThatCharOnly() {
            final CharRange range = CharRange.is('c');

            assertFalse(range.contains('b'));
            assertTrue(range.contains('c'));
            assertFalse(range.contains('d'));
        }

        @Test
        @DisplayName("Given a multi-char range, contains() should return true for chars within the range")
        void givenMultiCharRange_whenContainsChar_thenReturnsTrueForCharsInRange() {
            final CharRange range = CharRange.isIn('c', 'd');

            assertFalse(range.contains('b'));
            assertTrue(range.contains('c'));
            assertTrue(range.contains('d'));
            assertFalse(range.contains('e'));
        }

        @Test
        @DisplayName("Given a negated range, contains() should return false for chars within the range")
        void givenNegatedRange_whenContainsChar_thenReturnsFalseForCharsInRange() {
            final CharRange range = CharRange.isNotIn('c', 'd');

            assertTrue(range.contains('b'));
            assertFalse(range.contains('c'));
            assertFalse(range.contains('d'));
            assertTrue(range.contains('e'));
            assertTrue(range.contains((char) 0));
            assertTrue(range.contains(Character.MAX_VALUE));
        }
    }

    @Nested
    @DisplayName("contains(CharRange) Tests")
    class ContainsCharRangeTests {

        @Test
        @DisplayName("A normal range should contain itself and sub-ranges")
        void givenNormalRange_whenContainsNormalRange_thenContainsSubRanges() {
            final CharRange range_a_to_e = CharRange.isIn('a', 'e');
            final CharRange range_b_to_d = CharRange.isIn('b', 'd');
            final CharRange range_c = CharRange.is('c');

            assertTrue(range_a_to_e.contains(range_a_to_e), "A range should contain itself");
            assertTrue(range_a_to_e.contains(range_b_to_d), "A range should contain a sub-range");
            assertTrue(range_a_to_e.contains(range_c), "A range should contain a single-char sub-range");
        }

        @Test
        @DisplayName("A normal range should not contain super-ranges or disjoint ranges")
        void givenNormalRange_whenContainsNormalRange_thenDoesNotContainSuperOrDisjointRanges() {
            final CharRange range_b_to_c = CharRange.isIn('b', 'c');
            final CharRange range_a_to_d = CharRange.isIn('a', 'd');
            final CharRange range_d_to_e = CharRange.isIn('d', 'e');

            assertFalse(range_b_to_c.contains(range_a_to_d), "A range should not contain a super-range");
            assertFalse(range_b_to_c.contains(range_d_to_e), "A range should not contain a disjoint range");
        }

        @Test
        @DisplayName("A normal range should not contain a negated range")
        void givenNormalRange_whenContainsNegatedRange_thenReturnsFalse() {
            final CharRange normalRange = CharRange.isIn('a', 'e');
            final CharRange negatedRange = CharRange.isNotIn('b', 'c');

            assertFalse(normalRange.contains(negatedRange));
        }

        @Test
        @DisplayName("A negated range should contain a normal range that is fully excluded")
        void givenNegatedRange_whenContainsNormalRange_thenContainsExcludedRanges() {
            final CharRange negated_b_to_d = CharRange.isNotIn('b', 'd');
            final CharRange normal_f_to_g = CharRange.isIn('f', 'g');

            assertTrue(negated_b_to_d.contains(normal_f_to_g));
        }

        @Test
        @DisplayName("A negated range should not contain a normal range that is not fully excluded")
        void givenNegatedRange_whenContainsNormalRange_thenDoesNotContainOverlappingRanges() {
            final CharRange negated_b_to_d = CharRange.isNotIn('b', 'd');
            final CharRange overlapping_a_to_c = CharRange.isIn('a', 'c');
            final CharRange fullRange = CharRange.isIn((char) 0, Character.MAX_VALUE);

            assertFalse(negated_b_to_d.contains(overlapping_a_to_c));
            assertFalse(negated_b_to_d.contains(fullRange));
        }

        @Test
        @DisplayName("negatedA.contains(negatedB) is true if Excluded(A) is a subset of Excluded(B)")
        void givenNegatedRange_whenContainsNegatedRange_thenFollowsSubsetLogic() {
            // For two negated ranges A and B, A.contains(B) is true if the set of characters
            // excluded by A is a subset of the characters excluded by B.
            final CharRange containerRange = CharRange.isNotIn('b', 'd'); // Excludes [b, d]
            final CharRange containedRange = CharRange.isNotIn('a', 'e'); // Excludes [a, e]

            // Excluded([b,d]) is a subset of Excluded([a,e]).
            // Therefore, containerRange should contain containedRange.
            assertTrue(containerRange.contains(containedRange));

            // The reverse should be false, as Excluded([a,e]) is not a subset of Excluded([b,d]).
            assertFalse(containedRange.contains(containerRange));
        }

        @Test
        @DisplayName("contains() with a null argument should throw NullPointerException")
        void contains_withNullArgument_shouldThrowNullPointerException() {
            final CharRange range = CharRange.is('a');
            final NullPointerException e = assertThrows(NullPointerException.class, () -> range.contains(null));
            assertEquals("range", e.getMessage());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Contract")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("equals() should return true for the same object and for an identical range")
        void equals_shouldReturnTrueForIdenticalRanges() {
            final CharRange rangeA = CharRange.isIn('a', 'e');
            final CharRange rangeB = CharRange.isIn('a', 'e');

            assertEquals(rangeA, rangeA, "An object should be equal to itself");
            assertEquals(rangeA, rangeB, "Objects with the same state should be equal");
        }

        @Test
        @DisplayName("equals() should return false for different ranges")
        void equals_shouldReturnFalseForDifferentRanges() {
            final CharRange rangeA = CharRange.is('a');
            final CharRange rangeB = CharRange.isIn('a', 'e');
            final CharRange rangeC = CharRange.isNotIn('a', 'e');

            assertNotEquals(rangeA, rangeB);
            assertNotEquals(rangeB, rangeC);
            assertNotEquals(rangeA, rangeC);
        }

        @Test
        @DisplayName("equals() should return false for null and different types")
        void equals_shouldReturnFalseForNullAndDifferentTypes() {
            final CharRange rangeA = CharRange.is('a');
            assertNotEquals(null, rangeA);
            assertNotEquals("a", rangeA);
        }

        @Test
        @DisplayName("hashCode() should be consistent for equal objects")
        void hashCode_shouldBeConsistentForEqualObjects() {
            final CharRange rangeA1 = CharRange.isIn('a', 'e');
            final CharRange rangeA2 = CharRange.isIn('a', 'e');

            assertEquals(rangeA1, rangeA2);
            assertEquals(rangeA1.hashCode(), rangeA2.hashCode());
        }

        @Test
        @DisplayName("hashCode() should generally be different for non-equal objects")
        void hashCode_shouldBeDifferentForNonEqualObjects() {
            final CharRange rangeA = CharRange.is('a');
            final CharRange rangeB = CharRange.isIn('a', 'e');

            assertNotEquals(rangeA, rangeB);
            assertNotEquals(rangeA.hashCode(), rangeB.hashCode());
        }
    }

    @Nested
    @DisplayName("Iterator Tests")
    class IteratorTests {

        @Test
        @DisplayName("iterator() on a single-char range should yield only that character")
        void iterator_onSingleCharRange_shouldYieldSingleChar() {
            final CharRange range = CharRange.is('a');
            final Iterator<Character> iterator = range.iterator();

            assertNotNull(iterator);
            assertTrue(iterator.hasNext());
            assertEquals('a', iterator.next());
            assertFalse(iterator.hasNext());
        }

        @Test
        @DisplayName("iterator() on a multi-char range should yield all characters in order")
        void iterator_onMultiCharRange_shouldYieldAllCharsInRange() {
            final CharRange range = CharRange.isIn('a', 'd');
            final Iterator<Character> iterator = range.iterator();

            assertNotNull(iterator);
            assertTrue(iterator.hasNext());
            assertEquals('a', iterator.next());
            assertEquals('b', iterator.next());
            assertEquals('c', iterator.next());
            assertEquals('d', iterator.next());
            assertFalse(iterator.hasNext());
        }

        @Test
        @DisplayName("iterator() on an empty (negated full) range should be empty")
        void iterator_onEmptySet_shouldBeEmptyAndThrowExceptionOnNext() {
            final CharRange emptySet = CharRange.isNotIn((char) 0, Character.MAX_VALUE);
            final Iterator<Character> iterator = emptySet.iterator();

            assertNotNull(iterator);
            assertFalse(iterator.hasNext());
            assertThrows(NoSuchElementException.class, iterator::next);
        }

        @Test
        @DisplayName("iterator() on a negated single-char range should yield all other characters")
        void iterator_onNegatedRange_shouldYieldAllOtherChars() {
            final CharRange range = CharRange.isNot('a');
            final Iterator<Character> iterator = range.iterator();

            assertNotNull(iterator);
            assertTrue(iterator.hasNext());
            // It's too slow to iterate all 65535 chars, so we just verify it doesn't contain 'a'
            int count = 0;
            while (iterator.hasNext() && count < 200) {
                assertNotEquals('a', iterator.next().charValue());
                count++;
            }
        }

        @Test
        @DisplayName("iterator().remove() should throw UnsupportedOperationException")
        void iteratorRemove_shouldThrowException() {
            final CharRange range = CharRange.is('a');
            final Iterator<Character> iterator = range.iterator();
            assertThrows(UnsupportedOperationException.class, iterator::remove);
        }
    }

    @Test
    @DisplayName("Serialization should preserve the range's state")
    void serialization_shouldPreserveRangeState() {
        // Arrange
        final CharRange singleRange = CharRange.is('a');
        final CharRange multiRange = CharRange.isIn('a', 'e');
        final CharRange negatedRange = CharRange.isNotIn('a', 'e');

        // Act & Assert
        assertEquals(singleRange, SerializationUtils.clone(singleRange));
        assertEquals(multiRange, SerializationUtils.clone(multiRange));
        assertEquals(negatedRange, SerializationUtils.clone(negatedRange));
    }
}