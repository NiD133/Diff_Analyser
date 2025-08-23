package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class XYInterval_ESTestTest8 extends XYInterval_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        XYInterval xYInterval0 = new XYInterval(1.0, 1.0, 1.0, 1.0, 1.0);
        double double0 = xYInterval0.getYHigh();
        assertEquals(1.0, xYInterval0.getXLow(), 0.01);
        assertEquals(1.0, xYInterval0.getY(), 0.01);
        assertEquals(1.0, xYInterval0.getXHigh(), 0.01);
        assertEquals(1.0, double0, 0.01);
        assertEquals(1.0, xYInterval0.getYLow(), 0.01);
    }
}
