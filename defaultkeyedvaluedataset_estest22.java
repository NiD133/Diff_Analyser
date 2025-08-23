package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link DefaultKeyedValueDataset} class.
 */
public class DefaultKeyedValueDatasetTest {

    /**
     * Verifies that calling setValue() does not have the unintended side effect
     * of disabling dataset notifications. The 'notify' flag, which controls
     * event firing, should remain in its default 'true' state.
     */
    @Test
    public void setValue_shouldPreserveNotifyFlag() {
        // Arrange: Create a dataset with an initial key-value pair.
        // By default, the 'notify' flag is enabled.
        Comparable<String> key = "Series 1";
        Number value = 100;
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(key, value);

        // Act: Update the value for the existing key.
        dataset.setValue(key, 200);

        // Assert: Confirm that the notify flag has not been changed and is still true.
        assertTrue("Calling setValue should not disable the notify flag.", dataset.getNotify());
    }
}