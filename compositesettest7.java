package org.apache.commons.collections4.set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Tests for CompositeSet.
 * This class name has been improved from the original "CompositeSetTestTest7".
 */
public class CompositeSetTest<E> extends AbstractSetTest<E> {

    @SuppressWarnings("unchecked")
    public Set<E> buildOne() {
        final HashSet<E> set = new HashSet<>();
        set.add((E) "1");
        set.add((E) "2");
        return set;
    }

    @SuppressWarnings("unchecked")
    public Set<E> buildTwo() {
        final HashSet<E> set = new HashSet<>();
        set.add((E) "3");
        set.add((E) "4");
        return set;
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    protected int getIterationBehaviour() {
        return UNORDERED;
    }

    @Override
    public CompositeSet<E> makeObject() {
        final HashSet<E> contained = new HashSet<>();
        final CompositeSet<E> set = new CompositeSet<>(contained);
        // The mutator is not used in the test below, but is required by the abstract test framework.
        set.setMutator(new EmptySetMutator<>(contained));
        return set;
    }

    /**
     * Tests that removing an element from the CompositeSet also removes it
     * from the correct underlying set that contains the element.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testRemove_whenElementExists_removesFromCorrectUnderlyingSet() {
        // Arrange
        final Set<E> set1 = buildOne(); // Contains {"1", "2"}
        final Set<E> set2 = buildTwo(); // Contains {"3", "4"}
        final CompositeSet<E> compositeSet = new CompositeSet<>(set1, set2);

        // Verify initial state
        assertTrue(compositeSet.contains("1"), "Precondition: compositeSet should contain '1'");
        assertTrue(compositeSet.contains("3"), "Precondition: compositeSet should contain '3'");
        assertEquals(4, compositeSet.size(), "Precondition: compositeSet size should be 4");

        // Act: Remove an element present in the first set
        final boolean removedFromSet1 = compositeSet.remove("1");

        // Assert: The element is removed from the composite and the first underlying set
        assertTrue(removedFromSet1, "remove('1') should return true");
        assertFalse(compositeSet.contains("1"), "CompositeSet should no longer contain '1'");
        assertFalse(set1.contains("1"), "Underlying set1 should no longer contain '1'");
        assertEquals(3, compositeSet.size(), "Size should be 3 after removing '1'");

        // Act: Remove an element present in the second set
        final boolean removedFromSet2 = compositeSet.remove("3");

        // Assert: The element is removed from the composite and the second underlying set
        assertTrue(removedFromSet2, "remove('3') should return true");
        assertFalse(compositeSet.contains("3"), "CompositeSet should no longer contain '3'");
        assertFalse(set2.contains("3"), "Underlying set2 should no longer contain '3'");
        assertEquals(2, compositeSet.size(), "Size should be 2 after removing '3'");

        // Assert: Other elements are unaffected
        assertTrue(set1.contains("2"), "Element '2' in set1 should be unaffected");
        assertTrue(set2.contains("4"), "Element '4' in set2 should be unaffected");
    }
}