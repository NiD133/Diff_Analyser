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

public class WKTWriter_ESTestTest10 extends WKTWriter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        WKTWriter wKTWriter0 = new WKTWriter();
        StringWriter stringWriter0 = new StringWriter(1887);
        LinkedList<Point> linkedList0 = new LinkedList<Point>();
        GeodesicSphereDistCalc.LawOfCosines geodesicSphereDistCalc_LawOfCosines0 = new GeodesicSphereDistCalc.LawOfCosines();
        SpatialContext spatialContext0 = SpatialContext.GEO;
        PointImpl pointImpl0 = new PointImpl(1887, 3671, spatialContext0);
        linkedList0.add((Point) pointImpl0);
        Point point0 = geodesicSphereDistCalc_LawOfCosines0.pointOnBearing(pointImpl0, 274.25232505, 3671, spatialContext0, pointImpl0);
        linkedList0.add(point0);
        linkedList0.offerLast(point0);
        BufferedLineString bufferedLineString0 = new BufferedLineString(linkedList0, 1887, spatialContext0);
        ShapeCollection<BufferedLine> shapeCollection0 = bufferedLineString0.getSegments();
        wKTWriter0.write(stringWriter0, shapeCollection0);
        assertEquals("GEOMETRYCOLLECTION (BufferedLine(Pt(x=1456.3497532141264,y=-2.03979513747829), Pt(x=1456.3497532141264,y=-2.03979513747829) b=1887.0),BufferedLine(Pt(x=1456.3497532141264,y=-2.03979513747829), Pt(x=1456.3497532141264,y=-2.03979513747829) b=1887.0))", stringWriter0.toString());
    }
}
