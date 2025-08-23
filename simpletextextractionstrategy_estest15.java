package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfSigLockDictionary;
import com.itextpdf.text.pdf.PdfString;
import java.nio.CharBuffer;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import javax.swing.text.Segment;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SimpleTextExtractionStrategy_ESTestTest15 extends SimpleTextExtractionStrategy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        SimpleTextExtractionStrategy simpleTextExtractionStrategy0 = new SimpleTextExtractionStrategy();
        PdfSigLockDictionary.LockPermissions pdfSigLockDictionary_LockPermissions0 = PdfSigLockDictionary.LockPermissions.FORM_FILLING_AND_ANNOTATION;
        PdfSigLockDictionary pdfSigLockDictionary0 = new PdfSigLockDictionary(pdfSigLockDictionary_LockPermissions0);
        GraphicsState graphicsState0 = new GraphicsState();
        HashSet<MarkedContentInfo> hashSet0 = new HashSet<MarkedContentInfo>();
        ImageRenderInfo imageRenderInfo0 = ImageRenderInfo.createForXObject(graphicsState0, (PdfIndirectReference) null, (PdfDictionary) pdfSigLockDictionary0, (Collection<MarkedContentInfo>) hashSet0);
        simpleTextExtractionStrategy0.renderImage(imageRenderInfo0);
        assertEquals("", simpleTextExtractionStrategy0.getResultantText());
    }
}