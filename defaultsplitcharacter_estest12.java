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

public class DefaultSplitCharacter_ESTestTest12 extends DefaultSplitCharacter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        GreekList greekList0 = new GreekList(true, 0);
        Chunk chunk0 = greekList0.getSymbol();
        PdfAction pdfAction0 = new PdfAction();
        TabSettings tabSettings0 = new TabSettings();
        PdfChunk pdfChunk0 = new PdfChunk(chunk0, pdfAction0, tabSettings0);
        ArrayList<PdfChunk> arrayList0 = new ArrayList<PdfChunk>();
        PdfLine pdfLine0 = new PdfLine(0.0F, 2, 3013.6F, 512, false, arrayList0, true);
        PdfChunk pdfChunk1 = pdfLine0.add(pdfChunk0);
        assertNull(pdfChunk1);
    }
}
