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

public class DefaultSplitCharacter_ESTestTest13 extends DefaultSplitCharacter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        GreekList greekList0 = new GreekList();
        Chunk chunk0 = greekList0.getSymbol();
        PdfAction pdfAction0 = new PdfAction();
        PdfChunk pdfChunk0 = new PdfChunk(chunk0, pdfAction0);
        char[] charArray0 = new char[9];
        charArray0[2] = '-';
        DefaultSplitCharacter defaultSplitCharacter0 = new DefaultSplitCharacter(charArray0);
        PdfChunk[] pdfChunkArray0 = new PdfChunk[1];
        pdfChunkArray0[0] = pdfChunk0;
        boolean boolean0 = defaultSplitCharacter0.isSplitCharacter(16, 2, 2, charArray0, pdfChunkArray0);
        assertTrue(boolean0);
    }
}
