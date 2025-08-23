package org.jfree.chart.entity;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.Collection;
import java.util.Iterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class StandardEntityCollection_ESTestTest2 extends StandardEntityCollection_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        StandardEntityCollection standardEntityCollection0 = new StandardEntityCollection();
        Line2D.Double line2D_Double0 = new Line2D.Double();
        ChartEntity chartEntity0 = new ChartEntity(line2D_Double0);
        standardEntityCollection0.add(chartEntity0);
        standardEntityCollection0.add(chartEntity0);
        ChartEntity chartEntity1 = standardEntityCollection0.getEntity(0.0, 0.0);
        assertNull(chartEntity1);
    }
}
