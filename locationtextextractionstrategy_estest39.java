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

public class LocationTextExtractionStrategy_ESTestTest39 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test38() throws Throwable {
        LocationTextExtractionStrategy.TextChunkLocationStrategy locationTextExtractionStrategy_TextChunkLocationStrategy0 = mock(LocationTextExtractionStrategy.TextChunkLocationStrategy.class, new ViolatedAssumptionAnswer());
        LocationTextExtractionStrategy locationTextExtractionStrategy0 = new LocationTextExtractionStrategy(locationTextExtractionStrategy_TextChunkLocationStrategy0);
        locationTextExtractionStrategy0.endTextBlock();
    }
}
