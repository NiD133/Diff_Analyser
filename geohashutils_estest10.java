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

public class GeohashUtils_ESTestTest10 extends GeohashUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        GeohashUtils.encodeLatLon((double) 24, (-1258.5428078205061));
        GeohashUtils.encodeLatLon((-1422.830305), 803.7685944);
        GeohashUtils.encodeLatLon((double) 24, (-1.7976931348623157E308), 1970);
        // Undeclared exception!
        GeohashUtils.encodeLatLon((-1.0), (-1258.5428078205061));
    }
}
