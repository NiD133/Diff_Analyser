package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.util.Separators;

/**
 * Test suite for the Separators class, which defines separator characters
 * and spacing for JSON pretty printing (colons, commas, empty containers, etc.)
 */
public class SeparatorsTest {

    // Test constants for better readability
    private static final char COLON = ':';
    private static final char COMMA = ',';
    private static final char CUSTOM_SEPARATOR = 'T';
    private static final String EMPTY_STRING = "";
    private static final String SINGLE_SPACE = " ";
    private static final String CUSTOM_EMPTY_SEPARATOR = "custom";

    @Test
    public void testWithArrayValueSeparator_CustomSeparator_ReturnsNewInstanceWithUpdatedSeparator() {
        // Given: A Separators instance with custom configuration
        Separators original = new Separators(EMPTY_STRING, ')', Separators.Spacing.AFTER, 
                                           ')', Separators.Spacing.AFTER, EMPTY_STRING, 
                                           'M', Separators.Spacing.AFTER, EMPTY_STRING);
        
        // When: Changing the array value separator
        Separators updated = original.withArrayValueSeparator(CUSTOM_SEPARATOR);
        
        // Then: New instance has updated array separator, other properties unchanged
        assertEquals(CUSTOM_SEPARATOR, updated.getArrayValueSeparator());
        assertEquals(')', updated.getObjectEntrySeparator());
        assertEquals(')', updated.getObjectFieldValueSeparator());
        assertEquals(EMPTY_STRING, updated.getArrayEmptySeparator());
        assertEquals(EMPTY_STRING, updated.getObjectEmptySeparator());
        assertEquals(EMPTY_STRING, updated.getRootSeparator());
    }

    @Test
    public void testWithArrayValueSeparator_DefaultInstance_ReturnsNewInstanceWithCustomSeparator() {
        // Given: Default Separators instance
        Separators defaultSeparators = new Separators();
        
        // When: Setting custom array value separator
        Separators customSeparators = defaultSeparators.withArrayValueSeparator('(');
        
        // Then: Array separator is updated, other defaults preserved
        assertEquals('(', customSeparators.getArrayValueSeparator());
        assertEquals(COLON, defaultSeparators.getObjectFieldValueSeparator());
        assertEquals(COMMA, customSeparators.getObjectEntrySeparator());
        assertEquals(Separators.Spacing.NONE, customSeparators.getObjectEntrySpacing());
        assertEquals(Separators.Spacing.BOTH, customSeparators.getObjectFieldValueSpacing());
        assertEquals(Separators.Spacing.NONE, customSeparators.getArrayValueSpacing());
    }

    @Test
    public void testWithObjectEntrySpacing_DifferentSpacing_ReturnsNewInstanceWithUpdatedSpacing() {
        // Given: Separators with mixed spacing configurations
        Separators original = new Separators(EMPTY_STRING, ')', Separators.Spacing.AFTER, 
                                           ')', Separators.Spacing.BEFORE, EMPTY_STRING, 
                                           'M', Separators.Spacing.AFTER, EMPTY_STRING);
        
        // When: Changing object entry spacing to AFTER
        Separators updated = original.withObjectEntrySpacing(Separators.Spacing.AFTER);
        
        // Then: Object entry spacing is updated, other properties preserved
        assertEquals(Separators.Spacing.AFTER, updated.getObjectEntrySpacing());
        assertEquals(EMPTY_STRING, updated.getObjectEmptySeparator());
        assertEquals(EMPTY_STRING, updated.getRootSeparator());
        assertEquals(EMPTY_STRING, updated.getArrayEmptySeparator());
        assertNotSame(updated, original); // Verify new instance created
    }

    @Test
    public void testWithObjectEntrySeparator_CustomCharacter_ReturnsNewInstanceWithUpdatedSeparator() {
        // Given: Separators with custom configuration
        Separators original = new Separators(null, '6', Separators.Spacing.NONE, 
                                           'E', Separators.Spacing.NONE, null, 
                                           '3', Separators.Spacing.NONE, null);
        
        // When: Changing object entry separator
        Separators updated = original.withObjectEntrySeparator('>');
        
        // Then: Object entry separator is updated
        assertEquals('>', updated.getObjectEntrySeparator());
        assertEquals('6', original.getObjectFieldValueSeparator());
        assertEquals('3', original.getArrayValueSeparator());
    }

