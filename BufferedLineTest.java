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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BufferedLineTest extends RandomizedTest {

  // Define a SpatialContext with a non-geographical world bounds for testing.
  private final SpatialContext ctx = new SpatialContextFactory() {{
    geo = false;
    worldBounds = new RectangleImpl(-100, 100, -50, 50, null);
  }}.newSpatialContext();

  @Rule
  public TestLog testLog = TestLog.instance;

  // Helper method to convert a Rectangle to WKT (Well-Known Text) format for debugging.
  static private String rectToWkt(Rectangle rect) {
    return "POLYGON((" + rect.getMinX() + " " + rect.getMinY() + "," +
        rect.getMaxX() + " " + rect.getMinY() + "," +
        rect.getMaxX() + " " + rect.getMaxY() + "," +
        rect.getMinX() + " " + rect.getMaxY() + "," +
        rect.getMinX() + " " + rect.getMinY() + "))";
  }

  /**
   * Tests the distance calculation between a BufferedLine and a Point.
   * It checks if a point is contained within the BufferedLine based on the distance.
   */
  @Test
  public void distanceToPoint() {
    // Test cases with different line slopes and point positions.
    testDistanceToPoint(ctx.makePoint(7, -4), ctx.makePoint(3, 2), ctx.makePoint(5, 6), 3.88290); // Negative slope
    testDistanceToPoint(ctx.makePoint(3, 2), ctx.makePoint(7, 5), ctx.makePoint(5, 6), 2.0);   // Positive slope
    testDistanceToPoint(ctx.makePoint(3, 2), ctx.makePoint(3, 8), ctx.makePoint(4, 3), 1.0);   // Vertical line
    testDistanceToPoint(ctx.makePoint(3, 2), ctx.makePoint(6, 2), ctx.makePoint(4, 3), 1.0);   // Horizontal line
  }

  /**
   * Helper method to assert if a point is within a BufferedLine based on a given distance.
   *
   * @param pA   The starting point of the line.
   * @param pB   The ending point of the line.
   * @param pC   The point to check for containment.
   * @param dist The buffer distance of the line.
   */
  private void testDistanceToPoint(Point pA, Point pB, Point pC, double dist) {
    BufferedLine line = new BufferedLine(pA, pB, dist, ctx);

    if (dist > 0) {
      // If the distance is greater than 0, the point should NOT be contained if it's slightly outside the buffer.
      assertFalse("Point should not be contained within the buffered line.",
          new BufferedLine(pA, pB, dist * 0.999, ctx).contains(pC));
    } else {
      // If the distance is 0, the point should be contained if it is exactly on the line.
      assertTrue("Point should be contained within the buffered line (zero buffer).",
          new BufferedLine(pA, pB, 0, ctx).contains(pC));
    }

    // The point SHOULD be contained if it's slightly inside the buffer.
    assertTrue("Point should be contained within the buffered line.",
        new BufferedLine(pA, pB, dist * 1.001, ctx).contains(pC));
  }

  /**
   * Tests miscellaneous cases, such as when the start and end points of the line are the same.
   */
  @Test
  public void miscTests() {
    // Test case where the start and end points are the same.
    Point pt = ctx.makePoint(10, 1);
    BufferedLine line = new BufferedLine(pt, pt, 3, ctx);

    // Assert that a point slightly inside the buffer is contained.
    assertTrue("Point should be contained within the buffered line.",
        line.contains(ctx.makePoint(10, 1 + 3 - 0.1)));

    // Assert that a point slightly outside the buffer is not contained.
    assertFalse("Point should not be contained within the buffered line.",
        line.contains(ctx.makePoint(10, 1 + 3 + 0.1)));
  }

  /**
   * Tests the quadrant determination of a random line's bounding box center.
   * This test is repeated multiple times with random lines.
   */
  @Test
  @Repeat(iterations = 15)
  public void quadrantsTest() {
    // Generate a random BufferedLine.
    BufferedLine line = newRandomLine();
    Rectangle rect = newRandomLine().getBoundingBox();

    // Compute the closest corner of the rectangle to the line using brute force.
    ArrayList<Point> corners = getQuadrantCorners(rect);
    Collection<Integer> farthestDistanceQuads = new LinkedList<>();
    double farthestDistance = -1;
    int quad = 1;

    for (Point corner : corners) {
      double d = line.getLinePrimary().distanceUnbuffered(corner);
      if (Math.abs(d - farthestDistance) < 0.000001) {
        farthestDistanceQuads.add(quad);
      } else if (d > farthestDistance) {
        farthestDistanceQuads.clear();
        farthestDistanceQuads.add(quad);
        farthestDistance = d;
      }
      quad++;
    }

    // Compare the computed quadrant with the quadrant determined by the BufferedLine.
    int calcClosestQuad = line.getLinePrimary().quadrant(rect.getCenter());
    assertTrue("Calculated quadrant should be in the set of farthest distance quadrants.",
        farthestDistanceQuads.contains(calcClosestQuad));
  }

  /**
   * Generates a random BufferedLine within the defined SpatialContext.
   *
   * @return A randomly generated BufferedLine.
   */
  private BufferedLine newRandomLine() {
    Point pA = new PointImpl(randomInt(9), randomInt(9), ctx);
    Point pB = new PointImpl(randomInt(9), randomInt(9), ctx);
    int buf = randomInt(5);
    return new BufferedLine(pA, pB, buf, ctx);
  }

  /**
   * Returns the four corners of a rectangle as a list of Points, representing the four quadrants.
   *
   * @param rect The Rectangle to get the corners from.
   * @return An ArrayList containing the four corner Points of the Rectangle.
   */
  private ArrayList<Point> getQuadrantCorners(Rectangle rect) {
    ArrayList<Point> corners = new ArrayList<>(4);
    corners.add(ctx.makePoint(rect.getMaxX(), rect.getMaxY())); // Quadrant 1
    corners.add(ctx.makePoint(rect.getMinX(), rect.getMaxY())); // Quadrant 2
    corners.add(ctx.makePoint(rect.getMinX(), rect.getMinY())); // Quadrant 3
    corners.add(ctx.makePoint(rect.getMaxX(), rect.getMinY())); // Quadrant 4
    return corners;
  }

  /**
   * Tests the intersection of a BufferedLine with a Rectangle.
   */
  @Test
  public void testRectIntersection() {
    new RectIntersectionTestHelper<BufferedLine>(ctx) {

      @Override
      protected BufferedLine generateRandomShape(Point nearP) {
        Rectangle nearR = randomRectangle(nearP);
        ArrayList<Point> corners = getQuadrantCorners(nearR);
        int r4 = randomInt(3);//0..3
        Point pA = corners.get(r4);
        Point pB = corners.get((r4 + 2) % 4);
        double maxBuf = Math.max(nearR.getWidth(), nearR.getHeight());
        double buf = Math.abs(randomGaussian()) * maxBuf / 4;
        buf = randomInt((int) divisible(buf));
        return new BufferedLine(pA, pB, buf, ctx);
      }

      protected Point randomPointInEmptyShape(BufferedLine shape) {
        int r = randomInt(1);
        if (r == 0) return shape.getA();
        //if (r == 1)
        return shape.getB();
//        Point c = shape.getCenter();
//        if (shape.contains(c));
      }
    }.testRelateWithRectangle();
  }

  /**
   * Creates a new BufferedLine with given coordinates and buffer.
   *
   * @param x1  X coordinate of the first point.
   * @param y1  Y coordinate of the first point.
   * @param x2  X coordinate of the second point.
   * @param y2  Y coordinate of the second point.
   * @param buf The buffer distance.
   * @return A new BufferedLine object.
   */
  private BufferedLine newBufLine(int x1, int y1, int x2, int y2, int buf) {
    Point pA = ctx.makePoint(x1, y1);
    Point pB = ctx.makePoint(x2, y2);
    if (randomBoolean()) {
      return new BufferedLine(pB, pA, buf, ctx);
    } else {
      return new BufferedLine(pA, pB, buf, ctx);
    }
  }
}