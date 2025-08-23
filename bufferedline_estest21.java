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

public class BufferedLine_ESTestTest21 extends BufferedLine_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        SpatialContext spatialContext0 = SpatialContext.GEO;
        PointImpl pointImpl0 = new PointImpl(2115.863165, 2115.863165, spatialContext0);
        BufferedLine bufferedLine0 = new BufferedLine(pointImpl0, pointImpl0, 2115.863165, spatialContext0);
        // Undeclared exception!
        try {
            bufferedLine0.getBuffered((-1304.935812), spatialContext0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // maxY must be >= minY: 1304.935812 to 90.0
            //
            verifyException("org.locationtech.spatial4j.shape.impl.ShapeFactoryImpl", e);
        }
    }
}
