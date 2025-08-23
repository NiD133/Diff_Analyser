package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the clone() method of the {@link DefaultPieDataset} class.
 */
public class DefaultPieDataset_ESTestTest44 {

    /**
     * Verifies that a cloned DefaultPieDataset has dataset change notifications
     * enabled by default.
     * <p>
     * The 'notify' flag, inherited from AbstractDataset, controls whether change
     * events are fired. This test ensures that cloning preserves the default
     * state (enabled) for this flag.
     * </p>
     */
    @Test
    public void clone_returnsNewInstanceWithNotificationsEnabled() throws CloneNotSupportedException {
        // Arrange: Create an original DefaultPieDataset instance.
        DefaultPieDataset<String> originalDataset = new DefaultPieDataset<>();

        // Act: Create a clone of the original dataset.
        DefaultPieDataset<String> clonedDataset = (DefaultPieDataset<String>) originalDataset.clone();

        // Assert: The cloned dataset should have the 'notify' flag enabled by default.
        assertTrue("A cloned dataset should have notifications enabled by default.",
                clonedDataset.getNotify());
    }
}