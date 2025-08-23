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

public class WKTWriter_ESTestTest14 extends WKTWriter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        WKTWriter wKTWriter0 = new WKTWriter();
        LinkedList<Point> linkedList0 = new LinkedList<Point>();
        SpatialContext spatialContext0 = SpatialContext.GEO;
        BufferedLineString bufferedLineString0 = new BufferedLineString(linkedList0, 3671, true, spatialContext0);
        Point point0 = bufferedLineString0.getCenter();
        BufferedLine bufferedLine0 = new BufferedLine(point0, point0, 1887, spatialContext0);
        Rectangle rectangle0 = bufferedLine0.getBoundingBox();
        String string0 = wKTWriter0.toString((Shape) rectangle0);
        assertEquals("ENVELOPE (\uFFFD, \uFFFD, \uFFFD, \uFFFD)", string0);
    }
}
