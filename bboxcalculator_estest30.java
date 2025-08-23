package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.HashMap;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.distance.GeodesicSphereDistCalc;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;

public class BBoxCalculator_ESTestTest30 extends BBoxCalculator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        SpatialContext spatialContext0 = SpatialContext.GEO;
        BBoxCalculator bBoxCalculator0 = new BBoxCalculator(spatialContext0);
        GeodesicSphereDistCalc.LawOfCosines geodesicSphereDistCalc_LawOfCosines0 = new GeodesicSphereDistCalc.LawOfCosines();
        PointImpl pointImpl0 = new PointImpl(0.621371192, 0.621371192, spatialContext0);
        Point point0 = geodesicSphereDistCalc_LawOfCosines0.pointOnBearing(pointImpl0, 0.621371192, 0.621371192, spatialContext0, pointImpl0);
        RectangleImpl rectangleImpl0 = new RectangleImpl(point0, point0, spatialContext0);
        bBoxCalculator0.expandRange((Rectangle) rectangleImpl0);
        bBoxCalculator0.expandXRange(0.621371192, 0.621371192);
        bBoxCalculator0.getBoundary();
        assertEquals(1.242705837824413, bBoxCalculator0.getMinY(), 0.01);
    }
}
