package org.locationtech.spatial4j.shape;

import com.carrotsearch.randomizedtesting.RandomizedTest;
import com.carrotsearch.randomizedtesting.annotations.Repeat;
import org.junit.Rule;
import org.junit.Test;
import org.locationtech.spatial4j.TestLog;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.impl.BufferedLine;
import org.locationtech.spatial4j.shape.impl.PointImpl;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for BufferedLine focusing on:
 * - contains(Point) behavior at the buffer boundary
 * - degenerate cases (pA == pB)
 * - a quadrant heuristic vs brute-force comparison
 * - rectangle intersection relations via RectIntersectionTestHelper
 *
 * Notes:
 * - Tests use a simple Euclidean SpatialContext (geo=false) with a custom world bounds.
 * - Some tests are randomized; the intention is to verify invariants rather than exact positions.
 */
public class BufferedLineTest extends RandomizedTest {

  /**
   * Non-geo context with a manageable world extent to keep numbers readable.
   */
  private static final SpatialContext CTX = new SpatialContextFactory() {{
    geo = false;
    worldBounds = new RectangleImpl(-100, 100, -50, 50, null);
  }}.newSpatialContext();

  @Rule
  public TestLog testLog = TestLog.instance;

  /**
   * Verifies contains(Point) transitions from false to true exactly at the target distance
   * by testing a value just below and just above the expected distance-derived buffer.
   */
  @Test
  public void containsTransitionsAroundExpectedDistance() {
    // Negative slope segment
    assertContainsAtBufferMargin(
        CTX.makePoint(7, -4),  // A
        CTX.makePoint(3, 2),   // B
        CTX.makePoint(5, 6),   // C (test point)
        3.88290
    );

    // Positive slope segment
    assertContainsAtBufferMargin(
        CTX.makePoint(3, 2),
        CTX.makePoint(7, 5),
        CTX.makePoint(5, 6),
        2.0
    );

    // Vertical segment
    assertContainsAtBufferMargin(
        CTX.makePoint(3, 2),
        CTX.makePoint(3, 8),
        CTX.makePoint(4, 3),
        1.0
    );

    // Horizontal segment
    assertContainsAtBufferMargin(
        CTX.makePoint(3, 2),
        CTX.makePoint(6, 2),
        CTX.makePoint(4, 3),
        1.0
    );
  }

  /**
   * Degenerate case: pA == pB. BufferedLine becomes a disk of radius buf around the point.
   */
  @Test
  public void degenerateLineActsLikeDisk() {
    Point center = CTX.makePoint(10, 1);
    BufferedLine line = new BufferedLine(center, center, 3, CTX);

    // Just inside the buffer
    assertTrue(line.contains(CTX.makePoint(10, 1 + 3 - 0.1)));

    // Just outside the buffer
    assertFalse(line.contains(CTX.makePoint(10, 1 + 3 + 0.1)));
  }

  /**
   * The line's quadrant heuristic (relative to a rectangle center) should match a brute-force
   * "farthest rectangle corner" approach. This is a randomized invariant test.
   */
  @Test
  @Repeat(iterations = 15)
  public void quadrantHeuristicMatchesBruteForceCorner() {
    // Random buffered line and independent random bounding rectangle
    BufferedLine line = randomBufferedLine();
    Rectangle rect = randomBufferedLine().getBoundingBox();
    List<Point> corners = rectangleCornersClockwise(rect);

    // Determine which rectangle corners are farthest from the infinite primary line
    double farthest = -1;
    Collection<Integer> farthestCornerQuadrants = new LinkedList<>(); // ties possible
    int quadrantIndex = 1; // matches the implementation's quadrant numbering
    for (Point corner : corners) {
      double d = line.getLinePrimary().distanceUnbuffered(corner);
      if (Math.abs(d - farthest) < 1e-6) {
        farthestCornerQuadrants.add(quadrantIndex);
      } else if (d > farthest) {
        farthest = d;
        farthestCornerQuadrants.clear();
        farthestCornerQuadrants.add(quadrantIndex);
      }
      quadrantIndex++;
    }

    // Compare against the line's computed quadrant for the rectangle center
    int computedQuadrant = line.getLinePrimary().quadrant(rect.getCenter());
    assertTrue(farthestCornerQuadrants.contains(computedQuadrant));
  }

  /**
   * Rectangle intersection behavior exercised via RectIntersectionTestHelper.
   */
  @Test
  public void rectangleIntersectionRelations() {
    new RectIntersectionTestHelper<BufferedLine>(CTX) {

      @Override
      protected BufferedLine generateRandomShape(Point nearP) {
        Rectangle nearRect = randomRectangle(nearP);
        List<Point> corners = rectangleCornersClockwise(nearRect);

        // Build a diagonal line across the rectangle to ensure interesting intersections
        int startIdx = randomInt(3); // 0..3
        Point pA = corners.get(startIdx);
        Point pB = corners.get((startIdx + 2) % 4); // opposite corner

        // Choose a buffer relative to the rectangle size
        double maxDim = Math.max(nearRect.getWidth(), nearRect.getHeight());
        double buf = Math.abs(randomGaussian()) * maxDim / 4;
        buf = randomInt((int) divisible(buf)); // snap to an integer buffer for stability

        return new BufferedLine(pA, pB, buf, CTX);
      }

      @Override
      protected Point randomPointInEmptyShape(BufferedLine shape) {
        // Prefer endpoints if we need a deterministic "inside" candidate for degenerate cases
        return randomInt(1) == 0 ? shape.getA() : shape.getB();
      }
    }.testRelateWithRectangle();
  }

  // ---------- Helpers ----------

  /**
   * Verifies that for a given line segment AB and a test point C, the "contains" result flips
   * at the expected buffer distance:
   * - with slightly less than the target buffer, C is not contained
   * - with slightly more than the target buffer, C is contained
   */
  private void assertContainsAtBufferMargin(Point pA, Point pB, Point testPoint, double targetDistance) {
    if (targetDistance > 0) {
      assertFalse(new BufferedLine(pA, pB, targetDistance * 0.999, CTX).contains(testPoint));
      assertTrue(new BufferedLine(pA, pB, targetDistance * 1.001, CTX).contains(testPoint));
    } else {
      // Exact zero distance: C is on the segment
      assertTrue(new BufferedLine(pA, pB, 0, CTX).contains(testPoint));
    }
  }

  /**
   * Generates a random BufferedLine with integer coordinates and a small integer buffer.
   * Endpoints are in [0,9] x [0,9], buffer in [0,5].
   */
  private BufferedLine randomBufferedLine() {
    Point pA = new PointImpl(randomInt(9), randomInt(9), CTX);
    Point pB = new PointImpl(randomInt(9), randomInt(9), CTX);
    int buf = randomInt(5);
    return new BufferedLine(pA, pB, buf, CTX);
  }

  /**
   * Returns the four rectangle corners in clockwise order starting at (maxX, maxY).
   * The ordering aligns with the quadrant numbering used by the implementation.
   */
  private List<Point> rectangleCornersClockwise(Rectangle rect) {
    List<Point> corners = new ArrayList<>(4);
    corners.add(CTX.makePoint(rect.getMaxX(), rect.getMaxY()));
    corners.add(CTX.makePoint(rect.getMinX(), rect.getMaxY()));
    corners.add(CTX.makePoint(rect.getMinX(), rect.getMinY()));
    corners.add(CTX.makePoint(rect.getMaxX(), rect.getMinY()));
    return corners;
  }
}