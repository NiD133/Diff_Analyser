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

public class StandardEntityCollection_ESTestTest13 extends StandardEntityCollection_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        StandardEntityCollection standardEntityCollection0 = new StandardEntityCollection();
        Iterator<ChartEntity> iterator0 = standardEntityCollection0.iterator();
        assertNotNull(iterator0);
    }
}
