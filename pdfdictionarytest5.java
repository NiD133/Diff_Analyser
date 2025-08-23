package com.itextpdf.text.pdf;

import org.junit.Assert;
import org.junit.Test;

public class PdfDictionaryTestTest5 {

    @Test
    public void pdfDictionaryPutExThrowsExceptionIfKeyIsNull() {
        PdfDictionary dictionary = new PdfDictionary();
        try {
            dictionary.putEx(null, new PdfName("null"));
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "key is null.");
        }
    }
}
