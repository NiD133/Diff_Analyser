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

public class BufferedLine_ESTestTest58 extends BufferedLine_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test57() throws Throwable {
        SpatialContext spatialContext0 = SpatialContext.GEO;
        PointImpl pointImpl0 = new PointImpl(0.0, 0.0, spatialContext0);
        BufferedLine bufferedLine0 = new BufferedLine(pointImpl0, pointImpl0, 0.0, spatialContext0);
        InfBufLine infBufLine0 = bufferedLine0.getLinePerp();
        assertEquals(Double.NaN, infBufLine0.getDistDenomInv(), 0.01);
        assertEquals(0.0, infBufLine0.getIntercept(), 0.01);
        assertEquals(0.0, bufferedLine0.getBuf(), 0.01);
        assertEquals(0.0, infBufLine0.getBuf(), 0.01);
    }
}
