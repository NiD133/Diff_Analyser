package org.jfree.chart.block;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Readable, targeted tests for LabelBlock.
 * The tests verify equality sensitivity to each field, cloning, and serialization.
 */
public class LabelBlockTest {

    private static final String BASE_TEXT = "ABC";
    private static final String ALT_TEXT = "XYZ";

    private static final Font DIALOG_PLAIN_12 = new Font("Dialog", Font.PLAIN, 12);
    private static final Font DIALOG_BOLD_12 = new Font("Dialog", Font.BOLD, 12);

    private static LabelBlock newLabel(String text, Font font, java.awt.Paint paint) {
        return new LabelBlock(text, font, paint);
    }

    @Test
    @DisplayName("equals: same instance and identical state")
    public void equals_whenAllPropertiesEqual_returnsTrue() {
        LabelBlock a = newLabel(BASE_TEXT, DIALOG_PLAIN_12, Color.RED);
        LabelBlock b = newLabel(BASE_TEXT, DIALOG_PLAIN_12, Color.RED);

        // reflexive
        assertEquals(a, a, "An object should equal itself");
        // symmetric baseline
        assertEquals(a, b, "Two LabelBlocks with identical state should be equal");
        assertEquals(b, a, "Equality should be symmetric");
    }

    @Test
    @DisplayName("equals: different text -> not equal")
    public void equals_whenTextDiffers_returnsFalse() {
        LabelBlock a = newLabel(BASE_TEXT, DIALOG_PLAIN_12, Color.RED);
        LabelBlock b = newLabel(BASE_TEXT, DIALOG_PLAIN_12, Color.RED);

        // text is final, create a new instance with different text
        a = newLabel(ALT_TEXT, DIALOG_PLAIN_12, Color.RED);
        assertNotEquals(a, b, "Blocks with different text should not be equal");

        // make b match and confirm equality again
        b = newLabel(ALT_TEXT, DIALOG_PLAIN_12, Color.RED);
        assertEquals(a, b, "Blocks should be equal after matching text");
    }

    @Test
    @DisplayName("equals: different font -> not equal, match via setter -> equal")
    public void equals_whenFontDiffers_returnsFalseThenTrueAfterMatch() {
        LabelBlock a = newLabel(BASE_TEXT, DIALOG_PLAIN_12, Color.RED);
        LabelBlock b = newLabel(BASE_TEXT, DIALOG_PLAIN_12, Color.RED);

        a.setFont(DIALOG_BOLD_12);
        assertNotEquals(a, b, "Blocks with different fonts should not be equal");

        b.setFont(DIALOG_BOLD_12);
        assertEquals(a, b, "Blocks should be equal after matching font");
    }

    @Test
    @DisplayName("equals: different paint -> not equal, match via setter -> equal")
    public void equals_whenPaintDiffers_returnsFalseThenTrueAfterMatch() {
        LabelBlock a = newLabel(BASE_TEXT, DIALOG_PLAIN_12, Color.RED);
        LabelBlock b = newLabel(BASE_TEXT, DIALOG_PLAIN_12, Color.RED);

        a.setPaint(Color.BLUE);
        assertNotEquals(a, b, "Blocks with different paints should not be equal");

        b.setPaint(Color.BLUE);
        assertEquals(a, b, "Blocks should be equal after matching paint");
    }

    @Test
    @DisplayName("equals: different toolTipText -> not equal, match -> equal")
    public void equals_whenToolTipTextDiffers_returnsFalseThenTrueAfterMatch() {
        LabelBlock a = newLabel(BASE_TEXT, DIALOG_PLAIN_12, Color.RED);
        LabelBlock b = newLabel(BASE_TEXT, DIALOG_PLAIN_12, Color.RED);

        a.setToolTipText("Tooltip");
        assertNotEquals(a, b, "Blocks with different tooltips should not be equal");

        b.setToolTipText("Tooltip");
        assertEquals(a, b, "Blocks should be equal after matching tooltip");
    }

    @Test
    @DisplayName("equals: different urlText -> not equal, match -> equal")
    public void equals_whenUrlTextDiffers_returnsFalseThenTrueAfterMatch() {
        LabelBlock a = newLabel(BASE_TEXT, DIALOG_PLAIN_12, Color.RED);
        LabelBlock b = newLabel(BASE_TEXT, DIALOG_PLAIN_12, Color.RED);

        a.setURLText("URL");
        assertNotEquals(a, b, "Blocks with different URLs should not be equal");

        b.setURLText("URL");
        assertEquals(a, b, "Blocks should be equal after matching URL");
    }

    @Test
    @DisplayName("equals: different contentAlignmentPoint -> not equal, match -> equal")
    public void equals_whenContentAlignmentPointDiffers_returnsFalseThenTrueAfterMatch() {
        LabelBlock a = newLabel(BASE_TEXT, DIALOG_PLAIN_12, Color.RED);
        LabelBlock b = newLabel(BASE_TEXT, DIALOG_PLAIN_12, Color.RED);

        a.setContentAlignmentPoint(TextBlockAnchor.CENTER_RIGHT);
        assertNotEquals(a, b, "Blocks with different content alignment points should not be equal");

        b.setContentAlignmentPoint(TextBlockAnchor.CENTER_RIGHT);
        assertEquals(a, b, "Blocks should be equal after matching content alignment point");
    }

    @Test
    @DisplayName("equals: different textAnchor -> not equal, match -> equal")
    public void equals_whenTextAnchorDiffers_returnsFalseThenTrueAfterMatch() {
        LabelBlock a = newLabel(BASE_TEXT, DIALOG_PLAIN_12, Color.RED);
        LabelBlock b = newLabel(BASE_TEXT, DIALOG_PLAIN_12, Color.RED);

        a.setTextAnchor(RectangleAnchor.BOTTOM_RIGHT);
        assertNotEquals(a, b, "Blocks with different text anchors should not be equal");

        b.setTextAnchor(RectangleAnchor.BOTTOM_RIGHT);
        assertEquals(a, b, "Blocks should be equal after matching text anchor");
    }

    @Test
    @DisplayName("clone: deep copy with equal state")
    public void cloning_createsEqualButDistinctInstance() throws CloneNotSupportedException {
        LabelBlock original = newLabel(BASE_TEXT, DIALOG_PLAIN_12, Color.RED);

        LabelBlock clone = CloneUtils.clone(original);

        assertNotSame(original, clone, "Clone should be a different instance");
        assertSame(original.getClass(), clone.getClass(), "Clone should have the same runtime type");
        assertEquals(original, clone, "Clone should be equal to the original");
    }

    @Test
    @DisplayName("serialization: round-trip preserves state")
    public void serialization_roundTripPreservesEquality() {
        GradientPaint gradient = new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.BLUE);
        LabelBlock original = newLabel(BASE_TEXT, DIALOG_PLAIN_12, gradient);

        LabelBlock restored = TestUtils.serialised(original);

        assertEquals(original, restored, "Deserialized instance should be equal to the original");
    }
}