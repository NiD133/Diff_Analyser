// Copyright (c) 2015 Voyager Search and MITRE
// Licensed under the Apache License, Version 2.0

package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for GeohashUtils.
 *
 * Notes on coordinate order:
 * - Spatial4j's Point uses getX() = longitude, getY() = latitude.
 * - Test helpers below take arguments in the conventional (latitude, longitude) order.
 */
public class GeohashUtilsTest {

  private static final SpatialContext GEO = SpatialContext.GEO;

  // Common numeric tolerances used in approximate comparisons
  private static final double EPS_5 = 1e-5;  // 0.00001
  private static final double EPS_6 = 1e-6;  // 0.000001
  private static final double WIDTH_HEIGHT_EPS = 1e-4;

  // Known geohash fixtures used across multiple tests
  private static final String GH_AMSTERDAM_PRECISE = "u173zq37x014";
  private static final double LAT_AMSTERDAM = 52.3738007;
  private static final double LON_AMSTERDAM = 4.8909347;

  @Test
  public void encodesKnownPoints() {
    assertEncodesTo(42.6, -5.6, "ezs42e44yx96");
    assertEncodesTo(57.64911, 10.40744, "u4pruydqqvj8");
  }

  @Test
  public void decodesToOriginal_preciseCoordinates() {
    // Given a precise coordinate pair (Amsterdam, NL)
    final String geohash = GeohashUtils.encodeLatLon(LAT_AMSTERDAM, LON_AMSTERDAM);

    // When we decode
    final Point decoded = GeohashUtils.decode(geohash, GEO);

    // Then latitude (Y) and longitude (X) match within tolerance
    assertEquals(LAT_AMSTERDAM, decoded.getY(), EPS_5);
    assertEquals(LON_AMSTERDAM, decoded.getX(), EPS_5);
  }

  @Test
  public void decodesToOriginal_impreciseCoordinatesNearPoles() {
    // Higher latitudes have coarser east-west precision
    final String geohash = GeohashUtils.encodeLatLon(84.6, 10.5);

    final Point decoded = GeohashUtils.decode(geohash, GEO);

    assertEquals(84.6, decoded.getY(), EPS_5);
    assertEquals(10.5, decoded.getX(), EPS_5);
  }

  @Test
  public void roundTrip_encodeDecode_preservesValuesWithinTolerance() {
    // Full-precision known hash for Amsterdam
    assertEquals(GH_AMSTERDAM_PRECISE, GeohashUtils.encodeLatLon(LAT_AMSTERDAM, LON_AMSTERDAM));

    final Point decoded = GeohashUtils.decode(GH_AMSTERDAM_PRECISE, GEO);
    assertEquals(52.37380061d, decoded.getY(), EPS_6);
    assertEquals(4.8909343d, decoded.getX(), EPS_6);

    // Re-encoding the decoded point should give the original geohash
    assertEquals(GH_AMSTERDAM_PRECISE, GeohashUtils.encodeLatLon(decoded.getY(), decoded.getX()));
  }

  @Test
  public void roundTrip_withShortHash_isStable() {
    // For a short hash, decode -> encode -> decode should stabilize the point
    final String shortHash = "u173";
    final Point decoded1 = GeohashUtils.decode(shortHash, GEO);
    final String reencoded = GeohashUtils.encodeLatLon(decoded1.getY(), decoded1.getX());
    final Point decoded2 = GeohashUtils.decode(reencoded, GEO);

    assertEquals(decoded1.getY(), decoded2.getY(), EPS_6);
    assertEquals(decoded1.getX(), decoded2.getX(), EPS_6);
  }

  /**
   * Values verified against the table at:
   * https://en.wikipedia.org/wiki/Geohash
   */
  @Test
  public void lookupDegreesSizeForHashLen_matchesWikipediaTable() {
    // Odd-length geohash: equal width and height
    final double[] len3 = GeohashUtils.lookupDegreesSizeForHashLen(3);
    assertEquals(1.40625, len3[0], WIDTH_HEIGHT_EPS); // lon width
    assertEquals(1.40625, len3[1], WIDTH_HEIGHT_EPS); // lat height

    // Even-length geohash: rectangular cells
    final double[] len4 = GeohashUtils.lookupDegreesSizeForHashLen(4);
    assertEquals(0.1757, len4[0], WIDTH_HEIGHT_EPS); // lon width
    assertEquals(0.3515, len4[1], WIDTH_HEIGHT_EPS); // lat height
  }

  /**
   * Verifies that the API returns the shortest geohash length that achieves the requested
   * width/height (lon/lat) error bounds. Reference values align with Wikipedia’s table.
   */
  @Test
  public void lookupHashLenForWidthHeight_returnsShortestLength() {
    // Very large areas -> shortest hash
    assertEquals(1, GeohashUtils.lookupHashLenForWidthHeight(999, 999));

    // One dimension large, the other moderate -> still length 1
    assertEquals(1, GeohashUtils.lookupHashLenForWidthHeight(999, 46));
    assertEquals(1, GeohashUtils.lookupHashLenForWidthHeight(46, 999));

    // Tighten one dimension -> length increases accordingly
    assertEquals(2, GeohashUtils.lookupHashLenForWidthHeight(44, 999));
    assertEquals(2, GeohashUtils.lookupHashLenForWidthHeight(999, 44));
    assertEquals(2, GeohashUtils.lookupHashLenForWidthHeight(999, 5.7));
    assertEquals(2, GeohashUtils.lookupHashLenForWidthHeight(11.3, 999));

    // Slightly tighter still -> length 3
    assertEquals(3, GeohashUtils.lookupHashLenForWidthHeight(999, 5.5));
    assertEquals(3, GeohashUtils.lookupHashLenForWidthHeight(11.1, 999));

    // Extremely small bounds -> capped at MAX_PRECISION
    assertEquals(GeohashUtils.MAX_PRECISION,
        GeohashUtils.lookupHashLenForWidthHeight(10e-20, 10e-20));
  }

  // ------------------------
  // Helper assertions
  // ------------------------

  private static void assertEncodesTo(double lat, double lon, String expectedGeohash) {
    final String actual = GeohashUtils.encodeLatLon(lat, lon);
    assertEquals("Unexpected geohash for lat=" + lat + ", lon=" + lon, expectedGeohash, actual);
  }
}