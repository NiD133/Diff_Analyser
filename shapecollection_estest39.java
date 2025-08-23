package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link ShapeCollection}.
 */
public class ShapeCollectionTest {

    /**
     * This test verifies that the getContext() method of ShapeCollection
     * correctly returns the same SpatialContext instance that was provided
     * to its constructor.
     */
    @Test
    public void getContextShouldReturnContextProvidedInConstructor() {
        // Arrange: Create a standard spatial context and an empty shape collection.
        // Using SpatialContext.GEO is simpler and clearer than building a new context from a factory.
        final SpatialContext expectedContext = SpatialContext.GEO;
        List<Shape> emptyShapes = Collections.emptyList();
        ShapeCollection<Shape> shapeCollection = new ShapeCollection<>(emptyShapes, expectedContext);

        // Act: Retrieve the context from the shape collection.
        SpatialContext actualContext = shapeCollection.getContext();

        // Assert: The retrieved context should be the exact same instance as the one provided.
        // Using assertSame is a more direct and robust test for a getter than checking a property.
        assertSame("The context returned should be the same instance provided to the constructor.",
                expectedContext, actualContext);
    }
}