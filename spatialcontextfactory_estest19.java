package org.locationtech.spatial4j.context;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test suite for {@link SpatialContextFactory}.
 */
public class SpatialContextFactoryTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void initWorldBounds_whenArgsNotInitialized_throwsNullPointerException() {
        // Arrange: Create a factory instance directly. Its internal 'args' map will be null
        // because the init() method, which is typically called by makeSpatialContext(), has not been run.
        SpatialContextFactory factory = new SpatialContextFactory();

        // Assert: We expect a NullPointerException because the method relies on the 'args' map.
        thrown.expect(NullPointerException.class);

        // Act: Call the method under test.
        factory.initWorldBounds();
    }
}