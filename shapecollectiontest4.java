package org.locationtech.spatial4j.shape;

import org.locationtech.spatial4j.TestLog;
import org.locationtech.spatial4j.context.SpatialContext;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.locationtech.spatial4j.shape.SpatialRelation.CONTAINS;

/**
 * Tests for {@link ShapeCollection} focusing on bounding box calculations
 * and randomized relationship testing.
 */
public class ShapeCollectionTest extends RandomizedShapeTest {

    private static final String FULL_WORLD_LONGITUDE_RANGE_STR = getLonRangeString(SpatialContext.GEO.getWorldBounds());

    @Rule
    public final TestLog testLog = TestLog.instance;

    private static String getLonRangeString(Rectangle bbox) {
        return bbox.getMinX() + " " + bbox.getMaxX();
    }

    /**
     * Tests that the bounding box of a {@link ShapeCollection} correctly spans the entire
     * longitude range of the world when its constituent shapes are on opposite sides of
     * the globe.
     */
    @Test
    public void testBoundingBoxSpansWorld() {
        ctx = SpatialContext.GEO;
        // Two rectangles on opposite sides of the world
        assertCollectionBoundingBoxWrapsWorld(
                ctx.makeRectangle(-170, -160, 0, 0),
                ctx.makeRectangle(160, 170, 0, 0)
        );
        // A dateline-crossing rectangle and another rectangle
        assertCollectionBoundingBoxWrapsWorld(
                ctx.makeRectangle(170, -170, 0, 0),
                ctx.makeRectangle(-10, 10, 0, 0)
        );
    }

    private void assertCollectionBoundingBoxWrapsWorld(Rectangle r1, Rectangle r2) {
        // The order of shapes should not affect the bounding box result.
        // Test with order (r1, r2)
        ShapeCollection<Rectangle> s1 = new ShapeCollection<>(Arrays.asList(r1, r2), ctx);
        assertEquals("BBox of " + r1 + " and " + r2 + " should span the world",
                FULL_WORLD_LONGITUDE_RANGE_STR, getLonRangeString(s1.getBoundingBox()));

        // Test with reversed order (r2, r1)
        ShapeCollection<Rectangle> s2 = new ShapeCollection<>(Arrays.asList(r2, r1), ctx);
        assertEquals("BBox of " + r2 + " and " + r1 + " should span the world (reversed order)",
                FULL_WORLD_LONGITUDE_RANGE_STR, getLonRangeString(s2.getBoundingBox()));
    }

    /**
     * Runs randomized tests for spatial relations between a {@link ShapeCollection}
     * and a {@link Rectangle}.
     */
    @Test
    public void testRandomizedRelationsAgainstRectangles() {
        ctx = SpatialContext.GEO;
        new ShapeCollectionRectIntersectionTestHelper(ctx).testRelateWithRectangle();
    }

    private class ShapeCollectionRectIntersectionTestHelper extends RectIntersectionTestHelper<ShapeCollection<Rectangle>> {

        private ShapeCollectionRectIntersectionTestHelper(SpatialContext ctx) {
            super(ctx);
        }

        @Override
        protected ShapeCollection<Rectangle> generateRandomShape(Point nearP) {
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

        private void validateBoundingBox(ShapeCollection<Rectangle> shapeCollection) {
            List<Rectangle> shapes = shapeCollection.getShapes();
            Rectangle collectionBBox = shapeCollection.getBoundingBox();

            if (shapes.size() == 1) {
                assertEquals(shapes.get(0), collectionBBox);
                return;
            }

            // The collection's bounding box must contain every constituent shape.
            for (Rectangle shape : shapes) {
                assertRelation("Collection BBox should contain each inner shape", CONTAINS, collectionBBox, shape);
            }

            // If the bounding box spans the world longitudinally, we perform an additional check
            // to ensure it's a "tight" bounding box and not spuriously wrapping. We verify that
            // for any given longitude, at least one of the shapes contains it.
            if (ctx.isGeo() && collectionBBox.getMinX() == -180 && collectionBBox.getMaxX() == 180) {
                int randomLon = randomIntBetween(-180, 180);
                boolean longitudeCovered = false;
                for (Rectangle shape : shapes) {
                    if (shape.relateXRange(randomLon, randomLon).intersects()) {
                        longitudeCovered = true;
                        break;
                    }
                }
                if (!longitudeCovered) {
                    fail("ShapeCollection BBox wraps world, but longitude " + randomLon
                            + " is not covered by any shape in " + shapes);
                }
            }
        }

        @Override
        protected Point randomPointInEmptyShape(ShapeCollection<Rectangle> shape) {
            // The method name is inherited from the abstract superclass. For a collection,
            // this implementation returns a point within one of its member shapes, not
            // necessarily in an "empty" area of the collection's bounding box.
            Rectangle r = shape.getShapes().get(0);
            return randomPointIn(r);
        }
    }
}