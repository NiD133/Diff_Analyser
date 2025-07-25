package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CharSetUtils}.
 */
class CharSetUtilsTest extends AbstractLangTest {

    /**
     * Tests the constructor of CharSetUtils.
     */
    @Test
    void testConstructor() {
        // Ensure the constructor is not null
        assertNotNull(new CharSetUtils());

        // Verify the class has only one public constructor
        final Constructor<?>[] constructors = CharSetUtils.class.getDeclaredConstructors();
        assertEquals(1, constructors.length);
        assertTrue(Modifier.isPublic(constructors[0].getModifiers()));

        // Verify the class is public and not final
        assertTrue(Modifier.isPublic(CharSetUtils.class.getModifiers()));
        assertFalse(Modifier.isFinal(CharSetUtils.class.getModifiers()));
    }

    /**
     * Tests the containsAny method with String and String parameters.
     */
    @Test
    void testContainsAny_StringString() {
        // Test with null and empty strings
        assertFalse(CharSetUtils.containsAny(null, (String) null));
        assertFalse(CharSetUtils.containsAny(null, ""));
        assertFalse(CharSetUtils.containsAny("", (String) null));
        assertFalse(CharSetUtils.containsAny("", ""));
        assertFalse(CharSetUtils.containsAny("", "a-e"));

        // Test with non-empty strings
        assertFalse(CharSetUtils.containsAny("hello", (String) null));
        assertFalse(CharSetUtils.containsAny("hello", ""));
        assertTrue(CharSetUtils.containsAny("hello", "a-e"));
        assertTrue(CharSetUtils.containsAny("hello", "l-p"));
    }

    /**
     * Tests the containsAny method with String and String array parameters.
     */
    @Test
    void testContainsAny_StringStringArray() {
        // Test with null and empty strings
        assertFalse(CharSetUtils.containsAny(null, (String[]) null));
        assertFalse(CharSetUtils.containsAny(null));
        assertFalse(CharSetUtils.containsAny(null, (String) null));
        assertFalse(CharSetUtils.containsAny(null, "a-e"));

        assertFalse(CharSetUtils.containsAny("", (String[]) null));
        assertFalse(CharSetUtils.containsAny(""));
        assertFalse(CharSetUtils.containsAny("", (String) null));
        assertFalse(CharSetUtils.containsAny("", "a-e"));

        // Test with non-empty strings
        assertFalse(CharSetUtils.containsAny("hello", (String[]) null));
        assertFalse(CharSetUtils.containsAny("hello"));
        assertFalse(CharSetUtils.containsAny("hello", (String) null));
        assertTrue(CharSetUtils.containsAny("hello", "a-e"));
        assertTrue(CharSetUtils.containsAny("hello", "el"));
        assertFalse(CharSetUtils.containsAny("hello", "x"));
        assertTrue(CharSetUtils.containsAny("hello", "e-i"));
        assertTrue(CharSetUtils.containsAny("hello", "a-z"));
        assertFalse(CharSetUtils.containsAny("hello", ""));
    }

    /**
     * Tests the count method with String and String parameters.
     */
    @Test
    void testCount_StringString() {
        // Test with null and empty strings
        assertEquals(0, CharSetUtils.count(null, (String) null));
        assertEquals(0, CharSetUtils.count(null, ""));
        assertEquals(0, CharSetUtils.count("", (String) null));
        assertEquals(0, CharSetUtils.count("", ""));
        assertEquals(0, CharSetUtils.count("", "a-e"));

        // Test with non-empty strings
        assertEquals(0, CharSetUtils.count("hello", (String) null));
        assertEquals(0, CharSetUtils.count("hello", ""));
        assertEquals(1, CharSetUtils.count("hello", "a-e"));
        assertEquals(3, CharSetUtils.count("hello", "l-p"));
    }

