package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Contains tests for the {@link CompositeSet} class.
 * This specific test focuses on the behavior of the getMutator() method.
 */
// The original class name is kept for context, but a name like 'CompositeSetTest' would be more standard.
public class CompositeSet_ESTestTest85 extends CompositeSet_ESTest_scaffolding {

    /**
     * Tests that getMutator() returns null for a newly created CompositeSet
     * that has not had a mutator explicitly set.
     */
    @Test
    public void getMutatorShouldReturnNullWhenNotSet() {
        // Arrange: Create a new CompositeSet. By default, it has no mutator.
        final CompositeSet<String> compositeSet = new CompositeSet<>();

        // Act: Retrieve the mutator from the newly created set.
        final CompositeSet.SetMutator<String> mutator = compositeSet.getMutator();

        // Assert: The retrieved mutator should be null.
        assertNull("A new CompositeSet should have a null mutator by default", mutator);
    }
}