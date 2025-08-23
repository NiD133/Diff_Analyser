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

public class MultiFilteredRenderListener_ESTestTest5 extends MultiFilteredRenderListener_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test4() throws Throwable {
        MultiFilteredRenderListener multiFilteredRenderListener0 = new MultiFilteredRenderListener();
        multiFilteredRenderListener0.attachRenderListener((LocationTextExtractionStrategy) null, (RenderFilter[]) null);
        // Undeclared exception!
        try {
            multiFilteredRenderListener0.endTextBlock();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.itextpdf.text.pdf.parser.MultiFilteredRenderListener", e);
        }
    }
}