    /**
     * Tests the count method with String and String array parameters.
     */
    @Test
    void testCount_StringStringArray() {
        // Test with null and empty strings
        assertEquals(0, CharSetUtils.count(null, (String[]) null));
        assertEquals(0, CharSetUtils.count(null));
        assertEquals(0, CharSetUtils.count(null, (String) null));
        assertEquals(0, CharSetUtils.count(null, "a-e"));

        assertEquals(0, CharSetUtils.count("", (String[]) null));
        assertEquals(0, CharSetUtils.count(""));
        assertEquals(0, CharSetUtils.count("", (String) null));
        assertEquals(0, CharSetUtils.count("", "a-e"));

        // Test with non-empty strings
        assertEquals(0, CharSetUtils.count("hello", (String[]) null));
        assertEquals(0, CharSetUtils.count("hello"));
        assertEquals(0, CharSetUtils.count("hello", (String) null));
        assertEquals(1, CharSetUtils.count("hello", "a-e"));
        assertEquals(3, CharSetUtils.count("hello", "el"));
        assertEquals(0, CharSetUtils.count("hello", "x"));
        assertEquals(2, CharSetUtils.count("hello", "e-i"));
        assertEquals(5, CharSetUtils.count("hello", "a-z"));
        assertEquals(0, CharSetUtils.count("hello", ""));
    }

    /**
     * Tests the delete method with String and String parameters.
     */
    @Test
    void testDelete_StringString() {
        // Test with null and empty strings
        assertNull(CharSetUtils.delete(null, (String) null));
        assertNull(CharSetUtils.delete(null, ""));
        assertEquals("", CharSetUtils.delete("", (String) null));
        assertEquals("", CharSetUtils.delete("", ""));
        assertEquals("", CharSetUtils.delete("", "a-e"));

        // Test with non-empty strings
        assertEquals("hello", CharSetUtils.delete("hello", (String) null));
        assertEquals("hello", CharSetUtils.delete("hello", ""));
        assertEquals("hllo", CharSetUtils.delete("hello", "a-e"));
        assertEquals("he", CharSetUtils.delete("hello", "l-p"));
        assertEquals("hello", CharSetUtils.delete("hello", "z"));
    }

    /**
     * Tests the delete method with String and String array parameters.
     */
    @Test
    void testDelete_StringStringArray() {
        // Test with null and empty strings
        assertNull(CharSetUtils.delete(null, (String[]) null));
        assertNull(CharSetUtils.delete(null));
        assertNull(CharSetUtils.delete(null, (String) null));
        assertNull(CharSetUtils.delete(null, "el"));

        assertEquals("", CharSetUtils.delete("", (String[]) null));
        assertEquals("", CharSetUtils.delete(""));
        assertEquals("", CharSetUtils.delete("", (String) null));
        assertEquals("", CharSetUtils.delete("", "a-e"));

        // Test with non-empty strings
        assertEquals("hello", CharSetUtils.delete("hello", (String[]) null));
        assertEquals("hello", CharSetUtils.delete("hello"));
        assertEquals("hello", CharSetUtils.delete("hello", (String) null));
        assertEquals("hello", CharSetUtils.delete("hello", "xyz"));
        assertEquals("ho", CharSetUtils.delete("hello", "el"));
        assertEquals("", CharSetUtils.delete("hello", "elho"));
        assertEquals("hello", CharSetUtils.delete("hello", ""));
        assertEquals("", CharSetUtils.delete("hello", "a-z"));
        assertEquals("", CharSetUtils.delete("----", "-"));
        assertEquals("heo", CharSetUtils.delete("hello", "l"));
    }

    /**
     * Tests the keep method with String and String parameters.
     */
    @Test
    void testKeep_StringString() {
        // Test with null and empty strings
        assertNull(CharSetUtils.keep(null, (String) null));
        assertNull(CharSetUtils.keep(null, ""));
        assertEquals("", CharSetUtils.keep("", (String) null));
        assertEquals("", CharSetUtils.keep("", ""));
        assertEquals("", CharSetUtils.keep("", "a-e"));

        // Test with non-empty strings
        assertEquals("", CharSetUtils.keep("hello", (String) null));
        assertEquals("", CharSetUtils.keep("hello", ""));
        assertEquals("", CharSetUtils.keep("hello", "xyz"));
        assertEquals("hello", CharSetUtils.keep("hello", "a-z"));
        assertEquals("hello", CharSetUtils.keep("hello", "oleh"));
        assertEquals("ell", CharSetUtils.keep("hello", "el"));
    }

