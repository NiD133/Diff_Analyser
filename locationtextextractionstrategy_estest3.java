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

public class LocationTextExtractionStrategy_ESTestTest3 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        GraphicsState graphicsState0 = new GraphicsState();
        byte[] byteArray0 = new byte[3];
        PdfSigLockDictionary.LockAction pdfSigLockDictionary_LockAction0 = PdfSigLockDictionary.LockAction.EXCLUDE;
        String[] stringArray0 = new String[0];
        PdfSigLockDictionary pdfSigLockDictionary0 = new PdfSigLockDictionary(pdfSigLockDictionary_LockAction0, stringArray0);
        InlineImageInfo inlineImageInfo0 = new InlineImageInfo(byteArray0, pdfSigLockDictionary0);
        LinkedList<MarkedContentInfo> linkedList0 = new LinkedList<MarkedContentInfo>();
        ImageRenderInfo imageRenderInfo0 = ImageRenderInfo.createForEmbeddedImage(graphicsState0, inlineImageInfo0, pdfSigLockDictionary0, linkedList0);
        Vector vector0 = imageRenderInfo0.getStartPoint();
        Vector vector1 = vector0.multiply(1163.3555F);
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp locationTextExtractionStrategy_TextChunkLocationDefaultImp0 = new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(vector0, vector1, 7);
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp locationTextExtractionStrategy_TextChunkLocationDefaultImp1 = new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(vector0, vector0, 4);
        boolean boolean0 = locationTextExtractionStrategy_TextChunkLocationDefaultImp1.isAtWordBoundary(locationTextExtractionStrategy_TextChunkLocationDefaultImp0);
        assertEquals(1163.3555F, locationTextExtractionStrategy_TextChunkLocationDefaultImp0.distParallelEnd(), 0.01F);
        assertFalse(boolean0);
        assertEquals(0, locationTextExtractionStrategy_TextChunkLocationDefaultImp1.distPerpendicular());
        assertEquals(0.0F, locationTextExtractionStrategy_TextChunkLocationDefaultImp1.distParallelStart(), 0.01F);
        assertEquals(0, locationTextExtractionStrategy_TextChunkLocationDefaultImp1.orientationMagnitude());
    }
}
