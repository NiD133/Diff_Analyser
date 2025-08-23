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

public class LocationTextExtractionStrategy_ESTestTest30 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        Vector vector0 = new Vector(898.6386F, 898.6386F, 898.6386F);
        PdfDate pdfDate0 = new PdfDate();
        GraphicsState graphicsState0 = new GraphicsState();
        graphicsState0.rise = (float) 3;
        Matrix matrix0 = graphicsState0.getCtm();
        PdfOCProperties pdfOCProperties0 = new PdfOCProperties();
        CMapAwareDocumentFont cMapAwareDocumentFont0 = new CMapAwareDocumentFont(pdfOCProperties0);
        graphicsState0.font = cMapAwareDocumentFont0;
        LinkedList<MarkedContentInfo> linkedList0 = new LinkedList<MarkedContentInfo>();
        TextRenderInfo textRenderInfo0 = new TextRenderInfo(pdfDate0, graphicsState0, matrix0, linkedList0);
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp locationTextExtractionStrategy_TextChunkLocationDefaultImp0 = new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(vector0, vector0, 12);
        LocationTextExtractionStrategy.TextChunkLocationStrategy locationTextExtractionStrategy_TextChunkLocationStrategy0 = mock(LocationTextExtractionStrategy.TextChunkLocationStrategy.class, new ViolatedAssumptionAnswer());
        doReturn(locationTextExtractionStrategy_TextChunkLocationDefaultImp0).when(locationTextExtractionStrategy_TextChunkLocationStrategy0).createLocation(any(com.itextpdf.text.pdf.parser.TextRenderInfo.class), any(com.itextpdf.text.pdf.parser.LineSegment.class));
        LocationTextExtractionStrategy locationTextExtractionStrategy0 = new LocationTextExtractionStrategy(locationTextExtractionStrategy_TextChunkLocationStrategy0);
        locationTextExtractionStrategy0.renderText(textRenderInfo0);
        assertEquals(12.0F, locationTextExtractionStrategy_TextChunkLocationDefaultImp0.getCharSpaceWidth(), 0.01F);
        assertEquals((-898), locationTextExtractionStrategy_TextChunkLocationDefaultImp0.distPerpendicular());
        assertEquals(0, locationTextExtractionStrategy_TextChunkLocationDefaultImp0.orientationMagnitude());
        assertEquals(898.6386F, locationTextExtractionStrategy_TextChunkLocationDefaultImp0.distParallelEnd(), 0.01F);
        assertEquals(898.6386F, locationTextExtractionStrategy_TextChunkLocationDefaultImp0.distParallelStart(), 0.01F);
    }
}
