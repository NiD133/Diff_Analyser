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

public class BufferedLine_ESTestTest1 extends BufferedLine_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        SpatialContextFactory spatialContextFactory0 = new SpatialContextFactory();
        SpatialContext spatialContext0 = new SpatialContext(spatialContextFactory0);
        PointImpl pointImpl0 = new PointImpl(0.0, 0.0, spatialContext0);
        BufferedLine bufferedLine0 = new BufferedLine(pointImpl0, pointImpl0, 0.0, spatialContext0);
        BufferedLine bufferedLine1 = new BufferedLine(pointImpl0, pointImpl0, 90.0, spatialContext0);
        boolean boolean0 = bufferedLine1.equals(bufferedLine0);
        assertFalse(boolean0);
        assertFalse(bufferedLine0.equals((Object) bufferedLine1));
        assertTrue(bufferedLine1.hasArea());
    }
}
