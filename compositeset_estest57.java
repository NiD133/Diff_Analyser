package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

// The refactored test is placed within the original test class structure.
public class CompositeSet_ESTestTest57 extends CompositeSet_ESTest_scaffolding {

    /**
     * Tests that CompositeSet#addAll delegates to the SetMutator and returns its result.
     *
     * This test verifies that if the mutator's addAll method returns false,
     * the CompositeSet's addAll method also returns false, indicating that
     * the set was not modified.
     */
    @Test(timeout = 4000)
    public void addAllShouldReturnFalseWhenMutatorReturnsFalse() {
        // Arrange
        // 1. Create a mock SetMutator. The generic type is not important for this test.
        @SuppressWarnings("unchecked")
        final CompositeSet.SetMutator<String> mockMutator = mock(CompositeSet.SetMutator.class);

        // 2. Configure the mock to return 'false' when its addAll method is called.
        // This simulates a scenario where the addition does not change the collection.
        when(mockMutator.addAll(any(CompositeSet.class), anyList(), anyCollection()))
            .thenReturn(false);

        // 3. Create the CompositeSet under test and assign the mock mutator.
        final CompositeSet<String> compositeSet = new CompositeSet<>();
        compositeSet.setMutator(mockMutator);

        final Collection<String> collectionToAdd = Collections.emptyList();

        // Act
        // Call the method under test.
        final boolean wasModified = compositeSet.addAll(collectionToAdd);

        // Assert
        // 1. Verify that the result from the mutator is passed through.
        assertFalse("addAll should return false if the mutator returns false.", wasModified);

        // 2. Verify that the mutator's addAll method was called exactly once
        // with the expected parameters, confirming the delegation occurred correctly.
        verify(mockMutator, times(1)).addAll(
            eq(compositeSet),
            any(List.class),
            eq(collectionToAdd)
        );
        verifyNoMoreInteractions(mockMutator);
    }
}