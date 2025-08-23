package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.impl.BBoxCalculator;
import java.util.HashMap;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class BBoxCalculator_ESTest extends BBoxCalculator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testExpandXRangeWithNegativeInfinity() {
        HashMap<String, String> config = new HashMap<>();
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        SpatialContext spatialContext = SpatialContextFactory.makeSpatialContext(config, classLoader);
        BBoxCalculator bboxCalculator = new BBoxCalculator(spatialContext);

        bboxCalculator.expandXRange(Double.NEGATIVE_INFINITY, 0.0);
        assertEquals(0.0, bboxCalculator.getMaxX(), 0.01);

        bboxCalculator.expandXRange(Double.NEGATIVE_INFINITY, 4108.846834);
        assertEquals(Double.NEGATIVE_INFINITY, bboxCalculator.getMaxY(), 0.01);
        assertEquals(Double.POSITIVE_INFINITY, bboxCalculator.getMinY(), 0.01);
    }

    @Test(timeout = 4000)
    public void testExpandRangeWithPositiveInfinity() {
        SpatialContext spatialContext = SpatialContext.GEO;
        BBoxCalculator bboxCalculator = new BBoxCalculator(spatialContext);

        bboxCalculator.expandXRange(1107.2515, Double.POSITIVE_INFINITY);
        bboxCalculator.expandRange(-59.73795920817872, 6.283185307179586, 1.0, -180.0);

        assertEquals(1.0, bboxCalculator.getMinY(), 0.01);
        assertEquals(6.283185307179586, bboxCalculator.getMaxX(), 0.01);
    }

    @Test(timeout = 4000)
    public void testExpandRangeWithNegativeValues() {
        SpatialContextFactory spatialContextFactory = new SpatialContextFactory();
        SpatialContext spatialContext = spatialContextFactory.newSpatialContext();
        BBoxCalculator bboxCalculator = new BBoxCalculator(spatialContext);

        bboxCalculator.expandRange(2645.56006566, -1.0, 0.0, -388.0);
        assertEquals(-388.0, bboxCalculator.getMaxY(), 0.01);
        assertEquals(0.0, bboxCalculator.getMinY(), 0.01);
    }

    @Test(timeout = 4000)
    public void testExpandRangeWithIdenticalValues() {
        SpatialContextFactory spatialContextFactory = new SpatialContextFactory();
        SpatialContext spatialContext = spatialContextFactory.newSpatialContext();
        BBoxCalculator bboxCalculator = new BBoxCalculator(spatialContext);

        bboxCalculator.expandRange(1.0, 1.0, -388.0, 0.0);
        assertEquals(-388.0, bboxCalculator.getMinY(), 0.01);
    }

    @Test(timeout = 4000)
    public void testExpandRangeWithZeroAndNegativeValues() {
        SpatialContextFactory spatialContextFactory = new SpatialContextFactory();
        SpatialContext spatialContext = spatialContextFactory.newSpatialContext();
        BBoxCalculator bboxCalculator = new BBoxCalculator(spatialContext);

        bboxCalculator.expandRange(0.0, 1.0, 1.0, -388.0);
        assertEquals(-388.0, bboxCalculator.getMaxY(), 0.01);
        assertEquals(0.0, bboxCalculator.getMinX(), 0.01);
    }

    @Test(timeout = 4000)
    public void testExpandRangeWithNegativeAndPositiveValues() {
        SpatialContextFactory spatialContextFactory = new SpatialContextFactory();
        SpatialContext spatialContext = spatialContextFactory.newSpatialContext();
        BBoxCalculator bboxCalculator = new BBoxCalculator(spatialContext);

        bboxCalculator.expandRange(-388.0, 1.0, -388.0, -2148.3);
        assertEquals(-388.0, bboxCalculator.getMinY(), 0.01);
        assertEquals(-388.0, bboxCalculator.getMinX(), 0.01);
    }

    @Test(timeout = 4000)
    public void testGetBoundaryWithNullContext() {
        BBoxCalculator bboxCalculator = new BBoxCalculator(null);
        try {
            bboxCalculator.getBoundary();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.locationtech.spatial4j.shape.impl.BBoxCalculator", e);
        }
    }

    @Test(timeout = 4000)
    public void testExpandXRangeWithNullContext() {
        BBoxCalculator bboxCalculator = new BBoxCalculator(null);
        try {
            bboxCalculator.expandXRange(-751.777671, -751.777671);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.locationtech.spatial4j.shape.impl.BBoxCalculator", e);
        }
    }

    @Test(timeout = 4000)
    public void testExpandRangeWithNullRectangle() {
        SpatialContext spatialContext = SpatialContext.GEO;
        BBoxCalculator bboxCalculator = new BBoxCalculator(spatialContext);
        try {
            bboxCalculator.expandRange((Rectangle) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.locationtech.spatial4j.shape.impl.BBoxCalculator", e);
        }
    }

    @Test(timeout = 4000)
    public void testDoesXWorldWrapWithNullContext() {
        BBoxCalculator bboxCalculator = new BBoxCalculator(null);
        try {
            bboxCalculator.doesXWorldWrap();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.locationtech.spatial4j.shape.impl.BBoxCalculator", e);
        }
    }

    @Test(timeout = 4000)
    public void testInitialValuesForNewBBoxCalculator() {
        SpatialContext spatialContext = SpatialContext.GEO;
        BBoxCalculator bboxCalculator = new BBoxCalculator(spatialContext);

        assertEquals(Double.NEGATIVE_INFINITY, bboxCalculator.getMaxX(), 0.01);
        assertEquals(Double.NEGATIVE_INFINITY, bboxCalculator.getMaxY(), 0.01);
        assertEquals(Double.POSITIVE_INFINITY, bboxCalculator.getMinY(), 0.01);
    }

    @Test(timeout = 4000)
    public void testExpandRangeWithWorldBounds() {
        SpatialContext spatialContext = SpatialContext.GEO;
        BBoxCalculator bboxCalculator = new BBoxCalculator(spatialContext);
        Rectangle worldBounds = spatialContext.getWorldBounds();

        bboxCalculator.expandRange(worldBounds);
        bboxCalculator.getMaxX();
        boolean doesWrap = bboxCalculator.doesXWorldWrap();

        assertEquals(-90.0, bboxCalculator.getMinY(), 0.01);
        assertTrue(doesWrap);
    }

    @Test(timeout = 4000)
    public void testExpandXRangeWithPositiveInfinity() {
        SpatialContext spatialContext = SpatialContext.GEO;
        BBoxCalculator bboxCalculator = new BBoxCalculator(spatialContext);

        bboxCalculator.expandXRange(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        try {
            bboxCalculator.getBoundary();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.locationtech.spatial4j.shape.impl.ShapeFactoryImpl", e);
        }
    }

    @Test(timeout = 4000)
    public void testExpandXRangeWithNaN() {
        SpatialContext spatialContext = SpatialContext.GEO;
        BBoxCalculator bboxCalculator = new BBoxCalculator(spatialContext);

        bboxCalculator.expandXRange(1198.228879, Double.NaN);
        bboxCalculator.expandXRange(1198.228879, Double.NaN);

        assertEquals(Double.POSITIVE_INFINITY, bboxCalculator.getMinY(), 0.01);
        assertEquals(Double.NEGATIVE_INFINITY, bboxCalculator.getMaxY(), 0.01);
    }
}