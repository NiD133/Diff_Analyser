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

public class StandardEntityCollection_ESTestTest3 extends StandardEntityCollection_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        StandardEntityCollection standardEntityCollection0 = new StandardEntityCollection();
        Point point0 = new Point(380, 4376);
        Rectangle rectangle0 = new Rectangle(point0);
        ChartEntity chartEntity0 = new ChartEntity(rectangle0, "E',gl8z/3O[2/[", "E',gl8z/3O[2/[");
        standardEntityCollection0.add(chartEntity0);
        int int0 = standardEntityCollection0.getEntityCount();
        assertEquals(1, int0);
    }
}