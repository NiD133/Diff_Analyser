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

package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Objects;

import org.apache.commons.lang3.AppendableJoiner.Builder;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.text.TextStringBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests {@link AppendableJoiner}.
 */
class AppendableJoinerTest {

    /**
     * Test fixture that demonstrates custom rendering to an Appendable.
     */
    static class CustomRenderableObject {
        private final String name;

        CustomRenderableObject(final String name) {
            this.name = name;
        }

        /**
         * Renders this object to an Appendable with a custom format (name + exclamation).
         */
        void renderTo(final Appendable appendable) throws IOException {
            appendable.append(name);
            appendable.append('!');
        }
    }

    @Test
    void testJoinerWithAllConfigurationOptions() {
        // Given: A joiner configured with prefix, suffix, delimiter, and custom element appender
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder()
                .setPrefix("<")
                .setDelimiter(".")
                .setSuffix(">")
                .setElementAppender((appendable, element) -> appendable.append(String.valueOf(element)))
                .get();
        
        // When: Joining elements to an existing StringBuilder
        final StringBuilder result = new StringBuilder("A");
        joiner.join(result, "B", "C");
        
        // Then: Elements are joined with all configured options
        assertEquals("A<B.C>", result.toString());
        
        // And: Multiple join operations can be chained
        result.append("1");
        joiner.join(result, Arrays.asList("D", "E"));
        assertEquals("A<B.C>1<D.E>", result.toString());
    }

    @Test
    void testDefaultJoinerConfiguration() {
        // Given: A joiner with default configuration (no prefix, suffix, or delimiter)
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder().get();
        
        // When: Joining elements
        final StringBuilder result = new StringBuilder("A");
        joiner.join(result, "B", "C");
        
        // Then: Elements are concatenated without separators
        assertEquals("ABC", result.toString());
        
        // And: Can continue appending
        result.append("1");
        joiner.join(result, "D", "E");
        assertEquals("ABC1DE", result.toString());
    }

    @Test
    void testBuilderCreatesNewInstances() {
        // Given: Two builders
        final Builder<Object> builder1 = AppendableJoiner.builder();
        final Builder<Object> builder2 = AppendableJoiner.builder();
        
        // Then: Each builder is a new instance
        assertNotSame(builder1, builder2);
        
        // And: Each call to get() creates a new joiner instance
        final AppendableJoiner<Object> joiner1 = builder1.get();
        final AppendableJoiner<Object> joiner2 = builder1.get();
        assertNotSame(joiner1, joiner2);
    }

    @SuppressWarnings("deprecation") // Testing deprecated StrBuilder
    @ParameterizedTest
    @ValueSource(classes = { 
        StringBuilder.class, 
        StringBuffer.class, 
        StringWriter.class, 
        StrBuilder.class, 
        TextStringBuilder.class 
    })
    void testJoinerWorksWithDifferentAppendableTypes(final Class<? extends Appendable> appendableType) throws Exception {
        // Given: A joiner with delimiter
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder()
                .setDelimiter(".")
                .get();
        
        // And: An instance of the specified Appendable type
        final Appendable appendable = appendableType.newInstance();
        appendable.append("A");
        
        // When: Joining elements using joinA (which may throw IOException)
        joiner.joinA(appendable, "B", "C");
        
        // Then: Elements are joined with delimiter
        assertEquals("AB.C", appendable.toString());
        
        // And: Can continue appending
        appendable.append("1");
        joiner.joinA(appendable, Arrays.asList("D", "E"));
        assertEquals("AB.C1D.E", appendable.toString());
    }

    @Test
    void testJoinerWithDelimiterOnStringBuilder() {
        // Given: A joiner with only delimiter configured
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder()
                .setDelimiter(".")
                .get();
        
        // When: Using StringBuilder (which doesn't throw IOException)
        final StringBuilder result = new StringBuilder("A");
        joiner.join(result, "B", "C");
        
        // Then: Elements are joined with delimiter
        assertEquals("AB.C", result.toString());
        
        // And: Can chain multiple join operations
        result.append("1");
        joiner.join(result, Arrays.asList("D", "E"));
        assertEquals("AB.C1D.E", result.toString());
    }

    @Test
    void testCustomElementAppenderWithPrefixAndSuffix() {
        // Given: A joiner with custom element appender that adds a pipe before each element
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder()
                .setPrefix("<")
                .setDelimiter(".")
                .setSuffix(">")
                .setElementAppender((appendable, element) -> 
                    appendable.append("|").append(Objects.toString(element)))
                .get();
        
        // When: Joining elements
        final StringBuilder result = new StringBuilder("A");
        joiner.join(result, "B", "C");
        
        // Then: Each element is prefixed with pipe
        assertEquals("A<|B.|C>", result.toString());
        
        // And: Works with multiple join operations
        result.append("1");
        joiner.join(result, Arrays.asList("D", "E"));
        assertEquals("A<|B.|C>1<|D.|E>", result.toString());
    }

    @Test
    void testJoinerWithCustomRenderableObjects() {
        // Given: A joiner for objects that render themselves
        final AppendableJoiner<CustomRenderableObject> joiner = 
                AppendableJoiner.<CustomRenderableObject>builder()
                    .setElementAppender((appendable, element) -> element.renderTo(appendable))
                    .get();
        
        // When: Joining custom objects
        final StringBuilder result = new StringBuilder("[");
        joiner.join(result, 
            new CustomRenderableObject("B"), 
            new CustomRenderableObject("C"));
        
        // Then: Each object renders itself with exclamation
        assertEquals("[B!C!", result.toString());
        
        // And: Can continue building the string
        result.append("]");
        joiner.join(result, Arrays.asList(
            new CustomRenderableObject("D"), 
            new CustomRenderableObject("E")));
        assertEquals("[B!C!]D!E!", result.toString());
    }
}