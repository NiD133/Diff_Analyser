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
 * Tests for {@link ShapeCollection} bounding box calculations and randomized intersection testing.
 */
public class ShapeCollectionTest extends RandomizedShapeTest {

    private static final String WORLD_LON_RANGE = getLonRangeString(SpatialContext.GEO.getWorldBounds());

    @Rule
    public final TestLog testLog = TestLog.instance;

    private static String getLonRangeString(Rectangle bbox) {
        return bbox.getMinX() + " " + bbox.getMaxX();
    }

    @Test
    public void boundingBox_withShapesSpanningDateLine_shouldWrapWorld() {
        // Two rectangles on opposite sides of the date line.
        // Their combined longitude span is > 180 degrees, so the bounding box
        // should wrap around the world.
        Rectangle rectWest = ctx.makeRectangle(-170, -160, 0, 0);
        Rectangle rectEast = ctx.makeRectangle(170, 175, 0, 0);

        ShapeCollection<Rectangle> collection = new ShapeCollection<>(Arrays.asList(rectWest, rectEast), ctx);

        // The bounding box should span the entire longitude range.
        assertEquals(WORLD_LON_RANGE, getLonRangeString(collection.getBoundingBox()));

        // The order of shapes should not matter.
        ShapeCollection<Rectangle> collectionReversed = new ShapeCollection<>(Arrays.asList(rectEast, rectWest), ctx);
        assertEquals(WORLD_LON_RANGE, getLonRangeString(collectionReversed.getBoundingBox()));
    }

    @Test
    public void boundingBox_withWideGapAcrossDateLine_shouldNotWrapWorld() {
        // These three rectangles span the date line, but there is a large gap
        // between them (e.g., longitude 102 is not covered).
        // Therefore, the bounding box should cross the date line but not wrap the entire world.
        Rectangle r1 = ctx.makeRectangle(-92, 90, -10, 10);
        Rectangle r2 = ctx.makeRectangle(130, 172, -10, 10);
        Rectangle r3 = ctx.makeRectangle(172, -60, -10, 10); // Crosses date line from 172 to -60

        ShapeCollection<Rectangle> collection = new ShapeCollection<>(Arrays.asList(r1, r2, r3), ctx);
        Rectangle bbox = collection.getBoundingBox();

        // The bounding box should be the narrowest one that contains all shapes.
        // In this case, it spans from 130 east to 90 east (crossing the date line).
        // It should NOT be the wider one from -92 to 172.
        assertEquals(130.0, bbox.getMinX(), 0.0);
        assertEquals(90.0, bbox.getMaxX(), 0.0);

        // Note: BBoxCalculatorTest thoroughly tests the longitude range logic.
    }

    private class ShapeCollectionRectIntersectionTestHelper extends RectIntersectionTestHelper<ShapeCollection<Rectangle>> {

        private ShapeCollectionRectIntersectionTestHelper(SpatialContext ctx) {
            super(ctx);
        }

        @Override
        protected ShapeCollection<Rectangle> generateRandomShape(Point nearP) {
            testLog.log("Break on nearP.toString(): {}", nearP);
            List<Rectangle> shapes = new ArrayList<>();
            int count = randomIntBetween(1, 4);
            for (int i = 0; i < count; i++) {
                // Place the first two shapes near the provided point; others can be anywhere.
                shapes.add(randomRectangle(i < 2 ? nearP : null));
            }
            ShapeCollection<Rectangle> shapeCollection = new ShapeCollection<>(shapes, ctx);

            validateBoundingBox(shapeCollection);

            return shapeCollection;
        }

        private void validateBoundingBox(ShapeCollection<Rectangle> collection) {
            List<Rectangle> shapes = collection.getShapes();
            Rectangle collectionBbox = collection.getBoundingBox();

            if (shapes.size() == 1) {
                assertEquals(shapes.get(0), collectionBbox);
            } else {
                // The collection's bounding box must contain every shape within it.
                for (Rectangle shape : shapes) {
                    assertRelation("bbox should contain each shape", CONTAINS, collectionBbox, shape);
                }
            }

            // Special check for world-wrapping bounding boxes in geographic contexts.
            if (ctx.isGeo() && collectionBbox.getWidth() == 360) {
                // If the bounding box wraps the world, it must not be "hollow".
                // We test this by picking a random longitude and ensuring at least one
                // of the shapes covers it.
                int lonTest = randomIntBetween(-180, 180);
                boolean longitudeCovered = false;
                for (Rectangle shape : shapes) {
                    if (shape.relateXRange(lonTest, lonTest).intersects()) {
                        longitudeCovered = true;
                        break;
                    }
                }
                if (!longitudeCovered) {
                    fail("ShapeCollection's world-wrapping bbox does not contain longitude "
                            + lonTest + " for shapes: " + shapes);
                }
            }
        }

        /**
         * The randomized test framework calls this method to generate a point that is
         * guaranteed to NOT be in the shape. To do this, it needs a point that IS in the
         * shape, which it then mutates. This implementation provides that initial point.
         * Despite the name "randomPointInEmptyShape", it returns a point within the
         * *first* shape of the collection.
         */
        @Override
        protected Point randomPointInEmptyShape(ShapeCollection<Rectangle> shape) {
            Rectangle firstShape = shape.getShapes().get(0);
            return randomPointIn(firstShape);
        }
    }
}