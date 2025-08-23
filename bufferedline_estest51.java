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
import org.locationtech.spatial4j.distance.CartesianDistCalc;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.SpatialRelation;

public class BufferedLine_ESTestTest51 extends BufferedLine_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test50() throws Throwable {
        SpatialContext spatialContext0 = SpatialContext.GEO;
        PointImpl pointImpl0 = new PointImpl(0.0, 0.0, spatialContext0);
        double double0 = BufferedLine.expandBufForLongitudeSkew(pointImpl0, pointImpl0, (-2439.5224090430065));
        assertEquals(80.47759095699345, double0, 0.01);
    }
}
