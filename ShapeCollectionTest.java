package org.locationtech.spatial4j.shape;

import org.junit.Rule;
import org.junit.Test;
import org.locationtech.spatial4j.TestLog;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.locationtech.spatial4j.shape.SpatialRelation.CONTAINS;

/**
 * Tests for ShapeCollection focusing on bounding-box behavior and rectangle relations.
 * Test names and helper methods are written to clarify intent and reduce duplication.
 */
public class ShapeCollectionTest extends RandomizedShapeTest {

  // Fixed latitude band used across tests to focus on longitude behavior
  private static final double BAND_MIN_LAT = -10;
  private static final double BAND_MAX_LAT = 10;

  // Convenience string representing a "world-wide" longitude span (-180 to +180)
  public static final String WORLD180_LON_RANGE = getLonRangeString(SpatialContext.GEO.getWorldBounds());

  @Rule
  public final TestLog testLog = TestLog.instance;

  /**
   * Returns "minLon maxLon" for a rectangle's longitude range.
   */
  protected static String getLonRangeString(Rectangle bbox) {
    return bbox.getMinX() + " " + bbox.getMaxX();
  }

  /**
   * When two horizontal bands together cover the entire longitude range, the resulting
   * ShapeCollection bounding box should cover the world (-180 to +180), regardless of order.
   */
  @Test
  public void testBoundingBoxCoversWorldWhenTwoBandsCoverFullLongitude() {
    // Adjacent halves: [-180, 180] and [-180, 180]
    assertWorldBboxForTwoBands(-180, 180, -180, 180);

    // Two halves covering [-180, 0] and [0, 180]
    assertWorldBboxForTwoBands(-180, 0, 0, 180);

    // Two halves covering [-90, 90] and [90, -90] where the latter wraps across the dateline
    assertWorldBboxForTwoBands(-90, 90, 90, -90);
  }

  /**
   * If there is a gap in longitude coverage, the ShapeCollection bounding box should not
   * world-wrap to [-180, 180].
   */
  @Test
  public void testBoundingBoxDoesNotWorldWrapWhenLongitudeGapExists() {
    ctx = SpatialContext.GEO;

    // Note on gap: There is no coverage at 102 degrees longitude across all shapes,
    // so the bbox must not "wrap" to [-180, 180].
    final Rectangle r1 = horizontalBand(-92, 90);
    final Rectangle r2 = horizontalBand(130, 172);
    // This rectangle crosses the dateline (minX > maxX)
    final Rectangle r3 = horizontalBand(172, -60);

    final ShapeCollection<Rectangle> shapes = new ShapeCollection<>(Arrays.asList(r1, r2, r3), ctx);

    // Expect a non-world-wrapped bbox spanning [130, 90] (across the dateline).
    assertEquals("Unexpected bbox longitude range when there is a gap",
        "130.0 90.0", getLonRangeString(shapes.getBoundingBox()));

    // Note: BBoxCalculatorTest thoroughly tests longitude range logic elsewhere.
  }

  /**
   * Tests rectangle intersection behavior in a cartesian (non-geo) context.
   */
  @Test
  public void testRectIntersectInCartesianContext() {
    SpatialContextFactory factory = new SpatialContextFactory();
    factory.geo = false;
    factory.worldBounds = new RectangleImpl(-100, 100, -50, 50, null);

    ctx = factory.newSpatialContext();
    new ShapeCollectionRectIntersectionTestHelper(ctx).testRelateWithRectangle();
  }

  /**
   * Tests rectangle intersection behavior in a geo (spherical) context.
   */
  @Test
  public void testRectIntersectInGeoContext() {
    ctx = SpatialContext.GEO;
    new ShapeCollectionRectIntersectionTestHelper(ctx).testRelateWithRectangle();
  }

  /**
   * Verifies that a ShapeCollection composed of two horizontal bands produces a world-wide
   * longitude range, independent of the order of the shapes.
   */
  private void assertWorldBboxForTwoBands(double r1MinLon, double r1MaxLon,
                                          double r2MinLon, double r2MaxLon) {
    ctx = SpatialContext.GEO;

    final Rectangle r1 = horizontalBand(r1MinLon, r1MaxLon);
    final Rectangle r2 = horizontalBand(r2MinLon, r2MaxLon);

    ShapeCollection<Rectangle> collection = new ShapeCollection<>(Arrays.asList(r1, r2), ctx);
    assertEquals("Bbox should cover world when bands cover all longitudes",
        WORLD180_LON_RANGE, getLonRangeString(collection.getBoundingBox()));

    // Flip order to ensure order-independence
    collection = new ShapeCollection<>(Arrays.asList(r2, r1), ctx);
    assertEquals("Bbox should cover world regardless of shape order",
        WORLD180_LON_RANGE, getLonRangeString(collection.getBoundingBox()));
  }

  /**
   * Creates a rectangle with the fixed latitude band and the provided longitude span.
   */
  private Rectangle horizontalBand(double minLon, double maxLon) {
    return ctx.makeRectangle(minLon, maxLon, BAND_MIN_LAT, BAND_MAX_LAT);
  }

  /**
   * Helper that supplies random ShapeCollections for intersection tests.
   */
  private class ShapeCollectionRectIntersectionTestHelper
      extends RectIntersectionTestHelper<ShapeCollection> {

    private ShapeCollectionRectIntersectionTestHelper(SpatialContext ctx) {
      super(ctx);
    }

    @Override
    protected ShapeCollection generateRandomShape(Point nearP) {
      testLog.log("Randomizing ShapeCollection near point: {}", nearP);

      final List<Rectangle> rectangles = new ArrayList<>();
      final int rectCount = randomIntBetween(1, 4);

      for (int i = 0; i < rectCount; i++) {
        // First two rectangles are biased near 'nearP'; others anywhere.
        rectangles.add(randomRectangle(i < 2 ? nearP : null));
      }

      final ShapeCollection<Rectangle> collection = new ShapeCollection<>(rectangles, ctx);

      // Validate collection's bbox
      final Rectangle bbox = collection.getBoundingBox();
      if (rectangles.size() == 1) {
        // For a single rectangle, the collection bbox should effectively match the rectangle.
        assertEquals("Single-rectangle collection should have same bbox as the rectangle",
            rectangles.get(0), bbox.getBoundingBox());
      } else {
        for (Rectangle r : rectangles) {
          assertRelation("Collection bbox should contain each member rectangle", CONTAINS, bbox, r);
        }

        // In geo context, if the bbox spans the entire longitude range, ensure it isn't a false positive.
        if (ctx.isGeo() && bbox.getMinX() == -180 && bbox.getMaxX() == 180) {
          final int testLongitude = randomIntBetween(-180, 180);
          boolean anyShapeIntersectsRandomLongitude = false;
          for (Rectangle r : rectangles) {
            if (r.relateXRange(testLongitude, testLongitude).intersects()) {
              anyShapeIntersectsRandomLongitude = true;
              break;
            }
          }
          if (!anyShapeIntersectsRandomLongitude) {
            fail("World-wrapped bbox should contain test longitude " + testLongitude +
                " but none of the member rectangles do. Rectangles: " + rectangles);
          }
        }
      }
      return collection;
    }

    @Override
    protected Point randomPointInEmptyShape(ShapeCollection shape) {
      // For our generator, "empty" isn't expected; choose a safe point in the first rectangle.
      Rectangle first = (Rectangle) shape.getShapes().get(0);
      return randomPointIn(first);
    }
  }
}