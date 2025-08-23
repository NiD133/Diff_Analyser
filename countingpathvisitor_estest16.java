package org.apache.commons.io.file;

import org.junit.Test;

/**
 * Tests for the {@link CountingPathVisitor} constructor.
 */
public class CountingPathVisitorTest {

    /**
     * Tests that the constructor throws a NullPointerException when a null builder is provided.
     * The constructor relies on the builder to initialize its state, so a null argument is invalid.
     */
    @Test(expected = NullPointerException.class)
    public void constructorWithNullBuilderShouldThrowNullPointerException() {
        // Attempt to create a CountingPathVisitor with a null builder, which is expected to fail.
        new CountingPathVisitor((CountingPathVisitor.AbstractBuilder<?, ?>) null);
    }
}