package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.HashMap;
import java.util.LinkedList;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.distance.GeodesicSphereDistCalc;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.ShapeCollection;
import org.locationtech.spatial4j.shape.SpatialRelation;

public class BufferedLineString_ESTestTest21 extends BufferedLineString_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        LinkedList<Point> linkedList0 = new LinkedList<Point>();
        SpatialContext spatialContext0 = SpatialContext.GEO;
        BufferedLineString bufferedLineString0 = new BufferedLineString(linkedList0, (-1.0), spatialContext0);
        // Undeclared exception!
        try {
            bufferedLineString0.getBuffered((-1.0), (SpatialContext) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.locationtech.spatial4j.shape.impl.BufferedLineString", e);
        }
    }
}
