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

package org.apache.commons.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.commons.codec.language.Soundex;
import org.junit.jupiter.api.Test;

/**
 * Test cases for the StringEncoderComparator.
 */
class StringEncoderComparatorTest {

    @Test
    void testComparatorWithDoubleMetaphoneSortsCorrectly() {
        // Arrange
        final StringEncoderComparator comparator = new StringEncoderComparator(new DoubleMetaphone());
        final List<String> names = Arrays.asList("Jordan", "Sosa", "Prior", "Pryor");
        final List<String> expectedSortedNames = Arrays.asList("Jordan", "Prior", "Pryor", "Sosa");

        // Act
        names.sort(comparator);

        // Assert
        assertEquals(expectedSortedNames, names, "The list of names was not sorted correctly using DoubleMetaphone.");
    }

    @Test
    void testComparatorWithDoubleMetaphoneHandlesInvalidInputGracefully() {
        // Arrange
        final StringEncoderComparator comparator = new StringEncoderComparator(new DoubleMetaphone());

        // Act
        // Comparing unrelated object types should return 0, indicating they are considered equal for sorting purposes.
        final int comparisonResult = comparator.compare(Double.valueOf(3.0d), Long.valueOf(3));

        // Assert
        assertEquals(0, comparisonResult,
                "Comparing objects of incompatible types should return 0 (equal) to avoid exceptions.");
    }

    @Test
    void testComparatorWithSoundexComparesStringsWithSameSoundexAsEqual() {
        // Arrange
        final StringEncoderComparator comparator = new StringEncoderComparator(new Soundex());

        // Act
        final int comparisonResult = comparator.compare("O'Brien", "O'Brian");

        // Assert
        assertEquals(0, comparisonResult,
                "Strings with the same Soundex encoding should be considered equal by the comparator.");
    }
}