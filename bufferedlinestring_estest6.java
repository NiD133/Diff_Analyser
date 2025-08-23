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

public class BufferedLineString_ESTestTest6 extends BufferedLineString_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        LinkedList<Point> linkedList0 = new LinkedList<Point>();
        SpatialContext spatialContext0 = SpatialContext.GEO;
        BufferedLineString bufferedLineString0 = new BufferedLineString(linkedList0, 2689.299973955, false, spatialContext0);
        Rectangle rectangle0 = bufferedLineString0.getBoundingBox();
        GeodesicSphereDistCalc.LawOfCosines geodesicSphereDistCalc_LawOfCosines0 = new GeodesicSphereDistCalc.LawOfCosines();
        PointImpl pointImpl0 = new PointImpl(2689.299973955, (-462.55989283748), spatialContext0);
        geodesicSphereDistCalc_LawOfCosines0.calcBoxByDistFromPt(pointImpl0, 2689.299973955, spatialContext0, rectangle0);
        bufferedLineString0.relate(rectangle0);
        assertEquals(2689.299973955, bufferedLineString0.getBuf(), 0.01);
    }
}
