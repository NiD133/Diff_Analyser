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

public class BufferedLine_ESTestTest3 extends BufferedLine_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        SpatialContextFactory spatialContextFactory0 = new SpatialContextFactory();
        SpatialContext spatialContext0 = new SpatialContext(spatialContextFactory0);
        PointImpl pointImpl0 = new PointImpl(0.0, 1774.4545169222108, spatialContext0);
        PointImpl pointImpl1 = new PointImpl(1774.4545169222108, (-3282.38), spatialContext0);
        // Undeclared exception!
        try {
            BufferedLine.expandBufForLongitudeSkew(pointImpl0, pointImpl1, 261.90724681475126);
            fail("Expecting exception: AssertionError");
        } catch (AssertionError e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
