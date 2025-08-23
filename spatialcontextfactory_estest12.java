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

public class SpatialContextFactory_ESTestTest12 extends SpatialContextFactory_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        SpatialContextFactory spatialContextFactory0 = new SpatialContextFactory();
        HashMap<String, String> hashMap0 = new HashMap<String, String>();
        ClassLoader classLoader0 = ClassLoader.getSystemClassLoader();
        SpatialContext spatialContext0 = SpatialContextFactory.makeSpatialContext(hashMap0, classLoader0);
        spatialContextFactory0.makeBinaryCodec(spatialContext0);
        assertTrue(spatialContextFactory0.geo);
        assertFalse(spatialContextFactory0.normWrapLongitude);
    }
}
