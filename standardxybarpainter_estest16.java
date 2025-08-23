package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.text.DefaultCaret;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.chart.api.RectangleEdge;
import org.jfree.chart.util.GradientPaintTransformer;
import org.junit.runner.RunWith;

public class StandardXYBarPainter_ESTestTest16 extends StandardXYBarPainter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        StandardXYBarPainter standardXYBarPainter0 = new StandardXYBarPainter();
        Arc2D.Float arc2D_Float0 = new Arc2D.Float(1);
        BufferedImage bufferedImage0 = new BufferedImage(1, 1, 1);
        Graphics2D graphics2D0 = bufferedImage0.createGraphics();
        StackedXYBarRenderer stackedXYBarRenderer0 = new StackedXYBarRenderer(0.0F);
        RectangleEdge rectangleEdge0 = RectangleEdge.RIGHT;
        standardXYBarPainter0.paintBarShadow(graphics2D0, stackedXYBarRenderer0, 0, 0, arc2D_Float0, rectangleEdge0, false);
        assertTrue(stackedXYBarRenderer0.getShadowsVisible());
    }
}
