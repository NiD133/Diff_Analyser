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

import org.junit.jupiter.api.Test;

class DeprecatedAttributesTest {

    @Test
    void testBuilderWithNonDefaultValues() {
        // Create a DeprecatedAttributes instance with non-default values
        final DeprecatedAttributes attributes = DeprecatedAttributes.builder()
                .setDescription("Use Bar instead!")
                .setForRemoval(true)
                .setSince("2.0")
                .get();

        // Verify that the attributes are set correctly
        assertEquals("Use Bar instead!", attributes.getDescription());
        assertEquals("2.0", attributes.getSince());
        assertEquals(true, attributes.isForRemoval());
    }

    @Test
    void testBuilderToStringWithVariousCombinations() {
        // Test toString output with all attributes set
        assertEquals("Deprecated for removal since 2.0: Use Bar instead!", DeprecatedAttributes.builder()
                .setDescription("Use Bar instead!")
                .setForRemoval(true)
                .setSince("2.0")
                .get().toString());

        // Test toString output with description and forRemoval set
        assertEquals("Deprecated for removal: Use Bar instead!", DeprecatedAttributes.builder()
                .setDescription("Use Bar instead!")
                .setForRemoval(true)
                .get().toString());

        // Test toString output with description and since set
        assertEquals("Deprecated since 2.0: Use Bar instead!", DeprecatedAttributes.builder()
                .setDescription("Use Bar instead!")
                .setSince("2.0")
                .get().toString());

        // Test toString output with only description set
        assertEquals("Deprecated: Use Bar instead!", DeprecatedAttributes.builder()
                .setDescription("Use Bar instead!")
                .get().toString());
    }

    @Test
    void testDefaultBuilderValues() {
        // Create a DeprecatedAttributes instance with default values
        final DeprecatedAttributes defaultAttributes = DeprecatedAttributes.builder().get();

        // Verify that the default values match the DEFAULT instance
        assertEquals(DeprecatedAttributes.DEFAULT.getDescription(), defaultAttributes.getDescription());
        assertEquals(DeprecatedAttributes.DEFAULT.getSince(), defaultAttributes.getSince());
        assertEquals(DeprecatedAttributes.DEFAULT.isForRemoval(), defaultAttributes.isForRemoval());
    }

    @Test
    void testDefaultToStringOutput() {
        // Verify the toString output of the DEFAULT instance
        assertEquals("Deprecated", DeprecatedAttributes.DEFAULT.toString());
    }
}