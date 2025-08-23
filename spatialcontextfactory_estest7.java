package org.locationtech.spatial4j.context;

import org.junit.Test;
import java.util.Collections;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the SpatialContextFactory class.
 */
public class SpatialContextFactoryTest {

    /**
     * Verifies that the distance calculator is not initialized when the configuration
     * arguments do not specify one. The method should complete without errors
     * and leave the `distCalc` field as its default null value.
     */
    @Test
    public void initCalculator_whenNoDistCalcArgumentIsProvided_shouldNotSetCalculator() {
        // Arrange
        SpatialContextFactory factory = new SpatialContextFactory();
        // The 'args' map is normally populated by the public factory method.
        // We set it directly here to test the protected initCalculator() method in isolation.
        factory.args = Collections.emptyMap();

        // Act
        factory.initCalculator();

        // Assert
        // The distance calculator should remain uninitialized (null) because the
        // 'distCalculator' key was absent from the arguments.
        assertNull("Expected distCalc to be null when no 'distCalculator' argument is provided", factory.distCalc);
    }
}