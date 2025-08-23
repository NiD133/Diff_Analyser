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

public class BBoxCalculator_ESTestTest34 extends BBoxCalculator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        SpatialContext spatialContext0 = SpatialContext.GEO;
        BBoxCalculator bBoxCalculator0 = new BBoxCalculator(spatialContext0);
        bBoxCalculator0.expandRange(Double.POSITIVE_INFINITY, (-3518.505381128807), 1.0, 2.0);
        bBoxCalculator0.expandXRange(0.0, (-1.0));
        bBoxCalculator0.getBoundary();
        assertEquals(2.0, bBoxCalculator0.getMaxY(), 0.01);
    }
}
