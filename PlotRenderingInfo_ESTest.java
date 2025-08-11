import static org.junit.Assert.*;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.junit.Test;

/**
 * Tests for PlotRenderingInfo focusing on readability and maintainability.
 */
public class PlotRenderingInfoTest {

    // Helper to create a simple rectangle with stable coordinates
    private static Rectangle2D rect(double x, double y, double w, double h) {
        return new Rectangle2D.Double(x, y, w, h);
    }

    // ----------------------------------------------------------------------
    // Owner
    // ----------------------------------------------------------------------

    @Test
    public void owner_isStoredAndReturned() {
        ChartRenderingInfo owner = new ChartRenderingInfo();
        PlotRenderingInfo info = new PlotRenderingInfo(owner);

        assertSame(owner, info.getOwner());
    }

    @Test
    public void owner_canBeNull() {
        PlotRenderingInfo info = new PlotRenderingInfo(null);

        assertNull(info.getOwner());
    }

    // ----------------------------------------------------------------------
    // Plot area
    // ----------------------------------------------------------------------

    @Test
    public void plotArea_isNullByDefault() {
        PlotRenderingInfo info = new PlotRenderingInfo(new ChartRenderingInfo());

        assertNull(info.getPlotArea());
    }

    @Test
    public void plotArea_roundTripsThroughSetterAndGetter() {
        PlotRenderingInfo info = new PlotRenderingInfo(new ChartRenderingInfo());
        Rectangle2D r = rect(10, 20, 30, 40);

        info.setPlotArea(r);

        Rectangle2D actual = info.getPlotArea();
        assertNotNull(actual);
        assertEquals(r.getX(), actual.getX(), 0.0);
        assertEquals(r.getY(), actual.getY(), 0.0);
        assertEquals(r.getWidth(), actual.getWidth(), 0.0);
        assertEquals(r.getHeight(), actual.getHeight(), 0.0);
    }

    // ----------------------------------------------------------------------
    // Data area
    // ----------------------------------------------------------------------

    @Test
    public void dataArea_isNonNullByDefaultForNewInstance() {
        PlotRenderingInfo info = new PlotRenderingInfo(new ChartRenderingInfo());

        assertNotNull(info.getDataArea());
    }

    @Test
    public void dataArea_roundTripsThroughSetterAndGetter() {
        PlotRenderingInfo info = new PlotRenderingInfo(new ChartRenderingInfo());
        Rectangle2D r = rect(1, 2, 100, 200);

        info.setDataArea(r);

        Rectangle2D actual = info.getDataArea();
        assertNotNull(actual);
        assertEquals(r.getX(), actual.getX(), 0.0);
        assertEquals(r.getY(), actual.getY(), 0.0);
        assertEquals(r.getWidth(), actual.getWidth(), 0.0);
        assertEquals(r.getHeight(), actual.getHeight(), 0.0);
    }

    @Test
    public void dataArea_canBeSetToNull() {
        PlotRenderingInfo info = new PlotRenderingInfo(new ChartRenderingInfo());

        info.setDataArea(null);

        assertNull(info.getDataArea());
    }

    // ----------------------------------------------------------------------
    // Subplots
    // ----------------------------------------------------------------------

    @Test
    public void subplots_emptyByDefault() {
        PlotRenderingInfo info = new PlotRenderingInfo(new ChartRenderingInfo());

        assertEquals(0, info.getSubplotCount());
    }

    @Test
    public void subplot_addCountAndRetrieve() {
        ChartRenderingInfo owner = new ChartRenderingInfo(new StandardEntityCollection());
        PlotRenderingInfo parent = new PlotRenderingInfo(owner);
        PlotRenderingInfo child = new PlotRenderingInfo(owner);

        parent.addSubplotInfo(child);

        assertEquals(1, parent.getSubplotCount());
        assertSame(child, parent.getSubplotInfo(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void subplot_getWithInvalidIndex_throws() {
        PlotRenderingInfo info = new PlotRenderingInfo(new ChartRenderingInfo());

        // No subplots added, index 1 is invalid
        info.getSubplotInfo(1);
    }

    @Test
    public void subplotIndex_withNoSubplots_returnsMinusOne() {
        PlotRenderingInfo info = new PlotRenderingInfo(new ChartRenderingInfo());

        int idx = info.getSubplotIndex(new Point2D.Double(5, 5));

        assertEquals(-1, idx);
    }

    @Test(expected = IllegalArgumentException.class)
    public void subplotIndex_withNullSource_throwsIAE() {
        PlotRenderingInfo info = new PlotRenderingInfo(new ChartRenderingInfo());

        info.getSubplotIndex(null);
    }

    // ----------------------------------------------------------------------
    // Equality and hash code
    // ----------------------------------------------------------------------

    @Test
    public void equals_isReflexiveAndSymmetric_andHashCodeConsistent() {
        ChartRenderingInfo owner = new ChartRenderingInfo();
        PlotRenderingInfo a = new PlotRenderingInfo(owner);
        PlotRenderingInfo b = new PlotRenderingInfo(owner);

        // Same default state => equal
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
        assertTrue(a.equals(a));

        // Equal objects must have same hashCode
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_differsWhenPlotAreaDiffers() {
        ChartRenderingInfo owner = new ChartRenderingInfo();
        PlotRenderingInfo a = new PlotRenderingInfo(owner);
        PlotRenderingInfo b = new PlotRenderingInfo(owner);

        b.setPlotArea(rect(0, 0, 10, 10));

        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
    }

    @Test
    public void equals_differsWhenSubplotsDiffer() {
        ChartRenderingInfo owner = new ChartRenderingInfo();
        PlotRenderingInfo a = new PlotRenderingInfo(owner);
        PlotRenderingInfo b = new PlotRenderingInfo(owner);

        // Mutate only 'a'
        a.addSubplotInfo(new PlotRenderingInfo(owner));

        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
    }

    // ----------------------------------------------------------------------
    // Cloning
    // ----------------------------------------------------------------------

    @Test
    public void clone_createsEqualButIndependentCopy() throws CloneNotSupportedException {
        ChartRenderingInfo owner = new ChartRenderingInfo();
        PlotRenderingInfo original = new PlotRenderingInfo(owner);
        original.setPlotArea(rect(10, 10, 50, 50));
        original.setDataArea(rect(0, 0, 100, 100));
        original.addSubplotInfo(new PlotRenderingInfo(owner));

        PlotRenderingInfo copy = (PlotRenderingInfo) original.clone();

        assertNotSame(original, copy);
        assertTrue(original.equals(copy));
        assertEquals(original.hashCode(), copy.hashCode());

        // Verify independence by mutating the clone
        copy.setPlotArea(rect(1, 1, 2, 2));
        assertFalse(original.equals(copy));
    }

    @Test
    public void clone_worksWhenDataAreaIsNull() throws CloneNotSupportedException {
        PlotRenderingInfo original = new PlotRenderingInfo(new ChartRenderingInfo());
        original.setDataArea(null);

        PlotRenderingInfo copy = (PlotRenderingInfo) original.clone();

        assertNotSame(original, copy);
        assertTrue(original.equals(copy));
    }
}