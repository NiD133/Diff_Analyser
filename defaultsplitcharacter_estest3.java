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

public class DefaultSplitCharacter_ESTestTest3 extends DefaultSplitCharacter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        GreekList greekList0 = new GreekList();
        Chunk chunk0 = greekList0.getSymbol();
        PdfAction pdfAction0 = new PdfAction("[]~N<", true);
        PdfLine pdfLine0 = new PdfLine(2, 981.5F, 4, 1.0E-6F);
        PdfChunk pdfChunk0 = new PdfChunk(chunk0, pdfAction0);
        PdfChunk pdfChunk1 = new PdfChunk("(d{2,4}-d{2}-d{2,4})", pdfChunk0);
        PdfChunk pdfChunk2 = pdfLine0.add(pdfChunk1, (-1366.4F));
        assertNull(pdfChunk2);
    }
}
