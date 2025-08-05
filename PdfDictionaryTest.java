/*
    This file is part of the iText (R) project.
    ...
 */
package com.itextpdf.text.pdf;

import org.junit.Assert;
import org.junit.Test;

public class PdfDictionaryTest {
    @Test
    public void pdfDictionaryGetReturnsNullIfKeyIsNull() {
        PdfDictionary dictionary = new PdfDictionary();

        PdfObject value = dictionary.get(null);

        Assert.assertNull(value);
    }

    @Test
    public void pdfDictionaryContainsReturnsFalseIfKeyIsNull() {
        PdfDictionary dictionary = new PdfDictionary();

        boolean contained = dictionary.contains(null);

        Assert.assertFalse(contained);
    }

    @Test
    public void pdfDictionaryRemoveDoesNothingIfKeyIsNull() {
        PdfDictionary dictionary = new PdfDictionary();

        dictionary.remove(null);
    }

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