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

public class GeohashUtils_ESTestTest9 extends GeohashUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        // Undeclared exception!
        try {
            GeohashUtils.encodeLatLon(0.017453292519943295, 0.017453292519943295, (-3030));
            fail("Expecting exception: NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.lang.AbstractStringBuilder", e);
        }
    }
}