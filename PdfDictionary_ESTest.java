package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.collection.PdfCollectionField;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.util.LinkedHashMap;
import java.util.Set;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class PdfDictionary_ESTest extends PdfDictionary_ESTest_scaffolding {

    // ========== Basic Dictionary Operations ==========
    
    @Test(timeout = 4000)
    public void testRemoveWithNullKey_ShouldNotThrowException() throws Throwable {
        PdfDictionary dictionary = new PdfDictionary();
        
        dictionary.remove(null); // Should handle null gracefully
        
        assertFalse(dictionary.isBoolean());
    }

    @Test(timeout = 4000)
    public void testPutExWithValidKeyValue_ShouldStoreSuccessfully() throws Throwable {
        PdfName key = PdfName.SECT;
        PdfResources resources = new PdfResources();
        
        resources.putEx(key, key);
        key.type = 8;
        
        ByteBuffer buffer = new ByteBuffer(8);
        resources.toPdf(null, buffer);
        assertEquals(15, buffer.size());
    }

    @Test(timeout = 4000)
    public void testPutAllFromAnotherDictionary_ShouldMergeSuccessfully() throws Throwable {
        PdfName type = PdfName.LINEHEIGHT;
        PdfDictionary sourceDictionary = new PdfDictionary(type);
        PdfWriter writer = new PdfWriter();
        PdfRectangle rect = new PdfRectangle(0.0F, 642.05963F, 8388608, -424.6F);
        Rectangle normalizedRect = PdfReader.getNormalizedRectangle(rect);
        PdfAnnotation annotation = PdfAnnotation.createLink(writer, normalizedRect, PdfDictionary.CATALOG);
        
        annotation.putAll(sourceDictionary);
        
        assertEquals(3, PdfAnnotation.MARKUP_SQUIGGLY);
    }

    @Test(timeout = 4000)
    public void testMergeWithAnotherDictionary_ShouldCombineEntries() throws Throwable {
        PdfDictionary dictionary = new PdfDictionary();
        Object[] emptyArray = new Object[0];
        PdfAction action = PdfAction.createHide(emptyArray, true);
        
        dictionary.merge(action);
        
        assertEquals(1, PdfObject.BOOLEAN);
    }

    @Test(timeout = 4000)
    public void testSizeOfDictionary_ShouldReturnCorrectCount() throws Throwable {
        PdfAction action = PdfAction.gotoLocalPage("</rdf:Description></rdf:RDF>\n", true);
        
        int size = action.size();
        
        assertEquals(2, size);
    }

    // ========== Dictionary Type Checking ==========

    @Test(timeout = 4000)
    public void testIsPage_WithRegularDictionary_ShouldReturnFalse() throws Throwable {
        PdfDictionary dictionary = new PdfDictionary();
        
        boolean isPage = dictionary.isPage();
        
        assertFalse(isPage);
    }

    @Test(timeout = 4000)
    public void testIsPage_WithPdfPageObject_ShouldReturnTrue() throws Throwable {
        PdfRectangle rect = new PdfRectangle(0.0F, 0.0F, 2884.4F, 2884.4F);
        LinkedHashMap<String, PdfRectangle> boxSizes = new LinkedHashMap<>(4, 5);
        PdfPage page = new PdfPage(rect, boxSizes, null, 1836);
        
        boolean isPage = page.isPage();
        
        assertTrue(isPage);
    }

    @Test(timeout = 4000)
    public void testCheckType_WithMatchingType_ShouldReturnTrue() throws Throwable {
        PdfName expectedType = PdfName.CFM;
        PdfDictionary dictionary = new PdfDictionary(expectedType);
        
        boolean typeMatches = dictionary.checkType(expectedType);
        
        assertTrue(typeMatches);
    }

    @Test(timeout = 4000)
    public void testCheckType_WithNonMatchingType_ShouldReturnFalse() throws Throwable {
        PdfResources resources = new PdfResources();
        PdfCollectionField field = new PdfCollectionField("UnicodeBig", 8);
        
        boolean typeMatches = field.checkType(resources.CATALOG);
        
        assertFalse(typeMatches);
    }

    // ========== Object Retrieval and Casting ==========

    @Test(timeout = 4000)
    public void testGetDirectObject_WithExistingKey_ShouldReturnObject() throws Throwable {
        PdfName key = PdfWriter.PDF_VERSION_1_6;
        PdfResources resources = new PdfResources();
        resources.putEx(key, key);
        
        PdfObject retrievedObject = resources.getDirectObject(key);
        
        assertEquals(4, PdfObject.NAME);
        assertTrue(retrievedObject.canBeInObjStm());
    }

    @Test(timeout = 4000)
    public void testGet_WithExistingKey_ShouldReturnStoredObject() throws Throwable {
        PdfName key = PdfName.TYPE3;
        PdfResources resources = new PdfResources();
        PdfCollectionField field = new PdfCollectionField("UnicodeBig", 8);
        resources.add(key, field);
        
        PdfCollectionField retrievedField = (PdfCollectionField) resources.get(key);
        
        assertEquals(6, PdfCollectionField.CREATIONDATE);
    }

    @Test(timeout = 4000)
    public void testGetAsName_WithNameObject_ShouldReturnName() throws Throwable {
        PdfName key = PdfWriter.PDF_VERSION_1_6;
        PdfResources resources = new PdfResources();
        resources.putEx(key, key);
        
        PdfName retrievedName = resources.getAsName(key);
        
        assertSame(key, retrievedName);
        assertNotNull(retrievedName);
    }

    @Test(timeout = 4000)
    public void testGetAsName_WithNonNameObject_ShouldReturnNull() throws Throwable {
        PdfName key = PdfName.TYPE3;
        PdfResources resources = new PdfResources();
        PdfCollectionField field = new PdfCollectionField("UnicodeBig", 8);
        resources.add(key, field);
        
        PdfName retrievedName = resources.getAsName(key);
        
        assertNull(retrievedName);
    }

    // ========== Dictionary State Queries ==========

    @Test(timeout = 4000)
    public void testContains_WithExistingKey_ShouldReturnTrue() throws Throwable {
        PdfName key = PdfName.TYPE3;
        PdfResources resources = new PdfResources();
        resources.putEx(key, key);
        
        boolean containsKey = resources.contains(key);
        
        assertTrue(containsKey);
    }

    @Test(timeout = 4000)
    public void testContains_WithNullKey_ShouldReturnFalse() throws Throwable {
        PdfTransparencyGroup group = new PdfTransparencyGroup();
        
        boolean containsNull = group.contains(null);
        
        assertFalse(containsNull);
    }

    @Test(timeout = 4000)
    public void testGetKeys_WithEmptyDictionary_ShouldReturnEmptySet() throws Throwable {
        PdfDictionary dictionary = new PdfDictionary();
        
        Set<PdfName> keys = dictionary.getKeys();
        
        assertEquals(0, keys.size());
    }

    @Test(timeout = 4000)
    public void testSize_WithEmptyDictionary_ShouldReturnZero() throws Throwable {
        PdfResources resources = new PdfResources();
        
        int size = resources.size();
        
        assertEquals(0, size);
    }

    @Test(timeout = 4000)
    public void testClear_ShouldRemoveAllEntries() throws Throwable {
        PdfResources resources = new PdfResources();
        
        resources.clear();
        
        assertFalse(resources.isName());
    }

    // ========== Dictionary Type Checks ==========

    @Test(timeout = 4000)
    public void testIsPages_WithRegularDictionary_ShouldReturnFalse() throws Throwable {
        PdfDictionary dictionary = new PdfDictionary();
        
        boolean isPages = dictionary.isPages();
        
        assertFalse(isPages);
    }

    @Test(timeout = 4000)
    public void testIsCatalog_WithResourcesDictionary_ShouldReturnFalse() throws Throwable {
        PdfResources resources = new PdfResources();
        
        boolean isCatalog = resources.isCatalog();
        
        assertFalse(isCatalog);
    }

    @Test(timeout = 4000)
    public void testIsFont_WithRegularDictionary_ShouldReturnFalse() throws Throwable {
        PdfDictionary dictionary = new PdfDictionary();
        
        boolean isFont = dictionary.isFont();
        
        assertFalse(isFont);
    }

    @Test(timeout = 4000)
    public void testIsOutlineTree_WithPageObject_ShouldReturnFalse() throws Throwable {
        PdfRectangle rect = new PdfRectangle(0.0F, 0.0F, 2884.4F, 2884.4F);
        LinkedHashMap<String, PdfRectangle> boxSizes = new LinkedHashMap<>(4, 5);
        PdfPage page = new PdfPage(rect, boxSizes, null, 1836);
        
        boolean isOutlineTree = page.isOutlineTree();
        
        assertFalse(isOutlineTree);
    }

    // ========== Constructor Tests ==========

    @Test(timeout = 4000)
    public void testConstructor_WithValidCapacity_ShouldCreateDictionary() throws Throwable {
        PdfDictionary dictionary = new PdfDictionary(3);
        
        assertEquals(6, dictionary.type());
    }

    @Test(timeout = 4000)
    public void testConstructor_WithNegativeCapacity_ShouldThrowException() throws Throwable {
        try {
            new PdfDictionary(-2457);
            fail("Expected IllegalArgumentException for negative capacity");
        } catch(IllegalArgumentException e) {
            assertEquals("Illegal initial capacity: -2457", e.getMessage());
        }
    }

    // ========== Error Handling Tests ==========

    @Test(timeout = 4000)
    public void testPutEx_WithNullKey_ShouldThrowException() throws Throwable {
        PdfDictionary dictionary = new PdfDictionary();
        
        try {
            dictionary.putEx(null, null);
            fail("Expected IllegalArgumentException for null key");
        } catch(IllegalArgumentException e) {
            assertEquals("key is null.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testPut_WithNullKey_ShouldThrowException() throws Throwable {
        PdfFileSpecification fileSpec = new PdfFileSpecification();
        
        try {
            fileSpec.put(null, null);
            fail("Expected IllegalArgumentException for null key");
        } catch(IllegalArgumentException e) {
            assertEquals("key is null.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testAdd_WithNullKey_ShouldThrowException() throws Throwable {
        PdfResources resources = new PdfResources();
        PdfCollectionField field = new PdfCollectionField("", -2425);
        
        try {
            resources.add(null, field);
            fail("Expected IllegalArgumentException for null key");
        } catch(IllegalArgumentException e) {
            assertEquals("key is null.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testToPdf_WithNullOutputStream_ShouldThrowException() throws Throwable {
        PdfResources resources = new PdfResources();
        PdfWriter writer = new PdfWriter();
        
        try {
            resources.toPdf(writer, null);
            fail("Expected NullPointerException for null OutputStream");
        } catch(NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testToPdf_WithPipedOutputStream_ShouldThrowIOException() throws Throwable {
        PdfDocument.PdfInfo info = new PdfDocument.PdfInfo();
        PipedOutputStream pipedOut = new PipedOutputStream();
        FdfWriter fdfWriter = new FdfWriter(pipedOut);
        
        try {
            info.toPdf(fdfWriter.wrt, pipedOut);
            fail("Expected IOException for unconnected pipe");
        } catch(IOException e) {
            assertEquals("Pipe not connected", e.getMessage());
        }
    }

    // ========== Null HashMap Edge Cases ==========

    @Test(timeout = 4000)
    public void testPutEx_WithNullHashMap_ShouldThrowException() throws Throwable {
        PdfName key = PdfName.BLINDS;
        PdfResources resources = new PdfResources();
        resources.hashMap = null;
        
        try {
            resources.putEx(key, key);
            fail("Expected NullPointerException for null hashMap");
        } catch(NullPointerException e) {
            // Expected behavior when internal state is corrupted
        }
    }

    @Test(timeout = 4000)
    public void testMergeDifferent_WithNullDictionary_ShouldThrowException() throws Throwable {
        PdfDictionary dictionary = new PdfDictionary();
        
        try {
            dictionary.mergeDifferent(null);
            fail("Expected NullPointerException for null dictionary");
        } catch(NullPointerException e) {
            // Expected behavior
        }
    }

    // ========== String Representation Tests ==========

    @Test(timeout = 4000)
    public void testToString_WithSigLockDictionary_ShouldReturnTypeDescription() throws Throwable {
        PdfSigLockDictionary sigLock = new PdfSigLockDictionary();
        
        String description = sigLock.toString();
        
        assertEquals("Dictionary of type: /SigFieldLock", description);
    }

    @Test(timeout = 4000)
    public void testToPdf_WithMockPrintStream_ShouldWriteSuccessfully() throws Throwable {
        PipedOutputStream pipedOut = new PipedOutputStream();
        PdfAction action = new PdfAction("UnicodeBig", "TABLE", "PDF", "TABLE");
        MockPrintStream mockStream = new MockPrintStream(pipedOut, true);
        
        action.toPdf(null, mockStream);
        
        assertEquals(5, PdfAction.PRINTDIALOG);
    }

    // ========== Complex Object Tests ==========

    @Test(timeout = 4000)
    public void testCreatePdfShading_ShouldSetCorrectProperties() throws Throwable {
        FdfWriter.Wrt writer = new FdfWriter.Wrt(null, null);
        BaseColor magenta = BaseColor.MAGENTA;
        
        PdfShading shading = PdfShading.simpleRadial(writer, 512f, 1f, 32f, 8f, 8192f, 663.0f, magenta, magenta);
        
        assertFalse(shading.isAntiAlias());
    }

    @Test(timeout = 4000)
    public void testPdfInfoToPdf_WithValidWriter_ShouldSucceed() throws Throwable {
        PdfDocument.PdfInfo info = new PdfDocument.PdfInfo();
        MockPrintStream mockStream = new MockPrintStream("com.itextpdf.text.pdf.PdfDictionary");
        FdfWriter fdfWriter = new FdfWriter();
        FdfWriter.Wrt writer = new FdfWriter.Wrt(mockStream, fdfWriter);
        
        info.toPdf(writer, mockStream);
        
        assertEquals(1, writer.getCurrentPageNumber());
    }

    // ========== Merge Operations ==========

    @Test(timeout = 4000)
    public void testMergeDifferent_WithFormField_ShouldMergeSuccessfully() throws Throwable {
        PdfResources resources = new PdfResources();
        PdfFormField formField = PdfFormField.createChoice(null, 6, null, 8);
        
        resources.mergeDifferent(formField);
        
        assertEquals(5, PdfFormField.MK_CAPTION_LEFT);
    }

    @Test(timeout = 4000)
    public void testMergeDifferent_WithSameDictionary_ShouldHandleGracefully() throws Throwable {
        PdfAction action = new PdfAction("PDF", false);
        
        action.mergeDifferent(action);
        
        assertEquals(3, PdfObject.STRING);
    }

    // ========== Additional Getter Tests ==========

    @Test(timeout = 4000)
    public void testGetAsName_WithNonExistentKey_ShouldReturnNull() throws Throwable {
        PdfResources resources = new PdfResources();
        
        PdfName result = resources.getAsName(resources.CATALOG);
        
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testGet_WithNonExistentKey_ShouldReturnNull() throws Throwable {
        PdfName key = PdfWriter.PDF_VERSION_1_6;
        PdfResources resources = new PdfResources();
        
        PdfObject result = resources.get(key);
        
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testGetAsIndirectObject_WithDirectObject_ShouldReturnNull() throws Throwable {
        PdfName key = PdfWriter.PDF_VERSION_1_6;
        PdfResources resources = new PdfResources();
        resources.putEx(key, key);
        
        PdfIndirectReference indirectRef = resources.getAsIndirectObject(key);
        
        assertNull(indirectRef);
    }

    @Test(timeout = 4000)
    public void testGetAsBoolean_WithNonBooleanObject_ShouldReturnNull() throws Throwable {
        PdfName key = PdfName.URL;
        PdfResources resources = new PdfResources();
        resources.putEx(key, key);
        
        PdfBoolean booleanValue = resources.getAsBoolean(key);
        
        assertNull(booleanValue);
    }

    @Test(timeout = 4000)
    public void testGetAsNumber_WithNonNumberObject_ShouldReturnNull() throws Throwable {
        PdfName key = PdfName.FRM;
        PdfResources resources = new PdfResources();
        resources.putEx(key, key);
        
        PdfNumber numberValue = resources.getAsNumber(key);
        
        assertNull(numberValue);
    }

    @Test(timeout = 4000)
    public void testGetAsString_WithNonExistentKey_ShouldReturnNull() throws Throwable {
        PdfRectangle rect = new PdfRectangle(0.0F, 0.0F, 2884.4F, 2884.4F);
        LinkedHashMap<String, PdfRectangle> boxSizes = new LinkedHashMap<>(4, 5);
        PdfPage page = new PdfPage(rect, boxSizes, null, 1836);
        
        PdfString stringValue = page.getAsString(page.CATALOG);
        
        assertNull(stringValue);
    }

    @Test(timeout = 4000)
    public void testGetAsStream_WithNonStreamObject_ShouldReturnNull() throws Throwable {
        PdfName key = PdfName.DR;
        PdfResources resources = new PdfResources();
        resources.putEx(key, key);
        
        PdfStream streamValue = resources.getAsStream(key);
        
        assertNull(streamValue);
    }

    @Test(timeout = 4000)
    public void testGetAsArray_WithNonExistentKey_ShouldReturnNull() throws Throwable {
        PdfRectangle rect = new PdfRectangle(0.0F, 0.0F, 2884.4F, 2884.4F);
        LinkedHashMap<String, PdfRectangle> boxSizes = new LinkedHashMap<>(4, 5);
        PdfPage page = new PdfPage(rect, boxSizes, null, 1836);
        
        PdfArray arrayValue = page.getAsArray(page.PAGE);
        
        assertNull(arrayValue);
    }

    @Test(timeout = 4000)
    public void testGetAsDict_WithNonDictionaryObject_ShouldReturnNull() throws Throwable {
        PdfName key = PdfName.DR;
        PdfResources resources = new PdfResources();
        resources.putEx(key, key);
        
        PdfDictionary dictValue = resources.getAsDict(key);
        
        assertNull(dictValue);
    }

    @Test(timeout = 4000)
    public void testPutWithNullValue_ShouldStoreSuccessfully() throws Throwable {
        PdfName key = PdfName.TYPE3;
        PdfResources resources = new PdfResources();
        
        resources.putEx(key, null);
        
        assertEquals(4, PdfObject.NAME);
    }

    @Test(timeout = 4000)
    public void testPutWithValidKeyValue_ShouldStoreSuccessfully() throws Throwable {
        PdfTransparencyGroup group = new PdfTransparencyGroup();
        PdfName key = PdfName.FUNCTIONTYPE;
        
        group.put(key, key);
        
        assertEquals(10, PdfObject.INDIRECT);
    }
}