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

public class BBoxCalculator_ESTestTest24 extends BBoxCalculator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        HashMap<String, String> hashMap0 = new HashMap<String, String>();
        ClassLoader classLoader0 = ClassLoader.getSystemClassLoader();
        SpatialContext spatialContext0 = SpatialContextFactory.makeSpatialContext(hashMap0, classLoader0);
        BBoxCalculator bBoxCalculator0 = new BBoxCalculator(spatialContext0);
        double double0 = bBoxCalculator0.getMinY();
        assertEquals(Double.NEGATIVE_INFINITY, bBoxCalculator0.getMaxY(), 0.01);
        assertEquals(Double.POSITIVE_INFINITY, double0, 0.01);
    }
}
