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

public class BufferedLine_ESTestTest52 extends BufferedLine_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test51() throws Throwable {
        SpatialContext spatialContext0 = SpatialContext.GEO;
        PointImpl pointImpl0 = new PointImpl(0.0, 0.0, spatialContext0);
        PointImpl pointImpl1 = new PointImpl((-3478.012754255683), 0.0, spatialContext0);
        BufferedLine bufferedLine0 = new BufferedLine(pointImpl1, pointImpl0, 0.0, spatialContext0);
        assertEquals(0.0, bufferedLine0.getBuf(), 0.01);
    }
}