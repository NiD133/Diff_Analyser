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

public class GeohashUtils_ESTestTest21 extends GeohashUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        double[] doubleArray0 = GeohashUtils.lookupDegreesSizeForHashLen(16);
        assertArrayEquals(new double[] { 1.6370904631912708E-10, 3.2741809263825417E-10 }, doubleArray0, 0.01);
    }
}
