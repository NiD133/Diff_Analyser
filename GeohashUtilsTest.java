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
 * Tests for {@link GeohashUtils}
 */
public class TestGeohashUtils {
    private static final SpatialContext CTX = SpatialContext.GEO;
    private static final double PRECISION = 0.00001;
    private static final double HIGH_PRECISION = 0.000001;

    // Test points with known geohashes from geohash.org
    private static final double[] TEST_POINT_1 = {42.6, -5.6};
    private static final String GEOHASH_1 = "ezs42e44yx96";
    private static final double[] TEST_POINT_2 = {57.64911, 10.40744};
    private static final String GEOHASH_2 = "u4pruydqqvj8";

    @Test
    public void encodeLatLon_returnsCorrectGeohashForKnownLocation1() {
        String hash = GeohashUtils.encodeLatLon(TEST_POINT_1[0], TEST_POINT_1[1]);
        assertEquals(GEOHASH_1, hash);
    }

    @Test
    public void encodeLatLon_returnsCorrectGeohashForKnownLocation2() {
        String hash = GeohashUtils.encodeLatLon(TEST_POINT_2[0], TEST_POINT_2[1]);
        assertEquals(GEOHASH_2, hash);
    }

    @Test
    public void decode_returnsOriginalPointWithinPrecisionForStandardLocation() {
        // Test with coordinates from Amsterdam
        double latitude = 52.3738007;
        double longitude = 4.8909347;
        
        String hash = GeohashUtils.encodeLatLon(latitude, longitude);
        Point point = GeohashUtils.decode(hash, CTX);

        assertEquals("Latitude should match original", latitude, point.getY(), PRECISION);
        assertEquals("Longitude should match original", longitude, point.getX(), PRECISION);
    }

    @Test
    public void decode_returnsOriginalPointWithinPrecisionForPolarLocation() {
        // Test with coordinates near North Pole
        double latitude = 84.6;
        double longitude = 10.5;
        
        String hash = GeohashUtils.encodeLatLon(latitude, longitude);
        Point point = GeohashUtils.decode(hash, CTX);

        assertEquals("Latitude should match original", latitude, point.getY(), PRECISION);
        assertEquals("Longitude should match original", longitude, point.getX(), PRECISION);
    }

    @Test
    public void encodeThenDecode_returnsConsistentResultsForFullPrecisionHash() {
        // Known geohash for Amsterdam coordinates
        String expectedGeohash = "u173zq37x014";
        double expectedLat = 52.3738007;
        double expectedLon = 4.8909347;
        
        // Verify encoding
        String actualGeohash = GeohashUtils.encodeLatLon(expectedLat, expectedLon);
        assertEquals(expectedGeohash, actualGeohash);
        
        // Verify decoding
        Point point = GeohashUtils.decode(expectedGeohash, CTX);
        assertEquals("Latitude should be consistent", 52.37380061, point.getY(), HIGH_PRECISION);
        assertEquals("Longitude should be consistent", 4.8909343, point.getX(), HIGH_PRECISION);
        
        // Verify round trip
        String roundTripGeohash = GeohashUtils.encodeLatLon(point.getY(), point.getX());
        assertEquals(expectedGeohash, roundTripGeohash);
    }

    @Test
    public void encodeThenDecode_returnsConsistentResultsForShortHash() {
        // Test with 4-character geohash
        String geohash = "u173";
        
        Point point = GeohashUtils.decode(geohash, CTX);
        String roundTripGeohash = GeohashUtils.encodeLatLon(point.getY(), point.getX());
        
        // Verify round trip consistency
        Point roundTripPoint = GeohashUtils.decode(roundTripGeohash, CTX);
        assertEquals("Latitude should be consistent", point.getY(), roundTripPoint.getY(), HIGH_PRECISION);
        assertEquals("Longitude should be consistent", point.getX(), roundTripPoint.getX(), HIGH_PRECISION);
    }

    @Test
    public void lookupDegreesSizeForHashLen_returnsCorrectDimensions() {
        // Verify Wikipedia table values for hash lengths
        double[] boxOdd = GeohashUtils.lookupDegreesSizeForHashLen(3);
        assertEquals("Latitude error for odd length", 1.40625, boxOdd[0], 0.0001);
        assertEquals("Longitude error for odd length", 1.40625, boxOdd[1], 0.0001);
        
        double[] boxEven = GeohashUtils.lookupDegreesSizeForHashLen(4);
        assertEquals("Latitude error for even length", 0.1757, boxEven[0], 0.0001);
        assertEquals("Longitude error for even length", 0.3515, boxEven[1], 0.0001);
    }

    @Test
    public void lookupHashLenForWidthHeight_returnsCorrectLengths() {
        // Test cases from Wikipedia geohash precision table
        assertEquals(1, GeohashUtils.lookupHashLenForWidthHeight(999, 999));
        
        // Boundary cases for length=1
        assertEquals(1, GeohashUtils.lookupHashLenForWidthHeight(999, 46));
        assertEquals(1, GeohashUtils.lookupHashLenForWidthHeight(46, 999));
        
        // Boundary cases for length=2
        assertEquals(2, GeohashUtils.lookupHashLenForWidthHeight(44, 999));
        assertEquals(2, GeohashUtils.lookupHashLenForWidthHeight(999, 44));
        assertEquals(2, GeohashUtils.lookupHashLenForWidthHeight(999, 5.7));
        assertEquals(2, GeohashUtils.lookupHashLenForWidthHeight(11.3, 999));
        
        // Boundary cases for length=3
        assertEquals(3, GeohashUtils.lookupHashLenForWidthHeight(999, 5.5));
        assertEquals(3, GeohashUtils.lookupHashLenForWidthHeight(11.1, 999));
        
        // Minimum error case (maximum precision)
        assertEquals(GeohashUtils.MAX_PRECISION, 
                     GeohashUtils.lookupHashLenForWidthHeight(10e-20, 10e-20));
    }
}