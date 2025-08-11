package org.apache.commons.cli;

import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Readable unit tests for OptionGroup.
 *
 * These tests focus on the observable behavior exercised by the original
 * EvoSuite-generated tests but use clear names, Arrange-Act-Assert structure,
 * and precise assertions.
 */
public class OptionGroupTest {

    // ---------------------------------------------------------------------
    // Required flag
    // ---------------------------------------------------------------------

    @Test
    public void requiredFlag_canBeSetAndRead() {
        // Arrange
        OptionGroup group = new OptionGroup();

        // Act
        group.setRequired(true);

        // Assert
        assertTrue(group.isRequired());
    }

    @Test
    public void requiredFlag_isFalseByDefault() {
        // Arrange
        OptionGroup group = new OptionGroup();

        // Assert
        assertFalse(group.isRequired());
    }

    @Test
    public void requiredFlag_isNotChangedBySettingSelectedToNull() {
        // Arrange
        OptionGroup group = new OptionGroup();

        // Act
        group.setSelected(null); // should be a no-op

        // Assert
        assertFalse(group.isRequired());
    }

    // ---------------------------------------------------------------------
    // Selection
    // ---------------------------------------------------------------------

    @Test
    public void getSelected_returnsShortNameWhenShortNameProvided() {
        // Arrange
        OptionGroup group = new OptionGroup();
        Option shortOnly = new Option("oQxw", (String) null, true, "desc");

        // Act
        group.setSelected(shortOnly);

        // Assert
        assertEquals("oQxw", group.getSelected());
    }

    @Test
    public void getSelected_returnsLongNameWhenShortNameIsNull() {
        // Arrange
        OptionGroup group = new OptionGroup();
        // short = null, long = "" (empty)
        Option longOnlyEmpty = new Option(null, "", false, "desc");

        // Act
        group.setSelected(longOnlyEmpty);

        // Assert
        assertEquals("", group.getSelected());
    }

    @Test
    public void isSelected_isTrueAfterSelectingAnOption() {
        // Arrange
        OptionGroup group = new OptionGroup();
        Option opt = new Option("oQxw", (String) null, true, "desc");

        // Act
        group.setSelected(opt);

        // Assert
        assertTrue(group.isSelected());
    }

    @Test
    public void isSelected_isFalseByDefault() {
        // Arrange
        OptionGroup group = new OptionGroup();

        // Assert
        assertFalse(group.isSelected());
    }

    @Test
    public void getSelected_isNullByDefault() {
        // Arrange
        OptionGroup group = new OptionGroup();

        // Assert
        assertNull(group.getSelected());
    }

    @Test
    public void selectingSecondDifferentOption_throwsAlreadySelectedException_withHelpfulMessage() {
        // Arrange
        OptionGroup group = new OptionGroup();
        Option first = new Option("oQxw", (String) null, true, "desc");
        group.setSelected(first);
        Option second = new Option("Xr0g", false, "[");

        // Act
        AlreadySelectedException ex = assertThrows(AlreadySelectedException.class, () -> group.setSelected(second));

        // Assert (message should reference both options)
        String msg = ex.getMessage();
        assertNotNull(msg);
        assertTrue("message should contain the second option name", msg.contains("Xr0g"));
        assertTrue("message should contain the first option name", msg.contains("oQxw"));
    }

    @Test
    public void selectingSameOptionTwice_isAllowedAndDoesNotChangeOptionRequiredFlag() {
        // Arrange
        OptionGroup group = new OptionGroup();
        Option opt = new Option("oQxw", (String) null, true, "desc");

        // Act
        group.setSelected(opt);
        group.setSelected(opt); // selecting the same option twice is a no-op

        // Assert
        // Original behavior checked the Option's own "required" flag remains false.
        assertFalse(opt.isRequired());
    }

    // ---------------------------------------------------------------------
    // Adding options and collections
    // ---------------------------------------------------------------------

    @Test
    public void addOption_preservesSelectedState_whenAddingPreviouslySelectedOption() {
        // Arrange
        OptionGroup group = new OptionGroup();
        Option opt = new Option("oQxw", (String) null, true, "desc");
        group.setSelected(opt);

        // Act
        OptionGroup returned = group.addOption(opt);

        // Assert
        assertSame(group, returned);
        assertTrue("group should remain selected after adding the same option", returned.isSelected());
    }

    @Test
    public void addOption_doesNotResetRequiredFlag() {
        // Arrange
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        Option opt = new Option(null, (String) null); // minimal option

        // Act
        group.addOption(opt);

        // Assert
        assertTrue(group.isRequired());
    }

    @Test
    public void addOption_null_throwsNullPointerException() {
        // Arrange
        OptionGroup group = new OptionGroup();

        // Act + Assert
        assertThrows(NullPointerException.class, () -> group.addOption(null));
    }

    @Test
    public void getOptions_returnsNonNullCollection() {
        // Arrange
        OptionGroup group = new OptionGroup();

        // Act
        Collection<Option> options = group.getOptions();

        // Assert
        assertNotNull(options);
    }

    @Test
    public void getNames_returnsNonNullCollection() {
        // Arrange
        OptionGroup group = new OptionGroup();

        // Act
        Collection<String> names = group.getNames();

        // Assert
        assertNotNull(names);
    }

    @Test
    public void toString_listsOptionsInInsertionOrder_withExpectedFormatting() {
        // Arrange
        OptionGroup group = new OptionGroup();

        // First option: both short and long are null in constructor (as in original test).
        group.addOption(new Option((String) null, (String) null));

        // Second option: short = "vN", long = "" (empty), no arg, empty description.
        group.addOption(new Option("vN", "", false, ""));

        // Act
        String text = group.toString();

        // Assert
        // The original EvoSuite test expected this exact formatting.
        assertEquals("[--null, -vN ]", text);
    }
}