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

public class ShapeCollection_ESTestTest24 extends ShapeCollection_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        Vector<JtsPoint> vector0 = new Vector<JtsPoint>();
        SpatialContextFactory spatialContextFactory0 = new SpatialContextFactory();
        SpatialContext spatialContext0 = new SpatialContext(spatialContextFactory0);
        ShapeCollection<JtsPoint> shapeCollection0 = new ShapeCollection<JtsPoint>(vector0, spatialContext0);
        ShapeCollection<JtsPoint> shapeCollection1 = new ShapeCollection<JtsPoint>(vector0, spatialContext0);
        boolean boolean0 = shapeCollection0.equals(shapeCollection1);
        assertTrue(boolean0);
    }
}
