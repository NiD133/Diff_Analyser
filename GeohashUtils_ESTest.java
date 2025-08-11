package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Readable tests for GeohashUtils.
 *
 * These tests focus on clear behavior-oriented checks using small, well-known examples.
 * They avoid brittle edge cases and generated values to keep maintenance simple.
 */
public class GeohashUtilsReadableTest {

  private static final SpatialContext GEO = SpatialContext.GEO;

  // The standard geohash Base32 alphabet used by most libraries (and by GeohashUtils).
  private static final String[] GEOHASH_ALPHABET = (
      "0123456789bcdefghjkmnpqrstuvwxyz"
  ).chars().mapToObj(c -> String.valueOf((char) c)).toArray(String[]::new);

  @Test
  public void encodeLatLon_withPrecision12_matchesCommonExample() {
    // Common reference point used in many geohash examples:
    // lat=42.6, lon=-5.6 -> "ezs42e44yx96" at precision 12
    String geohash = GeohashUtils.encodeLatLon(42.6, -5.6, 12);
    assertEquals("ezs42e44yx96", geohash);
  }

  @Test
  public void decode_singleChar_returnsCenterOfItsBoundary() {
    Rectangle boundary = GeohashUtils.decodeBoundary("s", GEO);
    Point center = GeohashUtils.decode("s", GEO);

    double expectedLon = (boundary.getMinX() + boundary.getMaxX()) / 2.0;
    double expectedLat = (boundary.getMinY() + boundary.getMaxY()) / 2.0;

    assertEquals(expectedLon, center.getX(), 1e-9);
    assertEquals(expectedLat, center.getY(), 1e-9);
  }

  @Test
  public void decodeBoundary_singleCharS_hasExpectedWorldQuadrant() {
    // The first-level geohash "s" covers the quadrant that includes (0,0),
    // with width 45 deg and height 45 deg in the standard geohash scheme.
    Rectangle r = GeohashUtils.decodeBoundary("s", GEO);

    assertEquals(0.0, r.getMinX(), 1e-12);  // lon min
    assertEquals(45.0, r.getMaxX(), 1e-12); // lon max
    assertEquals(0.0, r.getMinY(), 1e-12);  // lat min
    assertEquals(45.0, r.getMaxY(), 1e-12); // lat max
  }

  @Test
  public void getSubGeohashes_emptyBase_returnsAlphabetAsSingles() {
    String[] subs = GeohashUtils.getSubGeohashes("");
    assertEquals(32, subs.length);
    assertArrayEquals(GEOHASH_ALPHABET, subs);
  }

  @Test
  public void getSubGeohashes_nonEmptyBase_returns32SortedChildren() {
    String base = "u4pruyd";
    String[] subs = GeohashUtils.getSubGeohashes(base);

    // 32 children, each exactly base length + 1, all sharing the base prefix.
    assertEquals(32, subs.length);
    for (String s : subs) {
      assertTrue(s.startsWith(base));
      assertEquals(base.length() + 1, s.length());
    }

    // Children are sorted lexicographically.
    String[] sorted = subs.clone();
    Arrays.sort(sorted);
    assertArrayEquals(sorted, subs);
  }

  @Test
  public void lookupDegreesSizeForHashLen_decreasesWithExpectedPattern() {
    // According to GeohashUtils' internal construction, lat height and lon width
    // alternate divisors: starting from (180, 360), then /4 and /8 alternately.
    // We verify the progression for the first few lengths without hard-coding tiny values.
    double[] prev = GeohashUtils.lookupDegreesSizeForHashLen(0); // [180, 360]

    for (int len = 1; len <= 10; len++) {
      double[] curr = GeohashUtils.lookupDegreesSizeForHashLen(len);

      boolean odd = (len % 2 == 1);
      double expectedLat = prev[0] / (odd ? 4.0 : 8.0);
      double expectedLon = prev[1] / (odd ? 8.0 : 4.0);

      assertEquals("latHeight mismatch at len=" + len, expectedLat, curr[0], 1e-12);
      assertEquals("lonWidth mismatch at len=" + len, expectedLon, curr[1], 1e-12);

      prev = curr;
    }
  }

  @Test
  public void lookupHashLenForWidthHeight_roundTripsWithSizes() {
    // For each hash length, if we ask for a width/height equal to that cell size,
    // the shortest suitable length should be that same length.
    for (int len = 1; len <= GeohashUtils.MAX_PRECISION; len++) {
      double[] size = GeohashUtils.lookupDegreesSizeForHashLen(len);
      double lonWidth = size[1];
      double latHeight = size[0];

      int resolved = GeohashUtils.lookupHashLenForWidthHeight(lonWidth, latHeight);
      assertEquals("round-trip length mismatch at len=" + len, len, resolved);
    }
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void lookupDegreesSizeForHashLen_rejectsNegativeLength() {
    GeohashUtils.lookupDegreesSizeForHashLen(-1);
  }

  @Test(expected = NullPointerException.class)
  public void decode_throwsOnNullContext() {
    GeohashUtils.decode("s", null);
  }

  @Test
  public void encodeThenDecode_returnsPointNearOriginal() {
    // Pick a stable location and a moderate precision.
    double lat = 37.7749;   // San Francisco
    double lon = -122.4194;
    int precision = 10;

    String geohash = GeohashUtils.encodeLatLon(lat, lon, precision);
    Point decoded = GeohashUtils.decode(geohash, GEO);

    // The decoded point is the cell center; ensure it's reasonably close.
    double[] size = GeohashUtils.lookupDegreesSizeForHashLen(precision);
    double maxLatError = size[0] / 2.0 + 1e-12;
    double maxLonError = size[1] / 2.0 + 1e-12;

    assertEquals(lon, decoded.getX(), maxLonError);
    assertEquals(lat, decoded.getY(), maxLatError);
  }
}