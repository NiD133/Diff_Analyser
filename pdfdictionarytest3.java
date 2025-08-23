package com.itextpdf.text.pdf;

import org.junit.Assert;
import org.junit.Test;

public class PdfDictionaryTestTest3 {

    @Test
    public void pdfDictionaryRemoveDoesNothingIfKeyIsNull() {
        PdfDictionary dictionary = new PdfDictionary();
        dictionary.remove(null);
    }
}
