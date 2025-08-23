package org.locationtech.spatial4j.io;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GeohashUtilsTestTest2 {

    SpatialContext ctx = SpatialContext.GEO;

    /**
     * Pass condition: lat=52.3738007, lng=4.8909347 should be encoded and then
     * decoded within 0.00001 of the original value
     */
    @Test
    public void testDecodePreciseLongitudeLatitude() {
        String hash = GeohashUtils.encodeLatLon(52.3738007, 4.8909347);
        Point point = GeohashUtils.decode(hash, ctx);
        assertEquals(52.3738007, point.getY(), 0.00001D);
        assertEquals(4.8909347, point.getX(), 0.00001D);
    }
}
