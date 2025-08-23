package org.locationtech.spatial4j.io;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.ShapeCollection;
import org.junit.Test;
import java.io.*;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;

public class BinaryCodecTestTest3 extends BaseRoundTripTest<SpatialContext> {

    @Override
    public SpatialContext initContext() {
        return SpatialContext.GEO;
    }

    @Override
    protected void assertRoundTrip(Shape shape, boolean andEquals) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        binaryCodec.writeShape(new DataOutputStream(baos), shape);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        assertEquals(shape, binaryCodec.readShape(new DataInputStream(bais)));
    }

    @Test
    public void testCollection() throws Exception {
        ShapeCollection<Shape> s = ctx.makeCollection(Arrays.asList(randomShape(), randomShape(), randomShape()));
        assertRoundTrip(s);
    }
}
