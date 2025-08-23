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

public class BufferedLine_ESTestTest7 extends BufferedLine_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        SpatialContextFactory spatialContextFactory0 = new SpatialContextFactory();
        SpatialContext spatialContext0 = new SpatialContext(spatialContextFactory0);
        PointImpl pointImpl0 = new PointImpl(0.0, 0.0, spatialContext0);
        CartesianDistCalc cartesianDistCalc0 = CartesianDistCalc.INSTANCE_SQUARED;
        cartesianDistCalc0.pointOnBearing(pointImpl0, 1521.0, 2573.0, spatialContext0, pointImpl0);
        PointImpl pointImpl1 = new PointImpl(0.0, (-1573.0219511291966), spatialContext0);
        BufferedLine bufferedLine0 = new BufferedLine(pointImpl0, pointImpl1, 3550.72104647596, spatialContext0);
        InfBufLine infBufLine0 = bufferedLine0.getLinePerp();
        assertTrue(bufferedLine0.hasArea());
        assertEquals((-32.3417023265244), infBufLine0.getIntercept(), 0.01);
    }
}