    @Test
    public void testWithObjectFieldValueSeparator_CustomCharacter_ReturnsNewInstanceWithUpdatedSeparator() {
        // Given: Separators with all same characters
        Separators original = new Separators(null, 'A', Separators.Spacing.BOTH, 
                                           'A', Separators.Spacing.BOTH, null, 
                                           'A', Separators.Spacing.BOTH, null);
        
        // When: Changing object field value separator
        Separators updated = original.withObjectFieldValueSeparator('?');
        
        // Then: Object field value separator is updated, others unchanged
        assertEquals('?', updated.getObjectFieldValueSeparator());
        assertEquals('A', updated.getArrayValueSeparator());
        assertEquals('A', updated.getObjectEntrySeparator());
    }

    @Test
    public void testGetRootSeparator_CustomRootSeparator_ReturnsCorrectValue() {
        // Given: Separators with custom root separator
        String customRoot = "customRoot";
        Separators separators = new Separators(customRoot, 'a', Separators.Spacing.BOTH, 
                                             'O', Separators.Spacing.BOTH, "objectEmpty", 
                                             'a', Separators.Spacing.BOTH, "arrayEmpty");
        
        // When: Getting root separator
        String rootSeparator = separators.getRootSeparator();
        
        // Then: Returns the custom root separator
        assertEquals(customRoot, rootSeparator);
        assertEquals('a', separators.getArrayValueSeparator());
        assertEquals('O', separators.getObjectEntrySeparator());
    }

    @Test
    public void testGetObjectFieldValueSeparator_DefaultInstance_ReturnsColon() {
        // Given: Default Separators instance
        Separators defaultSeparators = Separators.createDefaultInstance();
        
        // When: Getting object field value separator
        char separator = defaultSeparators.getObjectFieldValueSeparator();
        
        // Then: Returns default colon character
        assertEquals(COLON, separator);
        assertEquals(COMMA, defaultSeparators.getObjectEntrySeparator());
        assertEquals(COMMA, defaultSeparators.getArrayValueSeparator());
    }

    @Test
    public void testGetObjectEntrySeparator_DefaultInstance_ReturnsComma() {
        // Given: Default Separators instance
        Separators defaultSeparators = new Separators();
        
        // When: Getting object entry separator
        char separator = defaultSeparators.getObjectEntrySeparator();
        
        // Then: Returns default comma character
        assertEquals(COMMA, separator);
        assertEquals(COMMA, defaultSeparators.getArrayValueSeparator());
        assertEquals(Separators.Spacing.BOTH, defaultSeparators.getObjectFieldValueSpacing());
        assertEquals(Separators.Spacing.NONE, defaultSeparators.getArrayValueSpacing());
        assertEquals(Separators.Spacing.NONE, defaultSeparators.getObjectEntrySpacing());
    }

    @Test
    public void testGetArrayValueSeparator_CustomCharacters_ReturnsCorrectSeparator() {
        // Given: Separators with custom array value separator
        Separators separators = new Separators('J', 'J', 'N');
        
        // When: Getting array value separator
        char separator = separators.getArrayValueSeparator();
        
        // Then: Returns the custom array separator
        assertEquals('N', separator);
        assertEquals('J', separators.getObjectFieldValueSeparator());
        assertEquals('J', separators.getObjectEntrySeparator());
        assertEquals(Separators.Spacing.NONE, separators.getObjectEntrySpacing());
        assertEquals(Separators.Spacing.BOTH, separators.getObjectFieldValueSpacing());
        assertEquals(Separators.Spacing.NONE, separators.getArrayValueSpacing());
    }

    @Test
    public void testGetArrayEmptySeparator_NullValue_ReturnsNull() {
        // Given: Separators with null array empty separator
        Separators separators = new Separators(null, 'A', Separators.Spacing.BOTH, 
                                             'A', Separators.Spacing.BOTH, null, 
                                             'A', Separators.Spacing.BOTH, null);
        
        // When: Getting array empty separator
        String emptySeparator = separators.getArrayEmptySeparator();
        
        // Then: Returns null as configured
        assertNull(emptySeparator);
        assertEquals('A', separators.getObjectEntrySeparator());
        assertEquals('A', separators.getObjectFieldValueSeparator());
        assertEquals('A', separators.getArrayValueSeparator());
    }

    @Test
    public void testWithArrayEmptySeparator_SameValue_ReturnsSameInstance() {
        // Given: Separators with null array empty separator
        Separators original = new Separators(null, '6', Separators.Spacing.NONE, 
                                           'E', Separators.Spacing.NONE, null, 
                                           '3', Separators.Spacing.NONE, null);
        
        // When: Setting array empty separator to same value (null)
        Separators result = original.withArrayEmptySeparator(null);
        
        // Then: Returns same instance (optimization)
        assertSame(result, original);
        assertEquals('E', result.getObjectEntrySeparator());
        assertEquals('3', result.getArrayValueSeparator());
        assertEquals('6', result.getObjectFieldValueSeparator());
    }

