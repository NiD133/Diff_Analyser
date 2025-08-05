package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A well-structured and understandable test suite for the {@link CharSet} class.
 * This version refactors an automatically generated test suite to improve clarity,
 * intent, and maintainability.
 */
public class CharSetRefactoredTest {

    // --- Test Factory Method: getInstance() ---

    @Test
    public void getInstance_withNullArray_shouldReturnNull() {
        // The factory method is specified to return null for a null input array.
        assertNull("getInstance(null) should return null", CharSet.getInstance((String[]) null));
    }

    @Test
    public void getInstance_withEmptyArray_shouldReturnEmptySet() {
        // An empty array of definitions should result in an empty CharSet.
        CharSet emptySet = CharSet.getInstance(new String[0]);
        assertNotNull(emptySet);
        assertFalse("Empty set should not contain 'a'", emptySet.contains('a'));
        assertEquals("toString() of empty set should be '[]'", "[]", emptySet.toString());
    }

    @Test
    public void getInstance_withArrayOfNulls_shouldReturnEmptySet() {
        // Null strings in the input array should be ignored, resulting in an empty set.
        CharSet emptySet = CharSet.getInstance(new String[] { null, null });
        assertNotNull(emptySet);
        assertFalse("Empty set from null strings should not contain 'a'", emptySet.contains('a'));
        assertEquals("toString() of empty set should be '[]'", "[]", emptySet.toString());
    }

    @Test
    public void getInstance_withSimpleRange_shouldCreateCorrectSet() {
        // Arrange: A simple character range definition.
        String[] definition = {"a-c"};

        // Act: Create the CharSet.
        CharSet charSet = CharSet.getInstance(definition);

        // Assert: Check characters inside and outside the range.
        assertTrue("'a' should be in the set 'a-c'", charSet.contains('a'));
        assertTrue("'b' should be in the set 'a-c'", charSet.contains('b'));
        assertTrue("'c' should be in the set 'a-c'", charSet.contains('c'));
        assertFalse("'d' should not be in the set 'a-c'", charSet.contains('d'));
    }

    @Test
    public void getInstance_withNegatedCharacter_shouldCreateCorrectSet() {
        // Arrange: A definition for a single negated character.
        String[] definition = {"^a"};

        // Act: Create the CharSet.
        CharSet charSet = CharSet.getInstance(definition);

        // Assert: The specified character should be absent, others present.
        assertFalse("'a' should not be in the set '^a'", charSet.contains('a'));
        assertTrue("'b' should be in the set '^a'", charSet.contains('b'));
    }

    @Test
    public void getInstance_withNegatedRange_shouldCreateCorrectSet() {
        // Arrange: A definition for a negated character range.
        String[] definition = {"^a-c"};

        // Act: Create the CharSet.
        CharSet charSet = CharSet.getInstance(definition);

        // Assert: Characters in the range should be absent, others present.
        assertFalse("'a' should not be in the set '^a-c'", charSet.contains('a'));
        assertFalse("'b' should not be in the set '^a-c'", charSet.contains('b'));
        assertFalse("'c' should not be in the set '^a-c'", charSet.contains('c'));
        assertTrue("'d' should be in the set '^a-c'", charSet.contains('d'));
    }

    @Test
    public void getInstance_withMixedRangesAndSingleChars_shouldParseCorrectly() {
        // Arrange: A complex definition with single characters and ranges.
        // This tests parsing of "x-y" ranges mixed with individual characters.
        String[] definition = {"a", "f-h", "z"};

        // Act: Create the CharSet.
        CharSet charSet = CharSet.getInstance(definition);

        // Assert: Check characters from each part of the definition.
        assertTrue(charSet.contains('a'));
        assertFalse(charSet.contains('b'));
        
        assertTrue(charSet.contains('f'));
        assertTrue(charSet.contains('g'));
        assertTrue(charSet.contains('h'));
        assertFalse(charSet.contains('i'));

        assertTrue(charSet.contains('z'));
    }

