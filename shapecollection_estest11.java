package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link ShapeCollection} class.
 */
public class ShapeCollectionTest {

    /**
     * Verifies that the getContext() method returns the exact SpatialContext instance
     * that was provided to the constructor.
     */
    @Test
    public void getContextShouldReturnTheContextProvidedInConstructor() {
        // Arrange: Create a specific SpatialContext to be used in the test.
        // Using a factory with a non-default setting ensures we are not accidentally
        // getting a global or default context.
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.normWrapLongitude = true;
        SpatialContext expectedContext = new SpatialContext(factory);

        List<Shape> emptyShapes = Collections.emptyList();

        // Act: Create a ShapeCollection and then retrieve its context.
        ShapeCollection<Shape> shapeCollection = new ShapeCollection<>(emptyShapes, expectedContext);
        SpatialContext actualContext = shapeCollection.getContext();

        // Assert: The retrieved context should be the same instance as the one provided.
        assertSame("The context from getContext() should be the same instance passed to the constructor.",
                expectedContext, actualContext);
    }
}