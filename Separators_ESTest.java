package com.fasterxml.jackson.core.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Separators} class, verifying its constructors,
 * immutability, and the behavior of its "with" methods.
 */
public class SeparatorsTest {

    // --- Constructor and Factory Tests ---

    @Test
    public void createDefaultInstance_shouldHaveStandardJsonSeparators() {
        // Arrange & Act
        Separators separators = Separators.createDefaultInstance();

        // Assert
        assertEquals("Default object field separator should be ':'", ':', separators.getObjectFieldValueSeparator());
        assertEquals("Default object entry separator should be ','", ',', separators.getObjectEntrySeparator());
        assertEquals("Default array value separator should be ','", ',', separators.getArrayValueSeparator());

        assertEquals("Default object field value spacing should be BOTH", Separators.Spacing.BOTH, separators.getObjectFieldValueSpacing());
        assertEquals("Default object entry spacing should be NONE", Separators.Spacing.NONE, separators.getObjectEntrySpacing());
        assertEquals("Default array value spacing should be NONE", Separators.Spacing.NONE, separators.getArrayValueSpacing());

        assertEquals("Default root separator should be a single space", " ", separators.getRootSeparator());
        assertEquals("Default empty object separator should be a single space", " ", separators.getObjectEmptySeparator());
        assertEquals("Default empty array separator should be a single space", " ", separators.getArrayEmptySeparator());
    }

    @Test
    public void defaultConstructor_shouldBeEquivalentToCreateDefaultInstance() {
        // Arrange
        Separators fromFactory = Separators.createDefaultInstance();
        
        // Act
        Separators fromConstructor = new Separators();

        // Assert
        assertEquals(fromFactory.getObjectFieldValueSeparator(), fromConstructor.getObjectFieldValueSeparator());
        assertEquals(fromFactory.getObjectEntrySeparator(), fromConstructor.getObjectEntrySeparator());
        assertEquals(fromFactory.getArrayValueSeparator(), fromConstructor.getArrayValueSeparator());
    }

    @Test
    public void customConstructor_shouldSetSeparatorsAndDefaultSpacings() {
        // Arrange & Act
        Separators separators = new Separators(';', '|', '-');

        // Assert
        assertEquals(';', separators.getObjectFieldValueSeparator());
        assertEquals('|', separators.getObjectEntrySeparator());
        assertEquals('-', separators.getArrayValueSeparator());

        // Check that default spacings are applied
        assertEquals(Separators.Spacing.BOTH, separators.getObjectFieldValueSpacing());
        assertEquals(Separators.Spacing.NONE, separators.getObjectEntrySpacing());
        assertEquals(Separators.Spacing.NONE, separators.getArrayValueSpacing());
    }

