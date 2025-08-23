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

public class LocationTextExtractionStrategy_ESTestTest27 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        LocationTextExtractionStrategy.TextChunkLocationStrategy locationTextExtractionStrategy_TextChunkLocationStrategy0 = mock(LocationTextExtractionStrategy.TextChunkLocationStrategy.class, new ViolatedAssumptionAnswer());
        LocationTextExtractionStrategy locationTextExtractionStrategy0 = new LocationTextExtractionStrategy(locationTextExtractionStrategy_TextChunkLocationStrategy0);
        Vector vector0 = new Vector(1.0E-4F, 480.96F, 2.0F);
        Vector vector1 = vector0.multiply(16.0301F);
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp locationTextExtractionStrategy_TextChunkLocationDefaultImp0 = new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(vector1, vector0, (-435.7F));
        LocationTextExtractionStrategy.TextChunk locationTextExtractionStrategy_TextChunk0 = new LocationTextExtractionStrategy.TextChunk("", locationTextExtractionStrategy_TextChunkLocationDefaultImp0);
        boolean boolean0 = locationTextExtractionStrategy0.isChunkAtWordBoundary(locationTextExtractionStrategy_TextChunk0, locationTextExtractionStrategy_TextChunk0);
        assertEquals((-1570), locationTextExtractionStrategy_TextChunkLocationDefaultImp0.orientationMagnitude());
        assertFalse(boolean0);
        assertEquals((-480.96417F), locationTextExtractionStrategy_TextChunkLocationDefaultImp0.distParallelEnd(), 0.01F);
        assertEquals((-435.7F), locationTextExtractionStrategy_TextChunk0.getCharSpaceWidth(), 0.01F);
    }
}