package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.impl.PointImpl;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Note: This class name and its parent class appear to be auto-generated.
 * For a production test suite, they would typically be renamed to something
 * more conventional, like `ShapeCollectionTest`.
 */
public class ShapeCollection_ESTestTest33 extends ShapeCollection_ESTest_scaffolding {

    /**
     * Verifies that calling relate() on an empty ShapeCollection correctly
     * returns DISJOINT, as there are no shapes to intersect with.
     */
    @Test
    public void relateWithEmptyCollectionShouldReturnDisjoint() {
        // Arrange
        SpatialContext spatialContext = SpatialContext.GEO;
        List<Shape> emptyShapes = Collections.emptyList();
        ShapeCollection<Shape> emptyCollection = new ShapeCollection<>(emptyShapes, spatialContext);
        Point testPoint = new PointImpl(0.5, 0.5, spatialContext);

        // Act
        SpatialRelation relation = emptyCollection.relate(testPoint);

        // Assert
        assertEquals("An empty collection should be disjoint from any shape.",
                SpatialRelation.DISJOINT, relation);
    }
}