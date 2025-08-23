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

public class BBoxCalculator_ESTestTest11 extends BBoxCalculator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        SpatialContext spatialContext0 = SpatialContext.GEO;
        BBoxCalculator bBoxCalculator0 = new BBoxCalculator(spatialContext0);
        assertEquals(Double.POSITIVE_INFINITY, bBoxCalculator0.getMinY(), 0.01);
        bBoxCalculator0.expandRange(Double.NaN, 0.0, 0.0, 0.0);
        Rectangle rectangle0 = bBoxCalculator0.getBoundary();
        assertEquals(0.0, rectangle0.getMaxX(), 0.01);
    }
}
