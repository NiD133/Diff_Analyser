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

package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.apache.commons.codec.CodecPolicy;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;

/**
 * Tests {@link Base16}.
 */
@DisplayName("Base16 Tests")
class Base16Test {

    private static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;
    private static final String HELLO_WORLD = "Hello World";
    private static final String HELLO_WORLD_ENCODED = "48656C6C6F20576F726C64";
    
    private final Random random = new Random();

    @Nested
    @DisplayName("Basic Encoding and Decoding")
    class BasicEncodingDecoding {
        
        @Test
        @DisplayName("Should encode and decode 'Hello World' correctly")
        void shouldEncodeAndDecodeHelloWorld() {
            // Given
            final Base16 base16 = new Base16();
            final byte[] originalBytes = StringUtils.getBytesUtf8(HELLO_WORLD);
            
            // When encoding
            final byte[] encodedBytes = base16.encode(originalBytes);
            final String encodedContent = StringUtils.newStringUtf8(encodedBytes);
            
            // Then
            assertEquals(HELLO_WORLD_ENCODED, encodedContent);
            
            // When decoding
            final byte[] decodedBytes = base16.decode(encodedBytes);
            final String decodedContent = StringUtils.newStringUtf8(decodedBytes);
            
            // Then
            assertEquals(HELLO_WORLD, decodedContent);
        }

        @Test
        @DisplayName("Should handle empty arrays correctly")
        void shouldHandleEmptyArrays() {
            // Given
            final Base16 base16 = new Base16();
            final byte[] emptyArray = new byte[0];
            
            // When/Then - encoding empty array
            byte[] result = base16.encode(emptyArray);
            assertEquals(0, result.length);
            
            // When/Then - encoding null
            assertNull(base16.encode(null));
            
            // When/Then - decoding empty array
            result = base16.decode(emptyArray);
            assertEquals(0, result.length);
            
            // When/Then - decoding null
            assertNull(base16.decode((byte[]) null));
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        
        @Test
        @DisplayName("Should create Base16 with default uppercase encoding")
        void shouldCreateDefaultUppercaseBase16() {
            // Given/When
            final Base16 base16 = new Base16();
            final byte[] encoded = base16.encode(BaseNTestData.DECODED);
            
            // Then
            assertEquals(Base16TestData.ENCODED_UTF8_UPPERCASE, StringUtils.newStringUtf8(encoded));
        }
        
        @Test
        @DisplayName("Should create Base16 with lowercase encoding when specified")
        void shouldCreateLowercaseBase16() {
            // Given/When
            final Base16 base16 = new Base16(true);
            final byte[] encoded = base16.encode(BaseNTestData.DECODED);
            
            // Then
            assertEquals(Base16TestData.ENCODED_UTF8_LOWERCASE, StringUtils.newStringUtf8(encoded));
        }
        
        @Test
        @DisplayName("Should create Base16 with specified decoding policy")
        void shouldCreateBase16WithDecodingPolicy() {
            // Given/When
            final Base16 base16 = new Base16(false, CodecPolicy.STRICT);
            final byte[] encoded = base16.encode(BaseNTestData.DECODED);
            
            // Then
            assertEquals(Base16TestData.ENCODED_UTF8_UPPERCASE, StringUtils.newStringUtf8(encoded));
            assertEquals(CodecPolicy.STRICT, base16.getCodecPolicy());
        }
    }

    @Nested
    @DisplayName("Buffer Position Tests")
    class BufferPositionTests {
        
        @Test
        @DisplayName("Should encode correctly when data is at buffer start")
        void shouldEncodeAtBufferStart() {
            testBase16InBuffer(0, 100);
        }
        
        @Test
        @DisplayName("Should encode correctly when data is in buffer middle")
        void shouldEncodeAtBufferMiddle() {
            testBase16InBuffer(100, 100);
        }
        
        @Test
        @DisplayName("Should encode correctly when data is at buffer end")
        void shouldEncodeAtBufferEnd() {
            testBase16InBuffer(100, 0);
        }
        
        private void testBase16InBuffer(final int startPadSize, final int endPadSize) {
            // Given
            final byte[] originalBytes = StringUtils.getBytesUtf8(HELLO_WORLD);
            byte[] buffer = ArrayUtils.addAll(originalBytes, new byte[endPadSize]);
            buffer = ArrayUtils.addAll(new byte[startPadSize], buffer);
            
            // When
            final byte[] encodedBytes = new Base16().encode(buffer, startPadSize, originalBytes.length);
            final String encodedContent = StringUtils.newStringUtf8(encodedBytes);
            
            // Then
            assertEquals(HELLO_WORLD_ENCODED, encodedContent);
        }
    }

    @Nested
    @DisplayName("Alphabet Validation Tests")
    class AlphabetValidationTests {
        
