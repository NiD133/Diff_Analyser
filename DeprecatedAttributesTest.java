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
        final DeprecatedAttributes value = DeprecatedAttributes.builder()
                .setDescription("Use Bar instead!")
                .setForRemoval(true)
                .setSince("2.0")
                .get();

        assertEquals("Use Bar instead!", value.getDescription());
        assertEquals("2.0", value.getSince());
        assertEquals(true, value.isForRemoval());
    }

    @Test
    void testToStringWithForRemovalAndSinceAndDescription() {
        DeprecatedAttributes attributes = DeprecatedAttributes.builder()
                .setDescription("Use Bar instead!")
                .setForRemoval(true)
                .setSince("2.0")
                .get();

        assertEquals("Deprecated for removal since 2.0: Use Bar instead!", attributes.toString());
    }

    @Test
    void testToStringWithForRemovalAndDescription() {
        DeprecatedAttributes attributes = DeprecatedAttributes.builder()
                .setDescription("Use Bar instead!")
                .setForRemoval(true)
                .get();

        assertEquals("Deprecated for removal: Use Bar instead!", attributes.toString());
    }

    @Test
    void testToStringWithSinceAndDescription() {
        DeprecatedAttributes attributes = DeprecatedAttributes.builder()
                .setDescription("Use Bar instead!")
                .setSince("2.0")
                .get();

        assertEquals("Deprecated since 2.0: Use Bar instead!", attributes.toString());
    }

    @Test
    void testToStringWithDescriptionOnly() {
        DeprecatedAttributes attributes = DeprecatedAttributes.builder()
                .setDescription("Use Bar instead!")
                .get();

        assertEquals("Deprecated: Use Bar instead!", attributes.toString());
    }

    @Test
    void testDefaultBuilderReturnsDefaultValues() {
        final DeprecatedAttributes defaultValue = DeprecatedAttributes.builder().get();

        assertEquals(DeprecatedAttributes.DEFAULT.getDescription(), defaultValue.getDescription());
        assertEquals(DeprecatedAttributes.DEFAULT.getSince(), defaultValue.getSince());
        assertEquals(DeprecatedAttributes.DEFAULT.isForRemoval(), defaultValue.isForRemoval());
    }

    @Test
    void testDefaultInstanceToString() {
        assertEquals("Deprecated", DeprecatedAttributes.DEFAULT.toString());
    }
}