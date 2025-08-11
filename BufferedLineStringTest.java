package org.locationtech.spatial4j.shape;

import com.carrotsearch.randomizedtesting.RandomizedTest;
import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.impl.BufferedLineString;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Verifies the rectangle intersection behavior of BufferedLineString by leveraging
 * RectIntersectionTestHelper. The test uses a small, cartesian world with no
 * geodesic behavior to keep reasoning about intersections simple.
 */
public class BufferedLineStringTest extends RandomizedTest {

  // Keep the world small and cartesian (geo=false) for predictable intersections.
  private final SpatialContext ctx = createCartesianContext();

  private static SpatialContext createCartesianContext() {
    SpatialContextFactory factory = new SpatialContextFactory();
    factory.geo = false;
    // xmin, xmax, ymin, ymax
    factory.worldBounds = new RectangleImpl(-100, 100, -50, 50, null);
    return factory.newSpatialContext();
  }

  @Test
  public void testRectangleIntersections() {
    new BufferedLineStringRectIntersectionHelper(ctx).testRelateWithRectangle();
  }

  /**
   * Specializes the generic rectangle-intersection helper for BufferedLineString.
   * It controls how random BufferedLineStrings are generated near a given point.
   */
  private static class BufferedLineStringRectIntersectionHelper
      extends RectIntersectionTestHelper<BufferedLineString> {

    private static final int MIN_POINTS = 2;      // A line segment requires at least two points
    private static final int EXTRA_POINTS_MAX = 3; // Up to 3 extra points -> 2..5 total points
    private static final double BUFFER_FRACTION_OF_REGION = 0.25; // Max buffer as a fraction of near-rectangle span

    BufferedLineStringRectIntersectionHelper(SpatialContext ctx) {
      super(ctx);
    }

    @Override
    protected BufferedLineString generateRandomShape(Point nearPoint) {
      // Generate a nearby rectangle and then place a short polyline within it.
      Rectangle nearRegion = randomRectangle(nearPoint);

      // Choose total number of control points: 2..5
      int numPoints = MIN_POINTS + randomInt(EXTRA_POINTS_MAX);

      List<Point> controlPoints = new ArrayList<>(numPoints);
      while (controlPoints.size() < numPoints) {
        controlPoints.add(randomPointIn(nearRegion));
      }

      // Compute a non-negative buffer. We cap it at roughly 1/4 of the larger rectangle axis
      // so the buffered line stays "near" the generated control points.
      double maxAxis = Math.max(nearRegion.getWidth(), nearRegion.getHeight());
      double maxBufferMagnitude = maxAxis * BUFFER_FRACTION_OF_REGION;

      // Start with a gaussian magnitude, clamp to [0, max], then coerce via 'divisible' and randomize to int.
      double gaussianScaled = Math.abs(randomGaussian()) * maxBufferMagnitude;
      // 'divisible' comes from RectIntersectionTestHelper and tends to normalize to "cleaner" values.
      int finalIntegerBuffer = randomInt((int) divisible(gaussianScaled));
      double buffer = finalIntegerBuffer;

      return new BufferedLineString(controlPoints, buffer, ctx);
    }

    /**
     * If the shape happens to be empty (e.g., degenerate), pick one of its control points
     * as a representative "nearby" point for the helper's purposes.
     */
    @Override
    protected Point randomPointInEmptyShape(BufferedLineString shape) {
      List<Point> points = shape.getPoints();
      return points.get(randomInt(points.size() - 1));
    }
  }
}