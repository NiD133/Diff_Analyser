package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Spliterator;

/**
 * This class contains refactored tests for CompositeSet.
 * The original test was a single, complex, auto-generated method.
 * It has been broken down into smaller, focused, and descriptively named tests
 * to improve readability and maintainability.
 */
public class CompositeSet_ESTestTest31 extends CompositeSet_ESTest_scaffolding {

    @Test
    public void retainAllOnEmptyCompositeSetShouldNotModifySet() {
        // Arrange
        CompositeSet<String> compositeSet = new CompositeSet<>();
        Set<String> setToRetain = new HashSet<>();

        // Act
        boolean wasModified = compositeSet.retainAll(setToRetain);

        // Assert
        assertFalse("retainAll on an empty set should return false", wasModified);
        assertTrue("The composite set should remain empty", compositeSet.isEmpty());
    }

    @Test
    public void removeAllOnEmptyCompositeSetShouldNotModifySet() {
        // Arrange
        CompositeSet<String> compositeSet = new CompositeSet<>();
        Set<String> setToRemove = new HashSet<>();

        // Act
        boolean wasModified = compositeSet.removeAll(setToRemove);

        // Assert
        assertFalse("removeAll on an empty set should return false", wasModified);
        assertTrue("The composite set should remain empty", compositeSet.isEmpty());
    }

    @Test
    public void removeAllWithOverlappingElementsShouldModifySetAndReturnTrue() {
        // Arrange
        Set<String> underlyingSet = new HashSet<>(Arrays.asList("a", "b", "c"));
        CompositeSet<String> compositeSet = new CompositeSet<>(underlyingSet);
        
        // Collection to remove contains one overlapping element ("b") and one non-existent one ("d")
        Set<String> elementsToRemove = new HashSet<>(Arrays.asList("b", "d"));

        // Act
        boolean wasModified = compositeSet.removeAll(elementsToRemove);

        // Assert
        assertTrue("removeAll should return true as an element was removed", wasModified);
        assertEquals("CompositeSet size should be reduced by one", 2, compositeSet.size());
        assertFalse("CompositeSet should no longer contain the removed element", compositeSet.contains("b"));
        assertTrue("CompositeSet should still contain other elements", compositeSet.contains("a") && compositeSet.contains("c"));
        
        // Verify underlying set was also modified
        assertEquals("Underlying set size should also be reduced", 2, underlyingSet.size());
        assertFalse("Underlying set should no longer contain the removed element", underlyingSet.contains("b"));
    }

    @Test
    public void sizeOfEmptyCompositeSetShouldBeZero() {
        // Arrange
        CompositeSet<String> compositeSet = new CompositeSet<>();

        // Act
        int size = compositeSet.size();

        // Assert
        assertEquals("Size of an empty CompositeSet should be 0", 0, size);
    }

    @Test
    public void iteratorOnEmptyCompositeSetShouldBeEmpty() {
        // Arrange
        CompositeSet<String> compositeSet = new CompositeSet<>();

        // Act & Assert
        assertFalse("Iterator of an empty CompositeSet should not have a next element", compositeSet.iterator().hasNext());
    }

    @Test
    public void spliteratorReportsCorrectSizeForNonEmptySet() {
        // Arrange
        Set<String> underlyingSet = new HashSet<>(Collections.singletonList("element"));
        CompositeSet<String> compositeSet = new CompositeSet<>(underlyingSet);

        // Act
        Spliterator<String> spliterator = compositeSet.spliterator();

        // Assert
        assertEquals("Spliterator should report the correct size", 1, spliterator.getExactSizeIfKnown());
    }

    @Test
    public void operationsOnCompositeSetContainingItselfShouldNotCauseInfiniteRecursion() {
        // Arrange
        CompositeSet<Object> selfReferencingSet = new CompositeSet<>();
        selfReferencingSet.addComposited(selfReferencingSet);

        // Act & Assert that operations do not cause a StackOverflowError
        try {
            assertEquals("Size of a set containing itself should be 0 (as it's empty)", 0, selfReferencingSet.size());
            assertFalse("contains(null) should be false", selfReferencingSet.contains(null));
            assertFalse("iterator().hasNext() should be false", selfReferencingSet.iterator().hasNext());
            
            // hashCode() and equals() are classic stack overflow candidates in recursive structures
            selfReferencingSet.hashCode();
            selfReferencingSet.equals(new HashSet<>());
        } catch (StackOverflowError e) {
            fail("Operation on a self-referencing CompositeSet caused a StackOverflowError");
        }
    }
}