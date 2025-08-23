package org.locationtech.spatial4j.context;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.HashMap;
import java.util.Map;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;
import org.locationtech.spatial4j.io.PolyshapeReader;
import org.locationtech.spatial4j.shape.ShapeFactory;

public class SpatialContextFactory_ESTestTest13 extends SpatialContextFactory_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        SpatialContextFactory spatialContextFactory0 = new SpatialContextFactory();
        Class<ShapeFactory> class0 = ShapeFactory.class;
        spatialContextFactory0.shapeFactoryClass = class0;
        // Undeclared exception!
        try {
            spatialContextFactory0.newSpatialContext();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // interface org.locationtech.spatial4j.shape.ShapeFactory needs a constructor that takes: [SpatialContext{geo=true, calculator=null, worldBounds=null}, org.locationtech.spatial4j.context.SpatialContextFactory@1]
            //
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }
}
