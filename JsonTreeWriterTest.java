/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.Strictness;
import com.google.gson.common.MoreAsserts;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

@SuppressWarnings("resource")
public final class JsonTreeWriterTest {

    // Tests for basic JSON structure construction
    // ==========================================

    @Test
    public void testWriteSimpleArray() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray()
            .value(1)
            .value(2)
            .value(3)
            .endArray();
        assertThat(writer.get().toString()).isEqualTo("[1,2,3]");
    }

    @Test
    public void testWriteNestedArrays() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray()
            .beginArray()
            .endArray()
            .beginArray()
            .beginArray()
            .endArray()
            .endArray()
            .endArray();
        assertThat(writer.get().toString()).isEqualTo("[[],[[]]]");
    }

    @Test
    public void testWriteSimpleObject() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject()
            .name("A").value(1)
            .name("B").value(2)
            .endObject();
        assertThat(writer.get().toString()).isEqualTo("{\"A\":1,\"B\":2}");
    }

    @Test
    public void testWriteNestedObjects() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject()
            .name("A")
            .beginObject()
            .name("B")
            .beginObject()
            .endObject()
            .endObject()
            .name("C")
            .beginObject()
            .endObject()
            .endObject();
        assertThat(writer.get().toString()).isEqualTo("{\"A\":{\"B\":{}},\"C\":{}}");
    }

    @Test
    public void testEmptyWriterProducesJsonNull() {
        JsonTreeWriter writer = new JsonTreeWriter();
        assertThat(writer.get()).isEqualTo(JsonNull.INSTANCE);
    }

    // Tests for edge cases and invalid usage
    // =====================================

    @Test
    public void testOperationsAfterCloseThrowException() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.setStrictness(Strictness.LENIENT);
        writer.beginArray().value("A").endArray();
        writer.close();
        assertThrows(IllegalStateException.class, writer::beginArray);
    }

    @Test
    public void testPrematureCloseThrowsException() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.setStrictness(Strictness.LENIENT);
        writer.beginArray();
        IOException e = assertThrows(IOException.class, writer::close);
        assertThat(e).hasMessageThat().isEqualTo("Incomplete document");
    }

    @Test
    public void testNameOutsideObjectThrowsException() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        // Top-level name
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> writer.name("invalid"));
        assertThat(e).hasMessageThat().isEqualTo("Did not expect a name");

        // Name after value in closed state
        writer.value(12);
        writer.close();
        e = assertThrows(IllegalStateException.class, () -> writer.name("invalid"));
        assertThat(e).hasMessageThat().isEqualTo("Please begin an object before writing a name.");
    }

    @Test
    public void testNameInsideArrayThrowsException() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();

        // Array doesn't accept names
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> writer.name("invalid"));
        assertThat(e).hasMessageThat().isEqualTo("Please begin an object before writing a name.");

        // Name after value in array
        writer.value(12);
        e = assertThrows(IllegalStateException.class, () -> writer.name("invalid"));
        assertThat(e).hasMessageThat().isEqualTo("Please begin an object before writing a name.");

        writer.endArray();
        assertThat(writer.get().toString()).isEqualTo("[12]");
    }

    @Test
    public void testDuplicateNameThrowsException() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject()
            .name("a");
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> writer.name("a"));
        assertThat(e).hasMessageThat().isEqualTo("Did not expect a name");
    }

    // Tests for configuration options
    // ==============================

    @Test
    public void testSerializeNullsFalseOmitsNulls() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.setSerializeNulls(false);
        writer.beginObject()
            .name("A")
            .nullValue()
            .endObject();
        assertThat(writer.get().toString()).isEqualTo("{}");
    }

    @Test
    public void testSerializeNullsTrueIncludesNulls() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.setSerializeNulls(true);
        writer.beginObject()
            .name("A")
            .nullValue()
            .endObject();
        assertThat(writer.get().toString()).isEqualTo("{\"A\":null}");
    }

    // Tests for method chaining
    // =========================

    @Test
    public void testMethodChainingReturnsWriterInstance() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        assertThat(writer.beginArray()).isSameInstanceAs(writer);
        assertThat(writer.beginObject()).isSameInstanceAs(writer);
        assertThat(writer.value("test")).isSameInstanceAs(writer);
        assertThat(writer.value(true)).isSameInstanceAs(writer);
        
        // Test boxed boolean
        Boolean boxedBool = true;
        assertThat(writer.value(boxedBool)).isSameInstanceAs(writer);
    }

    // Tests for special values
    // ========================

    @Test
    public void testLenientAcceptsSpecialFloatingPointValues() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.setStrictness(Strictness.LENIENT);
        writer.beginArray()
            .value(Float.NaN)
            .value(Float.NEGATIVE_INFINITY)
            .value(Float.POSITIVE_INFINITY)
            .value(Double.NaN)
            .value(Double.NEGATIVE_INFINITY)
            .value(Double.POSITIVE_INFINITY)
            .endArray();
        assertThat(writer.get().toString())
            .isEqualTo("[NaN,-Infinity,Infinity,NaN,-Infinity,Infinity]");
    }

    @Test
    public void testStrictRejectsSpecialFloatingPointValues() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.setStrictness(Strictness.LEGACY_STRICT);
        writer.beginArray();
        assertThrows(IllegalArgumentException.class, () -> writer.value(Float.NaN));
        assertThrows(IllegalArgumentException.class, () -> writer.value(Float.NEGATIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> writer.value(Float.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> writer.value(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> writer.value(Double.NEGATIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> writer.value(Double.POSITIVE_INFINITY));
    }

    @Test
    public void testStrictRejectsBoxedSpecialFloatingPointValues() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.setStrictness(Strictness.LEGACY_STRICT);
        writer.beginArray();
        assertThrows(IllegalArgumentException.class, () -> writer.value(Float.valueOf(Float.NaN)));
        assertThrows(IllegalArgumentException.class, 
            () -> writer.value(Float.valueOf(Float.NEGATIVE_INFINITY)));
        assertThrows(IllegalArgumentException.class, 
            () -> writer.value(Float.valueOf(Float.POSITIVE_INFINITY)));
        assertThrows(IllegalArgumentException.class, () -> writer.value(Double.valueOf(Double.NaN)));
        assertThrows(IllegalArgumentException.class, 
            () -> writer.value(Double.valueOf(Double.NEGATIVE_INFINITY)));
        assertThrows(IllegalArgumentException.class, 
            () -> writer.value(Double.valueOf(Double.POSITIVE_INFINITY)));
    }

    @Test
    public void testJsonValueThrowsUnsupportedOperation() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        assertThrows(UnsupportedOperationException.class, () -> writer.jsonValue("raw"));
    }

    // Test for implementation completeness
    // ===================================

    /**
     * Verifies that {@link JsonTreeWriter} overrides all relevant methods from {@link JsonWriter}
     * except configuration methods.
     */
    @Test
    public void testOverridesAllRelevantMethods() {
        List<String> ignoredMethods = Arrays.asList(
            "setLenient(boolean)",
            "isLenient()",
            "setStrictness(com.google.gson.Strictness)",
            "getStrictness()",
            "setIndent(java.lang.String)",
            "setHtmlSafe(boolean)",
            "isHtmlSafe()",
            "setFormattingStyle(com.google.gson.FormattingStyle)",
            "getFormattingStyle()",
            "setSerializeNulls(boolean)",
            "getSerializeNulls()"
        );
        MoreAsserts.assertOverridesMethods(JsonWriter.class, JsonTreeWriter.class, ignoredMethods);
    }
}