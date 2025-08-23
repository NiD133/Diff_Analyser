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

public class BBoxCalculator_ESTestTest2 extends BBoxCalculator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        SpatialContext spatialContext0 = SpatialContext.GEO;
        BBoxCalculator bBoxCalculator0 = new BBoxCalculator(spatialContext0);
        bBoxCalculator0.expandXRange(1107.2515, Double.POSITIVE_INFINITY);
        bBoxCalculator0.expandRange((-59.73795920817872), 6.283185307179586, 1.0, (-180.0));
        double double0 = bBoxCalculator0.getMaxX();
        assertEquals(1.0, bBoxCalculator0.getMinY(), 0.01);
        assertEquals(6.283185307179586, double0, 0.01);
    }
}