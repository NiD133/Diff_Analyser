package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.HashMap;
import java.util.LinkedList;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.distance.GeodesicSphereDistCalc;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.ShapeCollection;
import org.locationtech.spatial4j.shape.SpatialRelation;

public class BufferedLineString_ESTestTest17 extends BufferedLineString_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        LinkedList<Point> linkedList0 = new LinkedList<Point>();
        SpatialContextFactory spatialContextFactory0 = new SpatialContextFactory();
        SpatialContext spatialContext0 = new SpatialContext(spatialContextFactory0);
        PointImpl pointImpl0 = new PointImpl((-180.0), 465.46032036, spatialContext0);
        linkedList0.add((Point) pointImpl0);
        PointImpl pointImpl1 = new PointImpl(0.017453292519943295, 0.017453292519943295, spatialContext0);
        linkedList0.add((Point) pointImpl1);
        BufferedLineString bufferedLineString0 = new BufferedLineString(linkedList0, 0.017453292519943295, true, spatialContext0);
        double double0 = bufferedLineString0.getArea(spatialContext0);
        assertEquals(0.017453292519943295, bufferedLineString0.getBuf(), 0.01);
        assertEquals((-65.33078137174249), double0, 0.01);
    }
}
