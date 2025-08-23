package org.locationtech.spatial4j.shape;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import java.util.function.Predicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.distance.GeodesicSphereDistCalc;
import org.locationtech.spatial4j.shape.impl.PointImpl;
import org.locationtech.spatial4j.shape.jts.JtsPoint;

public class ShapeCollection_ESTestTest14 extends ShapeCollection_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        ArrayList<JtsPoint> arrayList0 = new ArrayList<JtsPoint>();
        SpatialContext spatialContext0 = SpatialContext.GEO;
        ShapeCollection<JtsPoint> shapeCollection0 = new ShapeCollection<JtsPoint>(arrayList0, spatialContext0);
        GeodesicSphereDistCalc.LawOfCosines geodesicSphereDistCalc_LawOfCosines0 = new GeodesicSphereDistCalc.LawOfCosines();
        PointImpl pointImpl0 = new PointImpl(2630.69905510879, 2630.69905510879, spatialContext0);
        Rectangle rectangle0 = shapeCollection0.getBoundingBox();
        geodesicSphereDistCalc_LawOfCosines0.calcBoxByDistFromPt(pointImpl0, 2630.69905510879, spatialContext0, rectangle0);
        Rectangle rectangle1 = shapeCollection0.getBoundingBox();
        assertEquals(90.0, rectangle1.getMaxY(), 0.01);
    }
}