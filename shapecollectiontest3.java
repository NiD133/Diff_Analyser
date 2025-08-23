package org.locationtech.spatial4j.shape;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.locationtech.spatial4j.shape.SpatialRelation.CONTAINS;

/**
 * Randomized tests for {@link ShapeCollection} focusing on bounding box calculations and the
 * {@code relate()} method.
 */
public class ShapeCollectionRandomizedTest extends RandomizedShapeTest {

    private static final String FULL_WORLD_LONGITUDE_RANGE_STRING = formatLonRange(SpatialContext.GEO.getWorldBounds());

    private static String formatLonRange(Rectangle bbox) {
        return bbox.getMinX() + " " + bbox.getMaxX();
    }

    /**
     * Tests that the bounding box of a {@link ShapeCollection} correctly spans the full longitude range
     * (-180 to 180) when its constituent shapes are on opposite sides of the dateline.
     */
    @Test
    public void testBoundingBoxSpansWorldWhenCrossingDateline() {
        ctx = SpatialContext.GEO;
        final double Y = 0; // An arbitrary latitude for the test shapes

        // Case: Two rectangles on opposite sides of the dateline.
        // Together, their bounding box should span the entire world longitudinally.
        Rectangle rectEast = ctx.makeRectangle(170, 180, Y, Y);
        Rectangle rectWest = ctx.makeRectangle(-180, -170, Y, Y);

        // The order of shapes should not affect the result.
        ShapeCollection<Rectangle> collection1 = new ShapeCollection<>(Arrays.asList(rectEast, rectWest), ctx);
        assertEquals(FULL_WORLD_LONGITUDE_RANGE_STRING, formatLonRange(collection1.getBoundingBox()));

        ShapeCollection<Rectangle> collection2 = new ShapeCollection<>(Arrays.asList(rectWest, rectEast), ctx);
        assertEquals(FULL_WORLD_LONGITUDE_RANGE_STRING, formatLonRange(collection2.getBoundingBox()));
    }

    /**
     * A randomized test of the {@link ShapeCollection#relate(Shape)} method against various
     * rectangles in a non-geographic context.
     */
    @Test
    public void testRelateWithRandomizedRectangles_nonGeoContext() {
        SpatialContext nonGeoCtx = new SpatialContextFactory() {
            {
                geo = false;
                worldBounds = new RectangleImpl(-100, 100, -50, 50, null);
            }
        }.newSpatialContext();
        new ShapeCollectionRectIntersectionTestHelper(nonGeoCtx).testRelateWithRectangle();
    }

    /**
     * Test helper that generates random {@link ShapeCollection} instances for testing.
     */
    private class ShapeCollectionRectIntersectionTestHelper extends RectIntersectionTestHelper<ShapeCollection> {

        private ShapeCollectionRectIntersectionTestHelper(SpatialContext ctx) {
            super(ctx);
        }

        @Override
        protected ShapeCollection<Rectangle> generateRandomShape(Point nearP) {
            // Create a collection of 1 to 4 random rectangles.
            // The first two are generated near the provided point to ensure a high
            // probability of intersection, which is useful for testing `relate()`.
            List<Rectangle> shapes = new ArrayList<>();
            int numberOfShapes = randomIntBetween(1, 4);
            for (int i = 0; i < numberOfShapes; i++) {
                boolean generateNearPoint = (i < 2);
                shapes.add(randomRectangle(generateNearPoint ? nearP : null));
            }
            ShapeCollection<Rectangle> shapeCollection = new ShapeCollection<>(shapes, ctx);

            // As part of generation, validate the bounding box of the created collection.
            validateBoundingBox(shapeCollection);

            return shapeCollection;
        }

        private void validateBoundingBox(ShapeCollection<Rectangle> collection) {
            Rectangle collectionBbox = collection.getBoundingBox();
            List<Rectangle> shapes = collection.getShapes();

            if (shapes.size() == 1) {
                // For a single shape, the collection's bounding box should equal the shape's bounding box.
                assertEquals(shapes.get(0).getBoundingBox(), collectionBbox);
            } else {
                // For multiple shapes, the collection's bounding box must contain every shape.
                for (Rectangle shape : shapes) {
                    assertRelation("BBox should contain each shape", CONTAINS, collectionBbox, shape);
                }
            }

            // Special validation for geographic contexts where the bounding box spans the full longitude.
            if (ctx.isGeo() && collectionBbox.getWidth() == 360) {
                validateWorldSpanningBBox(collection, collectionBbox);
            }
        }

        private void validateWorldSpanningBBox(ShapeCollection<Rectangle> collection, Rectangle collectionBbox) {
            // If the bounding box wraps the world, it should not be an empty box from -180 to 180.
            // It must contain geometry at any given longitude. We test this by picking a random
            // longitude and checking that at least one shape in the collection covers it.
            int randomLon = randomIntBetween(-180, 180);
            boolean longitudeCovered = false;
            for (Rectangle shape : collection.getShapes()) {
                if (shape.relateXRange(randomLon, randomLon).intersects()) {
                    longitudeCovered = true;
                    break;
                }
            }
            if (!longitudeCovered) {
                fail("The world-spanning bounding box " + collectionBbox + " does not actually contain " +
                        "any geometry at longitude " + randomLon + ". Shapes: " + collection.getShapes());
            }
        }

        /**
         * Returns a random point within the given ShapeCollection.
         * <p>
         * NOTE: The method name {@code randomPointInEmptyShape} is inherited from the
         * base class and is misleading. This method is designed to find a point
         * within a *non-empty* shape for testing purposes.
         * <p>
         * For simplicity, this implementation returns a random point from the *first*
         * shape in the collection. This is sufficient for testing intersection logic.
         */
        @Override
        protected Point randomPointInEmptyShape(ShapeCollection shape) {
            Rectangle firstRectangle = (Rectangle) shape.getShapes().get(0);
            return randomPointIn(firstRectangle);
        }
    }
}