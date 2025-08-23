package org.locationtech.spatial4j.context;

import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Test for {@link SpatialContextFactory}.
 */
public class SpatialContextFactoryTest {

    /**
     * Tests that when a SpatialContext is created with a specific configuration
     * for one property (like 'distCalculator'), other properties that are not
     * explicitly set will correctly fall back to their default values.
     */
    @Test
    public void makeSpatialContext_whenOnePropertyIsSet_shouldUseDefaultsForOthers() {
        // Arrange: Create configuration specifying only the distance calculator.
        // The 'normWrapLongitude' property is left unset to test its default behavior.
        Map<String, String> config = new HashMap<>();
        config.put("distCalculator", "lawofcosines");
        
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        // Act: Create the SpatialContext using the factory.
        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, classLoader);

        // Assert: Verify the context was created and that 'normWrapLongitude'
        // has its expected default value of false.
        assertNotNull("The created context should not be null.", context);
        assertFalse(
            "Expected normWrapLongitude to be its default value (false) when not specified.",
            context.isNormWrapLongitude()
        );
    }
}