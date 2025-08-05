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
    
    private static final SpatialContext SPATIAL_CONTEXT = SpatialContext.GEO;
    private static final double COORDINATE_PRECISION_TOLERANCE = 0.00001D;
    private static final double HIGH_PRECISION_TOLERANCE = 0.000001D;

    @Test
    public void testEncodeLatLon_WithKnownCoordinates_ReturnsExpectedGeohash() {
        // Test case 1: Coordinates in Spain
        double spanishLat = 42.6;
        double spanishLon = -5.6;
        String expectedSpanishGeohash = "ezs42e44yx96";
        
        String actualSpanishGeohash = GeohashUtils.encodeLatLon(spanishLat, spanishLon);
        
        assertEquals("Spanish coordinates should encode to expected geohash", 
                    expectedSpanishGeohash, actualSpanishGeohash);

        // Test case 2: Coordinates in Denmark
        double danishLat = 57.64911;
        double danishLon = 10.40744;
        String expectedDanishGeohash = "u4pruydqqvj8";
        
        String actualDanishGeohash = GeohashUtils.encodeLatLon(danishLat, danishLon);
        
        assertEquals("Danish coordinates should encode to expected geohash", 
                    expectedDanishGeohash, actualDanishGeohash);
    }

    @Test
    public void testDecodeGeohash_WithPreciseCoordinates_ReturnsOriginalCoordinatesWithinTolerance() {
        // Amsterdam coordinates with high precision
        double originalLat = 52.3738007;
        double originalLon = 4.8909347;
        
        String geohash = GeohashUtils.encodeLatLon(originalLat, originalLon);
        Point decodedPoint = GeohashUtils.decode(geohash, SPATIAL_CONTEXT);

        assertEquals("Decoded latitude should match original within tolerance", 
                    originalLat, decodedPoint.getY(), COORDINATE_PRECISION_TOLERANCE);
        assertEquals("Decoded longitude should match original within tolerance", 
                    originalLon, decodedPoint.getX(), COORDINATE_PRECISION_TOLERANCE);
    }

    @Test
    public void testDecodeGeohash_WithSimpleCoordinates_ReturnsOriginalCoordinatesWithinTolerance() {
        // High latitude coordinates with simple decimal values
        double originalLat = 84.6;
        double originalLon = 10.5;
        
        String geohash = GeohashUtils.encodeLatLon(originalLat, originalLon);
        Point decodedPoint = GeohashUtils.decode(geohash, SPATIAL_CONTEXT);

        assertEquals("Decoded latitude should match original within tolerance", 
                    originalLat, decodedPoint.getY(), COORDINATE_PRECISION_TOLERANCE);
        assertEquals("Decoded longitude should match original within tolerance", 
                    originalLon, decodedPoint.getX(), COORDINATE_PRECISION_TOLERANCE);
    }

    /**
     * Tests round-trip encoding/decoding consistency.
     * See https://issues.apache.org/jira/browse/LUCENE-1815 for details
     */
    @Test
    public void testEncodeDecodeRoundTrip_MaintainsConsistency() {
        // Test with known Amsterdam coordinates and expected geohash
        String expectedGeohash = "u173zq37x014";
        double originalLat = 52.3738007;
        double originalLon = 4.8909347;
        
        // Verify encoding produces expected geohash
        String actualGeohash = GeohashUtils.encodeLatLon(originalLat, originalLon);
        assertEquals("Encoding should produce expected geohash", expectedGeohash, actualGeohash);
        
        // Verify decoding produces expected coordinates
        Point decodedPoint = GeohashUtils.decode(expectedGeohash, SPATIAL_CONTEXT);
        assertEquals("Decoded latitude should match expected value", 
                    52.37380061d, decodedPoint.getY(), HIGH_PRECISION_TOLERANCE);
        assertEquals("Decoded longitude should match expected value", 
                    4.8909343d, decodedPoint.getX(), HIGH_PRECISION_TOLERANCE);

        // Verify re-encoding the decoded point produces the same geohash
        String reEncodedGeohash = GeohashUtils.encodeLatLon(decodedPoint.getY(), decodedPoint.getX());
        assertEquals("Re-encoding decoded coordinates should produce same geohash", 
                    expectedGeohash, reEncodedGeohash);

        // Test with shorter geohash for consistency
        testRoundTripConsistencyWithShorterGeohash();
    }
    
    private void testRoundTripConsistencyWithShorterGeohash() {
        String shortGeohash = "u173";
        Point firstDecoding = GeohashUtils.decode(shortGeohash, SPATIAL_CONTEXT);
        String reEncodedGeohash = GeohashUtils.encodeLatLon(firstDecoding.getY(), firstDecoding.getX());
        Point secondDecoding = GeohashUtils.decode(reEncodedGeohash, SPATIAL_CONTEXT);
        
        assertEquals("Multiple decode/encode cycles should maintain coordinate consistency", 
                    firstDecoding.getY(), secondDecoding.getY(), HIGH_PRECISION_TOLERANCE);
        assertEquals("Multiple decode/encode cycles should maintain coordinate consistency", 
                    firstDecoding.getX(), secondDecoding.getX(), HIGH_PRECISION_TOLERANCE);
    }

    /**
     * Tests geohash length to geographic area size mapping.
     * Reference: http://en.wikipedia.org/wiki/Geohash
     */
    @Test
    public void testLookupDegreesSizeForHashLen_ReturnsCorrectDimensions() {
        // Test odd hash length (3 characters)
        int oddHashLength = 3;
        double expectedOddDimension = 1.40625;
        
        double[] oddHashDimensions = GeohashUtils.lookupDegreesSizeForHashLen(oddHashLength);
        
        assertEquals("Odd hash length should have equal width and height", 
                    expectedOddDimension, oddHashDimensions[0], 0.0001);
        assertEquals("Odd hash length should have equal width and height", 
                    expectedOddDimension, oddHashDimensions[1], 0.0001);

        // Test even hash length (4 characters)
        int evenHashLength = 4;
        double expectedEvenWidth = 0.1757;
        double expectedEvenHeight = 0.3515;
        
        double[] evenHashDimensions = GeohashUtils.lookupDegreesSizeForHashLen(evenHashLength);
        
        assertEquals("Even hash length should have specific width", 
                    expectedEvenWidth, evenHashDimensions[0], 0.0001);
        assertEquals("Even hash length should have specific height", 
                    expectedEvenHeight, evenHashDimensions[1], 0.0001);
    }

    /**
     * Tests hash length calculation based on required geographic precision.
     * Reference: http://en.wikipedia.org/wiki/Geohash
     */
    @Test
    public void testLookupHashLenForWidthHeight_ReturnsAppropriateHashLength() {
        // Very large areas should require minimal precision (hash length 1)
        assertEquals("Very large dimensions should require hash length 1", 
                    1, GeohashUtils.lookupHashLenForWidthHeight(999, 999));
        assertEquals("Large width, medium height should require hash length 1", 
                    1, GeohashUtils.lookupHashLenForWidthHeight(999, 46));
        assertEquals("Medium width, large height should require hash length 1", 
                    1, GeohashUtils.lookupHashLenForWidthHeight(46, 999));

        // Medium areas should require hash length 2
        assertEquals("Medium-large dimensions should require hash length 2", 
                    2, GeohashUtils.lookupHashLenForWidthHeight(44, 999));
        assertEquals("Large width, medium height should require hash length 2", 
                    2, GeohashUtils.lookupHashLenForWidthHeight(999, 44));
        assertEquals("Large width, small height should require hash length 2", 
                    2, GeohashUtils.lookupHashLenForWidthHeight(999, 5.7));
        assertEquals("Medium width, large height should require hash length 2", 
                    2, GeohashUtils.lookupHashLenForWidthHeight(11.3, 999));

        // Smaller areas should require hash length 3
        assertEquals("Large width, smaller height should require hash length 3", 
                    3, GeohashUtils.lookupHashLenForWidthHeight(999, 5.5));
        assertEquals("Smaller width, large height should require hash length 3", 
                    3, GeohashUtils.lookupHashLenForWidthHeight(11.1, 999));

        // Extremely small areas should require maximum precision
        assertEquals("Extremely small dimensions should require maximum precision", 
                    GeohashUtils.MAX_PRECISION, GeohashUtils.lookupHashLenForWidthHeight(10e-20, 10e-20));
    }
}