package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class XYInterval_ESTestTest14 extends XYInterval_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        XYInterval xYInterval0 = new XYInterval(2850.5914155, 2850.5914155, 2850.5914155, 2850.5914155, (-1461.548));
        double double0 = xYInterval0.getXHigh();
        assertEquals(2850.5914155, xYInterval0.getXLow(), 0.01);
        assertEquals((-1461.548), xYInterval0.getYHigh(), 0.01);
        assertEquals(2850.5914155, double0, 0.01);
        assertEquals(2850.5914155, xYInterval0.getY(), 0.01);
        assertEquals(2850.5914155, xYInterval0.getYLow(), 0.01);
    }
}