    @Test
    public void getInstance_withCommonSetDefinition_shouldReturnCachedInstance() {
        // The getInstance factory method caches common CharSet instances.
        CharSet set1 = CharSet.getInstance("a-z");
        CharSet set2 = CharSet.getInstance("a-z");
        
        // For common sets, the factory should return the same instance.
        assertSame("Instances for 'a-z' should be cached and be the same", set1, set2);
        assertEquals(CharSet.ASCII_ALPHA_LOWER, set1);
    }

    // --- Test Constructor ---

    @Test(expected = NullPointerException.class)
    public void constructor_withNullArray_shouldThrowNullPointerException() {
        // The constructor is protected, but its contract states it throws NPE for null.
        new CharSet((String[]) null);
    }

    // --- Test contains() method ---

    @Test
    public void contains_onEmptySet_shouldReturnFalse() {
        // The EMPTY constant should not contain any characters.
        assertFalse("CharSet.EMPTY should not contain 'a'", CharSet.EMPTY.contains('a'));
    }

    // --- Test equals() and hashCode() ---

    @Test
    public void equals_forSameInstance_shouldReturnTrue() {
        // An object must be equal to itself (reflexivity).
        assertTrue("A CharSet instance should be equal to itself", CharSet.ASCII_ALPHA.equals(CharSet.ASCII_ALPHA));
    }
    
    @Test
    public void equals_withDifferentObjectType_shouldReturnFalse() {
        // A CharSet should not be equal to an object of a different type.
        assertFalse("A CharSet should not be equal to a simple Object", CharSet.EMPTY.equals(new Object()));
    }

    @Test
    public void equals_withNull_shouldReturnFalse() {
        // A CharSet should not be equal to null.
        assertFalse("A CharSet should not be equal to null", CharSet.EMPTY.equals(null));
    }

    @Test
    public void equals_and_hashCode_contract() {
        // Two CharSet instances created with the same definitions should be equal and have the same hashCode.
        // Using a non-cached definition to ensure new instances are created.
        CharSet set1 = CharSet.getInstance("a", "b", "c");
        CharSet set2 = CharSet.getInstance("a", "b", "c");

        assertTrue("Two sets with the same definition should be equal", set1.equals(set2));
        assertEquals("Two equal sets should have the same hashCode", set1.hashCode(), set2.hashCode());
        assertNotSame("Instances should be different for non-cached definitions", set1, set2);
    }

    @Test
    public void equals_forDifferentDefinitionsWithSameChars_shouldReturnFalse() {
        // Per Javadoc, equals() compares the definitions, not just the resulting characters.
        // "abc" and "a-c" contain the same characters but have different definitions.
        CharSet setFromChars = CharSet.getInstance("abc");
        CharSet setFromRange = CharSet.getInstance("a-c");

        assertFalse("Sets from 'abc' and 'a-c' should not be equal", setFromChars.equals(setFromRange));
    }

    // --- Test toString() ---

    @Test
    public void toString_onEmptySet_shouldReturnBrackets() {
        // The string representation of an empty set is "[]".
        assertEquals("toString() of an empty set should be '[]'", "[]", CharSet.EMPTY.toString());
    }

    @Test
    public void toString_onNonEmptySet_shouldReturnRepresentationOfRanges() {
        // The string representation should reflect the internal CharRange definitions.
        CharSet charSet = CharSet.getInstance("a-c", "z");
        // The order of ranges in the string representation is not guaranteed.
        String result = charSet.toString();
        assertTrue("toString() should contain '[a-c]'", result.contains("[a-c]"));
        assertTrue("toString() should contain '[z]'", result.contains("[z]"));
    }

    // --- Test Predefined Constants ---

    @Test
    public void constant_EMPTY_shouldBeEmpty() {
        CharRange[] ranges = CharSet.EMPTY.getCharRanges();
        assertEquals("CharSet.EMPTY should have 0 ranges", 0, ranges.length);
    }

    @Test
    public void constant_ASCII_ALPHA_shouldContainLettersOnly() {
        assertTrue(CharSet.ASCII_ALPHA.contains('a'));
        assertTrue(CharSet.ASCII_ALPHA.contains('Z'));
        assertFalse(CharSet.ASCII_ALPHA.contains('5'));
        assertFalse(CharSet.ASCII_ALPHA.contains('-'));
    }
}