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

public class SpatialContextFactoryTestTest3 {

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
    public void testJtsContextFactory() {
        JtsSpatialContext ctx = (JtsSpatialContext) call("spatialContextFactory", JtsSpatialContextFactory.class.getName(), "geo", "true", "normWrapLongitude", "true", "precisionScale", "2.0", "wktShapeParserClass", CustomWktShapeParser.class.getName(), "datelineRule", "ccwRect", "validationRule", "repairConvexHull", "autoIndex", "true");
        assertTrue(ctx.isNormWrapLongitude());
        assertEquals(2.0, ctx.getGeometryFactory().getPrecisionModel().getScale(), 0.0);
        //cheap way to test it was created
        assertTrue(CustomWktShapeParser.once);
        assertEquals(DatelineRule.ccwRect, ctx.getDatelineRule());
        assertEquals(ValidationRule.repairConvexHull, ctx.getValidationRule());
        //ensure geo=false with worldbounds works -- fixes #72
        ctx = (JtsSpatialContext) call("spatialContextFactory", JtsSpatialContextFactory.class.getName(), //set to false
        "geo", //set to false
        "false", "worldBounds", "ENVELOPE(-500,500,300,-300)", "normWrapLongitude", "true", "precisionScale", "2.0", "wktShapeParserClass", CustomWktShapeParser.class.getName(), "datelineRule", "ccwRect", "validationRule", "repairConvexHull", "autoIndex", "true");
        assertEquals(300, ctx.getWorldBounds().getMaxY(), 0.0);
    }
}
