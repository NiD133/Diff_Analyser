/*
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import org.junit.Test;

/**
 * Unit tests for the {@link OptionGroup} class.
 */
public class OptionGroupTest {

    @Test
    public void testNewGroupIsInitiallyUnselectedAndNotRequired() {
        // Arrange
        final OptionGroup group = new OptionGroup();

        // Assert
        assertFalse("A new group should not be required by default", group.isRequired());
        assertFalse("A new group should not have a selection", group.isSelected());
        assertNull("A new group should not have a selected option name", group.getSelected());
    }

    @Test
    public void testNewGroupHasNoOptionsOrNames() {
        // Arrange
        final OptionGroup group = new OptionGroup();

        // Assert
        assertNotNull("getOptions() should not return null", group.getOptions());
        assertTrue("A new group should have no options", group.getOptions().isEmpty());

        assertNotNull("getNames() should not return null", group.getNames());
        assertTrue("A new group should have no names", group.getNames().isEmpty());
    }

    @Test
    public void testSetRequired() {
        // Arrange
        final OptionGroup group = new OptionGroup();

        // Act
        group.setRequired(true);

        // Assert
        assertTrue("The group should be marked as required", group.isRequired());
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullOptionThrowsException() {
        // Arrange
        final OptionGroup group = new OptionGroup();

        // Act
        group.addOption(null); // Should throw NullPointerException
    }

    @Test
    public void testAddOptionDoesNotChangeRequiredStatus() {
        // Arrange
        final OptionGroup group = new OptionGroup();
        group.setRequired(true);
        final Option option = new Option("a", "alpha", false, "The alpha option");

        // Act
        group.addOption(option);

        // Assert
        assertTrue("Adding an option should not change the required status", group.isRequired());
        assertEquals("Group should contain the added option's name", "a", group.getNames().iterator().next());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedThrowsExceptionWhenAnotherOptionAlreadySelected() throws AlreadySelectedException {
        // Arrange
        final OptionGroup group = new OptionGroup();
        final Option optionA = new Option("a", "alpha", false, "The alpha option");
        final Option optionB = new Option("b", "bravo", false, "The bravo option");
        group.addOption(optionA);
        group.addOption(optionB);

        group.setSelected(optionA); // First selection is OK

        // Act
        group.setSelected(optionB); // Second selection should throw exception
    }

    @Test
    public void testSetSelectedWithSameOptionIsAllowed() throws AlreadySelectedException {
        // Arrange
        final OptionGroup group = new OptionGroup();
        final Option optionA = new Option("a", "alpha", false, "The alpha option");
        group.addOption(optionA);
        group.setSelected(optionA);

        // Act: Re-selecting the same option should not throw an exception
        group.setSelected(optionA);

        // Assert
        assertEquals("Selection should remain the same", "a", group.getSelected());
        assertTrue("Group should remain selected", group.isSelected());
    }

    @Test
    public void testSetSelectedWithNullClearsSelection() throws AlreadySelectedException {
        // Arrange
        final OptionGroup group = new OptionGroup();
        final Option optionA = new Option("a", "alpha", false, "The alpha option");
        group.addOption(optionA);
        group.setSelected(optionA);

        assertTrue("Group should be selected initially", group.isSelected());

        // Act
        group.setSelected(null);

        // Assert
        assertFalse("Group should not be selected after clearing", group.isSelected());
        assertNull("Selected option name should be null after clearing", group.getSelected());
    }

    @Test
    public void testGetSelectedReturnsShortNameOfSelectedOption() throws AlreadySelectedException {
        // Arrange
        final OptionGroup group = new OptionGroup();
        final Option option = new Option("a", "alpha", false, "The alpha option");
        group.addOption(option);

        // Act
        group.setSelected(option);

        // Assert
        assertEquals("getSelected should return the short name", "a", group.getSelected());
        assertTrue(group.isSelected());
    }

    @Test
    public void testGetSelectedReturnsLongNameWhenShortNameIsNull() throws AlreadySelectedException {
        // Arrange
        final OptionGroup group = new OptionGroup();
        final Option option = new Option(null, "bravo", false, "The bravo option");
        group.addOption(option);

        // Act
        group.setSelected(option);

        // Assert
        assertEquals("getSelected should return the long name when short name is null", "bravo", group.getSelected());
    }

    @Test
    public void testToString() {
        // Arrange
        final OptionGroup group = new OptionGroup();
        group.addOption(new Option("a", "alpha", false, "description 1"));
        group.addOption(new Option(null, "bravo", false, "description 2"));
        group.addOption(new Option("c", false, "description 3"));

        // Act
        final String result = group.toString();

        // Assert
        assertEquals("[-a, --bravo, -c]", result);
    }
}