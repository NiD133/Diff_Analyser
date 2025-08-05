/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link DeprecatedAttributes} class.
 */
public class DeprecatedAttributesTest {

    @Test
    public void builderShouldCreateInstanceWithDefaultValues() {
        // Arrange
        DeprecatedAttributes.Builder builder = DeprecatedAttributes.builder();

        // Act
        DeprecatedAttributes attributes = builder.get();

        // Assert
        assertFalse("forRemoval should be false by default", attributes.isForRemoval());
        assertEquals("since should be empty by default", "", attributes.getSince());
        assertEquals("description should be empty by default", "", attributes.getDescription());
    }

    @Test
    public void isForRemovalShouldReturnTrueWhenSetByBuilder() {
        // Arrange
        DeprecatedAttributes attributes = DeprecatedAttributes.builder()
                .setForRemoval(true)
                .get();

        // Act & Assert
        assertTrue(attributes.isForRemoval());
    }

    @Test
    public void getSinceShouldReturnProvidedValueWhenSetByBuilder() {
        // Arrange
        String expectedSince = "1.8.0";
        DeprecatedAttributes attributes = DeprecatedAttributes.builder()
                .setSince(expectedSince)
                .get();

        // Act
        String actualSince = attributes.getSince();

        // Assert
        assertEquals(expectedSince, actualSince);
    }

    @Test
    public void getDescriptionShouldReturnProvidedValueWhenSetByBuilder() {
        // Arrange
        String expectedDescription = "Use --new-option instead.";
        DeprecatedAttributes attributes = DeprecatedAttributes.builder()
                .setDescription(expectedDescription)
                .get();

        // Act
        String actualDescription = attributes.getDescription();

        // Assert
        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void defaultInstanceShouldHaveDefaultValues() {
        // Arrange
        DeprecatedAttributes defaultAttributes = DeprecatedAttributes.DEFAULT;

        // Act & Assert
        assertFalse("forRemoval should be false for the default instance", defaultAttributes.isForRemoval());
        assertEquals("since should be empty for the default instance", "", defaultAttributes.getSince());
        assertEquals("description should be empty for the default instance", "", defaultAttributes.getDescription());
    }

    @Test
    public void toStringShouldIndicateForRemovalWhenForRemovalIsTrue() {
        // Arrange
        DeprecatedAttributes attributes = DeprecatedAttributes.builder()
                .setForRemoval(true)
                .setSince("1.8.0") // This should be ignored by toString
                .get();

        // Act
        String stringRepresentation = attributes.toString();

        // Assert
        assertEquals("Deprecated for removal", stringRepresentation);
    }

    @Test
    public void toStringShouldIncludeSinceWhenSinceIsSet() {
        // Arrange
        DeprecatedAttributes attributes = DeprecatedAttributes.builder()
                .setSince("1.9.0")
                .setDescription("This should be ignored by toString") // This should be ignored
                .get();

        // Act
        String stringRepresentation = attributes.toString();

        // Assert
        assertEquals("Deprecated since 1.9.0", stringRepresentation);
    }

    @Test
    public void toStringShouldIncludeDescriptionWhenOnlyDescriptionIsSet() {
        // Arrange
        String description = "Will be removed in 2.0.";
        DeprecatedAttributes attributes = DeprecatedAttributes.builder()
                .setDescription(description)
                .get();

        // Act
        String stringRepresentation = attributes.toString();

        // Assert
        assertEquals("Deprecated: " + description, stringRepresentation);
        assertEquals(description, attributes.getDescription());
    }
}