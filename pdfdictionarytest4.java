package com.itextpdf.text.pdf;

import org.junit.Assert;
import org.junit.Test;

public class PdfDictionaryTestTest4 {

    @Test
    public void pdfDictionaryPutThrowsExceptionIfKeyIsNull() {
        PdfDictionary dictionary = new PdfDictionary();
        try {
            dictionary.put(null, new PdfName("null"));
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "key is null.");
        }
    }
}
