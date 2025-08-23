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

public class StandardEntityCollection_ESTestTest12 extends StandardEntityCollection_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        StandardEntityCollection standardEntityCollection0 = new StandardEntityCollection();
        Dimension dimension0 = new Dimension((-1), 0);
        Rectangle rectangle0 = new Rectangle(dimension0);
        rectangle0.setFrameFromDiagonal((double) (-1), (-3226.3016161567016), (-1490.292), (double) 0);
        ChartEntity chartEntity0 = new ChartEntity(rectangle0, ">", ">");
        standardEntityCollection0.add(chartEntity0);
        ChartEntity chartEntity1 = standardEntityCollection0.getEntity((double) (-1491), (double) (-1491));
        assertEquals("rect", chartEntity1.getShapeType());
    }
}
