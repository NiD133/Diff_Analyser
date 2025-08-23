package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class XYInterval_ESTestTest20 extends XYInterval_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        XYInterval xYInterval0 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        XYInterval xYInterval1 = new XYInterval(1.0, 0.0, 0.0, 0.0, 0.0);
        boolean boolean0 = xYInterval0.equals(xYInterval1);
        assertEquals(0.0, xYInterval1.getY(), 0.01);
        assertEquals(0.0, xYInterval0.getXLow(), 0.01);
        assertFalse(xYInterval1.equals((Object) xYInterval0));
        assertFalse(boolean0);
        assertEquals(0.0, xYInterval1.getYHigh(), 0.01);
        assertEquals(0.0, xYInterval1.getYLow(), 0.01);
        assertEquals(0.0, xYInterval1.getXHigh(), 0.01);
    }
}
