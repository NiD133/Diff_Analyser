/*******************************************************************************
 * Copyright (c) 2015 Voyager Search and MITRE
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0 which
 * accompanies this distribution and is available at
 *    http://www.apache.org/licenses/LICENSE-2.0.txt
 ******************************************************************************/

package org.locationtech.spatial4j.io;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link GeohashUtils}
 */
public class TestGeohashUtils {

  // Spatial context for geographical calculations
  private final SpatialContext geoContext = SpatialContext.GEO;

  /**
   * Test encoding of latitude and longitude into geohash strings.
   * Expected geohash values are based on known correct values.
   */
  @Test
  public void testEncodeLatLonToGeohash() {
    // Test case 1: Coordinates (42.6, -5.6)
    String geohash1 = GeohashUtils.encodeLatLon(42.6, -5.6);
    assertEquals("ezs42e44yx96", geohash1);

    // Test case 2: Coordinates (57.64911, 10.40744)
    String geohash2 = GeohashUtils.encodeLatLon(57.64911, 10.40744);
    assertEquals("u4pruydqqvj8", geohash2);
  }

  /**
   * Test decoding of geohash to latitude and longitude with high precision.
   * The decoded values should be within a small delta of the original values.
   */
  @Test
  public void testDecodeGeohashToPreciseLatLon() {
    // Known coordinates
    double originalLat = 52.3738007;
    double originalLon = 4.8909347;

    // Encode and decode
    String geohash = GeohashUtils.encodeLatLon(originalLat, originalLon);
    Point decodedPoint = GeohashUtils.decode(geohash, geoContext);

    // Assert decoded values are close to original
    assertEquals(originalLat, decodedPoint.getY(), 0.00001);
    assertEquals(originalLon, decodedPoint.getX(), 0.00001);
  }

  /**
   * Test decoding of geohash to latitude and longitude with less precision.
   * The decoded values should be within a small delta of the original values.
   */
  @Test
  public void testDecodeGeohashToImpreciseLatLon() {
    // Known coordinates
    double originalLat = 84.6;
    double originalLon = 10.5;

    // Encode and decode
    String geohash = GeohashUtils.encodeLatLon(originalLat, originalLon);
    Point decodedPoint = GeohashUtils.decode(geohash, geoContext);

    // Assert decoded values are close to original
    assertEquals(originalLat, decodedPoint.getY(), 0.00001);
    assertEquals(originalLon, decodedPoint.getX(), 0.00001);
  }

  /**
   * Test encoding and decoding consistency.
   * The geohash should encode and decode back to the original values.
   */
  @Test
  public void testEncodeDecodeConsistency() {
    // Known geohash and coordinates
    String initialGeohash = "u173zq37x014";
    double originalLat = 52.3738007;
    double originalLon = 4.8909347;

    // Encode and decode
    assertEquals(initialGeohash, GeohashUtils.encodeLatLon(originalLat, originalLon));
    Point decodedPoint = GeohashUtils.decode(initialGeohash, geoContext);

    // Assert decoded values are close to original
    assertEquals(52.37380061, decodedPoint.getY(), 0.000001);
    assertEquals(4.8909343, decodedPoint.getX(), 0.000001);

    // Re-encode and check consistency
    assertEquals(initialGeohash, GeohashUtils.encodeLatLon(decodedPoint.getY(), decodedPoint.getX()));

    // Test with a shorter geohash
    String shortGeohash = "u173";
    Point shortDecodedPoint = GeohashUtils.decode(shortGeohash, geoContext);
    String reEncodedGeohash = GeohashUtils.encodeLatLon(shortDecodedPoint.getY(), shortDecodedPoint.getX());
    Point reDecodedPoint = GeohashUtils.decode(reEncodedGeohash, geoContext);

    // Assert re-decoded values are consistent
    assertEquals(shortDecodedPoint.getY(), reDecodedPoint.getY(), 0.000001);
    assertEquals(shortDecodedPoint.getX(), reDecodedPoint.getX(), 0.000001);
  }

  /**
   * Test conversion from geohash length to latitude and longitude width.
   * Expected values are based on known correct values from reference tables.
   */
  @Test
  public void testGeohashLengthToWidthHeight() {
    // Test for odd length geohash
    double[] oddLengthBox = GeohashUtils.lookupDegreesSizeForHashLen(3);
    assertEquals(1.40625, oddLengthBox[0], 0.0001);
    assertEquals(1.40625, oddLengthBox[1], 0.0001);

    // Test for even length geohash
    double[] evenLengthBox = GeohashUtils.lookupDegreesSizeForHashLen(4);
    assertEquals(0.1757, evenLengthBox[0], 0.0001);
    assertEquals(0.3515, evenLengthBox[1], 0.0001);
  }

  /**
   * Test lookup of geohash length for given width and height.
   * Expected values are based on known correct values from reference tables.
   */
  @Test
  public void testLookupGeohashLengthForWidthHeight() {
    // Test various width and height combinations
    assertEquals(1, GeohashUtils.lookupHashLenForWidthHeight(999, 999));
    assertEquals(1, GeohashUtils.lookupHashLenForWidthHeight(999, 46));
    assertEquals(1, GeohashUtils.lookupHashLenForWidthHeight(46, 999));

    assertEquals(2, GeohashUtils.lookupHashLenForWidthHeight(44, 999));
    assertEquals(2, GeohashUtils.lookupHashLenForWidthHeight(999, 44));
    assertEquals(2, GeohashUtils.lookupHashLenForWidthHeight(999, 5.7));
    assertEquals(2, GeohashUtils.lookupHashLenForWidthHeight(11.3, 999));

    assertEquals(3, GeohashUtils.lookupHashLenForWidthHeight(999, 5.5));
    assertEquals(3, GeohashUtils.lookupHashLenForWidthHeight(11.1, 999));

    // Test for very small width and height
    assertEquals(GeohashUtils.MAX_PRECISION, GeohashUtils.lookupHashLenForWidthHeight(10e-20, 10e-20));
  }
}