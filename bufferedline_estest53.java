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

public class BufferedLine_ESTestTest53 extends BufferedLine_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test52() throws Throwable {
        SpatialContext spatialContext0 = SpatialContext.GEO;
        PointImpl pointImpl0 = new PointImpl(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, spatialContext0);
        BufferedLine bufferedLine0 = null;
        try {
            bufferedLine0 = new BufferedLine(pointImpl0, pointImpl0, Double.POSITIVE_INFINITY, spatialContext0);
            fail("Expecting exception: AssertionError");
        } catch (AssertionError e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
