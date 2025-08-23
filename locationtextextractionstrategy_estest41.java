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

public class LocationTextExtractionStrategy_ESTestTest41 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test40() throws Throwable {
        LocationTextExtractionStrategy locationTextExtractionStrategy0 = new LocationTextExtractionStrategy();
        GraphicsState graphicsState0 = new GraphicsState();
        PdfSigLockDictionary.LockPermissions pdfSigLockDictionary_LockPermissions0 = PdfSigLockDictionary.LockPermissions.FORM_FILLING;
        PdfSigLockDictionary pdfSigLockDictionary0 = new PdfSigLockDictionary(pdfSigLockDictionary_LockPermissions0);
        java.util.Vector<MarkedContentInfo> vector0 = new java.util.Vector<MarkedContentInfo>();
        ImageRenderInfo imageRenderInfo0 = ImageRenderInfo.createForXObject(graphicsState0, (PdfIndirectReference) null, (PdfDictionary) pdfSigLockDictionary0, (Collection<MarkedContentInfo>) vector0);
        locationTextExtractionStrategy0.renderImage(imageRenderInfo0);
        assertEquals(1.0F, imageRenderInfo0.getArea(), 0.01F);
    }
}
