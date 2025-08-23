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

public class StandardEntityCollection_ESTestTest8 extends StandardEntityCollection_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        StandardEntityCollection standardEntityCollection0 = new StandardEntityCollection();
        Line2D.Float line2D_Float0 = new Line2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
        ChartEntity chartEntity0 = new ChartEntity(line2D_Float0, " -ZHr,Q8I`#,X{EAHK", " -ZHr,Q8I`#,X{EAHK");
        standardEntityCollection0.add(chartEntity0);
        standardEntityCollection0.clone();
        assertEquals(1, standardEntityCollection0.getEntityCount());
    }
}
