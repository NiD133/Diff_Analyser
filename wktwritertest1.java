package org.locationtech.spatial4j.io;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.ShapeCollection;

public class WKTWriterTestTest1 {

    private SpatialContext ctx;

    @Test
    public void testToStringOnEmptyPoint() throws Exception {
        ShapeWriter writer = ctx.getFormats().getWktWriter();
        Point emptyPoint = ctx.makePoint(Double.NaN, Double.NaN);
        assertEquals("POINT EMPTY", writer.toString(emptyPoint));
    }
}
