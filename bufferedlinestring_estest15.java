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

public class BufferedLineString_ESTestTest15 extends BufferedLineString_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        LinkedList<Point> linkedList0 = new LinkedList<Point>();
        SpatialContextFactory spatialContextFactory0 = new SpatialContextFactory();
        SpatialContext spatialContext0 = spatialContextFactory0.newSpatialContext();
        PointImpl pointImpl0 = new PointImpl((-1.0), Double.NaN, spatialContext0);
        linkedList0.add((Point) pointImpl0);
        BufferedLineString bufferedLineString0 = new BufferedLineString(linkedList0, (-1.0), spatialContext0);
        Rectangle rectangle0 = bufferedLineString0.getBoundingBox();
        assertEquals(1, linkedList0.size());
        assertEquals(0.0, rectangle0.getMinX(), 0.01);
    }
}
