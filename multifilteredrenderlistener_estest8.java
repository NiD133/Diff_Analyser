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

public class MultiFilteredRenderListener_ESTestTest8 extends MultiFilteredRenderListener_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test7() throws Throwable {
        MultiFilteredRenderListener multiFilteredRenderListener0 = new MultiFilteredRenderListener();
        multiFilteredRenderListener0.renderText((TextRenderInfo) null);
    }
}