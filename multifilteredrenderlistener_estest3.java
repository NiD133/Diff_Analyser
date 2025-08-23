package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.itextpdf.awt.geom.Rectangle2D;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class MultiFilteredRenderListener_ESTestTest3 extends MultiFilteredRenderListener_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test2() throws Throwable {
        MultiFilteredRenderListener multiFilteredRenderListener0 = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy.TextChunkLocationStrategy locationTextExtractionStrategy_TextChunkLocationStrategy0 = mock(LocationTextExtractionStrategy.TextChunkLocationStrategy.class, new ViolatedAssumptionAnswer());
        LocationTextExtractionStrategy locationTextExtractionStrategy0 = new LocationTextExtractionStrategy(locationTextExtractionStrategy_TextChunkLocationStrategy0);
        RenderFilter[] renderFilterArray0 = new RenderFilter[3];
        Rectangle2D.Double rectangle2D_Double0 = new Rectangle2D.Double((-2030.332760438), (-2030.332760438), (-2030.332760438), (-846.7357066836466));
        RegionTextRenderFilter regionTextRenderFilter0 = new RegionTextRenderFilter(rectangle2D_Double0);
        renderFilterArray0[0] = (RenderFilter) regionTextRenderFilter0;
        renderFilterArray0[1] = (RenderFilter) regionTextRenderFilter0;
        renderFilterArray0[2] = (RenderFilter) regionTextRenderFilter0;
        multiFilteredRenderListener0.attachRenderListener(locationTextExtractionStrategy0, renderFilterArray0);
        multiFilteredRenderListener0.renderImage((ImageRenderInfo) null);
    }
}
