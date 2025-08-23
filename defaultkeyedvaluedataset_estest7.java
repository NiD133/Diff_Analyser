package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * This class contains an improved version of a test for the {@link DefaultKeyedValueDataset} class.
 * The original test was automatically generated and lacked clarity.
 */
public class DefaultKeyedValueDataset_ESTestTest7 extends DefaultKeyedValueDataset_ESTest_scaffolding {

    /**
     * Verifies that calling {@code getValue()} on a newly instantiated,
     * empty {@link DefaultKeyedValueDataset} returns null.
     */
    @Test
    public void getValue_onNewEmptyDataset_shouldReturnNull() {
        // Arrange: Create a new dataset using the default constructor.
        // This results in an empty dataset with no key or value.
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();

        // Act & Assert: The value retrieved from the empty dataset should be null.
        assertNull("The value of a new, empty dataset should be null.", dataset.getValue());
    }
}