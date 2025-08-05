package org.locationtech.spatial4j.io;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;

/**
 * Test suite for GeohashUtils functionality including encoding, decoding,
 * boundary calculations, and error handling.
 */
public class GeohashUtilsTest {

    private static final SpatialContext GEO_CONTEXT = SpatialContext.GEO;
    private static final double DELTA = 0.01;

    // ========== Encoding Tests ==========

    @Test
    public void encodeLatLon_withValidCoordinates_returnsExpectedGeohash() {
        String geohash = GeohashUtils.encodeLatLon(1.0, 3.4332275390625E-4, 31);
        
        assertEquals("s00j8n01rvxbrgrupfzgpbxzpbpbp00", geohash);
    }

    @Test
    public void encodeLatLon_withZeroPrecision_completesSuccessfully() {
        // Should not throw exception even with zero precision
        GeohashUtils.encodeLatLon(13.0, 13.0, 0);
    }

    @Test
    public void encodeLatLon_withOutOfRangeCoordinates_handlesGracefully() {
        // Test with coordinates outside normal lat/lon ranges
        GeohashUtils.encodeLatLon(740.519, 740.519, 11520);
        GeohashUtils.encodeLatLon(24.0, -1258.5428078205061);
        GeohashUtils.encodeLatLon(-1422.830305, 803.7685944);
        GeohashUtils.encodeLatLon(24.0, -Double.MAX_VALUE, 1970);
        GeohashUtils.encodeLatLon(-1.0, -1258.5428078205061);
    }

    @Test(expected = NegativeArraySizeException.class)
    public void encodeLatLon_withNegativePrecision_throwsException() {
        GeohashUtils.encodeLatLon(0.017453292519943295, 0.017453292519943295, -3030);
    }

    // ========== Decoding Tests ==========

    @Test
    public void decode_withValidGeohash_returnsCorrectPoint() {
        String longGeohash = "h0pb421bn842p8h85bj0hbp000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        Point point = GeohashUtils.decode(longGeohash, GEO_CONTEXT);
        
        assertEquals(11.0, point.getX(), DELTA);
        assertEquals(-90.0, point.getLat(), DELTA);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void decode_withInvalidCharacters_throwsException() {
        GeohashUtils.decode("R9ENOYUZj]oNX(A", GEO_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void decode_withNullContext_throwsException() {
        GeohashUtils.decode("A0D", null);
    }

    @Test
    public void decode_withEncodedGeohash_handlesLargeValues() {
        // Test decoding a geohash that was encoded with large coordinate values
        String encodedGeohash = GeohashUtils.encodeLatLon(3859.99041074114, 3859.99041074114, 1773);
        GeohashUtils.decode(encodedGeohash, null); // This should complete without throwing
    }

    // ========== Boundary Decoding Tests ==========

    @Test
    public void decodeBoundary_withSingleCharacter_returnsValidRectangle() {
        Rectangle boundary = GeohashUtils.decodeBoundary("d", GEO_CONTEXT);
        assertNotNull(boundary);
    }

    @Test
    public void decodeBoundary_withMediumLengthGeohash_returnsValidRectangle() {
        Rectangle boundary = GeohashUtils.decodeBoundary("eurbxcpfpurb", GEO_CONTEXT);
        assertNotNull(boundary);
    }

    @Test
    public void decodeBoundary_withNumericGeohash_returnsValidRectangle() {
        Rectangle boundary = GeohashUtils.decodeBoundary("8h2081040h20", GEO_CONTEXT);
        assertNotNull(boundary);
    }

    @Test
    public void decodeBoundary_withLongRepeatingPattern_returnsValidRectangle() {
        String longGeohash = "pbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbp";
        Rectangle boundary = GeohashUtils.decodeBoundary(longGeohash, GEO_CONTEXT);
        assertNotNull(boundary);
    }

    @Test
    public void decodeBoundary_withSpecificPattern_returnsExpectedBounds() {
        Rectangle boundary = GeohashUtils.decodeBoundary("kpbpbpbpbpbp", GEO_CONTEXT);
        
        assertEquals(3.3527612686157227E-7, boundary.getMaxX(), DELTA);
        assertEquals(0.0, boundary.getMinX(), DELTA);
        assertEquals(-1.6763806343078613E-7, boundary.getMinY(), DELTA);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void decodeBoundary_withInvalidCharacters_throwsException() {
        GeohashUtils.decodeBoundary("J-", GEO_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void decodeBoundary_withNullContext_throwsException() {
        GeohashUtils.decodeBoundary("", null);
    }

    @Test
    public void decodeBoundary_withEncodedLargeCoordinates_handlesGracefully() {
        String encodedGeohash = GeohashUtils.encodeLatLon(2517.9302048088, 2517.9302048088, 1701);
        GeohashUtils.decodeBoundary(encodedGeohash, GEO_CONTEXT);
    }

    // ========== Utility Method Tests ==========

    @Test
    public void lookupDegreesSizeForHashLen_withValidLength_returnsExpectedArray() {
        double[] degrees = GeohashUtils.lookupDegreesSizeForHashLen(16);
        
        assertArrayEquals(new double[]{1.6370904631912708E-10, 3.2741809263825417E-10}, degrees, DELTA);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void lookupDegreesSizeForHashLen_withNegativeLength_throwsException() {
        GeohashUtils.lookupDegreesSizeForHashLen(-482);
    }

    @Test
    public void lookupHashLenForWidthHeight_withSmallDimensions_returnsAppropriateLength() {
        int hashLength = GeohashUtils.lookupHashLenForWidthHeight(1.0728836059570312E-5, 1115.07072940934);
        assertEquals(11, hashLength);
    }

    @Test
    public void lookupHashLenForWidthHeight_withVerySmallDimensions_returnsMaxPrecision() {
        int hashLength = GeohashUtils.lookupHashLenForWidthHeight(1.2490009027033011E-15, 1.2490009027033011E-15);
        assertEquals(24, hashLength);
    }

    @Test
    public void getSubGeohashes_withEmptyString_returns32Subhashes() {
        String[] subGeohashes = GeohashUtils.getSubGeohashes("");
        
        assertEquals(32, subGeohashes.length);
    }

    // ========== Helper Methods ==========

    private SpatialContext createCustomSpatialContext() {
        HashMap<String, String> config = new HashMap<>();
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        return SpatialContextFactory.makeSpatialContext(config, classLoader);
    }
}