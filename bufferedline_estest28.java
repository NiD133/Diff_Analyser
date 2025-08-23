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

public class BufferedLine_ESTestTest28 extends BufferedLine_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        SpatialContextFactory spatialContextFactory0 = new SpatialContextFactory();
        SpatialContext spatialContext0 = new SpatialContext(spatialContextFactory0);
        PointImpl pointImpl0 = new PointImpl(3.150319189088167, Double.POSITIVE_INFINITY, spatialContext0);
        BufferedLine bufferedLine0 = new BufferedLine(pointImpl0, pointImpl0, Double.POSITIVE_INFINITY, spatialContext0);
        boolean boolean0 = bufferedLine0.contains(pointImpl0);
        assertEquals(Double.POSITIVE_INFINITY, bufferedLine0.getBuf(), 0.01);
        assertFalse(boolean0);
    }
}
