package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class XYInterval_ESTestTest22 extends XYInterval_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        XYInterval xYInterval0 = new XYInterval((-4479.95885), (-4479.95885), (-446.52402887375), (-482.4691016522), 1.0);
        boolean boolean0 = xYInterval0.equals(xYInterval0);
        assertEquals((-446.52402887375), xYInterval0.getY(), 0.01);
        assertEquals((-482.4691016522), xYInterval0.getYLow(), 0.01);
        assertTrue(boolean0);
        assertEquals(1.0, xYInterval0.getYHigh(), 0.01);
        assertEquals((-4479.95885), xYInterval0.getXLow(), 0.01);
        assertEquals((-4479.95885), xYInterval0.getXHigh(), 0.01);
    }
}
