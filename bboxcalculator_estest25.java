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

public class BBoxCalculator_ESTestTest25 extends BBoxCalculator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        SpatialContext spatialContext0 = SpatialContext.GEO;
        BBoxCalculator bBoxCalculator0 = new BBoxCalculator(spatialContext0);
        bBoxCalculator0.expandRange((-180.0), 3844.24, 2361.0, 939.22948782);
        bBoxCalculator0.getMaxX();
        bBoxCalculator0.expandXRange(3844.24, Double.POSITIVE_INFINITY);
        assertEquals(939.22948782, bBoxCalculator0.getMaxY(), 0.01);
    }
}
