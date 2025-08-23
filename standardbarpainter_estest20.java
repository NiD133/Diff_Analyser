package org.jfree.chart.renderer.category;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;
import javax.swing.text.DefaultCaret;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.chart.api.RectangleEdge;
import org.jfree.chart.util.GradientPaintTransformer;
import org.junit.runner.RunWith;

public class StandardBarPainter_ESTestTest20 extends StandardBarPainter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        StandardBarPainter standardBarPainter0 = new StandardBarPainter();
        BufferedImage bufferedImage0 = new BufferedImage(4, 4, 4);
        GroupedStackedBarRenderer groupedStackedBarRenderer0 = new GroupedStackedBarRenderer();
        DefaultCaret defaultCaret0 = new DefaultCaret();
        Graphics2D graphics2D0 = bufferedImage0.createGraphics();
        groupedStackedBarRenderer0.setGradientPaintTransformer((GradientPaintTransformer) null);
        RectangleEdge rectangleEdge0 = RectangleEdge.TOP;
        standardBarPainter0.paintBar(graphics2D0, groupedStackedBarRenderer0, 0, 0, defaultCaret0, rectangleEdge0);
        assertEquals(0, groupedStackedBarRenderer0.getColumnCount());
    }
}
