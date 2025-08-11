/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
    Authors: iText Software.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS
    
    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/
    
    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.
    
    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.
    
    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.
    
    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for PdfDictionary null key handling behavior.
 * 
 * This test class verifies that PdfDictionary properly handles null keys
 * across all its key-based operations, ensuring consistent behavior
 * throughout the API.
 */
public class PdfDictionaryTest {
    
    private PdfDictionary dictionary;
    private PdfObject testValue;
    
    @Before
    public void setUp() {
        dictionary = new PdfDictionary();
        testValue = new PdfName("testValue");
    }

    /**
     * Test that get() method returns null when called with a null key.
     * 
     * This verifies defensive programming - the method should handle
     * null input gracefully rather than throwing an exception.
     */
    @Test
    public void get_WithNullKey_ReturnsNull() {
        // When: attempting to get a value with null key
        PdfObject result = dictionary.get(null);

        // Then: should return null (not throw exception)
        Assert.assertNull("get() should return null when key is null", result);
    }

    /**
     * Test that contains() method returns false when called with a null key.
     * 
     * Since null keys are not valid, the dictionary should never
     * contain a null key, so this should always return false.
     */
    @Test
    public void contains_WithNullKey_ReturnsFalse() {
        // When: checking if dictionary contains null key
        boolean result = dictionary.contains(null);

        // Then: should return false
        Assert.assertFalse("contains() should return false when key is null", result);
    }

    /**
     * Test that remove() method handles null keys gracefully.
     * 
     * The remove operation should not fail when given a null key,
     * it should simply do nothing (no-op behavior).
     */
    @Test
    public void remove_WithNullKey_DoesNotThrowException() {
        // When: attempting to remove with null key
        // Then: should not throw any exception
        dictionary.remove(null);
        
        // Test passes if no exception is thrown
    }

    /**
     * Test that put() method throws IllegalArgumentException when key is null.
     * 
     * Since PDF dictionaries require valid PdfName keys, attempting to
     * put a null key should result in a clear error message.
     */
    @Test
    public void put_WithNullKey_ThrowsIllegalArgumentException() {
        // When: attempting to put with null key
        try {
            dictionary.put(null, testValue);
            Assert.fail("Expected IllegalArgumentException when putting null key");
        } catch (IllegalArgumentException exception) {
            // Then: should throw exception with specific message
            Assert.assertEquals("Exception should have correct error message", 
                              "key is null.", 
                              exception.getMessage());
        }
    }

    /**
     * Test that putEx() method throws IllegalArgumentException when key is null.
     * 
     * Like put(), the putEx() method should also reject null keys
     * with the same error handling behavior.
     */
    @Test
    public void putEx_WithNullKey_ThrowsIllegalArgumentException() {
        // When: attempting to putEx with null key
        try {
            dictionary.putEx(null, testValue);
            Assert.fail("Expected IllegalArgumentException when putting null key with putEx");
        } catch (IllegalArgumentException exception) {
            // Then: should throw exception with specific message
            Assert.assertEquals("Exception should have correct error message", 
                              "key is null.", 
                              exception.getMessage());
        }
    }
}