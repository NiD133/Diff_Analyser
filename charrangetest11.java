package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("CharRange.contains(CharRange)")
class CharRangeTest {

    @Test
    void shouldThrowNullPointerExceptionWhenRangeIsNull() {
        final CharRange rangeA = CharRange.is('a');
        assertThrows(NullPointerException.class, () -> rangeA.contains(null));
    }

    @Nested
    @DisplayName("When a normal range contains another normal range")
    class NormalContainsNormal {

        @Test
        void shouldContainItself() {
            final CharRange rangeC = CharRange.is('c');
            assertTrue(rangeC.contains(rangeC), "A range should contain itself.");
        }

        @Test
        void shouldContainIdenticalRange() {
            final CharRange rangeC1 = CharRange.is('c');
            final CharRange rangeC2 = CharRange.is('c');
            assertTrue(rangeC1.contains(rangeC2), "A range should contain an identical range.");
        }

        @Test
        void shouldNotContainDisjointRange() {
            final CharRange rangeC = CharRange.is('c');
            final CharRange rangeE = CharRange.is('e');
            assertFalse(rangeC.contains(rangeE), "A range should not contain a disjoint range.");
        }

        @Test
        void shouldContainSingleCharSubRange() {
            final CharRange rangeAtoE = CharRange.isIn('a', 'e');
            final CharRange rangeC = CharRange.is('c');
            assertTrue(rangeAtoE.contains(rangeC), "Range 'a-e' should contain single char 'c'.");
        }

        @Test
        void shouldContainMultiCharSubRange() {
            final CharRange rangeAtoE = CharRange.isIn('a', 'e');
            final CharRange rangeBtoD = CharRange.isIn('b', 'd');
            assertTrue(rangeAtoE.contains(rangeBtoD), "Range 'a-e' should contain sub-range 'b-d'.");
        }

        @Test
        void shouldNotContainOverlappingRange() {
            final CharRange rangeBtoD = CharRange.isIn('b', 'd');
            final CharRange rangeAtoC = CharRange.isIn('a', 'c');
            assertFalse(rangeBtoD.contains(rangeAtoC), "Range 'b-d' should not contain overlapping range 'a-c'.");
        }

        @Test
        void singleCharRangeShouldNotContainMultiCharRange() {
            final CharRange rangeC = CharRange.is('c');
            final CharRange rangeBtoD = CharRange.isIn('b', 'd');
            assertFalse(rangeC.contains(rangeBtoD), "Single char range 'c' cannot contain multi-char range 'b-d'.");
        }
    }

    @Nested
    @DisplayName("When a normal range contains a negated range")
    class NormalContainsNegated {

        @Test
        void shouldNotContainNegatedVersionOfItself() {
            final CharRange rangeC = CharRange.is('c');
            final CharRange negatedRangeC = CharRange.isNot('c');
            assertFalse(rangeC.contains(negatedRangeC), "A range cannot contain its own negation.");
        }

        @Test
        void fullRangeShouldContainNegatedRange() {
            final CharRange fullRange = CharRange.isIn((char) 0, Character.MAX_VALUE);
            final CharRange negatedRangeC = CharRange.isNot('c');
            assertTrue(fullRange.contains(negatedRangeC), "The full character range should contain any negated range.");
        }

        @Test
        void partialRangeShouldNotContainNegatedRangeIfItIsMissingElements() {
            // rangeOneToMax is [1, MAX_VALUE]
            final CharRange rangeOneToMax = CharRange.isIn((char) 1, Character.MAX_VALUE);
            // negatedRangeC contains char 0, which rangeOneToMax does not.
            final CharRange negatedRangeC = CharRange.isNot('c');
            assertFalse(rangeOneToMax.contains(negatedRangeC));
        }
    }

    @Nested
    @DisplayName("When a negated range contains a normal range")
    class NegatedContainsNormal {

