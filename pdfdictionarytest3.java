package com.itextpdf.text.pdf;

import org.junit.Assert;
import org.junit.Test;

public class PdfDictionaryTestTest3 {

    @Test
    public void remove_withNullKey_shouldNotModifyDictionary() {
        // Arrange: Create a dictionary with a known entry.
        PdfDictionary dictionary = new PdfDictionary();
        PdfName existingKey = new PdfName("TestKey");
        PdfString existingValue = new PdfString("TestValue");
        dictionary.put(existingKey, existingValue);

        // Act: Attempt to remove an entry using a null key.
        // The expected behavior is that this operation does nothing and does not throw an exception.
        dictionary.remove(null);

        // Assert: Verify the dictionary remains unchanged.
        Assert.assertEquals("Dictionary size should remain 1.", 1, dictionary.size());
        Assert.assertTrue("Dictionary should still contain the original key.", dictionary.contains(existingKey));
        Assert.assertEquals("The value for the original key should be unchanged.", existingValue, dictionary.get(existingKey));
    }
}