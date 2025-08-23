package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * This test class focuses on the DefaultPieDataset.
 * This specific test case verifies the behavior of the clear() method.
 */
public class DefaultPieDatasetTest {

    /**
     * Verifies that calling clear() on an already empty dataset does not
     * change the 'notify' flag. The 'notify' flag, which controls whether
     * change events are sent to listeners, should remain in its default state (true).
     */
    @Test
    public void clear_onEmptyDataset_shouldNotChangeNotifyFlag() {
        // Arrange: Create a new, empty pie dataset.
        // By default, the 'notify' flag is initialized to true.
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        // Act: Call the clear() method on the dataset, which is already empty.
        dataset.clear();

        // Assert: The 'notify' flag should remain true.
        assertTrue("The notify flag should not be changed after clearing an empty dataset.",
                dataset.getNotify());
    }
}