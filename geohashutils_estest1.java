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

public class GeohashUtils_ESTestTest1 extends GeohashUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        HashMap<String, String> hashMap0 = new HashMap<String, String>();
        ClassLoader classLoader0 = ClassLoader.getSystemClassLoader();
        SpatialContext spatialContext0 = SpatialContextFactory.makeSpatialContext(hashMap0, classLoader0);
        // Undeclared exception!
        try {
            GeohashUtils.decode("R9ENOYUZj]oNX(A", spatialContext0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // -8
            //
            verifyException("org.locationtech.spatial4j.io.GeohashUtils", e);
        }
    }
}
