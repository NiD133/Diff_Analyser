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

public class SpatialContextFactory_ESTestTest24 extends SpatialContextFactory_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        HashMap<String, String> hashMap0 = new HashMap<String, String>();
        hashMap0.put("shapeFactoryClass", "shapeFactoryClass");
        ClassLoader classLoader0 = ClassLoader.getSystemClassLoader();
        SpatialContextFactory spatialContextFactory0 = new SpatialContextFactory();
        // Undeclared exception!
        try {
            spatialContextFactory0.init(hashMap0, classLoader0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Invalid value 'shapeFactoryClass' on field shapeFactoryClass of type class java.lang.Class
            //
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }
}
