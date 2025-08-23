package org.locationtech.spatial4j.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.HashMap;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;

public class GeohashUtils_ESTestTest19 extends GeohashUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        SpatialContext spatialContext0 = SpatialContext.GEO;
        Rectangle rectangle0 = GeohashUtils.decodeBoundary("kpbpbpbpbpbp", spatialContext0);
        assertEquals(3.3527612686157227E-7, rectangle0.getMaxX(), 0.01);
        assertEquals(0.0, rectangle0.getMinX(), 0.01);
        assertEquals((-1.6763806343078613E-7), rectangle0.getMinY(), 0.01);
    }
}
