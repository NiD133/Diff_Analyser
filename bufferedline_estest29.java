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

public class BufferedLine_ESTestTest29 extends BufferedLine_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        SpatialContext spatialContext0 = SpatialContext.GEO;
        BufferedLine bufferedLine0 = null;
        try {
            bufferedLine0 = new BufferedLine((Point) null, (Point) null, (-512.59591197), spatialContext0);
            fail("Expecting exception: AssertionError");
        } catch (AssertionError e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
