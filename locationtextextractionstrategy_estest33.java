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

public class LocationTextExtractionStrategy_ESTestTest33 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test32() throws Throwable {
        PdfDate pdfDate0 = new PdfDate();
        GraphicsState graphicsState0 = new GraphicsState();
        Matrix matrix0 = new Matrix(8, 2);
        PdfOCProperties pdfOCProperties0 = new PdfOCProperties();
        CMapAwareDocumentFont cMapAwareDocumentFont0 = new CMapAwareDocumentFont(pdfOCProperties0);
        LinkedList<MarkedContentInfo> linkedList0 = new LinkedList<MarkedContentInfo>();
        graphicsState0.font = cMapAwareDocumentFont0;
        TextRenderInfo textRenderInfo0 = new TextRenderInfo(pdfDate0, graphicsState0, matrix0, linkedList0);
        LocationTextExtractionStrategy locationTextExtractionStrategy0 = new LocationTextExtractionStrategy();
        locationTextExtractionStrategy0.renderText(textRenderInfo0);
        LocationTextExtractionStrategy.TextChunkFilter locationTextExtractionStrategy_TextChunkFilter0 = mock(LocationTextExtractionStrategy.TextChunkFilter.class, new ViolatedAssumptionAnswer());
        doReturn(true).when(locationTextExtractionStrategy_TextChunkFilter0).accept(any(com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy.TextChunk.class));
        String string0 = locationTextExtractionStrategy0.getResultantText(locationTextExtractionStrategy_TextChunkFilter0);
        assertEquals("", string0);
    }
}