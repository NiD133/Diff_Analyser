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

public class DefaultSplitCharacter_ESTestTest8 extends DefaultSplitCharacter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        DefaultSplitCharacter defaultSplitCharacter0 = new DefaultSplitCharacter();
        char[] charArray0 = new char[0];
        PdfChunk[] pdfChunkArray0 = new PdfChunk[0];
        // Undeclared exception!
        try {
            defaultSplitCharacter0.isSplitCharacter((-1743), (-1743), (-1743), charArray0, pdfChunkArray0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // -1743
            //
            verifyException("com.itextpdf.text.pdf.DefaultSplitCharacter", e);
        }
    }
}
