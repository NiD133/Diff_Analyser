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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("DeprecatedAttributes")
class DeprecatedAttributesTest {

    private static final String TEST_DESCRIPTION = "Use Bar instead!";
    private static final String TEST_SINCE = "2.0";

    @Nested
    @DisplayName("Builder")
    class BuilderTest {

        @Test
        @DisplayName("should create a default instance when no properties are set")
        void shouldBuildDefaultInstance() {
            // Arrange
            final DeprecatedAttributes expectedDefault = DeprecatedAttributes.DEFAULT;

            // Act
            final DeprecatedAttributes createdDefault = DeprecatedAttributes.builder().get();

            // Assert
            assertAll("Created instance should match the default attributes",
                () -> assertEquals(expectedDefault.getDescription(), createdDefault.getDescription(), "description"),
                () -> assertEquals(expectedDefault.getSince(), createdDefault.getSince(), "since"),
                () -> assertEquals(expectedDefault.isForRemoval(), createdDefault.isForRemoval(), "forRemoval")
            );
        }

        @Test
        @DisplayName("should create an instance with all properties set")
        void shouldBuildWithAllPropertiesSet() {
            // Arrange
            final DeprecatedAttributes.Builder builder = DeprecatedAttributes.builder()
                .setDescription(TEST_DESCRIPTION)
                .setForRemoval(true)
                .setSince(TEST_SINCE);

            // Act
            final DeprecatedAttributes attributes = builder.get();

            // Assert
            assertAll("All properties should be correctly set",
                () -> assertEquals(TEST_DESCRIPTION, attributes.getDescription()),
                () -> assertEquals(TEST_SINCE, attributes.getSince()),
                () -> assertTrue(attributes.isForRemoval())
            );
        }
    }

    @Nested
    @DisplayName("toString()")
    class ToStringTest {

        @Test
        @DisplayName("should format correctly when all attributes are set")
        void shouldFormatWithAllAttributes() {
            // Arrange
            final DeprecatedAttributes attributes = DeprecatedAttributes.builder()
                .setDescription(TEST_DESCRIPTION)
                .setForRemoval(true)
                .setSince(TEST_SINCE)
                .get();
            final String expected = "Deprecated for removal since 2.0: Use Bar instead!";

            // Act & Assert
            assertEquals(expected, attributes.toString());
        }

        @Test
        @DisplayName("should omit 'since' part when 'since' is not set")
        void shouldFormatWithoutSince() {
            // Arrange
            final DeprecatedAttributes attributes = DeprecatedAttributes.builder()
                .setDescription(TEST_DESCRIPTION)
                .setForRemoval(true)
                .get();
            final String expected = "Deprecated for removal: Use Bar instead!";

            // Act & Assert
            assertEquals(expected, attributes.toString());
        }

        @Test
        @DisplayName("should omit 'for removal' part when 'forRemoval' is false")
        void shouldFormatWithoutForRemoval() {
            // Arrange
            final DeprecatedAttributes attributes = DeprecatedAttributes.builder()
                .setDescription(TEST_DESCRIPTION)
                .setSince(TEST_SINCE)
                .get();
            final String expected = "Deprecated since 2.0: Use Bar instead!";

            // Act & Assert
            assertEquals(expected, attributes.toString());
        }

        @Test
        @DisplayName("should format with only description when other attributes are not set")
        void shouldFormatWithOnlyDescription() {
            // Arrange
            final DeprecatedAttributes attributes = DeprecatedAttributes.builder()
                .setDescription(TEST_DESCRIPTION)
                .get();
            final String expected = "Deprecated: Use Bar instead!";

            // Act & Assert
            assertEquals(expected, attributes.toString());
        }

        @Test
        @DisplayName("should return a simple message for the default instance")
        void shouldFormatForDefaultInstance() {
            // Arrange
            final DeprecatedAttributes defaultAttributes = DeprecatedAttributes.DEFAULT;
            final String expected = "Deprecated";

            // Act & Assert
            assertEquals(expected, defaultAttributes.toString());
        }
    }
}