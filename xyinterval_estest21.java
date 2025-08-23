package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class XYInterval_ESTestTest21 extends XYInterval_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        XYInterval xYInterval0 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        Object object0 = new Object();
        boolean boolean0 = xYInterval0.equals(object0);
        assertEquals(0.0, xYInterval0.getYHigh(), 0.01);
        assertEquals(0.0, xYInterval0.getXLow(), 0.01);
        assertFalse(boolean0);
        assertEquals(0.0, xYInterval0.getYLow(), 0.01);
        assertEquals(0.0, xYInterval0.getY(), 0.01);
        assertEquals(0.0, xYInterval0.getXHigh(), 0.01);
    }
}
