package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.GreekList;
import com.itextpdf.text.TabSettings;
import java.util.ArrayList;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class DefaultSplitCharacter_ESTestTest1 extends DefaultSplitCharacter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        DefaultSplitCharacter defaultSplitCharacter0 = new DefaultSplitCharacter();
        char[] charArray0 = new char[2];
        PdfChunk[] pdfChunkArray0 = new PdfChunk[1];
        // Undeclared exception!
        try {
            defaultSplitCharacter0.getCurrentCharacter(6724, charArray0, pdfChunkArray0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 6724
            //
            verifyException("com.itextpdf.text.pdf.DefaultSplitCharacter", e);
        }
    }
}
