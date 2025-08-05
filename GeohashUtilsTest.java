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
 * Tests for {@link GeohashUtils}.
 */
public class TestGeohashUtils {

  private final SpatialContext ctx = SpatialContext.GEO;
  private static final double GEO_PRECISION_DELTA = 0.00001D;

  /**
   * Tests encoding against known geohash values from external examples.
   */
  @Test
  public void testEncodeLatLon_withKnownValues() {
    // Test case 1: From original test suite
    String expectedGeohash1 = "ezs42e44yx96";
    String actualGeohash1 = GeohashUtils.encodeLatLon(42.6, -5.6);
    assertEquals(expectedGeohash1, actualGeohash1);

    // Test case 2: From original test suite
    String expectedGeohash2 = "u4pruydqqvj8";
    String actualGeohash2 = GeohashUtils.encodeLatLon(57.64911, 10.40744);
    assertEquals(expectedGeohash2, actualGeohash2);
  }

  /**
   * Tests that encoding a lat/lon and then decoding the resulting geohash
   * yields a point that is very close to the original coordinates.
   */
  @Test
  public void testEncodeThenDecode_isNearlyReversible() {
    // Arrange: A coordinate pair with high precision
    double originalLat1 = 52.3738007;
    double originalLon1 = 4.8909347;

    // Act
    String geohash1 = GeohashUtils.encodeLatLon(originalLat1, originalLon1);
    Point decodedPoint1 = GeohashUtils.decode(geohash1, ctx);

    // Assert
    assertEquals(originalLat1, decodedPoint1.getY(), GEO_PRECISION_DELTA);
    assertEquals(originalLon1, decodedPoint1.getX(), GEO_PRECISION_DELTA);

    // Arrange: A coordinate pair with lower precision
    double originalLat2 = 84.6;
    double originalLon2 = 10.5;

    // Act
    String geohash2 = GeohashUtils.encodeLatLon(originalLat2, originalLon2);
    Point decodedPoint2 = GeohashUtils.decode(geohash2, ctx);

    // Assert
    assertEquals(originalLat2, decodedPoint2.getY(), GEO_PRECISION_DELTA);
    assertEquals(originalLon2, decodedPoint2.getX(), GEO_PRECISION_DELTA);
  }

  /**
   * This test is based on a specific issue (LUCENE-1815) and verifies a complete
   * decode-and-encode round trip for a known complex geohash. It ensures that
   * decoding a geohash and then re-encoding the resulting point yields the original geohash.
   *
   * @see <a href="https://issues.apache.org/jira/browse/LUCENE-1815">LUCENE-1815</a>
   */
  @Test
  public void testDecodeAndEncodeRoundTrip_fromLuceneIssue() {
    // Arrange
    String originalGeohash = "u173zq37x014";

    // Act: Decode the geohash to a point
    Point decodedPoint = GeohashUtils.decode(originalGeohash, ctx);

    // Assert: Check that the decoded point has the expected coordinates (with precision loss)
    assertEquals(52.37380061d, decodedPoint.getY(), 0.000001d);
    assertEquals(4.8909343d, decodedPoint.getX(), 0.000001d);

    // Act: Re-encode the decoded point
    String reEncodedGeohash = GeohashUtils.encodeLatLon(decodedPoint.getY(), decodedPoint.getX());

    // Assert: The re-encoded geohash should match the original
    assertEquals(originalGeohash, reEncodedGeohash);
  }

  /**
   * Verifies that the geohash process is stable. Decoding a short geohash,
   * re-encoding it to full precision, and then decoding it again should result
   * in the same point as the initial decode. This confirms that adding precision
   * doesn't shift the center point of the original, less precise geohash.
   */
  @Test
  public void testDecodeEncodeDecode_isStableForShortHashes() {
    // Arrange: A short geohash
    String shortGeohash = "u173";

    // Act
    Point pointFromShortHash = GeohashUtils.decode(shortGeohash, ctx);
    String fullPrecisionGeohash = GeohashUtils.encodeLatLon(pointFromShortHash.getY(), pointFromShortHash.getX());
    Point pointFromFullHash = GeohashUtils.decode(fullPrecisionGeohash, ctx);

    // Assert: The two points should be effectively identical
    assertEquals(pointFromShortHash.getY(), pointFromFullHash.getY(), 0.000001d);
    assertEquals(pointFromShortHash.getX(), pointFromFullHash.getX(), 0.000001d);
  }

  /**
   * Verifies that the calculated bounding box dimensions for a geohash of a given
   * length match the expected values from the table on Wikipedia.
   *
   * @see <a href="http://en.wikipedia.org/wiki/Geohash">Geohash on Wikipedia</a>
   */
  @Test
  public void testLookupDegreesSizeForHashLen_fromWikipediaExamples() {
    // Arrange: Test with an odd length (3)
    int oddLength = 3;
    double[] dimensionsForOddLength = GeohashUtils.lookupDegreesSizeForHashLen(oddLength);
    // Per spec, for odd lengths, height and width error are the same.
    assertEquals(1.40625, dimensionsForOddLength[0], 0.0001); // [0] is latitude degrees
    assertEquals(1.40625, dimensionsForOddLength[1], 0.0001); // [1] is longitude degrees

    // Arrange: Test with an even length (4)
    int evenLength = 4;
    double[] dimensionsForEvenLength = GeohashUtils.lookupDegreesSizeForHashLen(evenLength);
    // Per spec, for even lengths, height and width error differ.
    assertEquals(0.1757, dimensionsForEvenLength[0], 0.0001); // [0] is latitude degrees
    assertEquals(0.3515, dimensionsForEvenLength[1], 0.0001); // [1] is longitude degrees
  }

  /**
   * Verifies that the correct geohash precision level (length) is returned
   * for various error dimensions (width/height).
   *
   * @see <a href="http://en.wikipedia.org/wiki/Geohash">Geohash on Wikipedia</a>
   */
  @Test
  public void testLookupHashLenForWidthHeight_shouldReturnCorrectPrecision() {
    // For very large error margins, the lowest precision (length 1) is sufficient.
    assertEquals(1, GeohashUtils.lookupHashLenForWidthHeight(999, 999));
    assertEquals(1, GeohashUtils.lookupHashLenForWidthHeight(999, 46));
    assertEquals(1, GeohashUtils.lookupHashLenForWidthHeight(46, 999));

    // Test boundary conditions where precision should increase.
    assertEquals(2, GeohashUtils.lookupHashLenForWidthHeight(999, 44));
    assertEquals(2, GeohashUtils.lookupHashLenForWidthHeight(44, 999));
    assertEquals(2, GeohashUtils.lookupHashLenForWidthHeight(999, 5.7));
    assertEquals(2, GeohashUtils.lookupHashLenForWidthHeight(11.3, 999));
    assertEquals(3, GeohashUtils.lookupHashLenForWidthHeight(999, 5.5));
    assertEquals(3, GeohashUtils.lookupHashLenForWidthHeight(11.1, 999));

    // For very small error margins, the maximum precision should be used.
    assertEquals(GeohashUtils.MAX_PRECISION, GeohashUtils.lookupHashLenForWidthHeight(10e-20, 10e-20));
  }
}