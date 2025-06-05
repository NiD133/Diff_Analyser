/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.SortedMap;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Charsets} utility class.
 *
 * <p>This class focuses on verifying the correctness of the charset constants and the {@code toCharset} methods
 * within the {@link Charsets} class.</p>
 */
@SuppressWarnings("deprecation") // testing deprecated code
public class CharsetsTest {

    @Test
    public void testIso8859_1Constant() {
        // Arrange: None, using the static constant.

        // Act: Get the name of the charset.
        final String charsetName = Charsets.ISO_8859_1.name();

        // Assert: Ensure it matches the expected value.
        assertEquals("ISO-8859-1", charsetName, "ISO-8859-1 charset name should match the expected value.");
    }

    @Test
    public void testUsAsciiConstant() {
        // Arrange: None, using the static constant.

        // Act: Get the name of the charset.
        final String charsetName = Charsets.US_ASCII.name();

        // Assert: Ensure it matches the expected value.
        assertEquals(StandardCharsets.US_ASCII.name(), charsetName, "US-ASCII charset name should match the expected value.");
    }

    @Test
    public void testUtf16Constant() {
        // Arrange: None, using the static constant.

        // Act: Get the name of the charset.
        final String charsetName = Charsets.UTF_16.name();

        // Assert: Ensure it matches the expected value.
        assertEquals(StandardCharsets.UTF_16.name(), charsetName, "UTF-16 charset name should match the expected value.");
    }

    @Test
    public void testUtf16BeConstant() {
        // Arrange: None, using the static constant.

        // Act: Get the name of the charset.
        final String charsetName = Charsets.UTF_16BE.name();

        // Assert: Ensure it matches the expected value.
        assertEquals(StandardCharsets.UTF_16BE.name(), charsetName, "UTF-16BE charset name should match the expected value.");
    }

    @Test
    public void testUtf16LeConstant() {
        // Arrange: None, using the static constant.

        // Act: Get the name of the charset.
        final String charsetName = Charsets.UTF_16LE.name();

        // Assert: Ensure it matches the expected value.
        assertEquals(StandardCharsets.UTF_16LE.name(), charsetName, "UTF-16LE charset name should match the expected value.");
    }

    @Test
    public void testUtf8Constant() {
        // Arrange: None, using the static constant.

        // Act: Get the name of the charset.
        final String charsetName = Charsets.UTF_8.name();

        // Assert: Ensure it matches the expected value.
        assertEquals(StandardCharsets.UTF_8.name(), charsetName, "UTF-8 charset name should match the expected value.");
    }


    @Test
    public void testRequiredCharsets() {
        // Arrange: None

        // Act: Get the required charsets.
        final SortedMap<String, Charset> requiredCharsets = Charsets.requiredCharsets();

        // Assert: Verify that the expected charsets are present and have the correct names.
        assertEquals("US-ASCII", requiredCharsets.get("US-ASCII").name(), "US-ASCII charset should be present and named correctly.");
        assertEquals("ISO-8859-1", requiredCharsets.get("ISO-8859-1").name(), "ISO-8859-1 charset should be present and named correctly.");
        assertEquals("UTF-8", requiredCharsets.get("UTF-8").name(), "UTF-8 charset should be present and named correctly.");
        assertEquals("UTF-16", requiredCharsets.get("UTF-16").name(), "UTF-16 charset should be present and named correctly.");
        assertEquals("UTF-16BE", requiredCharsets.get("UTF-16BE").name(), "UTF-16BE charset should be present and named correctly.");
        assertEquals("UTF-16LE", requiredCharsets.get("UTF-16LE").name(), "UTF-16LE charset should be present and named correctly.");
    }

    @Test
    public void testToCharset_String() {
        // Arrange: None

        // Act & Assert: Verify the behavior with null and valid charset names.
        assertEquals(Charset.defaultCharset(), Charsets.toCharset((String) null), "Null charset name should return the default charset.");
        assertEquals(StandardCharsets.UTF_8, Charsets.toCharset(StandardCharsets.UTF_8.name()), "UTF-8 charset name should return the UTF-8 charset.");
    }

   @Test
    public void testToCharset_Charset() {
        // Arrange: None

        // Act & Assert: Verify the behavior with null and valid Charset objects.
        assertEquals(Charset.defaultCharset(), Charsets.toCharset((Charset) null), "Null Charset should return the default charset.");
        assertEquals(Charset.defaultCharset(), Charsets.toCharset(Charset.defaultCharset()), "Default Charset should return the default charset.");
        assertEquals(StandardCharsets.UTF_8, Charsets.toCharset(StandardCharsets.UTF_8), "UTF-8 Charset should return the UTF-8 Charset.");
    }

    @Test
    public void testToCharset_String_Charset() {
        // Arrange: None

        // Act & Assert: Verify the behavior with null and valid charset names, and default Charset.
        assertNull(Charsets.toCharset((String) null, null), "Null charset name and null default should return null.");
        assertEquals(Charset.defaultCharset(), Charsets.toCharset((String) null, Charset.defaultCharset()), "Null charset name should return the default charset.");
        assertEquals(StandardCharsets.UTF_8, Charsets.toCharset(StandardCharsets.UTF_8.name(), Charset.defaultCharset()), "UTF-8 charset name should return the UTF-8 charset.");
        assertEquals(StandardCharsets.UTF_8, Charsets.toCharset(StandardCharsets.UTF_8.name(), null), "UTF-8 charset name should return the UTF-8 charset, even with null default.");
    }

    @Test
    public void testToCharset_Charset_Charset() {
        // Arrange: None

        // Act & Assert: Verify the behavior with null and valid Charset objects, and default Charset.
        assertNull(Charsets.toCharset((Charset) null, null), "Null Charset and null default should return null.");
        assertEquals(Charset.defaultCharset(), Charsets.toCharset((Charset) null, Charset.defaultCharset()), "Null Charset should return the default charset.");
        assertEquals(Charset.defaultCharset(), Charsets.toCharset(Charset.defaultCharset(), Charset.defaultCharset()), "Default Charset should return the default charset.");
        assertEquals(StandardCharsets.UTF_8, Charsets.toCharset(StandardCharsets.UTF_8, Charset.defaultCharset()), "UTF-8 Charset should return the UTF-8 Charset.");
        assertEquals(StandardCharsets.UTF_8, Charsets.toCharset(StandardCharsets.UTF_8, null), "UTF-8 Charset should return the UTF-8 Charset, even with null default.");
    }
}