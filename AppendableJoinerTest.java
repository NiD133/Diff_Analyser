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
     * Fixture class for testing.
     */
    static class Fixture {

        private final String name;

        Fixture(final String name) {
            this.name = name;
        }

        /**
         * Renders myself onto an Appendable to avoid creating intermediary strings.
         */
        void render(final Appendable appendable) throws IOException {
            appendable.append(name);
            appendable.append('!');
        }
    }

    //region Test appendable joiner
    /**
     * Tests all properties of the appendable joiner.
     */
    @Test
    void testAllBuilderPropertiesStringBuilder() {
        // Arrange
        final AppendableJoiner<Object> joiner = createAppendableJoinerWithPrefixDelimiterAndSuffix();
        final StringBuilder sbuilder = new StringBuilder("A");

        // Act and Assert
        assertEquals("A<B.C>", joiner.join(sbuilder, "B", "C").toString());
        sbuilder.append("1");
        assertEquals("A<B.C>1<D.E>", joiner.join(sbuilder, Arrays.asList("D", "E")).toString());
    }

    /**
     * Tests default builder with a StringBuilder.
     */
    @Test
    void testBuildDefaultStringBuilder() {
        // Arrange
        final Builder<Object> builder = AppendableJoiner.builder();
        final AppendableJoiner<Object> joiner = builder.get();
        final StringBuilder sbuilder = new StringBuilder("A");

        // Act and Assert
        assertEquals("ABC", joiner.join(sbuilder, "B", "C").toString());
        sbuilder.append("1");
        assertEquals("ABC1DE", joiner.join(sbuilder, "D", "E").toString());
    }

    /**
     * Tests that different builders are created for AppendableJoiner.
     */
    @Test
    void testBuilder() {
        assertNotSame(AppendableJoiner.builder(), AppendableJoiner.builder());
    }
    //endregion

    //region Test delimiter appendable
    /**
     * Tests the delimiter of an Appendable joiner with different appendables.
     *
     * @param clazz the class of the appendable
     */
    @ParameterizedTest
    @ValueSource(classes = { StringBuilder.class, StringBuffer.class, StringWriter.class, StrBuilder.class, TextStringBuilder.class })
    void testDelimiterAppendable(final Class<? extends Appendable> clazz) throws Exception {
        // Arrange
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder().setDelimiter(".").get();
        final Appendable sbuilder = clazz.newInstance();
        sbuilder.append("A");

        // Act and Assert
        assertEquals("AB.C", joiner.joinA(sbuilder, "B", "C").toString());
        sbuilder.append("1");
        assertEquals("AB.C1D.E", joiner.joinA(sbuilder, Arrays.asList("D", "E")).toString());
    }

    /**
     * Tests the delimiter of an Appendable joiner with a StringBuilder.
     */
    @Test
    void testDelimiterStringBuilder() {
        // Arrange
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder().setDelimiter(".").get();
        final StringBuilder sbuilder = new StringBuilder("A");

        // Act and Assert
        assertEquals("AB.C", joiner.join(sbuilder, "B", "C").toString());
        sbuilder.append("1");
        assertEquals("AB.C1D.E", joiner.join(sbuilder, Arrays.asList("D", "E")).toString());
    }
    //endregion

    //region Test to char sequence
    /**
     * Tests AppendableJoiner with prefix, delimiter and suffix set.
     */
    @Test
    void testToCharSequenceStringBuilder() {
        // Arrange
        final AppendableJoiner<Object> joiner = createAppendableJoinerWithPrefixDelimiterAndSuffixWithAppender();
        final StringBuilder sbuilder = new StringBuilder("A");

        // Act and Assert
        assertEquals("A<|B.|C>", joiner.join(sbuilder, "B", "C").toString());
        sbuilder.append("1");
        assertEquals("A<|B.|C>1<|D.|E>", joiner.join(sbuilder, Arrays.asList("D", "E")).toString());
    }

    /**
     * Tests AppendableJoiner with a Fixture class.
     */
    @Test
    void testToCharSequenceStringBuilder2() {
        // Arrange
        final AppendableJoiner<Fixture> joiner = AppendableJoiner.<Fixture>builder().setElementAppender((a, e) -> e.render(a)).get();
        final StringBuilder sbuilder = new StringBuilder("[");

        // Act and Assert
        assertEquals("[B!C!]", joiner.join(sbuilder, new Fixture("B"), new Fixture("C")).toString());
        sbuilder.append("]");
        assertEquals("[B!C!]D!E!", joiner.join(sbuilder, Arrays.asList(new Fixture("D"), new Fixture("E"))).toString());
    }
    //endregion

    /**
     * Creates an appendable joiner with prefix, delimiter and suffix set.
     * 
     * @return An appendable joiner with prefix, delimiter and suffix set.
     */
    private AppendableJoiner<Object> createAppendableJoinerWithPrefixDelimiterAndSuffix() {
        return AppendableJoiner.builder()
                .setPrefix("<")
                .setDelimiter(".")
                .setSuffix(">")
                .setElementAppender((a, e) -> a.append(String.valueOf(e)))
                .get();
    }

    /**
     * Creates an appendable joiner with prefix, delimiter and suffix set and a custom element appender.
     * 
     * @return An appendable joiner with prefix, delimiter and suffix set and a custom element appender.
     */
    private AppendableJoiner<Object> createAppendableJoinerWithPrefixDelimiterAndSuffixWithAppender() {
        return AppendableJoiner.builder()
                .setPrefix("<")
                .setDelimiter(".")
                .setSuffix(">")
                .setElementAppender((a, e) -> a.append("|").append(Objects.toString(e)))
                .get();
    }
}