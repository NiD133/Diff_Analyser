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

public class BBoxCalculator_ESTestTest29 extends BBoxCalculator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        SpatialContext spatialContext0 = SpatialContext.GEO;
        BBoxCalculator bBoxCalculator0 = new BBoxCalculator(spatialContext0);
        bBoxCalculator0.expandXRange(1198.228879, Double.NaN);
        bBoxCalculator0.expandXRange(1198.228879, Double.NaN);
        assertEquals(Double.POSITIVE_INFINITY, bBoxCalculator0.getMinY(), 0.01);
        assertEquals(Double.NEGATIVE_INFINITY, bBoxCalculator0.getMaxY(), 0.01);
    }
}
