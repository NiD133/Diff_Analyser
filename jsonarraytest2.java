package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.common.testing.EqualsTester;
import com.google.gson.common.MoreAsserts;
import java.math.BigInteger;
import org.junit.Test;

public class JsonArrayTestTest2 {

    @Test
    public void testEqualsNonEmptyArray() {
        JsonArray a = new JsonArray();
        JsonArray b = new JsonArray();
        new EqualsTester().addEqualityGroup(a).testEquals();
        a.add(new JsonObject());
        assertThat(a.equals(b)).isFalse();
        assertThat(b.equals(a)).isFalse();
        b.add(new JsonObject());
        MoreAsserts.assertEqualsAndHashCode(a, b);
        a.add(new JsonObject());
        assertThat(a.equals(b)).isFalse();
        assertThat(b.equals(a)).isFalse();
        b.add(JsonNull.INSTANCE);
        assertThat(a.equals(b)).isFalse();
        assertThat(b.equals(a)).isFalse();
    }
}
