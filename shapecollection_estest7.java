package org.locationtech.spatial4j.shape;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import java.util.function.Predicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.distance.GeodesicSphereDistCalc;
import org.locationtech.spatial4j.shape.impl.PointImpl;
import org.locationtech.spatial4j.shape.jts.JtsPoint;

public class ShapeCollection_ESTestTest7 extends ShapeCollection_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        ArrayList<JtsPoint> arrayList0 = new ArrayList<JtsPoint>();
        SpatialContextFactory spatialContextFactory0 = new SpatialContextFactory();
        SpatialContext spatialContext0 = new SpatialContext(spatialContextFactory0);
        ShapeCollection<JtsPoint> shapeCollection0 = new ShapeCollection<JtsPoint>(arrayList0, spatialContext0);
        SpatialContext spatialContext1 = spatialContextFactory0.newSpatialContext();
        Rectangle rectangle0 = shapeCollection0.computeBoundingBox(arrayList0, spatialContext1);
        assertEquals(Double.NaN, rectangle0.getMaxY(), 0.01);
        assertEquals(Double.NaN, rectangle0.getMaxX(), 0.01);
        assertEquals(Double.NaN, rectangle0.getMinX(), 0.01);
        assertEquals(Double.NaN, rectangle0.getMinY(), 0.01);
    }
}