        @Test
        @DisplayName("Should correctly identify valid uppercase Base16 characters")
        void shouldIdentifyValidUppercaseCharacters() {
            // Given
            final Base16 base16 = new Base16(false); // uppercase
            
            // Then - valid digits
            for (char c = '0'; c <= '9'; c++) {
                assertTrue(base16.isInAlphabet((byte) c), 
                    "Character '" + c + "' should be in uppercase Base16 alphabet");
            }
            
            // Then - valid uppercase letters
            for (char c = 'A'; c <= 'F'; c++) {
                assertTrue(base16.isInAlphabet((byte) c),
                    "Character '" + c + "' should be in uppercase Base16 alphabet");
            }
            
            // Then - invalid lowercase letters
            for (char c = 'a'; c <= 'f'; c++) {
                assertFalse(base16.isInAlphabet((byte) c),
                    "Character '" + c + "' should NOT be in uppercase Base16 alphabet");
            }
        }
        
        @Test
        @DisplayName("Should correctly identify valid lowercase Base16 characters")
        void shouldIdentifyValidLowercaseCharacters() {
            // Given
            final Base16 base16 = new Base16(true); // lowercase
            
            // Then - valid digits
            for (char c = '0'; c <= '9'; c++) {
                assertTrue(base16.isInAlphabet((byte) c),
                    "Character '" + c + "' should be in lowercase Base16 alphabet");
            }
            
            // Then - valid lowercase letters
            for (char c = 'a'; c <= 'f'; c++) {
                assertTrue(base16.isInAlphabet((byte) c),
                    "Character '" + c + "' should be in lowercase Base16 alphabet");
            }
            
            // Then - invalid uppercase letters
            for (char c = 'A'; c <= 'F'; c++) {
                assertFalse(base16.isInAlphabet((byte) c),
                    "Character '" + c + "' should NOT be in lowercase Base16 alphabet");
            }
        }
        
        @Test
        @DisplayName("Should reject invalid Base16 characters")
        void shouldRejectInvalidCharacters() {
            // Given
            final Base16 base16 = new Base16();
            final byte[] invalidChars = { '/', ':', '@', 'G', '%', '`', 'g' };
            
            // When/Then
            for (final byte invalidChar : invalidChars) {
                final byte[] encoded = new byte[] { invalidChar };
                assertThrows(IllegalArgumentException.class, 
                    () -> base16.decode(encoded),
                    "Should throw exception for invalid character: " + (char) invalidChar);
            }
        }
    }

    @Nested
    @DisplayName("Decoding Policy Tests")
    class DecodingPolicyTests {
        
        @Test
        @DisplayName("Should handle incomplete hex pairs with lenient policy")
        void shouldHandleIncompleteHexPairsLeniently() {
            // Given
            final String encodedWithTrailingChar = "aabbccdde"; // Note: trailing 'e' is incomplete
            final Base16 base16 = new Base16(true, CodecPolicy.LENIENT);
            
            // When
            final byte[] decoded = base16.decode(StringUtils.getBytesUtf8(encodedWithTrailingChar));
            
            // Then
            assertArrayEquals(new byte[] { (byte) 0xaa, (byte) 0xbb, (byte) 0xcc, (byte) 0xdd }, decoded);
        }
        
