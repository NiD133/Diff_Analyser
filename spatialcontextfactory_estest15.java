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

public class SpatialContextFactory_ESTestTest15 extends SpatialContextFactory_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        HashMap<String, String> hashMap0 = new HashMap<String, String>();
        hashMap0.putIfAbsent("shapeFactoryClass", "org.locationtech.spatial4j.context.spatialcontext");
        ClassLoader classLoader0 = ClassLoader.getSystemClassLoader();
        // Undeclared exception!
        try {
            SpatialContextFactory.makeSpatialContext(hashMap0, classLoader0);
            fail("Expecting exception: NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
        }
    }
}
