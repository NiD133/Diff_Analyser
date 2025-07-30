package org.locationtech.spatial4j.shape;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayList;
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

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ShapeCollection_ESTest extends ShapeCollection_ESTest_scaffolding {

    private static final double DELTA = 0.01;

    @Test(timeout = 4000)
    public void testGetAreaWithNullShape() {
        ArrayList<JtsPoint> points = new ArrayList<>();
        SpatialContext context = SpatialContext.GEO;
        ShapeCollection<JtsPoint> shapeCollection = new ShapeCollection<>(points, context);
        points.add(null);
        SpatialContextFactory contextFactory = new SpatialContextFactory();
        SpatialContext newContext = new SpatialContext(contextFactory);

        try {
            shapeCollection.getArea(newContext);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.locationtech.spatial4j.shape.ShapeCollection", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetAreaWithEmptyShapeCollection() {
        ArrayList<JtsPoint> points = new ArrayList<>();
        SpatialContext context = SpatialContext.GEO;
        ShapeCollection<JtsPoint> shapeCollection = new ShapeCollection<>(points, context);
        SpatialContextFactory contextFactory = new SpatialContextFactory();
        SpatialContext newContext = new SpatialContext(contextFactory);

        double area = shapeCollection.getArea(newContext);
        assertEquals(0.0, area, DELTA);
    }

    @Test(timeout = 4000)
    public void testComputeMutualDisjointWithEmptyShapeCollection() {
        ArrayList<JtsPoint> points = new ArrayList<>();
        SpatialContext context = SpatialContext.GEO;
        Stack<ShapeCollection<JtsPoint>> shapeStack = new Stack<>();
        ShapeCollection<JtsPoint> shapeCollection1 = new ShapeCollection<>(points, context);
        ShapeCollection<JtsPoint> shapeCollection2 = new ShapeCollection<>(points, context);
        shapeStack.add(shapeCollection1);
        shapeStack.add(shapeCollection2);

        boolean isDisjoint = ShapeCollection.computeMutualDisjoint(shapeStack);
        assertTrue(isDisjoint);
    }

    @Test(timeout = 4000)
    public void testGetBufferedWithNullContext() {
        ArrayList<JtsPoint> points = new ArrayList<>();
        SpatialContext context = SpatialContext.GEO;
        ShapeCollection<JtsPoint> shapeCollection = new ShapeCollection<>(points, context);

        try {
            shapeCollection.getBuffered(1983.31, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.locationtech.spatial4j.shape.ShapeCollection", e);
        }
    }

    @Test(timeout = 4000)
    public void testComputeBoundingBoxWithNullShape() {
        ArrayList<JtsPoint> points = new ArrayList<>();
        SpatialContext context = SpatialContext.GEO;
        ShapeCollection<JtsPoint> shapeCollection = new ShapeCollection<>(points, context);
        points.add(null);
        SpatialContextFactory contextFactory = new SpatialContextFactory();
        SpatialContext newContext = new SpatialContext(contextFactory);

        try {
            shapeCollection.computeBoundingBox(points, newContext);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.locationtech.spatial4j.shape.ShapeCollection", e);
        }
    }

    @Test(timeout = 4000)
    public void testSizeOfEmptyShapeCollection() {
        Vector<JtsPoint> points = new Vector<>();
        SpatialContextFactory contextFactory = new SpatialContextFactory();
        SpatialContext context = new SpatialContext(contextFactory);
        ShapeCollection<JtsPoint> shapeCollection = new ShapeCollection<>(points, context);

        int size = shapeCollection.size();
        assertEquals(0, size);
    }

    @Test(timeout = 4000)
    public void testSizeWithNullShape() {
        Vector<JtsPoint> points = new Vector<>();
        SpatialContextFactory contextFactory = new SpatialContextFactory();
        SpatialContext context = contextFactory.newSpatialContext();
        ShapeCollection<JtsPoint> shapeCollection = new ShapeCollection<>(points, context);
        points.add(null);

        int size = shapeCollection.size();
        assertEquals(1, size);
    }

    @Test(timeout = 4000)
    public void testGetShapesWithNullShape() {
        ArrayList<JtsPoint> points = new ArrayList<>();
        SpatialContext context = SpatialContext.GEO;
        ShapeCollection<JtsPoint> shapeCollection = new ShapeCollection<>(points, context);
        points.add(null);

        List<JtsPoint> shapes = shapeCollection.getShapes();
        assertFalse(shapes.isEmpty());
    }

    @Test(timeout = 4000)
    public void testGetContext() {
        ArrayList<JtsPoint> points = new ArrayList<>();
        SpatialContextFactory contextFactory = new SpatialContextFactory();
        contextFactory.normWrapLongitude = true;
        SpatialContext context = new SpatialContext(contextFactory);
        ShapeCollection<JtsPoint> shapeCollection = new ShapeCollection<>(points, context);

        SpatialContext retrievedContext = shapeCollection.getContext();
        assertSame(retrievedContext, context);
    }

    @Test(timeout = 4000)
    public void testGetCenterWithEmptyShapeCollection() {
        Vector<JtsPoint> points = new Vector<>();
        SpatialContextFactory contextFactory = new SpatialContextFactory();
        SpatialContext context = new SpatialContext(contextFactory);
        ShapeCollection<JtsPoint> shapeCollection = new ShapeCollection<>(points, context);

        Point center = shapeCollection.getCenter();
        assertTrue(center.isEmpty());
    }

    @Test(timeout = 4000)
    public void testEqualsWithSameShapeCollection() {
        Vector<JtsPoint> points = new Vector<>();
        SpatialContext context = SpatialContext.GEO;
        ShapeCollection<JtsPoint> shapeCollection = new ShapeCollection<>(points, context);

        boolean isEqual = shapeCollection.equals(shapeCollection);
        assertTrue(isEqual);
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentType() {
        ArrayList<JtsPoint> points = new ArrayList<>();
        SpatialContextFactory contextFactory = new SpatialContextFactory();
        SpatialContext context = new SpatialContext(contextFactory);
        ShapeCollection<JtsPoint> shapeCollection = new ShapeCollection<>(points, context);

        boolean isEqual = shapeCollection.equals(context);
        assertFalse(isEqual);
    }

    @Test(timeout = 4000)
    public void testEqualsWithNull() {
        ArrayList<JtsPoint> points = new ArrayList<>();
        SpatialContextFactory contextFactory = new SpatialContextFactory();
        SpatialContext context = new SpatialContext(contextFactory);
        ShapeCollection<JtsPoint> shapeCollection = new ShapeCollection<>(points, context);

        boolean isEqual = shapeCollection.equals(null);
        assertFalse(isEqual);
    }

    @Test(timeout = 4000)
    public void testToStringWithMultipleNullShapes() {
        ArrayList<JtsPoint> points = new ArrayList<>();
        SpatialContext context = SpatialContext.GEO;
        ShapeCollection<JtsPoint> shapeCollection = new ShapeCollection<>(points, context);
        for (int i = 0; i < 10; i++) {
            points.add(null);
        }
        points.addAll(shapeCollection);
        points.add(null);
        points.add(null);

        String description = shapeCollection.toString();
        assertEquals("ShapeCollection(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null ...23)", description);
    }

    @Test(timeout = 4000)
    public void testToStringWithTwoNullShapes() {
        ArrayList<JtsPoint> points = new ArrayList<>();
        SpatialContext context = SpatialContext.GEO;
        ShapeCollection<JtsPoint> shapeCollection = new ShapeCollection<>(points, context);
        points.add(null);
        points.add(null);

        String description = shapeCollection.toString();
        assertEquals("ShapeCollection(null, null)", description);
    }

    @Test(timeout = 4000)
    public void testRelateContainsShortCircuits() {
        ArrayList<JtsPoint> points = new ArrayList<>();
        SpatialContextFactory contextFactory = new SpatialContextFactory();
        SpatialContext context = new SpatialContext(contextFactory);
        ShapeCollection<JtsPoint> shapeCollection = new ShapeCollection<>(points, context);

        boolean result = shapeCollection.relateContainsShortCircuits();
        assertTrue(result);
    }
}