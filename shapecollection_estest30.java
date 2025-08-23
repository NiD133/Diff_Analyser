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

public class ShapeCollection_ESTestTest30 extends ShapeCollection_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        ArrayList<JtsPoint> arrayList0 = new ArrayList<JtsPoint>();
        SpatialContext spatialContext0 = SpatialContext.GEO;
        ShapeCollection<JtsPoint> shapeCollection0 = new ShapeCollection<JtsPoint>(arrayList0, spatialContext0);
        Stack<ShapeCollection<JtsPoint>> stack0 = new Stack<ShapeCollection<JtsPoint>>();
        stack0.add(shapeCollection0);
        ShapeCollection<ShapeCollection<JtsPoint>> shapeCollection1 = new ShapeCollection<ShapeCollection<JtsPoint>>(stack0, spatialContext0);
        double double0 = shapeCollection1.getArea(spatialContext0);
        assertEquals(0.0, double0, 0.01);
    }
}
