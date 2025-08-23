package org.locationtech.spatial4j.io;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GeohashUtilsTestTest3 {

    SpatialContext ctx = SpatialContext.GEO;

    /**
     * Pass condition: lat=84.6, lng=10.5 should be encoded and then decoded
     * within 0.00001 of the original value
     */
    @Test
    public void testDecodeImpreciseLongitudeLatitude() {
        String hash = GeohashUtils.encodeLatLon(84.6, 10.5);
        Point point = GeohashUtils.decode(hash, ctx);
        assertEquals(84.6, point.getY(), 0.00001D);
        assertEquals(10.5, point.getX(), 0.00001D);
    }
}
