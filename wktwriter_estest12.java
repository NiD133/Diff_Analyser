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

public class WKTWriter_ESTestTest12 extends WKTWriter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        SpatialContext spatialContext0 = SpatialContext.GEO;
        LinkedList<Point> linkedList0 = new LinkedList<Point>();
        WKTWriter wKTWriter0 = new WKTWriter();
        BufferedLineString bufferedLineString0 = new BufferedLineString(linkedList0, (-145.0981164), spatialContext0);
        String string0 = wKTWriter0.toString((Shape) bufferedLineString0);
        assertEquals("LINESTRING ()", string0);
    }
}
