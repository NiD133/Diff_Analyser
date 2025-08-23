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

public class ShapeCollection_ESTestTest34 extends ShapeCollection_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        ArrayList<JtsPoint> arrayList0 = new ArrayList<JtsPoint>();
        SpatialContext spatialContext0 = SpatialContext.GEO;
        ShapeCollection<JtsPoint> shapeCollection0 = new ShapeCollection<JtsPoint>(arrayList0, spatialContext0);
        PointImpl pointImpl0 = new PointImpl(18.5747965337, 18.5747965337, spatialContext0);
        Rectangle rectangle0 = shapeCollection0.getBoundingBox();
        GeodesicSphereDistCalc.LawOfCosines geodesicSphereDistCalc_LawOfCosines0 = new GeodesicSphereDistCalc.LawOfCosines();
        geodesicSphereDistCalc_LawOfCosines0.calcBoxByDistFromPt(pointImpl0, 18.5747965337, spatialContext0, rectangle0);
        Rectangle rectangle1 = spatialContext0.getWorldBounds();
        SpatialRelation spatialRelation0 = shapeCollection0.relate(rectangle1);
        assertTrue(spatialRelation0.intersects());
    }
}
