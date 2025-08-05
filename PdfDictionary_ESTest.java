package com.itextpdf.text.pdf;

import org.junit.Test;

import static org.junit.Assert.*;

import com.itextpdf.text.pdf.collection.PdfCollectionField;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedOutputStream;
import java.util.Set;

/**
 * Unit tests for the {@link PdfDictionary} class, focusing on understandability and maintainability.
 */
public class PdfDictionaryTest {

    // --- Constructor Tests ---

    @Test
    public void defaultConstructor_createsEmptyDictionary() {
        PdfDictionary dictionary = new PdfDictionary();
        assertTrue(dictionary.getKeys().isEmpty());
        assertEquals(0, dictionary.size());
    }

    @Test
    public void typedConstructor_createsDictionaryWithTypeEntry() {
        PdfDictionary pageDict = new PdfDictionary(PdfDictionary.PAGE);
        assertEquals(1, pageDict.size());
        assertEquals(PdfDictionary.PAGE, pageDict.get(PdfName.TYPE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithNegativeCapacity_throwsIllegalArgumentException() {
        new PdfDictionary(-1);
    }

    // --- Modification Method Tests (put, remove, clear, etc.) ---

    @Test
    public void put_addNewEntry_increasesSizeAndEntryIsPresent() {
        PdfDictionary dictionary = new PdfDictionary();
        PdfString value = new PdfString("test");

        dictionary.put(PdfName.A, value);

        assertEquals(1, dictionary.size());
        assertEquals(value, dictionary.get(PdfName.A));
    }

    @Test
    public void put_overwriteExistingEntry_replacesValueAndSizeIsUnchanged() {
        PdfDictionary dictionary = new PdfDictionary();
        dictionary.put(PdfName.A, new PdfString("old"));

        PdfString newValue = new PdfString("new");
        dictionary.put(PdfName.A, newValue);

        assertEquals(1, dictionary.size());
        assertEquals(newValue, dictionary.get(PdfName.A));
    }

    @Test
    public void put_withNullValue_removesEntry() {
        PdfDictionary dictionary = new PdfDictionary();
        dictionary.put(PdfName.A, new PdfString("test"));
        assertEquals(1, dictionary.size());

        dictionary.put(PdfName.A, null);

        assertEquals(0, dictionary.size());
        assertFalse(dictionary.contains(PdfName.A));
    }

    @Test(expected = IllegalArgumentException.class)
    public void put_withNullKey_throwsIllegalArgumentException() {
        PdfDictionary dictionary = new PdfDictionary();
        dictionary.put(null, new PdfString("test"));
    }

    @Test
    public void putEx_withNullValue_doesNotAddOrRemoveEntry() {
        PdfDictionary dictionary = new PdfDictionary();
        dictionary.put(PdfName.A, new PdfString("value"));

        dictionary.putEx(PdfName.B, null); // Should do nothing
        dictionary.putEx(PdfName.A, null); // Should do nothing

        assertEquals(1, dictionary.size());
        assertTrue(dictionary.contains(PdfName.A));
        assertFalse(dictionary.contains(PdfName.B));
    }

    @Test
    public void remove_existingKey_removesEntryAndDecreasesSize() {
        PdfDictionary dictionary = new PdfDictionary();
        dictionary.put(PdfName.A, new PdfString("test"));

        dictionary.remove(PdfName.A);

        assertEquals(0, dictionary.size());
        assertFalse(dictionary.contains(PdfName.A));
    }

    @Test
    public void remove_nonExistentKey_doesNothing() {
        PdfDictionary dictionary = new PdfDictionary();
        dictionary.put(PdfName.A, new PdfString("test"));

        dictionary.remove(PdfName.B);

        assertEquals(1, dictionary.size());
    }

    @Test
    public void putAll_copiesAllEntriesFromSourceDictionary() {
        PdfDictionary source = new PdfDictionary();
        source.put(PdfName.A, new PdfNumber(1));
        source.put(PdfName.B, new PdfString("two"));

        PdfDictionary target = new PdfDictionary();
        target.put(PdfName.C, new PdfBoolean(false));

        target.putAll(source);

        assertEquals(3, target.size());
        assertEquals(new PdfNumber(1), target.get(PdfName.A));
        assertEquals(new PdfString("two"), target.get(PdfName.B));
        assertEquals(new PdfBoolean(false), target.get(PdfName.C));
    }

    @Test
    public void clear_removesAllEntries() {
        PdfDictionary dictionary = new PdfDictionary();
        dictionary.put(PdfName.A, new PdfNumber(1));
        dictionary.put(PdfName.B, new PdfNumber(2));

        dictionary.clear();

        assertEquals(0, dictionary.size());
        assertTrue(dictionary.getKeys().isEmpty());
    }

    // --- Merge Method Tests ---

    @Test
    public void merge_copiesAllEntriesAndOverwritesExisting() {
        PdfDictionary source = new PdfDictionary();
        source.put(PdfName.A, new PdfString("new A"));
        source.put(PdfName.B, new PdfString("B"));

        PdfDictionary target = new PdfDictionary();
        target.put(PdfName.A, new PdfString("old A"));
        target.put(PdfName.C, new PdfString("C"));

        target.merge(source);

        assertEquals(3, target.size());
        assertEquals(new PdfString("new A"), target.get(PdfName.A)); // Overwritten
        assertEquals(new PdfString("B"), target.get(PdfName.B));     // Added
        assertEquals(new PdfString("C"), target.get(PdfName.C));     // Kept
    }

    @Test
    public void mergeDifferent_copiesOnlyNewEntries() {
        PdfDictionary source = new PdfDictionary();
        source.put(PdfName.A, new PdfString("new A"));
        source.put(PdfName.B, new PdfString("B"));

        PdfDictionary target = new PdfDictionary();
        target.put(PdfName.A, new PdfString("old A"));
        target.put(PdfName.C, new PdfString("C"));

        target.mergeDifferent(source);

        assertEquals(3, target.size());
        assertEquals(new PdfString("old A"), target.get(PdfName.A)); // Kept original
        assertEquals(new PdfString("B"), target.get(PdfName.B));     // Added
        assertEquals(new PdfString("C"), target.get(PdfName.C));     // Kept
    }

    // --- Accessor and Query Method Tests ---

    @Test
    public void getKeys_returnsAllKeysPresentInDictionary() {
        PdfDictionary dictionary = new PdfDictionary();
        dictionary.put(PdfName.A, new PdfNumber(1));
        dictionary.put(PdfName.B, new PdfNumber(2));

        Set<PdfName> keys = dictionary.getKeys();

        assertEquals(2, keys.size());
        assertTrue(keys.contains(PdfName.A));
        assertTrue(keys.contains(PdfName.B));
    }

    @Test
    public void getAsType_whenValueIsCorrectType_returnsCastedObject() {
        PdfDictionary dictionary = new PdfDictionary();
        dictionary.put(PdfName.DICT, new PdfDictionary());
        dictionary.put(PdfName.ARRAY, new PdfArray());
        dictionary.put(PdfName.STRING, new PdfString("test"));
        dictionary.put(PdfName.NUMBER, new PdfNumber(123));
        dictionary.put(PdfName.NAME, PdfName.YES);
        dictionary.put(PdfName.BOOLEAN, PdfBoolean.PDFTRUE);
        dictionary.put(PdfName.STREAM, new PdfStream(new byte[0]));

        assertNotNull(dictionary.getAsDict(PdfName.DICT));
        assertNotNull(dictionary.getAsArray(PdfName.ARRAY));
        assertNotNull(dictionary.getAsString(PdfName.STRING));
        assertNotNull(dictionary.getAsNumber(PdfName.NUMBER));
        assertNotNull(dictionary.getAsName(PdfName.NAME));
        assertNotNull(dictionary.getAsBoolean(PdfName.BOOLEAN));
        assertNotNull(dictionary.getAsStream(PdfName.STREAM));
    }

    @Test
    public void getAsType_whenValueIsWrongType_returnsNull() {
        PdfDictionary dictionary = new PdfDictionary();
        // Put a non-dictionary object for a key we'll query as a dictionary
        dictionary.put(PdfName.A, new PdfString("not a dictionary"));

        assertNull(dictionary.getAsDict(PdfName.A));
        assertNull(dictionary.getAsArray(PdfName.A));
        assertNull(dictionary.getAsNumber(PdfName.A));
    }

    @Test
    public void getAsType_whenKeyDoesNotExist_returnsNull() {
        PdfDictionary dictionary = new PdfDictionary();
        assertNull(dictionary.getAsDict(PdfName.NONEXISTENT));
        assertNull(dictionary.getAsArray(PdfName.NONEXISTENT));
        assertNull(dictionary.getAsString(PdfName.NONEXISTENT));
    }

    // --- Type Checking Method Tests ---

    @Test
    public void checkType_verifiesDictionaryType() {
        PdfDictionary pageDict = new PdfDictionary(PdfDictionary.PAGE);
        assertTrue(pageDict.checkType(PdfDictionary.PAGE));
        assertFalse(pageDict.checkType(PdfDictionary.CATALOG));
    }

    @Test
    public void isType_whenTypeMatches_returnsTrue() {
        assertTrue(new PdfDictionary(PdfDictionary.PAGE).isPage());
        assertTrue(new PdfDictionary(PdfDictionary.PAGES).isPages());
        assertTrue(new PdfDictionary(PdfDictionary.CATALOG).isCatalog());
        assertTrue(new PdfDictionary(PdfDictionary.FONT).isFont());
        assertTrue(new PdfDictionary(PdfDictionary.OUTLINES).isOutlineTree());
    }

    @Test
    public void isType_whenTypeDoesNotMatch_returnsFalse() {
        PdfDictionary dictionary = new PdfDictionary(); // No type
        assertFalse(dictionary.isPage());
        assertFalse(dictionary.isCatalog());

        PdfDictionary pageDict = new PdfDictionary(PdfDictionary.PAGE);
        assertFalse(pageDict.isCatalog());
    }

    // --- Serialization Tests ---

    @Test
    public void toString_onTypedDictionary_includesTypeInOutput() {
        PdfDictionary sigLockDict = new PdfSigLockDictionary();
        assertEquals("Dictionary of type: /SigFieldLock", sigLockDict.toString());
    }

    @Test
    public void toPdf_onEmptyDictionary_writesEmptyDictionarySyntax() throws IOException {
        PdfDictionary dictionary = new PdfDictionary();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        dictionary.toPdf(null, baos);

        assertEquals("<< >>", baos.toString());
    }

    @Test
    public void toPdf_withContent_writesDictionarySyntaxWithKeyValuePairs() throws IOException {
        PdfDictionary dictionary = new PdfDictionary();
        dictionary.put(PdfName.K, PdfBoolean.PDFTRUE);
        dictionary.put(PdfName.V, new PdfString("value"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        dictionary.toPdf(null, baos);

        // Note: HashMap does not guarantee order, so we check for content presence.
        String result = baos.toString();
        assertTrue(result.startsWith("<<"));
        assertTrue(result.endsWith(">>"));
        assertTrue(result.contains("/K true"));
        assertTrue(result.contains("/V (value)"));
    }

    @Test(expected = IOException.class)
    public void toPdf_withBadOutputStream_throwsIOException() throws IOException {
        PdfDictionary dictionary = new PdfDictionary();
        // A non-connected PipedOutputStream will throw an IOException on write
        PipedOutputStream badOs = new PipedOutputStream();

        dictionary.toPdf(null, badOs);
    }
}