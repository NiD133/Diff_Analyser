package org.jfree.data.general;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for the {@link DefaultPieDataset} class, focusing on handling invalid arguments.
 */
public class DefaultPieDataset_ESTestTest18 {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Verifies that calling getValue() with a null key throws an IllegalArgumentException.
     * A null key is considered invalid input, and the method should reject it
     * explicitly rather than failing with a NullPointerException later.
     */
    @Test
    public void getValueWithNullKeyThrowsIllegalArgumentException() {
        // Arrange: Create an empty dataset.
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        // Assert: Specify the expected exception and its message.
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Null 'key' argument.");

        // Act: Attempt to retrieve a value using a null key.
        dataset.getValue(null);
    }
}