    @Test
    public void fullConstructor_shouldSetAllProperties() {
        // Arrange
        Separators.Spacing spacing = Separators.Spacing.AFTER;

        // Act
        Separators separators = new Separators("||", ':', spacing, ',', spacing, "{}", '-', spacing, "[]");

        // Assert
        assertEquals("||", separators.getRootSeparator());
        assertEquals(':', separators.getObjectFieldValueSeparator());
        assertEquals(spacing, separators.getObjectFieldValueSpacing());
        assertEquals(',', separators.getObjectEntrySeparator());
        assertEquals(spacing, separators.getObjectEntrySpacing());
        assertEquals("{}", separators.getObjectEmptySeparator());
        assertEquals('-', separators.getArrayValueSeparator());
        assertEquals(spacing, separators.getArrayValueSpacing());
        assertEquals("[]", separators.getArrayEmptySeparator());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void deprecatedConstructor_shouldSetSeparatorsAndSpacings() {
        // Arrange
        Separators.Spacing spacing = Separators.Spacing.BEFORE;

        // Act
        Separators separators = new Separators("~", 'a', spacing, 'b', spacing, 'c', spacing);

        // Assert
        assertEquals("~", separators.getRootSeparator());
        assertEquals('a', separators.getObjectFieldValueSeparator());
        assertEquals(spacing, separators.getObjectFieldValueSpacing());
        assertEquals('b', separators.getObjectEntrySeparator());
        assertEquals(spacing, separators.getObjectEntrySpacing());
        assertEquals('c', separators.getArrayValueSeparator());
        assertEquals(spacing, separators.getArrayValueSpacing());

        // Check that empty separators get their default values
        assertEquals(Separators.DEFAULT_OBJECT_EMPTY_SEPARATOR, separators.getObjectEmptySeparator());
        assertEquals(Separators.DEFAULT_ARRAY_EMPTY_SEPARATOR, separators.getArrayEmptySeparator());
    }


    // --- "with..." Method Tests (Immutability) ---

    @Test
    public void withRootSeparator_whenValueIsChanged_shouldReturnNewInstance() {
        // Arrange
        Separators initialSeparators = Separators.createDefaultInstance();

        // Act
        Separators updatedSeparators = initialSeparators.withRootSeparator("\n");

        // Assert
        assertNotSame("A new instance should be returned", initialSeparators, updatedSeparators);
        assertEquals("\n", updatedSeparators.getRootSeparator());
    }

    @Test
    public void withRootSeparator_whenValueIsUnchanged_shouldReturnSameInstance() {
        // Arrange
        Separators initialSeparators = Separators.createDefaultInstance();

        // Act
        Separators updatedSeparators = initialSeparators.withRootSeparator(initialSeparators.getRootSeparator());

        // Assert
        assertSame("The same instance should be returned", initialSeparators, updatedSeparators);
    }

    @Test
    public void withObjectFieldValueSeparator_whenValueIsChanged_shouldReturnNewInstance() {
        // Arrange
        Separators initialSeparators = Separators.createDefaultInstance();

        // Act
        Separators updatedSeparators = initialSeparators.withObjectFieldValueSeparator('=');

        // Assert
        assertNotSame("A new instance should be returned", initialSeparators, updatedSeparators);
        assertEquals('=', updatedSeparators.getObjectFieldValueSeparator());
    }

    @Test
    public void withObjectFieldValueSeparator_whenValueIsUnchanged_shouldReturnSameInstance() {
        // Arrange
        Separators initialSeparators = Separators.createDefaultInstance();

        // Act
        Separators updatedSeparators = initialSeparators.withObjectFieldValueSeparator(':');

        // Assert
        assertSame("The same instance should be returned", initialSeparators, updatedSeparators);
    }

    @Test
    public void withObjectFieldValueSpacing_whenValueIsChanged_shouldReturnNewInstance() {
        // Arrange
        Separators initialSeparators = Separators.createDefaultInstance();

        // Act
        Separators updatedSeparators = initialSeparators.withObjectFieldValueSpacing(Separators.Spacing.NONE);

        // Assert
        assertNotSame("A new instance should be returned", initialSeparators, updatedSeparators);
        assertEquals(Separators.Spacing.NONE, updatedSeparators.getObjectFieldValueSpacing());
    }

    @Test
    public void withObjectFieldValueSpacing_whenValueIsUnchanged_shouldReturnSameInstance() {
        // Arrange
        Separators initialSeparators = Separators.createDefaultInstance();

        // Act
        Separators updatedSeparators = initialSeparators.withObjectFieldValueSpacing(Separators.Spacing.BOTH);

        // Assert
        assertSame("The same instance should be returned", initialSeparators, updatedSeparators);
    }

    @Test
    public void withObjectEntrySeparator_whenValueIsChanged_shouldReturnNewInstance() {
        // Arrange
        Separators initialSeparators = Separators.createDefaultInstance();

        // Act
        Separators updatedSeparators = initialSeparators.withObjectEntrySeparator(';');

        // Assert
        assertNotSame("A new instance should be returned", initialSeparators, updatedSeparators);
        assertEquals(';', updatedSeparators.getObjectEntrySeparator());
    }

    @Test
    public void withObjectEntrySeparator_whenValueIsUnchanged_shouldReturnSameInstance() {
        // Arrange
        Separators initialSeparators = Separators.createDefaultInstance();

        // Act
        Separators updatedSeparators = initialSeparators.withObjectEntrySeparator(',');

        // Assert
        assertSame("The same instance should be returned", initialSeparators, updatedSeparators);
    }

    @Test
    public void withObjectEntrySpacing_whenValueIsChanged_shouldReturnNewInstance() {
        // Arrange
        Separators initialSeparators = Separators.createDefaultInstance();

        // Act
        Separators updatedSeparators = initialSeparators.withObjectEntrySpacing(Separators.Spacing.AFTER);

        // Assert
        assertNotSame("A new instance should be returned", initialSeparators, updatedSeparators);
        assertEquals(Separators.Spacing.AFTER, updatedSeparators.getObjectEntrySpacing());
    }

    @Test
    public void withObjectEntrySpacing_whenValueIsUnchanged_shouldReturnSameInstance() {
        // Arrange
        Separators initialSeparators = Separators.createDefaultInstance();

        // Act
        Separators updatedSeparators = initialSeparators.withObjectEntrySpacing(Separators.Spacing.NONE);

        // Assert
        assertSame("The same instance should be returned", initialSeparators, updatedSeparators);
    }

    @Test
    public void withObjectEmptySeparator_whenValueIsChanged_shouldReturnNewInstance() {
        // Arrange
        Separators initialSeparators = Separators.createDefaultInstance();

        // Act
        Separators updatedSeparators = initialSeparators.withObjectEmptySeparator("");

        // Assert
        assertNotSame("A new instance should be returned", initialSeparators, updatedSeparators);
        assertEquals("", updatedSeparators.getObjectEmptySeparator());
    }

    @Test
    public void withObjectEmptySeparator_whenValueIsUnchanged_shouldReturnSameInstance() {
        // Arrange
        Separators initialSeparators = Separators.createDefaultInstance();

        // Act
        Separators updatedSeparators = initialSeparators.withObjectEmptySeparator(" ");

        // Assert
        assertSame("The same instance should be returned", initialSeparators, updatedSeparators);
    }

    @Test
    public void withArrayValueSeparator_whenValueIsChanged_shouldReturnNewInstance() {
        // Arrange
        Separators initialSeparators = Separators.createDefaultInstance();

        // Act
        Separators updatedSeparators = initialSeparators.withArrayValueSeparator(';');

        // Assert
        assertNotSame("A new instance should be returned", initialSeparators, updatedSeparators);
        assertEquals(';', updatedSeparators.getArrayValueSeparator());
    }

    @Test
    public void withArrayValueSeparator_whenValueIsUnchanged_shouldReturnSameInstance() {
        // Arrange
        Separators initialSeparators = Separators.createDefaultInstance();

        // Act
        Separators updatedSeparators = initialSeparators.withArrayValueSeparator(',');

        // Assert
        assertSame("The same instance should be returned", initialSeparators, updatedSeparators);
    }

    @Test
    public void withArrayValueSpacing_whenValueIsChanged_shouldReturnNewInstance() {
        // Arrange
        Separators initialSeparators = Separators.createDefaultInstance();

        // Act
        Separators updatedSeparators = initialSeparators.withArrayValueSpacing(Separators.Spacing.BOTH);

        // Assert
        assertNotSame("A new instance should be returned", initialSeparators, updatedSeparators);
        assertEquals(Separators.Spacing.BOTH, updatedSeparators.getArrayValueSpacing());
    }

    @Test
    public void withArrayValueSpacing_whenValueIsUnchanged_shouldReturnSameInstance() {
        // Arrange
        Separators initialSeparators = Separators.createDefaultInstance();

        // Act
        Separators updatedSeparators = initialSeparators.withArrayValueSpacing(Separators.Spacing.NONE);

        // Assert
        assertSame("The same instance should be returned", initialSeparators, updatedSeparators);
    }

    @Test
    public void withArrayEmptySeparator_whenValueIsChanged_shouldReturnNewInstance() {
        // Arrange
        Separators initialSeparators = Separators.createDefaultInstance();

        // Act
        Separators updatedSeparators = initialSeparators.withArrayEmptySeparator("");

        // Assert
        assertNotSame("A new instance should be returned", initialSeparators, updatedSeparators);
        assertEquals("", updatedSeparators.getArrayEmptySeparator());
    }

    @Test
    public void withArrayEmptySeparator_whenValueIsUnchanged_shouldReturnSameInstance() {
        // Arrange
        Separators initialSeparators = Separators.createDefaultInstance();

        // Act
        Separators updatedSeparators = initialSeparators.withArrayEmptySeparator(" ");

        // Assert
        assertSame("The same instance should be returned", initialSeparators, updatedSeparators);
    }


    // --- Spacing Enum Tests ---

    @Test
    public void spacingEnum_none_shouldApplyNoSpaces() {
        // Arrange
        Separators.Spacing spacing = Separators.Spacing.NONE;

        // Act & Assert
        assertEquals("", spacing.spacesBefore());
        assertEquals("", spacing.spacesAfter());
        assertEquals("s", spacing.apply('s'));
    }

    @Test
    public void spacingEnum_before_shouldApplySpaceBefore() {
        // Arrange
        Separators.Spacing spacing = Separators.Spacing.BEFORE;

        // Act & Assert
        assertEquals(" ", spacing.spacesBefore());
        assertEquals("", spacing.spacesAfter());
        assertEquals(" s", spacing.apply('s'));
    }

    @Test
    public void spacingEnum_after_shouldApplySpaceAfter() {
        // Arrange
        Separators.Spacing spacing = Separators.Spacing.AFTER;

        // Act & Assert
        assertEquals("", spacing.spacesBefore());
        assertEquals(" ", spacing.spacesAfter());
        assertEquals("s ", spacing.apply('s'));
    }

    @Test
    public void spacingEnum_both_shouldApplySpacesBeforeAndAfter() {
        // Arrange
        Separators.Spacing spacing = Separators.Spacing.BOTH;

        // Act & Assert
        assertEquals(" ", spacing.spacesBefore());
        assertEquals(" ", spacing.spacesAfter());
        assertEquals(" s ", spacing.apply('s'));
    }
}