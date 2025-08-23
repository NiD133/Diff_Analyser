package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.distance.DistanceCalculator;
import org.locationtech.spatial4j.distance.GeodesicSphereDistCalc;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This test class contains improved tests for the {@link ShapeCollection} class.
 */
public class ShapeCollectionTest {

    /**
     * Tests that an empty ShapeCollection reports intersection if its bounding box is
     * externally mutated to be non-empty.
     * <p>
     * This test case highlights a specific, and potentially fragile, implementation detail:
     * <ul>
     *   <li>An empty {@code ShapeCollection} is created, which has an empty bounding box.</li>
     *   <li>The test assumes {@code getBoundingBox()} returns a mutable reference to the
     *       collection's internal bounding box state.</li>
     *   <li>This bounding box is then modified to represent a non-empty area.</li>
     *   <li>Consequently, when {@code relate()} is called, it first checks this now-non-empty
     *       bounding box. Finding an intersection, it returns {@code INTERSECTS} without
     *       inspecting the list of shapes (which is empty).</li>
     * </ul>
     * This behavior might be unexpected, as an empty collection logically shouldn't intersect with anything.
     */
    @Test
    public void testRelateReturnsIntersectsForEmptyCollectionWithMutatedBoundingBox() {
        // Arrange
        final SpatialContext spatialContext = SpatialContext.GEO;

        // 1. Create an empty ShapeCollection. Its internal bounding box should be empty.
        ShapeCollection<Shape> emptyCollection = new ShapeCollection<>(Collections.emptyList(), spatialContext);
        Rectangle collectionBBox = emptyCollection.getBoundingBox();
        assertTrue("Precondition: An empty collection's bounding box should be empty.", collectionBBox.isEmpty());

        // 2. Externally mutate the state of the collection's bounding box.
        // This relies on getBoundingBox() returning a mutable, internal object.
        // We use a helper to calculate a new box and update the existing Rectangle object.
        Point centerPoint = spatialContext.makePoint(0, 0);
        double distanceDeg = 10.0; // An arbitrary distance to create a non-empty box.
        DistanceCalculator distCalc = new GeodesicSphereDistCalc.LawOfCosines();
        distCalc.calcBoxByDistFromPt(centerPoint, distanceDeg, spatialContext, collectionBBox);

        // Verify that the bounding box was successfully mutated.
        assertFalse("Side-effect check: The bounding box should no longer be empty.", collectionBBox.isEmpty());

        // 3. The shape to compare against is the entire world.
        Rectangle worldBounds = spatialContext.getWorldBounds();

        // Act
        // Relate the empty collection (which now has a non-empty bbox) to the world bounds.
        SpatialRelation relation = emptyCollection.relate(worldBounds);

        // Assert
        // The result is INTERSECTS, based solely on the mutated bounding box,
        // even though the collection contains no actual shapes.
        assertEquals("Expected INTERSECTS due to mutated bounding box", SpatialRelation.INTERSECTS, relation);
    }
}