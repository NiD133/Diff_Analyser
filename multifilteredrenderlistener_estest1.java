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

public class MultiFilteredRenderListener_ESTestTest1 extends MultiFilteredRenderListener_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test0() throws Throwable {
        MultiFilteredRenderListener multiFilteredRenderListener0 = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy.TextChunkLocationStrategy locationTextExtractionStrategy_TextChunkLocationStrategy0 = mock(LocationTextExtractionStrategy.TextChunkLocationStrategy.class, new ViolatedAssumptionAnswer());
        LocationTextExtractionStrategy locationTextExtractionStrategy0 = new LocationTextExtractionStrategy(locationTextExtractionStrategy_TextChunkLocationStrategy0);
        RenderFilter[] renderFilterArray0 = new RenderFilter[7];
        multiFilteredRenderListener0.attachRenderListener(locationTextExtractionStrategy0, renderFilterArray0);
        multiFilteredRenderListener0.endTextBlock();
    }
}