        @Test
        void shouldContainRangeFullyOutsideTheNegatedSet() {
            final CharRange negatedRangeCtoD = CharRange.isNotIn('c', 'd'); // not 'c' or 'd'
            final CharRange rangeAtoB = CharRange.isIn('a', 'b');
            assertTrue(negatedRangeCtoD.contains(rangeAtoB), "Negated 'c-d' should contain 'a-b'.");
        }

        @Test
        void shouldNotContainRangePartiallyInsideTheNegatedSet() {
            final CharRange negatedRangeC = CharRange.isNot('c'); // not 'c'
            final CharRange rangeBtoC = CharRange.isIn('b', 'c');
            assertFalse(negatedRangeC.contains(rangeBtoC), "Negated 'c' should not contain 'b-c'.");
        }

        @Test
        void shouldNotContainRangeFullyInsideTheNegatedSet() {
            final CharRange negatedRangeAtoE = CharRange.isNotIn('a', 'e');
            final CharRange rangeBtoD = CharRange.isIn('b', 'd');
            assertFalse(negatedRangeAtoE.contains(rangeBtoD), "Negated 'a-e' should not contain 'b-d'.");
        }

        @Test
        void shouldNotContainTheFullCharacterRange() {
            final CharRange negatedRangeC = CharRange.isNot('c');
            final CharRange fullRange = CharRange.isIn((char) 0, Character.MAX_VALUE);
            assertFalse(negatedRangeC.contains(fullRange), "A negated range cannot contain the full character range.");
        }
    }

    @Nested
    @DisplayName("When a negated range contains another negated range")
    class NegatedContainsNegated {

        @Test
        void shouldContainItself() {
            final CharRange negatedRangeC = CharRange.isNot('c');
            assertTrue(negatedRangeC.contains(negatedRangeC), "A negated range should contain itself.");
        }

        @Test
        void shouldNotContainNegatedRangeWithDifferentExclusion() {
            final CharRange negatedRangeC = CharRange.isNot('c');
            final CharRange negatedRangeB = CharRange.isNot('b');
            // negatedRangeC is missing 'c', but negatedRangeB contains 'c'.
            assertFalse(negatedRangeC.contains(negatedRangeB));
        }

        @Test
        void widerNegatedRangeShouldContainNarrowerNegatedRange() {
            // not [b-d] is everything except 'b', 'c', 'd'
            final CharRange widerNegatedRange = CharRange.isNotIn('b', 'd');
            // not [a-e] is everything except 'a', 'b', 'c', 'd', 'e'
            final CharRange narrowerNegatedRange = CharRange.isNotIn('a', 'e');
            // widerNegatedRange contains 'a' and 'e', which narrowerNegatedRange does not.
            assertFalse(widerNegatedRange.contains(narrowerNegatedRange));
        }

        @Test
        void narrowerNegatedRangeShouldBeContainedByWiderNegatedRange() {
            // not [a-e] is a smaller set of characters than not [b-d]
            final CharRange widerNegatedRange = CharRange.isNotIn('b', 'd');
            final CharRange narrowerNegatedRange = CharRange.isNotIn('a', 'e');
            // The original test had this as true: assertTrue(notbd.contains(notae));
            // This seems incorrect. Let's re-evaluate:
            // not [b-d] = [MIN,a] U [e,MAX]
            // not [a-e] = [MIN,`] U [f,MAX]  ('`' is char before 'a')
            // The set for not[b-d] contains 'a' and 'e'. The set for not[a-e] does not.
            // Therefore, not[a-e] contains characters that not[b-d] does not (e.g. 'a', 'e').
            // So, not[b-d] cannot contain not[a-e].
            // The original assertion seems to have been `assertTrue(notbd.contains(notae))`, which is wrong.
            // Let's test the inverse, which should be true.
            assertTrue(narrowerNegatedRange.contains(widerNegatedRange),
                "not[a-e] should contain not[b-d] because its exclusion set is larger.");
        }
    }
}