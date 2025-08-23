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

public class BufferedLine_ESTestTest44 extends BufferedLine_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test43() throws Throwable {
        SpatialContext spatialContext0 = SpatialContext.GEO;
        PointImpl pointImpl0 = new PointImpl(19.95949838725778, 19.95949838725778, spatialContext0);
        BufferedLine bufferedLine0 = new BufferedLine(pointImpl0, pointImpl0, 19.95949838725778, spatialContext0);
        RectangleImpl rectangleImpl0 = new RectangleImpl(314.0527, 648.9138053, 19.95949838725778, 314.0527, spatialContext0);
        SpatialRelation spatialRelation0 = bufferedLine0.relate((Rectangle) rectangleImpl0);
        assertEquals(SpatialRelation.DISJOINT, spatialRelation0);
        assertEquals(19.95949838725778, bufferedLine0.getBuf(), 0.01);
    }
}
