package org.jfree.chart.renderer.category;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * This test class is an improved version of an auto-generated test.
 * It focuses on verifying the behavior of the StandardBarPainter class.
 */
public class StandardBarPainter_ESTestTest7 extends StandardBarPainter_ESTest_scaffolding {

    /**
     * Verifies that calling paintBarShadow with a null Graphics2D object
     * correctly throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void paintBarShadowWithNullGraphicsShouldThrowNPE() {
        // Arrange: Set up the painter and necessary parameters for the method call.
        StandardBarPainter painter = new StandardBarPainter();
        BarRenderer renderer = new WaterfallBarRenderer();
        Rectangle2D bar = new Rectangle2D.Double(0, 0, 10, 20);
        RectangleEdge barBaseEdge = RectangleEdge.TOP;
        int row = 0;
        int column = 0;
        boolean pegShadow = false;

        // Act: Call the method under test with a null Graphics2D context.
        // This is expected to throw the NullPointerException.
        painter.paintBarShadow(null, renderer, row, column, bar, barBaseEdge, pegShadow);

        // Assert: The @Test(expected) annotation handles the verification that
        // a NullPointerException was thrown.
    }
}