package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Collection;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for the OptionGroup class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class OptionGroup_ESTest extends OptionGroup_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testOptionGroupIsRequired() throws Throwable {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue("OptionGroup should be required", optionGroup.isRequired());
    }

    @Test(timeout = 4000)
    public void testSetAndGetSelectedOption() throws Throwable {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("oQxw", null, true, "[]");
        optionGroup.setSelected(option);
        assertEquals("Selected option should be 'oQxw'", "oQxw", optionGroup.getSelected());
    }

    @Test(timeout = 4000)
    public void testSetAndGetSelectedOptionWithEmptyName() throws Throwable {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option(null, "", false, "cHfx;>NW^}R|1DYvgS");
        optionGroup.setSelected(option);
        assertEquals("Selected option should have an empty name", "", optionGroup.getSelected());
    }

    @Test(timeout = 4000)
    public void testAddOptionToGroup() throws Throwable {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("oQxw", null, true, "[]");
        optionGroup.setSelected(option);
        OptionGroup updatedGroup = optionGroup.addOption(option);
        assertTrue("Option should be selected in the group", updatedGroup.isSelected());
    }

    @Test(timeout = 4000)
    public void testAddOptionAndCheckRequired() throws Throwable {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option(null, null);
        optionGroup.setRequired(true);
        optionGroup.addOption(option);
        assertTrue("OptionGroup should be required after adding an option", optionGroup.isRequired());
    }

    @Test(timeout = 4000)
    public void testAddNullOptionThrowsException() throws Throwable {
        OptionGroup optionGroup = new OptionGroup();
        try {
            optionGroup.addOption(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.cli.OptionGroup", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetOptionsReturnsNonNullCollection() throws Throwable {
        OptionGroup optionGroup = new OptionGroup();
        Collection<Option> options = optionGroup.getOptions();
        assertNotNull("Options collection should not be null", options);
    }

    @Test(timeout = 4000)
    public void testToStringWithMultipleOptions() throws Throwable {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option(null, null);
        Option option2 = new Option("vN", "", false, "");
        optionGroup.addOption(option1).addOption(option2);
        assertEquals("String representation should match", "[--null, -vN ]", optionGroup.toString());
    }

    @Test(timeout = 4000)
    public void testSetSelectedThrowsExceptionIfAlreadySelected() throws Throwable {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("oQxw", null, true, "[]");
        optionGroup.setSelected(option1);
        Option option2 = new Option("Xr0g", false, "[");
        try {
            optionGroup.setSelected(option2);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.cli.OptionGroup", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetSelectedOptionTwice() throws Throwable {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("oQxw", null, true, "[]");
        optionGroup.setSelected(option);
        optionGroup.setSelected(option);
        assertFalse("Option should not be required after being selected", option.isRequired());
    }

    @Test(timeout = 4000)
    public void testSetSelectedNullOption() throws Throwable {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setSelected(null);
        assertFalse("OptionGroup should not be required when selected option is null", optionGroup.isRequired());
    }

    @Test(timeout = 4000)
    public void testIsSelectedReturnsTrueIfOptionSelected() throws Throwable {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("oQxw", null, true, "[]");
        optionGroup.setSelected(option);
        assertTrue("Option should be selected", optionGroup.isSelected());
    }

    @Test(timeout = 4000)
    public void testIsSelectedReturnsFalseIfNoOptionSelected() throws Throwable {
        OptionGroup optionGroup = new OptionGroup();
        assertFalse("No option should be selected initially", optionGroup.isSelected());
    }

    @Test(timeout = 4000)
    public void testGetSelectedReturnsNullIfNoOptionSelected() throws Throwable {
        OptionGroup optionGroup = new OptionGroup();
        assertNull("Selected option should be null if none is selected", optionGroup.getSelected());
    }

    @Test(timeout = 4000)
    public void testGetNamesReturnsNonNullCollection() throws Throwable {
        OptionGroup optionGroup = new OptionGroup();
        Collection<String> names = optionGroup.getNames();
        assertNotNull("Names collection should not be null", names);
    }

    @Test(timeout = 4000)
    public void testIsRequiredReturnsFalseByDefault() throws Throwable {
        OptionGroup optionGroup = new OptionGroup();
        assertFalse("OptionGroup should not be required by default", optionGroup.isRequired());
    }
}