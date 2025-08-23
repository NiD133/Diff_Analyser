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

public class SpatialContextFactory_ESTestTest44 extends SpatialContextFactory_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test43() throws Throwable {
        HashMap<String, String> hashMap0 = new HashMap<String, String>();
        hashMap0.put("spatialContextFactory", "org.locationtech.spatial4j.context.SpatialContextFactory");
        SpatialContext spatialContext0 = SpatialContextFactory.makeSpatialContext(hashMap0, (ClassLoader) null);
        assertFalse(spatialContext0.isNormWrapLongitude());
        assertTrue(spatialContext0.isGeo());
    }
}
