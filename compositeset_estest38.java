package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections4.set.CompositeSet.SetMutator;

// The original test class name is kept to show the context of the refactoring.
public class CompositeSet_ESTestTest38 extends CompositeSet_ESTest_scaffolding {

    /**
     * Tests various operations on a CompositeSet, particularly how it interacts with a
     * mock SetMutator that prevents additions.
     *
     * This test verifies:
     * 1. An empty CompositeSet correctly reports its state (isEmpty, toArray).
     * 2. The `removeIf` operation on an empty set returns false.
     * 3. The `addAll` operation respects the return value of its configured SetMutator.
     * 4. The `removeComposited` method correctly removes a component set.
     */
    @Test(timeout = 4000)
    public void testOperationsWithMutatorOnEmptyAndCompositedSets() {
        // Arrange: Create a mock SetMutator that will always report that no changes were made.
        @SuppressWarnings("unchecked")
        final SetMutator<Integer> mockMutator = mock(SetMutator.class);
        when(mockMutator.addAll(any(CompositeSet.class), anyList(), anyCollection())).thenReturn(false);

        // Arrange: Create the main CompositeSet and assign the mock mutator.
        final CompositeSet<Integer> compositeSet = new CompositeSet<>();
        compositeSet.setMutator(mockMutator);

        // --- Section 1: Verify behavior of an empty CompositeSet ---

        assertTrue("A new CompositeSet should be empty", compositeSet.isEmpty());

        // Act & Assert: removeIf on an empty set should do nothing and return false.
        // The original test used a complex, auto-generated predicate; a simple one suffices.
        final boolean wasRemoved = compositeSet.removeIf(element -> true);
        assertFalse("removeIf should return false when the set is empty", wasRemoved);

        // Act & Assert: toArray on an empty set should return the provided empty array instance.
        final Integer[] emptyArray = new Integer[0];
        final Integer[] resultArray = compositeSet.toArray(emptyArray);
        assertSame("toArray on an empty set should return the same empty array instance", emptyArray, resultArray);

        // --- Section 2: Verify interaction with the SetMutator ---

        // Act & Assert: addAll should delegate to the mutator and return its result (false).
        final boolean wasAdded = compositeSet.addAll(Collections.emptySet());
        assertFalse("addAll should return false as specified by the mock mutator", wasAdded);

        // Verify the mutator's addAll method was actually called with the correct arguments.
        verify(mockMutator).addAll(eq(compositeSet), anyList(), any(Collection.class));

        // --- Section 3: Verify removing a composited set ---

        // Arrange: Create a composite set that contains another set as its component.
        final Set<Integer> componentSet = Collections.singleton(123);
        final CompositeSet<Integer> setWithComponent = new CompositeSet<>(componentSet);

        // Assert: The set is not empty because its component has an element.
        assertFalse("Set with a non-empty component should not be empty", setWithComponent.isEmpty());
        assertEquals("Set size should match its component's size", 1, setWithComponent.size());

        // Act: Remove the component set.
        setWithComponent.removeComposited(componentSet);

        // Assert: The set should now be empty after removing its only component.
        assertTrue("Set should be empty after its component is removed", setWithComponent.isEmpty());
    }
}