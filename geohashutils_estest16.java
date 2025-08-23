package org.locationtech.spatial4j.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.HashMap;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;

public class GeohashUtils_ESTestTest16 extends GeohashUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        int int0 = GeohashUtils.lookupHashLenForWidthHeight(1.0728836059570312E-5, 1115.07072940934);
        assertEquals(11, int0);
    }
}
