package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfOCProperties;
import com.itextpdf.text.pdf.PdfSigLockDictionary;
import com.itextpdf.text.pdf.PdfString;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Collection;
import java.util.LinkedList;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class LocationTextExtractionStrategy_ESTestTest23 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        LocationTextExtractionStrategy locationTextExtractionStrategy0 = new LocationTextExtractionStrategy();
        LocationTextExtractionStrategy.DUMP_STATE = true;
        LocationTextExtractionStrategy.TextChunkFilter locationTextExtractionStrategy_TextChunkFilter0 = mock(LocationTextExtractionStrategy.TextChunkFilter.class, new ViolatedAssumptionAnswer());
        String string0 = locationTextExtractionStrategy0.getResultantText(locationTextExtractionStrategy_TextChunkFilter0);
        assertEquals("", string0);
    }
}
