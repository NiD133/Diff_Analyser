package com.itextpdf.text.pdf;

import org.junit.Assert;
import org.junit.Test;

public class PdfDictionaryTestTest2 {

    @Test
    public void pdfDictionaryContainsReturnsFalseIfKeyIsNull() {
        PdfDictionary dictionary = new PdfDictionary();
        boolean contained = dictionary.contains(null);
        Assert.assertFalse(contained);
    }
}
