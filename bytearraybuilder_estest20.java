package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Contains tests for the {@link ByteArrayBuilder} class.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that the bufferRecycler() method returns the same BufferRecycler
     * instance that was provided during the ByteArrayBuilder's construction.
     */
    @Test
    public void shouldReturnTheSameBufferRecyclerInstanceProvidedInConstructor() {
        // Arrange: Create a BufferRecycler and a ByteArrayBuilder that uses it.
        BufferRecycler expectedRecycler = new BufferRecycler();
        ByteArrayBuilder builder = new ByteArrayBuilder(expectedRecycler);

        // Act: Retrieve the BufferRecycler from the builder.
        BufferRecycler actualRecycler = builder.bufferRecycler();

        // Assert: The retrieved recycler should be the exact same instance as the one provided.
        assertSame("The bufferRecycler() method should return the instance supplied in the constructor.",
                expectedRecycler, actualRecycler);
    }
}