    /**
     * Tests the keep method with String and String array parameters.
     */
    @Test
    void testKeep_StringStringArray() {
        // Test with null and empty strings
        assertNull(CharSetUtils.keep(null, (String[]) null));
        assertNull(CharSetUtils.keep(null));
        assertNull(CharSetUtils.keep(null, (String) null));
        assertNull(CharSetUtils.keep(null, "a-e"));

        assertEquals("", CharSetUtils.keep("", (String[]) null));
        assertEquals("", CharSetUtils.keep(""));
        assertEquals("", CharSetUtils.keep("", (String) null));
        assertEquals("", CharSetUtils.keep("", "a-e"));

        // Test with non-empty strings
        assertEquals("", CharSetUtils.keep("hello", (String[]) null));
        assertEquals("", CharSetUtils.keep("hello"));
        assertEquals("", CharSetUtils.keep("hello", (String) null));
        assertEquals("e", CharSetUtils.keep("hello", "a-e"));
        assertEquals("ell", CharSetUtils.keep("hello", "el"));
        assertEquals("hello", CharSetUtils.keep("hello", "elho"));
        assertEquals("hello", CharSetUtils.keep("hello", "a-z"));
        assertEquals("----", CharSetUtils.keep("----", "-"));
        assertEquals("ll", CharSetUtils.keep("hello", "l"));
    }

    /**
     * Tests the squeeze method with String and String parameters.
     */
    @Test
    void testSqueeze_StringString() {
        // Test with null and empty strings
        assertNull(CharSetUtils.squeeze(null, (String) null));
        assertNull(CharSetUtils.squeeze(null, ""));
        assertEquals("", CharSetUtils.squeeze("", (String) null));
        assertEquals("", CharSetUtils.squeeze("", ""));
        assertEquals("", CharSetUtils.squeeze("", "a-e"));

        // Test with non-empty strings
        assertEquals("hello", CharSetUtils.squeeze("hello", (String) null));
        assertEquals("hello", CharSetUtils.squeeze("hello", ""));
        assertEquals("hello", CharSetUtils.squeeze("hello", "a-e"));
        assertEquals("helo", CharSetUtils.squeeze("hello", "l-p"));
        assertEquals("heloo", CharSetUtils.squeeze("helloo", "l"));
        assertEquals("hello", CharSetUtils.squeeze("helloo", "^l"));
    }

    /**
     * Tests the squeeze method with String and String array parameters.
     */
    @Test
    void testSqueeze_StringStringArray() {
        // Test with null and empty strings
        assertNull(CharSetUtils.squeeze(null, (String[]) null));
        assertNull(CharSetUtils.squeeze(null));
        assertNull(CharSetUtils.squeeze(null, (String) null));
        assertNull(CharSetUtils.squeeze(null, "el"));

        assertEquals("", CharSetUtils.squeeze("", (String[]) null));
        assertEquals("", CharSetUtils.squeeze(""));
        assertEquals("", CharSetUtils.squeeze("", (String) null));
        assertEquals("", CharSetUtils.squeeze("", "a-e"));

        // Test with non-empty strings
        assertEquals("hello", CharSetUtils.squeeze("hello", (String[]) null));
        assertEquals("hello", CharSetUtils.squeeze("hello"));
        assertEquals("hello", CharSetUtils.squeeze("hello", (String) null));
        assertEquals("hello", CharSetUtils.squeeze("hello", "a-e"));
        assertEquals("helo", CharSetUtils.squeeze("hello", "el"));
        assertEquals("hello", CharSetUtils.squeeze("hello", "e"));
        assertEquals("fofof", CharSetUtils.squeeze("fooffooff", "of"));
        assertEquals("fof", CharSetUtils.squeeze("fooooff", "fo"));
    }
}