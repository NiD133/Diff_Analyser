package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.gson.common.TestTypes.BagOfPrimitives;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

public class JsonParserTestTest12 {

    @Test
    public void testReadWriteTwoObjects() throws Exception {
        Gson gson = new Gson();
        CharArrayWriter writer = new CharArrayWriter();
        BagOfPrimitives expectedOne = new BagOfPrimitives(1, 1, true, "one");
        writer.write(gson.toJson(expectedOne).toCharArray());
        BagOfPrimitives expectedTwo = new BagOfPrimitives(2, 2, false, "two");
        writer.write(gson.toJson(expectedTwo).toCharArray());
        CharArrayReader reader = new CharArrayReader(writer.toCharArray());
        JsonReader parser = new JsonReader(reader);
        parser.setStrictness(Strictness.LENIENT);
        JsonElement element1 = Streams.parse(parser);
        JsonElement element2 = Streams.parse(parser);
        BagOfPrimitives actualOne = gson.fromJson(element1, BagOfPrimitives.class);
        assertThat(actualOne.stringValue).isEqualTo("one");
        BagOfPrimitives actualTwo = gson.fromJson(element2, BagOfPrimitives.class);
        assertThat(actualTwo.stringValue).isEqualTo("two");
    }
}
