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

public class LocationTextExtractionStrategy_ESTestTest35 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test34() throws Throwable {
        GraphicsState graphicsState0 = new GraphicsState();
        PdfDictionary pdfDictionary0 = new PdfDictionary();
        ImageRenderInfo imageRenderInfo0 = ImageRenderInfo.createForXObject(graphicsState0, (PdfIndirectReference) null, pdfDictionary0);
        Vector vector0 = imageRenderInfo0.getStartPoint();
        LocationTextExtractionStrategy.TextChunk locationTextExtractionStrategy_TextChunk0 = new LocationTextExtractionStrategy.TextChunk("", vector0, vector0, 1114.7446F);
        locationTextExtractionStrategy_TextChunk0.getEndLocation();
        assertEquals(1114.7446F, locationTextExtractionStrategy_TextChunk0.getCharSpaceWidth(), 0.01F);
    }
}
