package com.itextpdf.text.pdf;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Focused and readable tests for PdfDictionary behavior.
 *
 * These tests avoid brittle assertions on internal constants and unrelated classes,
 * and instead verify the core Map-like and type-related behavior of PdfDictionary.
 */
public class PdfDictionaryTest {

    // Helper keys used across tests
    private static final PdfName K1 = new PdfName("K1");
    private static final PdfName K2 = new PdfName("K2");
    private static final PdfName K3 = new PdfName("K3");

    @Test
    public void defaultConstruction_hasNoEntries() {
        PdfDictionary dict = new PdfDictionary();

        assertEquals("New dictionary should be empty", 0, dict.size());
        assertFalse(dict.contains(K1));
        assertTrue(dict.getKeys().isEmpty());
        assertEquals("Dictionary", dict.toString());
    }

    @Test
    public void put_and_get_basic() {
        PdfDictionary dict = new PdfDictionary();

        PdfString value = new PdfString("v");
        dict.put(K1, value);

        assertTrue(dict.contains(K1));
        assertSame(value, dict.get(K1));
        assertSame(value, dict.getDirectObject(K1)); // direct values are returned as-is
        assertSame(value, dict.getAsString(K1));
        assertNull(dict.getAsNumber(K1));
        assertEquals(1, dict.size());
    }

    @Test
    public void putNull_removesExistingKey() {
        PdfDictionary dict = new PdfDictionary();

        dict.put(K1, new PdfString("v"));
        assertTrue(dict.contains(K1));

        dict.put(K1, null); // null -> remove
        assertFalse("Null value should remove the key", dict.contains(K1));
        assertNull(dict.get(K1));
        assertEquals(0, dict.size());
    }

