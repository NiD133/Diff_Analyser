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

public class SpatialContextFactory_ESTestTest27 extends SpatialContextFactory_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        SpatialContextFactory spatialContextFactory0 = new SpatialContextFactory();
        Class<PolyshapeReader> class0 = PolyshapeReader.class;
        spatialContextFactory0.addReaderIfNoggitExists(class0);
        assertTrue(spatialContextFactory0.geo);
        assertFalse(spatialContextFactory0.normWrapLongitude);
        assertFalse(spatialContextFactory0.hasFormatConfig);
    }
}
