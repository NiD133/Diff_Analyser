package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * This class contains the refactored test case from the original
 * CompositeSet_ESTestTest10.
 */
public class CompositeSetRefactoredTest {

    /**
     * Tests that calling retainAll with a collection of a dissimilar element type
     * correctly removes all elements from the CompositeSet.
     */
    @Test
    public void retainAllWithDissimilarCollectionTypeShouldRemoveAllElements() {
        // Arrange
        // 1. Create a source set with one integer element.
        final LinkedHashSet<Integer> sourceSet = new LinkedHashSet<>();
        final Integer element = -1;
        sourceSet.add(element);

        // 2. Create a CompositeSet containing the source set.
        final CompositeSet<Integer> compositeSet = new CompositeSet<>(sourceSet);

        // 3. Verify the initial state: the composite set is not empty and contains the element.
        assertFalse("Precondition: CompositeSet should not be empty", compositeSet.isEmpty());
        assertTrue("Precondition: CompositeSet should contain the element", compositeSet.contains(element));

        // 4. Create a collection for retainAll that contains an element of a different type (a Set)
        //    than the elements in the composite set (Integer). This ensures no elements will be retained.
        final List<Set<Integer>> collectionWithDifferentType = Collections.singletonList(new LinkedHashSet<>());

        // Act
        // Call retainAll. This should remove the element because it's not present in
        // the collectionWithDifferentType.
        final boolean wasModified = compositeSet.retainAll(collectionWithDifferentType);

        // Assert
        // 1. The operation should report that the set was modified.
        assertTrue("retainAll should return true as the set was modified", wasModified);
        
        // 2. The composite set and its underlying source set should now be empty.
        assertTrue("CompositeSet should be empty after retainAll", compositeSet.isEmpty());
        assertTrue("Underlying source set should also be empty", sourceSet.isEmpty());
    }
}