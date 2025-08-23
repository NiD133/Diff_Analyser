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

public class BufferedLineString_ESTestTest27 extends BufferedLineString_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        LinkedList<Point> linkedList0 = new LinkedList<Point>();
        SpatialContextFactory spatialContextFactory0 = new SpatialContextFactory();
        SpatialContext spatialContext0 = new SpatialContext(spatialContextFactory0);
        BufferedLineString bufferedLineString0 = new BufferedLineString(linkedList0, (-2877.398196062), true, spatialContext0);
        Point point0 = bufferedLineString0.getCenter();
        linkedList0.add(point0);
        BufferedLineString bufferedLineString1 = new BufferedLineString(linkedList0, 1569.552036879501, false, spatialContext0);
        bufferedLineString1.getPoints();
        assertEquals(1, linkedList0.size());
        assertEquals(1569.552036879501, bufferedLineString1.getBuf(), 0.01);
    }
}
