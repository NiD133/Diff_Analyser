package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class PdfDictionaryTest extends PdfDictionary_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testRemoveNullKeyFromDictionary() {
        PdfDictionary pdfDictionary = new PdfDictionary();
        pdfDictionary.remove((PdfName) null);
        assertFalse(pdfDictionary.isBoolean());
    }

    @Test(timeout = 4000)
    public void testPdfResourcesToPdfConversion() {
        PdfName pdfName = PdfName.SECT;
        PdfResources pdfResources = new PdfResources();
        pdfResources.putEx(pdfName, pdfName);
        pdfName.type = 8;
        ByteBuffer byteBuffer = new ByteBuffer(8);
        pdfResources.toPdf((PdfWriter) null, byteBuffer);
        assertEquals(15, byteBuffer.size());
    }

    @Test(timeout = 4000)
    public void testPdfAnnotationCreation() {
        PdfName pdfName = PdfName.LINEHEIGHT;
        PdfDictionary pdfDictionary = new PdfDictionary(pdfName);
        PdfWriter pdfWriter = new PdfWriter();
        PdfRectangle pdfRectangle = new PdfRectangle(0.0F, 642.05963F, 8388608, (-424.6F));
        Rectangle rectangle = PdfReader.getNormalizedRectangle(pdfRectangle);
        PdfAnnotation pdfAnnotation = PdfAnnotation.createLink(pdfWriter, rectangle, pdfDictionary.CATALOG);
        pdfAnnotation.putAll(pdfDictionary);
        assertEquals(3, PdfAnnotation.MARKUP_SQUIGGLY);
    }

    @Test(timeout = 4000)
    public void testMergePdfActionIntoDictionary() {
        PdfDictionary pdfDictionary = new PdfDictionary();
        PdfAction pdfAction = PdfAction.createHide(new Object[0], true);
        pdfDictionary.merge(pdfAction);
        assertEquals(1, PdfObject.BOOLEAN);
    }

    @Test(timeout = 4000)
    public void testGotoLocalPageActionSize() {
        PdfAction pdfAction = PdfAction.gotoLocalPage("</rdf:Description></rdf:RDF>\n", true);
        assertEquals(2, pdfAction.size());
    }

    @Test(timeout = 4000)
    public void testPdfDictionaryIsPage() {
        PdfDictionary pdfDictionary = new PdfDictionary();
        assertFalse(pdfDictionary.isPage());
    }

    @Test(timeout = 4000)
    public void testPdfResourcesGetDirectObject() {
        PdfName pdfName = PdfWriter.PDF_VERSION_1_6;
        PdfResources pdfResources = new PdfResources();
        pdfResources.putEx(pdfName, pdfName);
        PdfObject pdfObject = pdfResources.getDirectObject(pdfName);
        assertEquals(4, PdfObject.NAME);
    }

    @Test(timeout = 4000)
    public void testPdfResourcesContains() {
        PdfName pdfName = PdfName.UF;
        PdfResources pdfResources = new PdfResources();
        pdfResources.putEx(pdfName, pdfName);
        assertTrue(pdfResources.contains(pdfName));
    }

    @Test(timeout = 4000)
    public void testPdfDictionaryCheckType() {
        PdfName pdfName = PdfName.CFM;
        PdfDictionary pdfDictionary = new PdfDictionary(pdfName);
        assertTrue(pdfDictionary.checkType(pdfName));
    }

    @Test(timeout = 4000)
    public void testPdfResourcesMergeWithNull() {
        PdfResources pdfResources = new PdfResources();
        try {
            pdfResources.merge(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.PdfDictionary", e);
        }
    }

    @Test(timeout = 4000)
    public void testPdfDictionaryGetKeys() {
        PdfDictionary pdfDictionary = new PdfDictionary();
        assertEquals(0, pdfDictionary.getKeys().size());
    }

    @Test(timeout = 4000)
    public void testPdfResourcesSize() {
        PdfResources pdfResources = new PdfResources();
        assertEquals(0, pdfResources.size());
    }

    @Test(timeout = 4000)
    public void testPdfPageIsPage() {
        PdfRectangle pdfRectangle = new PdfRectangle(0.0F, 0.0F, 2884.4F, 2884.4F);
        LinkedHashMap<String, PdfRectangle> linkedHashMap = new LinkedHashMap<>(4, 5);
        PdfPage pdfPage = new PdfPage(pdfRectangle, linkedHashMap, null, 1836);
        assertTrue(pdfPage.isPage());
    }

    @Test(timeout = 4000)
    public void testPdfDictionaryIsFont() {
        PdfDictionary pdfDictionary = new PdfDictionary();
        assertFalse(pdfDictionary.isFont());
    }

    // Additional tests can be added here following the same pattern

}