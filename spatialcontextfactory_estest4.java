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

public class SpatialContextFactory_ESTestTest4 extends SpatialContextFactory_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        HashMap<String, String> hashMap0 = new HashMap<String, String>();
        ClassLoader classLoader0 = ClassLoader.getSystemClassLoader();
        SpatialContextFactory spatialContextFactory0 = new SpatialContextFactory();
        spatialContextFactory0.init(hashMap0, classLoader0);
        spatialContextFactory0.initWorldBounds();
        assertFalse(spatialContextFactory0.hasFormatConfig);
        assertFalse(spatialContextFactory0.normWrapLongitude);
        assertTrue(spatialContextFactory0.geo);
    }
}
