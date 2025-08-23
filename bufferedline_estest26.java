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

public class BufferedLine_ESTestTest26 extends BufferedLine_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        SpatialContextFactory spatialContextFactory0 = new SpatialContextFactory();
        SpatialContext spatialContext0 = spatialContextFactory0.newSpatialContext();
        PointImpl pointImpl0 = new PointImpl(2.0, 2.0, spatialContext0);
        PointImpl pointImpl1 = new PointImpl(6378.137, 2.0, spatialContext0);
        BufferedLine bufferedLine0 = new BufferedLine(pointImpl0, pointImpl0, 3958.7613145272735, spatialContext0);
        boolean boolean0 = bufferedLine0.contains(pointImpl1);
        assertEquals(3958.7613145272735, bufferedLine0.getBuf(), 0.01);
        assertFalse(boolean0);
    }
}
