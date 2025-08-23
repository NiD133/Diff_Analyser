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

public class SpatialContextFactory_ESTestTest42 extends SpatialContextFactory_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test41() throws Throwable {
        HashMap<String, String> hashMap0 = new HashMap<String, String>();
        hashMap0.put("readers", ",");
        SpatialContextFactory spatialContextFactory0 = new SpatialContextFactory();
        spatialContextFactory0.args = (Map<String, String>) hashMap0;
        // Undeclared exception!
        try {
            spatialContextFactory0.initField("readers");
            fail("Expecting exception: Error");
        } catch (Error e) {
            //
            // unsupported field type: interface java.util.List
            //
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }
}
