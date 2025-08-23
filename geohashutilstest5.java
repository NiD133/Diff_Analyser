package org.locationtech.spatial4j.io;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GeohashUtilsTestTest5 {

    SpatialContext ctx = SpatialContext.GEO;

    /**
     * see the table at http://en.wikipedia.org/wiki/Geohash
     */
    @Test
    public void testHashLenToWidth() {
        //test odd & even len
        double[] boxOdd = GeohashUtils.lookupDegreesSizeForHashLen(3);
        assertEquals(1.40625, boxOdd[0], 0.0001);
        assertEquals(1.40625, boxOdd[1], 0.0001);
        double[] boxEven = GeohashUtils.lookupDegreesSizeForHashLen(4);
        assertEquals(0.1757, boxEven[0], 0.0001);
        assertEquals(0.3515, boxEven[1], 0.0001);
    }
}
