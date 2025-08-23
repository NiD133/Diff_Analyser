package org.jfree.chart.renderer.xy;

import org.jfree.data.statistics.DefaultBoxAndWhiskerXYDataset;
import org.junit.Test;

/**
 * This test class focuses on the exception-handling behavior of the
 * {@link ClusteredXYBarRenderer} class.
 */
public class ClusteredXYBarRenderer_ESTestTest13 extends ClusteredXYBarRenderer_ESTest_scaffolding {

    /**
     * Verifies that {@code findDomainBounds()} throws a {@code NullPointerException}
     * when the provided dataset contains a null series key.
     * <p>
     * This exception is expected because the underlying dataset implementation
     * (in this case, from {@code AbstractSeriesDataset}) does not permit null keys
     * for its series. The renderer's method triggers this by interacting with the dataset.
     */
    @Test(expected = NullPointerException.class)
    public void findDomainBoundsShouldThrowExceptionForDatasetWithNullKey() {
        // Arrange: Create a renderer and a dataset with an invalid null series key.
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        DefaultBoxAndWhiskerXYDataset<Short> datasetWithNullKey =
                new DefaultBoxAndWhiskerXYDataset<>((Short) null);

        // Act & Assert: Calling the method with the invalid dataset should throw an NPE.
        // The test will pass only if a NullPointerException is thrown.
        renderer.findDomainBounds(datasetWithNullKey);
    }
}