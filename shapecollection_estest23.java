package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Test suite for the {@link ShapeCollection} constructor.
 */
public class ShapeCollectionConstructorTest {

    /**
     * Verifies that the constructor throws a NullPointerException when the provided
     * SpatialContext is null, as a valid context is a required dependency.
     */
    @Test(expected = NullPointerException.class)
    public void constructor_shouldThrowNullPointerException_whenContextIsNull() {
        // Arrange: Create a valid list of shapes and a null context.
        List<Shape> shapes = new ArrayList<>();
        SpatialContext nullContext = null;

        // Act & Assert: Attempting to create a ShapeCollection with a null context
        // is expected to throw a NullPointerException.
        new ShapeCollection<>(shapes, nullContext);
    }
}