        @Test
        @DisplayName("Should reject incomplete hex pairs with strict policy")
        void shouldRejectIncompleteHexPairsStrictly() {
            // Given
            final String encodedWithTrailingChar = "aabbccdde"; // Note: trailing 'e' is incomplete
            final Base16 base16 = new Base16(true, CodecPolicy.STRICT);
            
            // When/Then
            assertThrows(IllegalArgumentException.class, 
                () -> base16.decode(StringUtils.getBytesUtf8(encodedWithTrailingChar)));
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandling {
        
        @Test
        @DisplayName("Should throw exception when encoding length would overflow")
        void shouldThrowExceptionOnEncodeLengthOverflow() {
            // Given
            final Base16 base16 = new Base16();
            
            // When/Then
            assertThrows(IllegalArgumentException.class, 
                () -> base16.encode(new byte[10], 0, 1 << 30));
        }
        
        @Test
        @DisplayName("Should handle CODEC-68 regression - non-Base16 bytes")
        void shouldHandleCodec68Regression() {
            // Given
            final byte[] problematicInput = { 'n', 'H', '=', '=', (byte) 0x9c };
            final Base16 base16 = new Base16();
            
            // When/Then
            assertThrows(RuntimeException.class, () -> base16.decode(problematicInput));
        }
    }

    @Nested
    @DisplayName("Object Encoding/Decoding Tests")
    class ObjectEncodingDecodingTests {
        
        @Test
        @DisplayName("Should encode byte array objects correctly")
        void shouldEncodeByteArrayObjects() throws EncoderException {
            // Given
            final Base16 base16 = new Base16();
            final byte[] originalBytes = HELLO_WORLD.getBytes(CHARSET_UTF8);
            
            // When
            final Object encoded = base16.encode((Object) originalBytes);
            
            // Then
            assertEquals(HELLO_WORLD_ENCODED, new String((byte[]) encoded));
        }
        
        @Test
        @DisplayName("Should decode string objects correctly")
        void shouldDecodeStringObjects() throws DecoderException {
            // Given
            final Base16 base16 = new Base16();
            
            // When
            final Object decoded = base16.decode((Object) HELLO_WORLD_ENCODED);
            
            // Then
            assertEquals(HELLO_WORLD, new String((byte[]) decoded, CHARSET_UTF8));
        }
        
        @Test
        @DisplayName("Should reject invalid object types for encoding")
        void shouldRejectInvalidObjectTypesForEncoding() {
            // Given
            final Base16 base16 = new Base16();
            
            // When/Then
            assertThrows(EncoderException.class, 
                () -> base16.encode("Not a byte array"));
        }
        
        @Test
        @DisplayName("Should reject invalid object types for decoding")
        void shouldRejectInvalidObjectTypesForDecoding() {
            // Given
            final Base16 base16 = new Base16();
            
            // When/Then
            assertThrows(DecoderException.class, 
                () -> base16.decode(Integer.valueOf(5)));
        }
    }

    @Nested
    @DisplayName("Random Data Tests")
    class RandomDataTests {
        
        @Test
        @DisplayName("Should correctly encode and decode random small arrays")
        void shouldEncodeDecodeSmallRandomArrays() {
            for (int size = 0; size < 12; size++) {
                // Given
                final byte[] originalData = new byte[size];
                random.nextBytes(originalData);
                
                // When
                final byte[] encoded = new Base16().encode(originalData);
                final byte[] decoded = new Base16().decode(encoded);
                
                // Then
                assertArrayEquals(originalData, decoded, 
                    "Failed for array size " + size);
            }
        }
        
        @Test
        @DisplayName("Should correctly encode and decode random large arrays")
        void shouldEncodeDecodeLargeRandomArrays() {
            for (int i = 1; i < 5; i++) {
                // Given
                final int size = random.nextInt(10000) + 1;
                final byte[] originalData = new byte[size];
                random.nextBytes(originalData);
                
                // When
                final byte[] encoded = new Base16().encode(originalData);
                final byte[] decoded = new Base16().decode(encoded);
                
                // Then
                assertArrayEquals(originalData, decoded,
                    "Failed for array size " + size);
            }
        }
    }

    @Nested
    @DisplayName("Known Value Tests")
    class KnownValueTests {
        
        @Test
        @DisplayName("Should encode single bytes to correct hex values")
        void shouldEncodeSingleBytesCorrectly() {
            // Test a few representative values
            assertSingleByteEncoding((byte) 0, "00");
            assertSingleByteEncoding((byte) 15, "0F");
            assertSingleByteEncoding((byte) 16, "10");
            assertSingleByteEncoding((byte) 255, "FF");
            assertSingleByteEncoding((byte) -1, "FF");
        }
        
        @Test
        @DisplayName("Should encode known strings correctly")
        void shouldEncodeKnownStringsCorrectly() {
            final Base16 base16 = new Base16(true); // lowercase
            
            assertEncodedString(base16, "The quick brown fox jumped over the lazy dogs.",
                "54686520717569636b2062726f776e20666f78206a756d706564206f76657220746865206c617a7920646f67732e");
            
            assertEncodedString(base16, "xyzzy!", "78797a7a7921");
        }
        
        private void assertSingleByteEncoding(byte value, String expectedHex) {
            assertEquals(expectedHex, new String(new Base16().encode(new byte[] { value })),
                "Incorrect encoding for byte value " + value);
        }
        
        private void assertEncodedString(Base16 base16, String input, String expectedHex) {
            assertEquals(expectedHex, 
                new String(base16.encode(input.getBytes(CHARSET_UTF8))),
                "Incorrect encoding for string: " + input);
        }
    }

    @Test
    @DisplayName("Should handle incremental decoding correctly")
    void shouldHandleIncrementalDecoding() {
        // Given
        final String encoded = "556E74696C206E6578742074696D6521"; // "Until next time!"
        final BaseNCodec.Context context = new BaseNCodec.Context();
        final Base16 base16 = new Base16();
        final byte[] encodedBytes = StringUtils.getBytesUtf8(encoded);
        
        // When - decode in various chunks
        base16.decode(encodedBytes, 0, 1, context);
        base16.decode(encodedBytes, 1, 1, context); // yields "U"
        base16.decode(encodedBytes, 2, 1, context);
        base16.decode(encodedBytes, 3, 1, context); // yields "n"
        base16.decode(encodedBytes, 4, 3, context); // yields "t"
        base16.decode(encodedBytes, 7, 3, context); // yields "il"
        base16.decode(encodedBytes, 10, 3, context); // yields " "
        base16.decode(encodedBytes, 13, 19, context); // yields "next time!"
        
        // Then
        final byte[] decodedBytes = new byte[context.pos];
        System.arraycopy(context.buffer, context.readPos, decodedBytes, 0, decodedBytes.length);
        final String decoded = StringUtils.newStringUtf8(decodedBytes);
        
        assertEquals("Until next time!", decoded);
    }
}