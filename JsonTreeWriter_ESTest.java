package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class JsonTreeWriterTest {

  @Test
  public void buildsArrayWithSingleObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginArray()
        .beginObject()
        .name("k").value(1)
        .endObject()
        .endArray();

    JsonElement root = writer.get();
    assertTrue(root.isJsonArray());

    JsonArray array = root.getAsJsonArray();
    assertEquals(1, array.size());

    JsonObject obj = array.get(0).getAsJsonObject();
    assertEquals(1, obj.get("k").getAsInt());
  }

  @Test
  public void getBeforeAnyWritesReturnsJsonNull() {
    JsonTreeWriter writer = new JsonTreeWriter();

    JsonElement root = writer.get();
    assertTrue(root.isJsonNull());
  }

  @Test
  public void getAfterCompletedEmptyObjectReturnsEmptyObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject().endObject();

    JsonElement root = writer.get();
    assertTrue(root.isJsonObject());
    assertEquals(0, root.getAsJsonObject().size());
  }

  @Test
  public void nameOutsideObjectThrows() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> writer.name("a"));
    assertNotNull(ex.getMessage());
    assertTrue(ex.getMessage().contains("begin an object"));
  }

  @Test
  public void duplicateNameThrows() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("a");

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> writer.name("b"));
    assertNotNull(ex.getMessage());
  }

  @Test
  public void valueInObjectWithoutNameThrows() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();

    assertThrows(IllegalStateException.class, () -> writer.value(1));
  }

  @Test
  public void getWhileDocumentIncompleteThrows() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();

    assertThrows(IllegalStateException.class, writer::get);
  }

  @Test
  public void nullStringAtTopLevelWritesJsonNull() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.value((String) null);

    JsonElement root = writer.get();
    assertTrue(root.isJsonNull());
    assertSame(JsonNull.INSTANCE, root);
  }

  @Test
  public void serializeNullsFalseDropsNamedNull() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.setSerializeNulls(false);
    writer.name("a");
    writer.nullValue();
    writer.endObject();

    JsonObject obj = writer.get().getAsJsonObject();
    assertTrue(obj.entrySet().isEmpty());
  }

  @Test
  public void serializeNullsTrueKeepsNamedNull() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.setSerializeNulls(true);
    writer.name("a");
    writer.nullValue();
    writer.endObject();

    JsonObject obj = writer.get().getAsJsonObject();
    assertTrue(obj.get("a").isJsonNull());
  }

  @Test
  public void jsonValueIsUnsupported() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();

    assertThrows(UnsupportedOperationException.class, () -> writer.jsonValue("{}"));
  }

  @Test
  public void closeOnIncompleteDocumentThrows() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();

    assertThrows(IOException.class, writer::close);
  }

  @Test
  public void cannotWriteAfterClose() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();

    assertThrows(IllegalStateException.class, writer::beginObject);
  }

  @Test
  public void chainingReturnsSameInstance() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();

    JsonWriter w1 = writer.beginArray();
    assertSame(writer, w1);

    JsonWriter w2 = writer.endArray();
    assertSame(writer, w2);

    JsonWriter w3 = writer.beginObject();
    assertSame(writer, w3);

    JsonWriter w4 = writer.endObject();
    assertSame(writer, w4);
  }
}