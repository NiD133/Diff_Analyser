package org.locationtech.spatial4j.io;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GeohashUtilsTestTest1 {

    SpatialContext ctx = SpatialContext.GEO;

    /**
     * Pass condition: lat=42.6, lng=-5.6 should be encoded as "ezs42e44yx96",
     * lat=57.64911 lng=10.40744 should be encoded as "u4pruydqqvj8"
     */
    @Test
    public void testEncode() {
        String hash = GeohashUtils.encodeLatLon(42.6, -5.6);
        assertEquals("ezs42e44yx96", hash);
        hash = GeohashUtils.encodeLatLon(57.64911, 10.40744);
        assertEquals("u4pruydqqvj8", hash);
    }
}
