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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

@SuppressWarnings("deprecation") // OptionBuilder is marked deprecated
class OptionBuilderTest {

    // Common test data to avoid string duplication and magic literals
    private static final String DESC = "option description";
    private static final String SIMPLE_LONG = "simple option";
    private static final String SIMPLE_DESC = "this is a simple option";
    private static final String DIMPLE_LONG = "dimple option";
    private static final String DIMPLE_DESC = "this is a dimple option";

    @Test
    void create_withChar_buildsOptionWithDescription_andNoArg() {
        // Arrange + Act
        final Option option = OptionBuilder.withDescription(DESC).create('o');

        // Assert
        assertAll(
            () -> assertEquals("o", option.getOpt()),
            () -> assertEquals(DESC, option.getDescription()),
            () -> assertFalse(option.hasArg(), "By default options should not have args")
        );
    }

    @Test
    void create_withString_buildsOptionWithDescription_andNoArg() {
        // Arrange + Act
        final Option option = OptionBuilder.withDescription(DESC).create("o");

        // Assert
        assertAll(
            () -> assertEquals("o", option.getOpt()),
            () -> assertEquals(DESC, option.getDescription()),
            () -> assertFalse(option.hasArg(), "By default options should not have args")
        );
    }

    @Test
    void builder_isReset_afterCreate_andAfterFailure() {
        // First, a failing create() should not leak previously configured state
        assertThrows(IllegalArgumentException.class, () -> OptionBuilder.withDescription("JUnit").create('"'));

        // The next created option should not inherit the previous description
        assertNull(OptionBuilder.create('x').getDescription(), "Builder leaked description across create() calls");

        // Calling create() without an opt/longOpt should fail
        assertThrows(IllegalStateException.class, () -> OptionBuilder.create());

        // And after the failure, state should still be reset for the next option
        assertNull(OptionBuilder.create('x').getDescription(), "Builder leaked description after a failing create()");
    }

    @Test
    void create_withAllAttributes_setsAllFlagsAndValues() {
        // Arrange + Act
        final Option option = OptionBuilder
            .withLongOpt(SIMPLE_LONG)
            .hasArg()
            .isRequired()
            .hasArgs()
            .withType(Float.class)
            .withDescription(SIMPLE_DESC)
            .create('s');

        // Assert
        assertAll(
            () -> assertEquals("s", option.getOpt()),
            () -> assertEquals(SIMPLE_LONG, option.getLongOpt()),
            () -> assertEquals(SIMPLE_DESC, option.getDescription()),
            () -> assertEquals(Float.class, option.getType()),
            () -> assertTrue(option.hasArg()),
            () -> assertTrue(option.isRequired()),
            () -> assertTrue(option.hasArgs())
        );
    }

    @Test
    void create_withoutOpt_throws_andIsReset() {
        // No opt nor longOpt configured -> should throw
        assertThrows(IllegalStateException.class, () -> OptionBuilder.create());

        // Implicitly resets the builder for subsequent tests
        OptionBuilder.create("opt");
    }

    @Test
    void create_withInvalidOpt_throws_andValidOptSucceeds() {
        // Invalid single-character option
        assertThrows(IllegalArgumentException.class, () -> OptionBuilder.withDescription(DESC).create('"'));

        // Invalid character in multi-char option
        assertThrows(IllegalArgumentException.class, () -> OptionBuilder.create("opt`"));

        // A valid option should succeed (and reset the builder)
        OptionBuilder.create("opt");
    }

    @Test
    void hasArgs_withCount_setsExpectedArgsCount() {
        // Arrange + Act
        final Option option = OptionBuilder
            .withDescription(DESC)
            .hasArgs(2)
            .create('o');

        // Assert
        assertEquals(2, option.getArgs());
    }

    @Test
    void create_allowsQuestionMarkAndAtSymbol_butRejectsSpace() {
        // '?'
        final Option q = OptionBuilder.withDescription("help options").create('?');
        assertEquals("?", q.getOpt());

        // '@'
        final Option at = OptionBuilder.withDescription("read from stdin").create('@');
        assertEquals("@", at.getOpt());

        // ' ' (space) is not allowed
        assertThrows(IllegalArgumentException.class, () -> OptionBuilder.create(' '));
    }

    @Test
    void creatingMultipleOptions_doesNotLeakState() {
        // First option with many attributes set
        Option first = OptionBuilder
            .withLongOpt(SIMPLE_LONG)
            .hasArg()
            .isRequired()
            .hasArgs()
            .withType(Float.class)
            .withDescription(SIMPLE_DESC)
            .create('s');

        assertAll(
            () -> assertEquals("s", first.getOpt()),
            () -> assertEquals(SIMPLE_LONG, first.getLongOpt()),
            () -> assertEquals(SIMPLE_DESC, first.getDescription()),
            () -> assertEquals(Float.class, first.getType()),
            () -> assertTrue(first.hasArg()),
            () -> assertTrue(first.isRequired()),
            () -> assertTrue(first.hasArgs())
        );

        // Second option: ensure none of the previous attributes leak in
        first = OptionBuilder
            .withLongOpt(DIMPLE_LONG)
            .hasArg()
            .withDescription(DIMPLE_DESC)
            .create('d');

        assertAll(
            () -> assertEquals("d", first.getOpt()),
            () -> assertEquals(DIMPLE_LONG, first.getLongOpt()),
            () -> assertEquals(DIMPLE_DESC, first.getDescription()),
            () -> assertEquals(String.class, first.getType(), "Default type should be String when not set"),
            () -> assertTrue(first.hasArg()),
            () -> assertFalse(first.isRequired(), "Required should not leak from previous option"),
            () -> assertFalse(first.hasArgs(), "Args count should not leak from previous option")
        );
    }
}