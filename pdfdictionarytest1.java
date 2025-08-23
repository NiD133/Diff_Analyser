package com.itextpdf.text.pdf;

import org.junit.Assert;
import org.junit.Test;

public class PdfDictionaryTestTest1 {

    @Test
    public void pdfDictionaryGetReturnsNullIfKeyIsNull() {
        PdfDictionary dictionary = new PdfDictionary();
        PdfObject value = dictionary.get(null);
        Assert.assertNull(value);
    }
}
