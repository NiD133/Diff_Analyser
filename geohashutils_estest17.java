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

public class GeohashUtils_ESTestTest17 extends GeohashUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        int int0 = GeohashUtils.lookupHashLenForWidthHeight(1.2490009027033011E-15, 1.2490009027033011E-15);
        assertEquals(24, int0);
    }
}
