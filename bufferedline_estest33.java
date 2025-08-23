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

public class BufferedLine_ESTestTest33 extends BufferedLine_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test32() throws Throwable {
        SpatialContext spatialContext0 = SpatialContext.GEO;
        PointImpl pointImpl0 = new PointImpl(47.596567977381824, 47.596567977381824, spatialContext0);
        BufferedLine bufferedLine0 = new BufferedLine(pointImpl0, pointImpl0, 47.596567977381824, spatialContext0);
        PointImpl pointImpl1 = new PointImpl(47.596567977381824, 47.596567977381824, spatialContext0);
        pointImpl1.reset((-2340.0), (-2340.0));
        BufferedLine bufferedLine1 = new BufferedLine(pointImpl0, pointImpl1, 47.596567977381824, spatialContext0);
        boolean boolean0 = bufferedLine0.equals(bufferedLine1);
        assertFalse(boolean0);
        assertEquals(47.596567977381824, bufferedLine1.getBuf(), 0.01);
    }
}
