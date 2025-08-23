package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.common.testing.EqualsTester;
import com.google.gson.common.MoreAsserts;
import java.math.BigInteger;
import org.junit.Test;

public class JsonArrayTestTest8 {

    @Test
    public void testGetAs_WrongArraySize() {
        JsonArray jsonArray = new JsonArray();
        var e = assertThrows(IllegalStateException.class, () -> jsonArray.getAsByte());
        assertThat(e).hasMessageThat().isEqualTo("Array must have size 1, but has size 0");
        jsonArray.add(true);
        jsonArray.add(false);
        e = assertThrows(IllegalStateException.class, () -> jsonArray.getAsByte());
        assertThat(e).hasMessageThat().isEqualTo("Array must have size 1, but has size 2");
    }
}
