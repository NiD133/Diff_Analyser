package org.locationtech.spatial4j.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ChoiceFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.distance.GeodesicSphereDistCalc;
import org.locationtech.spatial4j.shape.Circle;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.ShapeCollection;
import org.locationtech.spatial4j.shape.impl.BufferedLine;
import org.locationtech.spatial4j.shape.impl.BufferedLineString;
import org.locationtech.spatial4j.shape.impl.PointImpl;

public class WKTWriter_ESTestTest16 extends WKTWriter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        SpatialContext spatialContext0 = SpatialContext.GEO;
        PointImpl pointImpl0 = new PointImpl(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, spatialContext0);
        WKTWriter wKTWriter0 = new WKTWriter();
        String string0 = wKTWriter0.toString((Shape) pointImpl0);
        assertEquals("WKT", wKTWriter0.getFormatName());
        assertEquals("POINT (-\u221E -\u221E)", string0);
    }
}
