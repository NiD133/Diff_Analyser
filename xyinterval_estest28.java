package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class XYInterval_ESTestTest28 extends XYInterval_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        XYInterval xYInterval0 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        double double0 = xYInterval0.getYLow();
        assertEquals(0.0, xYInterval0.getYHigh(), 0.01);
        assertEquals(0.0, double0, 0.01);
        assertEquals(0.0, xYInterval0.getXLow(), 0.01);
        assertEquals(0.0, xYInterval0.getXHigh(), 0.01);
        assertEquals(0.0, xYInterval0.getY(), 0.01);
    }
}
