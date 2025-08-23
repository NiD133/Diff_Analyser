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

public class SpatialContextFactory_ESTestTest3 extends SpatialContextFactory_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        HashMap<String, String> hashMap0 = new HashMap<String, String>();
        hashMap0.put("readers", "%{Y,*MT'j<t9mHJr/M_");
        // Undeclared exception!
        try {
            SpatialContextFactory.makeSpatialContext(hashMap0, (ClassLoader) null);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Unable to find format class
            //
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }
}
