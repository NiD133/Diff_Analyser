package org.locationtech.spatial4j.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;

import java.util.HashMap;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class GeohashUtils_ESTest extends GeohashUtils_ESTest_scaffolding {

    private static final SpatialContext GEO_CONTEXT = SpatialContext.GEO;
    private static final String INVALID_GEOHASH = "R9ENOYUZj]oNX(A";
    private static final String EMPTY_STRING = "";
    private static final double LATITUDE_13 = 13.0;
    private static final double LONGITUDE_13 = 13.0;
    private static final double LATITUDE_24 = 24.0;
    private static final double INVALID_LATITUDE = -1422.830305;
    private static final double INVALID_LONGITUDE = 803.7685944;
    private static final double PRECISION_0 = 0;
    private static final int INVALID_PRECISION = -3030;

    @Test(timeout = 4000)
    public void testDecodeWithInvalidGeohashThrowsException() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        SpatialContext spatialContext = SpatialContextFactory.makeSpatialContext(config, classLoader);

        try {
            GeohashUtils.decode(INVALID_GEOHASH, spatialContext);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.locationtech.spatial4j.io.GeohashUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testEncodeLatLonWithZeroPrecision() throws Throwable {
        GeohashUtils.encodeLatLon(LATITUDE_13, LONGITUDE_13, PRECISION_0);
    }

    @Test(timeout = 4000)
    public void testDecodeBoundaryWithSingleCharacterGeohash() throws Throwable {
        GeohashUtils.decodeBoundary("d", GEO_CONTEXT);
    }

    @Test(timeout = 4000)
    public void testDecodeBoundaryWithValidGeohash() throws Throwable {
        GeohashUtils.decodeBoundary("eurbxcpfpurb", GEO_CONTEXT);
    }

    @Test(timeout = 4000)
    public void testDecodeBoundaryWithAnotherValidGeohash() throws Throwable {
        GeohashUtils.decodeBoundary("8h2081040h20", GEO_CONTEXT);
    }

    @Test(timeout = 4000)
    public void testDecodeBoundaryWithLongGeohash() throws Throwable {
        GeohashUtils.decodeBoundary("pbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbp", GEO_CONTEXT);
    }

    @Test(timeout = 4000)
    public void testLookupDegreesSizeForNegativeHashLenThrowsException() throws Throwable {
        try {
            GeohashUtils.lookupDegreesSizeForHashLen(-482);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.locationtech.spatial4j.io.GeohashUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testEncodeLatLonWithLargePrecision() throws Throwable {
        GeohashUtils.encodeLatLon(740.519, 740.519, 11520);
    }

    @Test(timeout = 4000)
    public void testEncodeLatLonWithNegativePrecisionThrowsException() throws Throwable {
        try {
            GeohashUtils.encodeLatLon(0.017453292519943295, 0.017453292519943295, INVALID_PRECISION);
            fail("Expecting exception: NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            verifyException("java.lang.AbstractStringBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testEncodeLatLonWithVariousValues() throws Throwable {
        GeohashUtils.encodeLatLon(LATITUDE_24, -1258.5428078205061);
        GeohashUtils.encodeLatLon(INVALID_LATITUDE, INVALID_LONGITUDE);
        GeohashUtils.encodeLatLon(LATITUDE_24, Double.NEGATIVE_INFINITY, 1970);
        GeohashUtils.encodeLatLon(-1.0, -1258.5428078205061);
    }

    @Test(timeout = 4000)
    public void testEncodeAndDecodeBoundaryWithHighPrecision() throws Throwable {
        String geohash = GeohashUtils.encodeLatLon(2517.9302048088, 2517.9302048088, 1701);
        GeohashUtils.decodeBoundary(geohash, GEO_CONTEXT);
    }

    @Test(timeout = 4000)
    public void testDecodeBoundaryWithNullContextThrowsException() throws Throwable {
        try {
            GeohashUtils.decodeBoundary(EMPTY_STRING, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.locationtech.spatial4j.io.GeohashUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testEncodeAndDecodeWithNullContext() throws Throwable {
        String geohash = GeohashUtils.encodeLatLon(3859.99041074114, 3859.99041074114, 1773);
        GeohashUtils.decode(geohash, null);
    }

    @Test(timeout = 4000)
    public void testDecodeWithNullContextThrowsException() throws Throwable {
        try {
            GeohashUtils.decode("A0D", null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.locationtech.spatial4j.io.GeohashUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testEncodeLatLonWithSpecificPrecision() throws Throwable {
        String geohash = GeohashUtils.encodeLatLon(1.0, 3.4332275390625E-4, 31);
        assertEquals("s00j8n01rvxbrgrupfzgpbxzpbpbp00", geohash);
    }

    @Test(timeout = 4000)
    public void testLookupHashLenForWidthHeight() throws Throwable {
        int hashLen = GeohashUtils.lookupHashLenForWidthHeight(1.0728836059570312E-5, 1115.07072940934);
        assertEquals(11, hashLen);
    }

    @Test(timeout = 4000)
    public void testLookupHashLenForSmallWidthHeight() throws Throwable {
        int hashLen = GeohashUtils.lookupHashLenForWidthHeight(1.2490009027033011E-15, 1.2490009027033011E-15);
        assertEquals(24, hashLen);
    }

    @Test(timeout = 4000)
    public void testGetSubGeohashesWithEmptyString() throws Throwable {
        String[] subGeohashes = GeohashUtils.getSubGeohashes(EMPTY_STRING);
        assertEquals(32, subGeohashes.length);
    }

    @Test(timeout = 4000)
    public void testDecodeBoundaryWithValidGeohashAndContext() throws Throwable {
        Rectangle rectangle = GeohashUtils.decodeBoundary("kpbpbpbpbpbp", GEO_CONTEXT);
        assertEquals(3.3527612686157227E-7, rectangle.getMaxX(), 0.01);
        assertEquals(0.0, rectangle.getMinX(), 0.01);
        assertEquals(-1.6763806343078613E-7, rectangle.getMinY(), 0.01);
    }

    @Test(timeout = 4000)
    public void testDecodeBoundaryWithInvalidGeohashThrowsException() throws Throwable {
        try {
            GeohashUtils.decodeBoundary("J-", GEO_CONTEXT);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.locationtech.spatial4j.io.GeohashUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testLookupDegreesSizeForHashLen() throws Throwable {
        double[] degreesSize = GeohashUtils.lookupDegreesSizeForHashLen(16);
        assertArrayEquals(new double[]{1.6370904631912708E-10, 3.2741809263825417E-10}, degreesSize, 0.01);
    }

    @Test(timeout = 4000)
    public void testDecodeWithValidGeohashAndContext() throws Throwable {
        Point point = GeohashUtils.decode("h0pb421bn842p8h85bj0hbp000000000000000000000000000000000000000000000000000000000000000000000000000000000", GEO_CONTEXT);
        assertEquals(11.0, point.getX(), 0.01);
        assertEquals(-90.0, point.getLat(), 0.01);
    }
}