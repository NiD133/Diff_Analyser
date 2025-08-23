package org.locationtech.spatial4j.context;

import org.locationtech.spatial4j.context.jts.DatelineRule;
import org.locationtech.spatial4j.context.jts.JtsSpatialContext;
import org.locationtech.spatial4j.context.jts.JtsSpatialContextFactory;
import org.locationtech.spatial4j.context.jts.ValidationRule;
import org.locationtech.spatial4j.distance.CartesianDistCalc;
import org.locationtech.spatial4j.distance.GeodesicSphereDistCalc;
import org.locationtech.spatial4j.io.ShapeIO;
import org.locationtech.spatial4j.io.WKTReader;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;
import org.junit.After;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SpatialContextFactoryTestTest2 {

    public static final String PROP = "SpatialContextFactory";

    @After
    public void tearDown() {
        System.getProperties().remove(PROP);
    }

    private SpatialContext call(String... argsStr) {
        Map<String, String> args = new HashMap<>();
        for (int i = 0; i < argsStr.length; i += 2) {
            String key = argsStr[i];
            String val = argsStr[i + 1];
            args.put(key, val);
        }
        return SpatialContextFactory.makeSpatialContext(args, getClass().getClassLoader());
    }

    public static class DSCF extends SpatialContextFactory {

        @Override
        public SpatialContext newSpatialContext() {
            geo = false;
            return new SpatialContext(this);
        }
    }

    public static class CustomWktShapeParser extends WKTReader {

        //cheap way to test it was created
        static boolean once = false;

        public CustomWktShapeParser(JtsSpatialContext ctx, JtsSpatialContextFactory factory) {
            super(ctx, factory);
            once = true;
        }
    }

    @Test
    public void testCustom() {
        SpatialContext ctx = call("geo", "false");
        assertTrue(!ctx.isGeo());
        assertEquals(new CartesianDistCalc(), ctx.getDistCalc());
        ctx = call("geo", "false", "distCalculator", "cartesian^2", "worldBounds", //xMin, xMax, yMax, yMin
        "ENVELOPE(-100, 75, 200, 0)");
        assertEquals(new CartesianDistCalc(true), ctx.getDistCalc());
        assertEquals(new RectangleImpl(-100, 75, 0, 200, ctx), ctx.getWorldBounds());
        ctx = call("geo", "true", "distCalculator", "lawOfCosines");
        assertTrue(ctx.isGeo());
        assertEquals(new GeodesicSphereDistCalc.LawOfCosines(), ctx.getDistCalc());
    }
}
