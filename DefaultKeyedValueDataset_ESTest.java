package org.jfree.data.general;

import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultKeyedValueDatasetTest {

    @Test
    public void emptyDataset_hasNullKeyAndValue() {
        DefaultKeyedValueDataset ds = new DefaultKeyedValueDataset();

        assertNull("Empty dataset should have no key", ds.getKey());
        assertNull("Empty dataset should have no value", ds.getValue());
    }

    @Test
    public void constructorWithKeyAndValue_exposesSameKeyAndValue() {
        String key = "k1";
        Number value = 42;

        DefaultKeyedValueDataset ds = new DefaultKeyedValueDataset(key, value);

        assertEquals("Key should match constructor argument", key, ds.getKey());
        assertEquals("Value should match constructor argument", value, ds.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorRejectsNullKey() {
        new DefaultKeyedValueDataset(null, 1);
    }

    @Test
    public void setValue_replacesKeyAndValue() {
        DefaultKeyedValueDataset ds = new DefaultKeyedValueDataset("initial", 1);

        ds.setValue("updatedKey", 2);

        assertEquals("Key should be replaced", "updatedKey", ds.getKey());
        assertEquals("Value should be replaced", 2, ds.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setValueRejectsNullKey() {
        DefaultKeyedValueDataset ds = new DefaultKeyedValueDataset();
        ds.setValue(null, 123);
    }

    @Test
    public void updateValue_onExistingKey_updatesOnlyValue() {
        DefaultKeyedValueDataset ds = new DefaultKeyedValueDataset("k", 10);

        ds.updateValue(20);

        assertEquals("Key should not change when updating value", "k", ds.getKey());
        assertEquals("Value should be updated", 20, ds.getValue());
    }

    @Test
    public void updateValue_allowsNullWhenKeyExists() {
        DefaultKeyedValueDataset ds = new DefaultKeyedValueDataset("k", 10);

        ds.updateValue(null);

        assertEquals("Key should remain unchanged", "k", ds.getKey());
        assertNull("Value should be set to null", ds.getValue());
    }

    @Test(expected = RuntimeException.class)
    public void updateValue_onEmptyDataset_throws() {
        DefaultKeyedValueDataset ds = new DefaultKeyedValueDataset();

        // There is no key to update; updating should fail.
        ds.updateValue(1);
    }

    @Test
    public void equalsAndHashCode_basedOnKeyAndValue() {
        DefaultKeyedValueDataset a = new DefaultKeyedValueDataset("k", 1);
        DefaultKeyedValueDataset b = new DefaultKeyedValueDataset("k", 1);
        DefaultKeyedValueDataset c = new DefaultKeyedValueDataset("k", 2);

        assertEquals("Datasets with same key/value should be equal", a, b);
        assertEquals("Equal datasets must have equal hash codes", a.hashCode(), b.hashCode());
        assertNotEquals("Datasets differing by value should not be equal", a, c);
    }

    @Test
    public void equals_isReflexive_andNotEqualToOtherType_orNull() {
        DefaultKeyedValueDataset ds = new DefaultKeyedValueDataset("k", 1);

        assertEquals("Object must be equal to itself", ds, ds);
        assertNotEquals("Dataset must not be equal to null", ds, null);
        assertNotEquals("Dataset must not be equal to an unrelated type", ds, "not a dataset");
    }

    @Test
    public void cloneProducesEqualButDistinctInstance() throws CloneNotSupportedException {
        DefaultKeyedValueDataset original = new DefaultKeyedValueDataset("k", 1);

        Object copy = original.clone();

        assertTrue("Clone should be equal to original", original.equals(copy));
        assertNotSame("Clone should be a different instance", original, copy);
    }

    @Test
    public void emptyDatasets_areEqual() {
        DefaultKeyedValueDataset a = new DefaultKeyedValueDataset();
        DefaultKeyedValueDataset b = new DefaultKeyedValueDataset();

        assertEquals("Two empty datasets should be equal", a, b);
    }
}