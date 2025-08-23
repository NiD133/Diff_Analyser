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

public class BufferedLineString_ESTestTest3 extends BufferedLineString_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        LinkedList<Point> linkedList0 = new LinkedList<Point>();
        HashMap<String, String> hashMap0 = new HashMap<String, String>();
        ClassLoader classLoader0 = ClassLoader.getSystemClassLoader();
        SpatialContext spatialContext0 = SpatialContextFactory.makeSpatialContext(hashMap0, classLoader0);
        BufferedLineString bufferedLineString0 = new BufferedLineString(linkedList0, 0.0, spatialContext0);
        BufferedLineString bufferedLineString1 = new BufferedLineString(linkedList0, 1114.712856653245, spatialContext0);
        boolean boolean0 = bufferedLineString1.equals(bufferedLineString0);
        assertFalse(boolean0);
        assertFalse(bufferedLineString0.equals((Object) bufferedLineString1));
        assertEquals(1114.712856653245, bufferedLineString1.getBuf(), 0.01);
    }
}
