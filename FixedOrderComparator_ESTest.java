package org.apache.commons.collections4.comparators;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for FixedOrderComparator.
 *
 * These tests favor clarity over randomness by:
 * - Using descriptive test names
 * - Grouping assertions by behavior
 * - Avoiding EvoSuite scaffolding and timeouts
 */
public class FixedOrderComparatorTest {

    // Construction

    @Test
    public void constructor_withArray_preservesGivenOrder() {
        FixedOrderComparator<String> comp = new FixedOrderComparator<>("Mercury", "Venus", "Earth");

        assertTrue(comp.compare("Mercury", "Venus") < 0);
        assertTrue(comp.isLocked()); // First comparison locks the comparator
    }

    @Test
    public void constructor_withList_preservesGivenOrder() {
        List<String> items = Arrays.asList("A", "B", "C");
        FixedOrderComparator<String> comp = new FixedOrderComparator<>(items);

        assertTrue(comp.compare("A", "C") < 0);
        assertTrue(comp.isLocked());
    }

    @Test
    public void constructor_withNullArray_throwsNPE() {
        assertThrows(NullPointerException.class, () -> new FixedOrderComparator<>((String[]) null));
    }

    @Test
    public void constructor_withNullList_throwsNPE() {
        assertThrows(NullPointerException.class, () -> new FixedOrderComparator<>((List<String>) null));
    }

    // Adding known items

    @Test
    public void add_returnsTrueOnFirstAdd_andFalseOnDuplicate() {
        FixedOrderComparator<String> comp = new FixedOrderComparator<>();
        assertTrue(comp.add("A"));
        assertFalse(comp.add("A")); // already known
        assertFalse(comp.isLocked()); // still unlocked until first compare
    }

    @Test
    public void addAsEqual_addsToEquivalenceGroup() {
        FixedOrderComparator<String> comp = new FixedOrderComparator<>();
        assertTrue(comp.add("A"));
        assertTrue(comp.addAsEqual("A", "AliasOfA"));

        assertEquals(0, comp.compare("A", "AliasOfA")); // in same equivalence group
        assertTrue(comp.isLocked());
    }

    @Test
    public void addAsEqual_withUnknownExisting_throwsIAE() {
        FixedOrderComparator<String> comp = new FixedOrderComparator<>();
        assertThrows(IllegalArgumentException.class, () -> comp.addAsEqual("NotKnown", "Another"));
        assertFalse(comp.isLocked());
    }

    @Test
    public void addAsEqual_withSameObject_returnsFalse() {
        FixedOrderComparator<String> comp = new FixedOrderComparator<>();
        comp.add("A");

        // Adding A as equal to itself is a no-op -> returns false
        assertFalse(comp.addAsEqual("A", "A"));
        assertFalse(comp.isLocked());
    }

    // Unknown object behavior

    @Test
    public void defaultUnknownBehavior_isException_andThrowsOnUnknown() {
        FixedOrderComparator<String> comp = new FixedOrderComparator<>("Known");
        assertThrows(IllegalArgumentException.class, () -> comp.compare("Known", "Unknown"));
    }

    @Test
    public void unknownBehavior_BEFORE_placesUnknownBeforeKnown() {
        FixedOrderComparator<String> comp = new FixedOrderComparator<>("Known");
        comp.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);

        // Unknown < Known
        assertTrue(comp.compare("Unknown", "Known") < 0);
        assertTrue(comp.compare("Known", "Unknown") > 0);

        // Both unknown -> equal
        assertEquals(0, comp.compare("U1", "U2"));
        assertTrue(comp.isLocked());
    }

    @Test
    public void unknownBehavior_AFTER_placesUnknownAfterKnown() {
        FixedOrderComparator<String> comp = new FixedOrderComparator<>("Known");
        comp.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);

        // Unknown > Known
        assertTrue(comp.compare("Unknown", "Known") > 0);
        assertTrue(comp.compare("Known", "Unknown") < 0);
    }

    @Test
    public void setUnknownObjectBehavior_null_throwsNPE() {
        FixedOrderComparator<String> comp = new FixedOrderComparator<>();
        assertThrows(NullPointerException.class, () -> comp.setUnknownObjectBehavior(null));
    }

    // Locking after first compare

    @Test
    public void isLocked_isFalseUntilFirstCompare_thenTrue() {
        FixedOrderComparator<String> comp = new FixedOrderComparator<>("A", "B");
        assertFalse(comp.isLocked());

        comp.compare("A", "B");
        assertTrue(comp.isLocked());
    }

    @Test
    public void cannotModifyAfterFirstCompare() {
        FixedOrderComparator<String> comp = new FixedOrderComparator<>("A");
        comp.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);
        comp.compare("A", "A"); // Locks comparator

        assertThrows(UnsupportedOperationException.class, () -> comp.add("Z"));
        assertThrows(UnsupportedOperationException.class, () -> comp.addAsEqual("A", "Alias"));
        assertThrows(UnsupportedOperationException.class,
                () -> comp.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER));
    }

    // Handling of null as a known value

    @Test
    public void nullCanBeAKnownValue_andRespectsOrder() {
        FixedOrderComparator<String> comp = new FixedOrderComparator<>(new String[]{null, "X"});
        comp.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);

        // Known vs known respects declared order: null before "X"
        assertTrue(comp.compare(null, "X") < 0);
        assertTrue(comp.compare("X", null) > 0);
    }

    // Equality and hashCode

    @Test
    public void equals_and_hashCode_reflectOrderAndBehavior() {
        FixedOrderComparator<String> a1 = new FixedOrderComparator<>(Arrays.asList("A", "B"));
        FixedOrderComparator<String> a2 = new FixedOrderComparator<>(Arrays.asList("A", "B"));
        FixedOrderComparator<String> b1 = new FixedOrderComparator<>(Arrays.asList("B", "A"));
        FixedOrderComparator<String> a3 = new FixedOrderComparator<>(Arrays.asList("A", "B"));
        a3.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());

        assertNotEquals(a1, b1);   // different order
        assertNotEquals(a1, a3);   // different unknown behavior
    }

    @Test
    public void equals_selfAndNotEqualToNullOrOtherType() {
        FixedOrderComparator<String> comp = new FixedOrderComparator<>();
        assertEquals(comp, comp);
        assertNotEquals(comp, null);
        assertNotEquals(comp, "not a comparator");
    }

    // Additional sanity checks

    @Test
    public void addAfterConstructorWithListStillRespectsOrder() {
        FixedOrderComparator<String> comp = new FixedOrderComparator<>(Collections.singletonList("First"));
        assertTrue(comp.add("Second"));

        assertTrue(comp.compare("First", "Second") < 0);
        assertTrue(comp.isLocked());
    }

    @Test
    public void compareSameUnknownObject_returnsZero_whenUnknownsAreAllowed() {
        FixedOrderComparator<Object> comp = new FixedOrderComparator<>();
        comp.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);

        Object o = new Object();
        assertEquals(0, comp.compare(o, o));
    }
}