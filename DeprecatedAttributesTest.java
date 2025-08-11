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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

/**
 * Tests for the DeprecatedAttributes class, which represents deprecation metadata
 * for command-line options including description, version, and removal status.
 */
class DeprecatedAttributesTest {

    // Test data constants for better maintainability
    private static final String SAMPLE_DESCRIPTION = "Use Bar instead!";
    private static final String SAMPLE_VERSION = "2.0";

    /**
     * Tests that the builder correctly sets all non-default values and
     * that the getters return the expected values.
     */
    @Test
    void shouldCreateDeprecatedAttributesWithCustomValues() {
        // Given: A builder with custom deprecation attributes
        DeprecatedAttributes deprecatedAttributes = DeprecatedAttributes.builder()
                .setDescription(SAMPLE_DESCRIPTION)
                .setForRemoval(true)
                .setSince(SAMPLE_VERSION)
                .get();

        // Then: All attributes should match the configured values
        assertEquals(SAMPLE_DESCRIPTION, deprecatedAttributes.getDescription());
        assertEquals(SAMPLE_VERSION, deprecatedAttributes.getSince());
        assertTrue(deprecatedAttributes.isForRemoval());
    }

    /**
     * Tests that the toString() method formats deprecation messages correctly
     * for different combinations of attributes.
     */
    @Test
    void shouldFormatToStringCorrectlyForDifferentAttributeCombinations() {
        // Test case 1: All attributes set (description, version, forRemoval=true)
        String fullDeprecationMessage = createDeprecatedAttributes(SAMPLE_DESCRIPTION, SAMPLE_VERSION, true)
                .toString();
        assertEquals("Deprecated for removal since 2.0: Use Bar instead!", fullDeprecationMessage);

        // Test case 2: Description and forRemoval=true, but no version
        String deprecationWithoutVersion = createDeprecatedAttributes(SAMPLE_DESCRIPTION, null, true)
                .toString();
        assertEquals("Deprecated for removal: Use Bar instead!", deprecationWithoutVersion);

        // Test case 3: Description and version, but forRemoval=false
        String deprecationWithoutRemoval = createDeprecatedAttributes(SAMPLE_DESCRIPTION, SAMPLE_VERSION, false)
                .toString();
        assertEquals("Deprecated since 2.0: Use Bar instead!", deprecationWithoutRemoval);

        // Test case 4: Only description set
        String basicDeprecation = createDeprecatedAttributes(SAMPLE_DESCRIPTION, null, false)
                .toString();
        assertEquals("Deprecated: Use Bar instead!", basicDeprecation);
    }

    /**
     * Tests that a builder with no custom values creates an instance
     * equivalent to the DEFAULT constant.
     */
    @Test
    void shouldCreateDefaultDeprecatedAttributesWhenNoValuesSet() {
        // Given: A builder with no custom values
        DeprecatedAttributes defaultFromBuilder = DeprecatedAttributes.builder().get();

        // Then: Should match the DEFAULT constant in all attributes
        assertEquals(DeprecatedAttributes.DEFAULT.getDescription(), defaultFromBuilder.getDescription());
        assertEquals(DeprecatedAttributes.DEFAULT.getSince(), defaultFromBuilder.getSince());
        assertEquals(DeprecatedAttributes.DEFAULT.isForRemoval(), defaultFromBuilder.isForRemoval());
        
        // And: Should not be marked for removal by default
        assertFalse(defaultFromBuilder.isForRemoval());
    }

    /**
     * Tests that the DEFAULT constant produces the expected simple string representation.
     */
    @Test
    void shouldFormatDefaultInstanceAsSimpleDeprecatedMessage() {
        // Given: The DEFAULT DeprecatedAttributes instance
        // Then: Should produce a simple "Deprecated" message
        assertEquals("Deprecated", DeprecatedAttributes.DEFAULT.toString());
    }

    /**
     * Helper method to create DeprecatedAttributes with specified values.
     * Handles null values by not setting those attributes on the builder.
     */
    private DeprecatedAttributes createDeprecatedAttributes(String description, String since, boolean forRemoval) {
        DeprecatedAttributes.Builder builder = DeprecatedAttributes.builder();
        
        if (description != null) {
            builder.setDescription(description);
        }
        if (since != null) {
            builder.setSince(since);
        }
        builder.setForRemoval(forRemoval);
        
        return builder.get();
    }
}