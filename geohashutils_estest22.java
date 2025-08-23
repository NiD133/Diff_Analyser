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

public class GeohashUtils_ESTestTest22 extends GeohashUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        SpatialContext spatialContext0 = SpatialContext.GEO;
        Point point0 = GeohashUtils.decode("h0pb421bn842p8h85bj0hbp000000000000000000000000000000000000000000000000000000000000000000000000000000000", spatialContext0);
        assertEquals(11.0, point0.getX(), 0.01);
        assertEquals((-90.0), point0.getLat(), 0.01);
    }
}
