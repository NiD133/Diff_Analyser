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

public class ShapeCollection_ESTestTest32 extends ShapeCollection_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test31() throws Throwable {
        ArrayList<JtsPoint> arrayList0 = new ArrayList<JtsPoint>();
        SpatialContext spatialContext0 = SpatialContext.GEO;
        PointImpl pointImpl0 = new PointImpl(624.2, 624.2, spatialContext0);
        ShapeCollection<JtsPoint> shapeCollection0 = new ShapeCollection<JtsPoint>(arrayList0, spatialContext0);
        Rectangle rectangle0 = shapeCollection0.getBoundingBox();
        arrayList0.add((JtsPoint) null);
        GeodesicSphereDistCalc.LawOfCosines geodesicSphereDistCalc_LawOfCosines0 = new GeodesicSphereDistCalc.LawOfCosines();
        Rectangle rectangle1 = geodesicSphereDistCalc_LawOfCosines0.calcBoxByDistFromPt(pointImpl0, 624.2, spatialContext0, rectangle0);
        // Undeclared exception!
        try {
            shapeCollection0.relate(rectangle1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.locationtech.spatial4j.shape.ShapeCollection", e);
        }
    }
}
