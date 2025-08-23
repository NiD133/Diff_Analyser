package org.locationtech.spatial4j.io;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GeohashUtilsTestTest6 {

    SpatialContext ctx = SpatialContext.GEO;

    /**
     * see the table at http://en.wikipedia.org/wiki/Geohash
     */
    @Test
    public void testLookupHashLenForWidthHeight() {
        assertEquals(1, GeohashUtils.lookupHashLenForWidthHeight(999, 999));
        assertEquals(1, GeohashUtils.lookupHashLenForWidthHeight(999, 46));
        assertEquals(1, GeohashUtils.lookupHashLenForWidthHeight(46, 999));
        assertEquals(2, GeohashUtils.lookupHashLenForWidthHeight(44, 999));
        assertEquals(2, GeohashUtils.lookupHashLenForWidthHeight(999, 44));
        assertEquals(2, GeohashUtils.lookupHashLenForWidthHeight(999, 5.7));
        assertEquals(2, GeohashUtils.lookupHashLenForWidthHeight(11.3, 999));
        assertEquals(3, GeohashUtils.lookupHashLenForWidthHeight(999, 5.5));
        assertEquals(3, GeohashUtils.lookupHashLenForWidthHeight(11.1, 999));
        assertEquals(GeohashUtils.MAX_PRECISION, GeohashUtils.lookupHashLenForWidthHeight(10e-20, 10e-20));
    }
}
