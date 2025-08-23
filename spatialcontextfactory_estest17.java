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

public class SpatialContextFactory_ESTestTest17 extends SpatialContextFactory_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        SpatialContextFactory spatialContextFactory0 = new SpatialContextFactory();
        // Undeclared exception!
        try {
            spatialContextFactory0.makeFormats((SpatialContext) null);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // org.evosuite.runtime.mock.java.lang.MockThrowable: java.lang.NullPointerException
            //
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }
}
