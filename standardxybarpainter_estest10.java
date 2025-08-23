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

public class StandardXYBarPainter_ESTestTest10 extends StandardXYBarPainter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        StandardXYBarPainter standardXYBarPainter0 = new StandardXYBarPainter();
        StandardXYBarPainter standardXYBarPainter1 = new StandardXYBarPainter();
        boolean boolean0 = standardXYBarPainter0.equals(standardXYBarPainter1);
        assertTrue(boolean0);
    }
}
