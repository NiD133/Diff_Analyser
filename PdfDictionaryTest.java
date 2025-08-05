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
import org.junit.Test;

public class PdfDictionaryTest {
    
    @Test
    public void get_WhenKeyIsNull_ReturnsNull() {
        // Arrange
        PdfDictionary dictionary = new PdfDictionary();
        
        // Act
        PdfObject result = dictionary.get(null);
        
        // Assert
        Assert.assertNull("Getting a value with null key should return null", result);
    }

    @Test
    public void contains_WhenKeyIsNull_ReturnsFalse() {
        // Arrange
        PdfDictionary dictionary = new PdfDictionary();
        
        // Act
        boolean result = dictionary.contains(null);
        
        // Assert
        Assert.assertFalse("Dictionary should not contain null key", result);
    }

    @Test
    public void remove_WhenKeyIsNull_DoesNotModifyDictionary() {
        // Arrange
        PdfDictionary dictionary = new PdfDictionary();
        dictionary.put(new PdfName("ValidKey"), new PdfString("TestValue"));
        int initialSize = dictionary.size();
        
        // Act
        dictionary.remove(null);
        
        // Assert
        Assert.assertEquals("Dictionary size should remain unchanged", 
                            initialSize, dictionary.size());
        Assert.assertTrue("Valid key should still be present", 
                         dictionary.contains(new PdfName("ValidKey")));
    }

    @Test
    public void put_WhenKeyIsNull_ThrowsIllegalArgumentException() {
        // Arrange
        PdfDictionary dictionary = new PdfDictionary();
        PdfObject value = new PdfName("TestValue");
        
        // Act & Assert
        try {
            dictionary.put(null, value);
            Assert.fail("put should throw IllegalArgumentException when key is null");
        } catch (IllegalArgumentException e) {
            // Verify exception details
            Assert.assertEquals("Exception message should indicate null key", 
                                "key is null.", e.getMessage());
        }
    }

    @Test
    public void putEx_WhenKeyIsNull_ThrowsIllegalArgumentException() {
        // Arrange
        PdfDictionary dictionary = new PdfDictionary();
        PdfObject value = new PdfName("TestValue");
        
        // Act & Assert
        try {
            dictionary.putEx(null, value);
            Assert.fail("putEx should throw IllegalArgumentException when key is null");
        } catch (IllegalArgumentException e) {
            // Verify exception details
            Assert.assertEquals("Exception message should indicate null key", 
                                "key is null.", e.getMessage());
        }
    }
}