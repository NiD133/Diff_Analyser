package org.locationtech.spatial4j.shape;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.locationtech.spatial4j.TestLog;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.locationtech.spatial4j.shape.SpatialRelation.CONTAINS;

/**
 * Tests for {@link ShapeCollection} focusing on bounding box calculations,
 * especially in geographic contexts with world-wrapping behavior.
 */
public class ShapeCollectionTest extends RandomizedShapeTest {

    @Rule
    public final TestLog testLog = TestLog.instance;

    private SpatialContext geoCtx;

    @Before
    public void setUp() {
        // The tests in this class are specifically for a geographic context.
        this.ctx = SpatialContext.GEO;
        this.geoCtx = SpatialContext.GEO;
    }

    @Test
    public void testBboxWorldWrapping_adjacentRects() {
        // ARRANGE
        // Two rectangles that are adjacent and together span the globe.
        Rectangle rect1 = geoCtx.makeRectangle(-180, 0, -10, 10);
        Rectangle rect2 = geoCtx.makeRectangle(0, 180, -10, 10);
        ShapeCollection<Rectangle> collection = new ShapeCollection<>(Arrays.asList(rect1, rect2), geoCtx);

        // ACT
        Rectangle bbox = collection.getBoundingBox();

        // ASSERT
        // The combined bounding box should span the entire globe horizontally.
        assertEquals(-180.0, bbox.getMinX(), 0.0);
        assertEquals(180.0, bbox.getMaxX(), 0.0);
        assertEquals("The bounding box should span the full 360 degrees", 360.0, bbox.getWidth(), 0.0);
    }

    @Test
    public void testBboxWorldWrapping_datelineCrossingRects() {
        // ARRANGE
        // Two rectangles that both cross the dateline and together span the globe.
        // rect1: from 90E to 90W (crossing dateline)
        // rect2: from 90W to 90E (the other way around the globe)
        Rectangle rect1 = geoCtx.makeRectangle(90, -90, -10, 10);
        Rectangle rect2 = geoCtx.makeRectangle(-90, 90, -10, 10);
        
        // Test with both orders to ensure commutativity
        ShapeCollection<Rectangle> collection1 = new ShapeCollection<>(Arrays.asList(rect1, rect2), geoCtx);
        ShapeCollection<Rectangle> collection2 = new ShapeCollection<>(Arrays.asList(rect2, rect1), geoCtx);

        // ACT
        Rectangle bbox1 = collection1.getBoundingBox();
        Rectangle bbox2 = collection2.getBoundingBox();

        // ASSERT
        // Both collections should have a bounding box that spans the entire globe.
        assertEquals(-180.0, bbox1.getMinX(), 0.0);
        assertEquals(180.0, bbox1.getMaxX(), 0.0);
        assertEquals(-180.0, bbox2.getMinX(), 0.0);
        assertEquals(180.0, bbox2.getMaxX(), 0.0);
    }

    @Test
    public void testBboxWorldWrapping_singleWorldCoveringRect() {
        // ARRANGE
        // A single rectangle that already covers the entire world.
        Rectangle worldRect = geoCtx.makeRectangle(-180, 180, -10, 10);
        ShapeCollection<Rectangle> collection = new ShapeCollection<>(Arrays.asList(worldRect), geoCtx);

        // ACT
        Rectangle bbox = collection.getBoundingBox();

        // ASSERT
        assertEquals(-180.0, bbox.getMinX(), 0.0);
        assertEquals(180.0, bbox.getMaxX(), 0.0);
    }

    /**
     * A helper for randomized intersection tests involving ShapeCollections.
     * This is used by the {@link RandomizedShapeTest} superclass.
     */
    private class ShapeCollectionRectIntersectionTestHelper extends RectIntersectionTestHelper<ShapeCollection> {

        private ShapeCollectionRectIntersectionTestHelper(SpatialContext ctx) {
            super(ctx);
        }

        @Override
        protected ShapeCollection generateRandomShape(Point nearP) {
            testLog.log("Generating random ShapeCollection near: {}", nearP);
            List<Rectangle> shapes = new ArrayList<>();
            int count = randomIntBetween(1, 4);
            for (int i = 0; i < count; i++) {
                // The first two shapes are generated near the provided point, others are random.
                shapes.add(randomRectangle(i < 2 ? nearP : null));
            }

            ShapeCollection<Rectangle> shapeCollection = new ShapeCollection<>(shapes, ctx);
            validateBoundingBox(shapeCollection);
            return shapeCollection;
        }

        /**
         * Validates that the bounding box of the collection is correctly calculated.
         */
        private void validateBoundingBox(ShapeCollection<Rectangle> collection) {
            Rectangle bbox = collection.getBoundingBox();
            List<Rectangle> shapes = collection.getShapes();

            if (shapes.size() == 1) {
                assertEquals(shapes.get(0).getBoundingBox(), bbox);
                return; // No more checks needed for a single shape.
            }

            // For multiple shapes, the bounding box must contain each individual shape.
            for (Rectangle shape : shapes) {
                assertRelation("Bounding box should contain shape: " + shape, CONTAINS, bbox, shape);
            }

            // Special check for world-wrapping bounding boxes in geographic contexts.
            if (ctx.isGeo() && bbox.getWidth() == 360) {
                assertWorldWrappingBboxIsValid(collection, bbox);
            }
        }

        /**
         * Verifies that a world-wrapping bounding box is not "hollow". A bounding box
         * that spans from -180 to 180 could be incorrectly calculated for a set of
         * shapes that are far apart (e.g., one at -170 and one at +170) but do not
         * actually cover the entire longitude range. This test ensures that for any
         * given longitude, at least one shape in the collection covers it.
         */
        private void assertWorldWrappingBboxIsValid(ShapeCollection<Rectangle> collection, Rectangle worldBbox) {
            int randomLon = randomIntBetween(-180, 180);
            boolean longitudeCovered = false;
            for (Shape shape : collection.getShapes()) {
                if (shape.relateXRange(randomLon, randomLon).intersects()) {
                    longitudeCovered = true;
                    break;
                }
            }
            if (!longitudeCovered) {
                fail("ShapeCollection's world-wrapping bbox does not actually contain longitude "
                        + randomLon + ". Shapes: " + collection.getShapes());
            }
        }

        @Override
        protected Point randomPointIn(ShapeCollection shape) {
            // The original name 'randomPointInEmptyShape' was misleading.
            // This method returns a point within the *first* shape of the collection.
            Rectangle firstRect = (Rectangle) shape.getShapes().get(0);
            return randomPointIn(firstRect);
        }
    }
}