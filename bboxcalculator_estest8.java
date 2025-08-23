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

public class BBoxCalculator_ESTestTest8 extends BBoxCalculator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        SpatialContext spatialContext0 = SpatialContext.GEO;
        BBoxCalculator bBoxCalculator0 = new BBoxCalculator(spatialContext0);
        bBoxCalculator0.expandRange((-104.6238989593), 2621.8399556940103, 2621.8399556940103, 1.0);
        double double0 = bBoxCalculator0.getMaxY();
        assertEquals(2621.8399556940103, bBoxCalculator0.getMinY(), 0.01);
        assertEquals(1.0, double0, 0.01);
    }
}
