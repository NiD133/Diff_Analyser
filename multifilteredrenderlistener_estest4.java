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

public class MultiFilteredRenderListener_ESTestTest4 extends MultiFilteredRenderListener_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test3() throws Throwable {
        MultiFilteredRenderListener multiFilteredRenderListener0 = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy.TextChunkLocationStrategy locationTextExtractionStrategy_TextChunkLocationStrategy0 = mock(LocationTextExtractionStrategy.TextChunkLocationStrategy.class, new ViolatedAssumptionAnswer());
        LocationTextExtractionStrategy locationTextExtractionStrategy0 = new LocationTextExtractionStrategy(locationTextExtractionStrategy_TextChunkLocationStrategy0);
        RenderFilter[] renderFilterArray0 = new RenderFilter[3];
        multiFilteredRenderListener0.attachRenderListener(locationTextExtractionStrategy0, renderFilterArray0);
        // Undeclared exception!
        try {
            multiFilteredRenderListener0.renderImage((ImageRenderInfo) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.itextpdf.text.pdf.parser.MultiFilteredRenderListener", e);
        }
    }
}
