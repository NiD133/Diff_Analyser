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

public class GeohashUtils_ESTestTest13 extends GeohashUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        String string0 = GeohashUtils.encodeLatLon(3859.99041074114, 3859.99041074114, 1773);
        // Undeclared exception!
        GeohashUtils.decode(string0, (SpatialContext) null);
    }
}
