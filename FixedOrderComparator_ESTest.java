package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.comparators.FixedOrderComparator.UnknownObjectBehavior;

/**
 * A clear, maintainable set of tests for the {@link FixedOrderComparator}.
 */
public class FixedOrderComparatorTest {

    private final String[] PLANETS = {"Mercury", "Venus", "Earth", "Mars"};

    /**
     * Tests that a comparator created with the default constructor is empty,
     * unlocked, and has the default EXCEPTION behavior for unknown objects.
     */
    @Test
    public void testEmptyConstructorAndDefaults() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>();
        assertFalse("A new comparator should not be locked", comparator.isLocked());
        assertEquals("Default unknown object behavior should be EXCEPTION",
                UnknownObjectBehavior.EXCEPTION, comparator.getUnknownObjectBehavior());
    }

    /**
     * Tests that the constructor correctly initializes the order from an array of items.
     */
    @Test
    public void testArrayConstructor() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(PLANETS);
        assertTrue("Venus should come after Mercury", comparator.compare("Mercury", "Venus") < 0);
        assertTrue("Mars should come after Earth", comparator.compare("Earth", "Mars") < 0);
    }

    /**
     * Tests that the constructor correctly initializes the order from a list of items.
     */
    @Test
    public void testListConstructor() {
        List<String> planetList = Arrays.asList(PLANETS);
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(planetList);
        assertTrue("Venus should come after Mercury", comparator.compare("Mercury", "Venus") < 0);
        assertTrue("Mars should come after Earth", comparator.compare("Earth", "Mars") < 0);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullArrayThrowsException() {
        new FixedOrderComparator<>((String[]) null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullListThrowsException() {
        new FixedOrderComparator<>((List<String>) null);
    }

    /**
     * Tests that if the initial list contains duplicates, the last occurrence
     * of the duplicate item dictates its order.
     */
    @Test
    public void testConstructorWithDuplicatesUsesLastOccurrence() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>("a", "b", "a"); // "a" appears last
        assertEquals("Duplicate items should be equal", 0, comparator.compare("a", "a"));
        assertTrue("Last occurrence of 'a' should be after 'b'", comparator.compare("b", "a") < 0);
    }

    /**
     * Tests the basic comparison of two known objects.
     */
    @Test
    public void testCompareKnownObjects() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(PLANETS);
        assertTrue("Lesser item should compare as -1", comparator.compare("Mercury", "Mars") < 0);
        assertTrue("Greater item should compare as 1", comparator.compare("Mars", "Mercury") > 0);
        assertEquals("Equal items should compare as 0", 0, comparator.compare("Earth", "Earth"));
    }

    /**
     * Tests that items added as equal correctly compare as 0.
     */
    @Test
    public void testCompareItemsAddedAsEqual() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>("a", "b");
        comparator.addAsEqual("a", "c");
        assertEquals("Items added as equal should compare as 0", 0, comparator.compare("a", "c"));
        assertEquals("Items added as equal should compare as 0 (reversed)", 0, comparator.compare("c", "a"));
        assertTrue("Equal item 'c' should be less than 'b'", comparator.compare("c", "b") < 0);
    }

    /**
     * Tests that adding a new item returns true, while adding an existing one returns false.
     */
    @Test
    public void testAdd() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>();
        assertTrue("Adding a new item should return true", comparator.add("a"));
        assertFalse("Adding an existing item should return false", comparator.add("a"));
    }

    /**
     * Tests that adding a new item as equal to an existing one returns true,
     * while adding an already-known item returns false.
     */
    @Test
    public void testAddAsEqual() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>("a", "b");
        assertTrue("Adding a new item as equal should return true", comparator.addAsEqual("a", "c"));
        assertFalse("Adding an existing item as equal should return false", comparator.addAsEqual("a", "b"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAsEqualForUnknownExistingObjectThrowsException() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>("a", "b");
        comparator.addAsEqual("unknown", "c"); // "unknown" is not in the comparator
    }

    /**
     * Tests that the comparator becomes locked after the first comparison.
     */
    @Test
    public void testIsLockedAfterCompare() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(PLANETS);
        assertFalse("Comparator should not be locked before compare", comparator.isLocked());
        comparator.compare("Mercury", "Venus");
        assertTrue("Comparator should be locked after compare", comparator.isLocked());
    }

    /**
     * Tests that any attempt to modify a locked comparator throws an UnsupportedOperationException.
     */
    @Test
    public void testModifyingLockedComparatorThrowsException() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(PLANETS);
        comparator.compare("Mercury", "Venus"); // Lock the comparator

        try {
            comparator.add("Pluto");
            fail("add() should throw UnsupportedOperationException on a locked comparator");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            comparator.addAsEqual("Earth", "Pluto");
            fail("addAsEqual() should throw UnsupportedOperationException on a locked comparator");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            comparator.setUnknownObjectBehavior(UnknownObjectBehavior.AFTER);
            fail("setUnknownObjectBehavior() should throw UnsupportedOperationException on a locked comparator");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    /**
     * Tests that comparing an unknown object throws an IllegalArgumentException by default.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCompareWithUnknownObjectAndDefaultBehaviorThrowsException() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(PLANETS);
        comparator.compare("Earth", "Pluto"); // Pluto is unknown
    }

    /**
     * Tests comparison behavior when UnknownObjectBehavior is set to BEFORE.
     * Unknown objects should be considered "less than" any known object.
     */
    @Test
    public void testCompareWithUnknownObjectBehaviorBefore() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(PLANETS);
        comparator.setUnknownObjectBehavior(UnknownObjectBehavior.BEFORE);

        assertEquals("Unknown vs Known should be -1", -1, comparator.compare("Pluto", "Earth"));
        assertEquals("Known vs Unknown should be 1", 1, comparator.compare("Earth", "Pluto"));
        assertEquals("Unknown vs Unknown should be 0", 0, comparator.compare("Pluto", "Charon"));
    }

    /**
     * Tests comparison behavior when UnknownObjectBehavior is set to AFTER.
     * Unknown objects should be considered "greater than" any known object.
     */
    @Test
    public void testCompareWithUnknownObjectBehaviorAfter() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(PLANETS);
        comparator.setUnknownObjectBehavior(UnknownObjectBehavior.AFTER);

        assertEquals("Unknown vs Known should be 1", 1, comparator.compare("Pluto", "Earth"));
        assertEquals("Known vs Unknown should be -1", -1, comparator.compare("Earth", "Pluto"));
        assertEquals("Unknown vs Unknown should be 0", 0, comparator.compare("Pluto", "Charon"));
    }

    @Test(expected = NullPointerException.class)
    public void testSetUnknownObjectBehaviorToNullThrowsException() {
        new FixedOrderComparator<>().setUnknownObjectBehavior(null);
    }

    /**
     * Verifies the equals() and hashCode() contracts.
     */
    @Test
    public void testEqualsAndHashCode() {
        FixedOrderComparator<String> comp1 = new FixedOrderComparator<>("a", "b", "c");
        FixedOrderComparator<String> comp2 = new FixedOrderComparator<>("a", "b", "c");
        FixedOrderComparator<String> comp3 = new FixedOrderComparator<>("c", "b", "a");
        FixedOrderComparator<String> comp4 = new FixedOrderComparator<>("x", "y", "z");

        // Basic equals checks
        assertEquals("A comparator must equal itself", comp1, comp1);
        assertNotEquals("A comparator must not equal null", null, comp1);
        assertNotEquals("A comparator must not equal an object of a different type", "a string", comp1);

        // Equality based on order
        assertEquals("Comparators with same order must be equal", comp1, comp2);
        assertEquals("Equal comparators must have same hash code", comp1.hashCode(), comp2.hashCode());

        assertNotEquals("Comparators with different order must not be equal", comp1, comp3);
        assertNotEquals("Comparators with different items must not be equal", comp1, comp4);

        // Equality based on UnknownObjectBehavior
        comp2.setUnknownObjectBehavior(UnknownObjectBehavior.AFTER);
        assertNotEquals("Comparators with different UnknownObjectBehavior must not be equal", comp1, comp2);

        // Equality based on lock status
        FixedOrderComparator<String> comp5 = new FixedOrderComparator<>("a", "b", "c");
        comp5.compare("a", "b"); // Lock it
        assertNotEquals("Comparators with different lock status must not be equal", comp1, comp5);
    }
}