package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Note: The original test class name is kept for context, but in a real-world scenario,
// this test would be added to a more general 'CompositeSetTest' suite.
public class CompositeSet_ESTestTest37_Refactored {

    /**
     * Tests that the addAll() method correctly delegates to the configured SetMutator
     * and returns the value provided by the mutator.
     */
    @Test
    public void addAllShouldDelegateToMutatorAndReturnItsResult() {
        // --- Arrange ---

        // 1. Create a mock SetMutator that will report that the collection was modified.
        @SuppressWarnings("unchecked")
        final CompositeSet.SetMutator<Integer> mockMutator = mock(CompositeSet.SetMutator.class);
        when(mockMutator.addAll(any(CompositeSet.class), anyList(), anyCollection())).thenReturn(true);

        // 2. Create the CompositeSet under test and assign the mock mutator.
        final CompositeSet<Integer> compositeSet = new CompositeSet<>();
        compositeSet.setMutator(mockMutator);

        // 3. Define the collection to be added. An empty set is sufficient for this test.
        final Set<Integer> collectionToAdd = Collections.emptySet();

        // --- Act ---
        final boolean wasModified = compositeSet.addAll(collectionToAdd);

        // --- Assert ---

        // 1. Verify that the result from addAll() matches the mutator's configured return value.
        assertTrue("addAll() should return true when the mutator returns true.", wasModified);

        // 2. Verify that the mutator's addAll method was called exactly once with the correct arguments.
        // This confirms the delegation happened as expected.
        verify(mockMutator, times(1)).addAll(
            eq(compositeSet),
            any(List.class),
            eq(collectionToAdd)
        );
    }
}