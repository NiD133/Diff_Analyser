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
     * Test fixture class that demonstrates custom rendering behavior.
     */
    static class Fixture {

        private final String name;

        Fixture(final String name) {
            this.name = name;
        }

        /**
         * Renders this fixture to an Appendable.
         * Example: renders "name" as "name!"
         */
        void render(final Appendable appendable) throws IOException {
            appendable.append(name);
            appendable.append('!');
        }
    }

    @Test
    void builderFactory_always_returnsNewInstance() {
        assertNotSame(AppendableJoiner.builder(), AppendableJoiner.builder());
    }

    @Test
    void defaultBuilder_shouldJoinElementsWithoutPrefixSuffixOrDelimiter() {
        // Create default joiner (no prefix, suffix, delimiter)
        final Builder<Object> builder = AppendableJoiner.builder();
        final AppendableJoiner<Object> joiner = builder.get();
        
        final StringBuilder sb = new StringBuilder("A");
        joiner.join(sb, "B", "C");
        assertEquals("ABC", sb.toString());
        
        sb.append("1");
        joiner.join(sb, Arrays.asList("D", "E"));
        assertEquals("ABC1DE", sb.toString());
    }

    @Test
    void builderWithAllProperties_shouldCorrectlyJoinElements() {
        // Configure joiner with all properties: prefix, suffix, delimiter, custom appender
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder()
                .setPrefix("<")
                .setDelimiter(".")
                .setSuffix(">")
                .setElementAppender((a, e) -> a.append(String.valueOf(e)))
                .get();
        
        final StringBuilder sb = new StringBuilder("A");
        joiner.join(sb, "B", "C");
        assertEquals("A<B.C>", sb.toString());
        
        sb.append("1");
        joiner.join(sb, Arrays.asList("D", "E"));
        assertEquals("A<B.C>1<D.E>", sb.toString());
    }

    @Test
    void customElementAppenderWithPrefixDelimiterSuffix_shouldRenderCorrectly() {
        // Configure joiner with custom element rendering (adds pipe before each element)
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder()
                .setPrefix("<")
                .setDelimiter(".")
                .setSuffix(">")
                .setElementAppender((a, e) -> a.append("|").append(Objects.toString(e)))
                .get();
        
        final StringBuilder sb = new StringBuilder("A");
        joiner.join(sb, "B", "C");
        assertEquals("A<|B.|C>", sb.toString());
        
        sb.append("1");
        joiner.join(sb, Arrays.asList("D", "E"));
        assertEquals("A<|B.|C>1<|D.|E>", sb.toString());
    }

    @Test
    void customElementAppenderWithRenderMethod_shouldUseFixtureRendering() {
        // Configure joiner to use custom render method from Fixture class
        final AppendableJoiner<Fixture> joiner = AppendableJoiner.<Fixture>builder()
                .setElementAppender((a, e) -> e.render(a))
                .get();
        
        final StringBuilder sb = new StringBuilder("[");
        joiner.join(sb, new Fixture("B"), new Fixture("C"));
        assertEquals("[B!C!", sb.toString());
        
        sb.append("]");
        joiner.join(sb, Arrays.asList(new Fixture("D"), new Fixture("E")));
        assertEquals("[B!C!]D!E!", sb.toString());
    }

    @Test
    void delimiter_shouldSeparateElementsWhenUsedWithStringBuilder() {
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder()
                .setDelimiter(".")
                .get();
        
        final StringBuilder sb = new StringBuilder("A");
        joiner.join(sb, "B", "C");
        assertEquals("AB.C", sb.toString());
        
        sb.append("1");
        joiner.join(sb, Arrays.asList("D", "E"));
        assertEquals("AB.C1D.E", sb.toString());
    }

    @SuppressWarnings("deprecation") // Test own StrBuilder
    @ParameterizedTest(name = "Appendable type: {0}")
    @ValueSource(classes = { 
        StringBuilder.class, 
        StringBuffer.class, 
        StringWriter.class, 
        StrBuilder.class, 
        TextStringBuilder.class 
    })
    void delimiter_shouldSeparateElementsForAllAppendableTypes(Class<? extends Appendable> clazz) 
            throws Exception {
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder()
                .setDelimiter(".")
                .get();
        
        final Appendable appendable = clazz.newInstance();
        appendable.append("A");
        joiner.joinA(appendable, "B", "C");
        assertEquals("AB.C", appendable.toString());
        
        appendable.append("1");
        joiner.joinA(appendable, Arrays.asList("D", "E"));
        assertEquals("AB.C1D.E", appendable.toString());
    }
}