    @Test
    public void testWithArrayEmptySeparator_DifferentValue_ReturnsNewInstance() {
        // Given: Separators with specific array empty separator
        Separators original = new Separators(EMPTY_STRING, 'J', Separators.Spacing.NONE, 
                                           'J', Separators.Spacing.NONE, EMPTY_STRING, 
                                           'J', Separators.Spacing.NONE, "original");
        
        // When: Changing array empty separator
        Separators updated = original.withArrayEmptySeparator(SINGLE_SPACE);
        
        // Then: New instance with updated array empty separator
        assertEquals(SINGLE_SPACE, updated.getArrayEmptySeparator());
        assertEquals(EMPTY_STRING, updated.getObjectEmptySeparator());
        assertEquals(EMPTY_STRING, updated.getRootSeparator());
        assertNotSame(updated, original);
    }

    @Test
    public void testSpacingEnum_BothSpacing_ReturnsSpacesAfter() {
        // Given: BOTH spacing enum value
        Separators.Spacing bothSpacing = Separators.Spacing.BOTH;
        
        // When: Getting spaces after
        String spacesAfter = bothSpacing.spacesAfter();
        
        // Then: Returns single space
        assertEquals(SINGLE_SPACE, spacesAfter);
    }

    @Test
    public void testSpacingEnum_BothSpacing_AppliesSpacesAroundCharacter() {
        // Given: BOTH spacing enum value
        Separators.Spacing bothSpacing = Separators.Spacing.BOTH;
        
        // When: Applying spacing to character
        String result = bothSpacing.apply('p');
        
        // Then: Character is surrounded by spaces
        assertEquals(" p ", result);
    }

    @Test
    public void testSpacingEnum_NoneSpacing_ReturnsEmptySpacesBefore() {
        // Given: Separators with NONE spacing
        Separators separators = new Separators('(', '(', '(');
        Separators.Spacing noneSpacing = separators.getArrayValueSpacing();
        
        // When: Getting spaces before
        String spacesBefore = noneSpacing.spacesBefore();
        
        // Then: Returns empty string
        assertEquals(EMPTY_STRING, spacesBefore);
        assertEquals(Separators.Spacing.BOTH, separators.getObjectFieldValueSpacing());
        assertEquals(Separators.Spacing.NONE, separators.getObjectEntrySpacing());
    }

    @Test
    public void testImmutability_WithMethods_ReturnSameInstanceWhenNoChange() {
        // Given: Separators with specific configuration
        Separators original = new Separators(EMPTY_STRING, 'J', Separators.Spacing.NONE, 
                                           'J', Separators.Spacing.NONE, EMPTY_STRING, 
                                           'J', Separators.Spacing.NONE, "arrayEmpty");
        
        // When: Setting same values using with methods
        Separators sameArraySeparator = original.withArrayValueSeparator('J');
        Separators sameObjectEmpty = original.withObjectEmptySeparator(EMPTY_STRING);
        Separators sameObjectEntry = original.withObjectEntrySeparator('J');
        Separators sameObjectFieldValue = original.withObjectFieldValueSeparator('J');
        
        // Then: Same instances returned (optimization for unchanged values)
        assertSame(sameArraySeparator, original);
        assertSame(sameObjectEmpty, original);
        assertSame(sameObjectEntry, original);
        assertSame(sameObjectFieldValue, original);
    }

    @Test
    public void testCreateDefaultInstance_ReturnsStandardJSONSeparators() {
        // Given: Default instance creation
        Separators defaultSeparators = Separators.createDefaultInstance();
        
        // When: Checking default values
        // Then: Standard JSON separators are used
        assertEquals(COMMA, defaultSeparators.getArrayValueSeparator());
        assertEquals(COMMA, defaultSeparators.getObjectEntrySeparator());
        assertEquals(COLON, defaultSeparators.getObjectFieldValueSeparator());
        assertEquals(Separators.Spacing.NONE, defaultSeparators.getObjectEntrySpacing());
        assertEquals(Separators.Spacing.BOTH, defaultSeparators.getObjectFieldValueSpacing());
        assertEquals(Separators.Spacing.NONE, defaultSeparators.getArrayValueSpacing());
    }
}