    @Test
    public void putEx_null_doesNothing() {
        PdfDictionary dict = new PdfDictionary();

        // When key is absent, putEx(null) should not add anything
        dict.putEx(K1, null);
        assertFalse(dict.contains(K1));
        assertEquals(0, dict.size());

        // When key is present, putEx(null) should not remove or change the mapping
        dict.put(K1, new PdfNumber(1));
        dict.putEx(K1, null);
        assertTrue(dict.contains(K1));
        assertEquals(1, dict.getAsNumber(K1).intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void put_nullKey_throwsIllegalArgumentException() {
        PdfDictionary dict = new PdfDictionary();
        dict.put(null, new PdfNumber(1));
    }

    @Test
    public void putAll_replacesAndAddsEntries() {
        PdfDictionary target = new PdfDictionary();
        target.put(K1, new PdfNumber(1));

        PdfDictionary source = new PdfDictionary();
        source.put(K1, new PdfNumber(2)); // same key, different value
        source.put(K2, new PdfString("b")); // new key

        target.putAll(source);

        assertEquals(2, target.size());
        assertEquals(2, target.getAsNumber(K1).intValue());
        assertEquals("b", target.getAsString(K2).toUnicodeString());
    }

    @Test(expected = NullPointerException.class)
    public void putAll_null_throwsNullPointerException() {
        PdfDictionary dict = new PdfDictionary();
        dict.putAll(null);
    }

    @Test
    public void merge_overwritesExistingValues_andAddsNewOnes() {
        PdfDictionary target = new PdfDictionary();
        target.put(K1, new PdfNumber(1));

        PdfDictionary other = new PdfDictionary();
        other.put(K1, new PdfNumber(2)); // should overwrite
        other.put(K2, new PdfString("b")); // should add

        target.merge(other);

        assertEquals(2, target.size());
        assertEquals(2, target.getAsNumber(K1).intValue());
        assertEquals("b", target.getAsString(K2).toUnicodeString());
    }

    @Test
    public void mergeDifferent_preservesExistingValues_andAddsOnlyMissing() {
        PdfDictionary target = new PdfDictionary();
        target.put(K1, new PdfNumber(1));

        PdfDictionary other = new PdfDictionary();
        other.put(K1, new PdfNumber(999)); // should NOT overwrite
        other.put(K2, new PdfString("b")); // should add

        target.mergeDifferent(other);

        assertEquals(2, target.size());
        assertEquals(1, target.getAsNumber(K1).intValue());
        assertEquals("b", target.getAsString(K2).toUnicodeString());
    }

    @Test(expected = NullPointerException.class)
    public void merge_null_throwsNullPointerException() {
        PdfDictionary dict = new PdfDictionary();
        dict.merge(null);
    }

    @Test(expected = NullPointerException.class)
    public void mergeDifferent_null_throwsNullPointerException() {
        PdfDictionary dict = new PdfDictionary();
        dict.mergeDifferent(null);
    }

    @Test
    public void downcastGetters_returnExpectedTypes() {
        PdfDictionary dict = new PdfDictionary();

        PdfDictionary child = new PdfDictionary();
        PdfArray array = new PdfArray();
        PdfString string = new PdfString("s");
        PdfNumber number = new PdfNumber(42);
        PdfBoolean bool = PdfBoolean.PDFTRUE;

        dict.put(new PdfName("D"), child);
        dict.put(new PdfName("A"), array);
        dict.put(new PdfName("S"), string);
        dict.put(new PdfName("N"), number);
        dict.put(new PdfName("B"), bool);
        dict.put(new PdfName("NM"), PdfName.CATALOG); // a name value

        assertSame(child, dict.getAsDict(new PdfName("D")));
        assertSame(array, dict.getAsArray(new PdfName("A")));
        assertSame(string, dict.getAsString(new PdfName("S")));
        assertSame(number, dict.getAsNumber(new PdfName("N")));
        assertSame(bool, dict.getAsBoolean(new PdfName("B")));
        assertSame(PdfName.CATALOG, dict.getAsName(new PdfName("NM")));
    }

    @Test
    public void downcastGetters_returnNullForMissingOrMismatched() {
        PdfDictionary dict = new PdfDictionary();
        dict.put(K1, new PdfString("s"));

        assertNull(dict.getAsDict(K1));
        assertNull(dict.getAsArray(K1));
        assertNull(dict.getAsNumber(K1));
        assertNull(dict.getAsBoolean(K1));
        assertNull(dict.getAsName(K1));
        assertNull(dict.getAsStream(K1));
        assertNull(dict.getAsIndirectObject(K1));

        assertNull(dict.getAsString(K2)); // missing key
    }

    @Test
    public void remove_existingKeyRemovesIt() {
        PdfDictionary dict = new PdfDictionary();
        dict.put(K1, new PdfNumber(1));
        dict.put(K2, new PdfNumber(2));

        dict.remove(K1);

        assertFalse(dict.contains(K1));
        assertTrue(dict.contains(K2));
        assertEquals(1, dict.size());
    }

    @Test
    public void clear_removesAllEntries() {
        PdfDictionary dict = new PdfDictionary();
        dict.put(K1, new PdfNumber(1));
        dict.put(K2, new PdfNumber(2));

        dict.clear();

        assertEquals(0, dict.size());
        assertTrue(dict.getKeys().isEmpty());
    }

    @Test
    public void getKeys_returnsAllCurrentKeys() {
        PdfDictionary dict = new PdfDictionary();
        dict.put(K1, new PdfNumber(1));
        dict.put(K2, new PdfString("a"));

        Set<PdfName> keys = dict.getKeys();

        assertEquals(2, keys.size());
        assertTrue(keys.contains(K1));
        assertTrue(keys.contains(K2));
    }

    @Test
    public void size_reflectsEntryCount() {
        PdfDictionary dict = new PdfDictionary();

        assertEquals(0, dict.size());
        dict.put(K1, new PdfNumber(1));
        assertEquals(1, dict.size());
        dict.put(K2, new PdfNumber(2));
        assertEquals(2, dict.size());
        dict.remove(K1);
        assertEquals(1, dict.size());
    }

    @Test
    public void typedDictionary_reportsTypeAndPredicates() {
        PdfDictionary page = new PdfDictionary(PdfName.PAGE);
        assertTrue(page.isPage());
        assertTrue(page.checkType(PdfName.PAGE));
        assertFalse(page.isFont());
        assertFalse(page.isPages());
        assertFalse(page.isCatalog());
        assertFalse(page.isOutlineTree());
        assertEquals("Dictionary of type: /Page", page.toString());

        PdfDictionary catalog = new PdfDictionary(PdfName.CATALOG);
        assertTrue(catalog.isCatalog());
        assertTrue(catalog.checkType(PdfName.CATALOG));
        assertEquals("Dictionary of type: /Catalog", catalog.toString());
    }

    @Test
    public void untypedDictionary_predicatesAreFalse_andToStringIsGeneric() {
        PdfDictionary dict = new PdfDictionary();

        assertFalse(dict.isFont());
        assertFalse(dict.isPage());
        assertFalse(dict.isPages());
        assertFalse(dict.isCatalog());
        assertFalse(dict.isOutlineTree());
        assertFalse(dict.checkType(null));
        assertEquals("Dictionary", dict.toString());
    }
}