package org.apache.commons.io.function;

import static org.junit.Assert.*;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.junit.Test;

/**
 * Readable tests for Uncheck: verifies happy paths, exception wrapping, and null argument handling.
 */
public class UncheckTest {

    // ----------------------------
    // accept(...) happy-path tests
    // ----------------------------

    @Test
    public void accept_IOConsumer_noop_doesNotThrow() {
        IOConsumer<String> consumer = s -> { /* no-op */ };
        Uncheck.accept(consumer, "value");
    }

    @Test
    public void accept_IOBiConsumer_noop_doesNotThrow() {
        IOBiConsumer<String, String> consumer = (a, b) -> { /* no-op */ };
        Uncheck.accept(consumer, "a", "b");
    }

    @Test
    public void accept_IOTriConsumer_noop_doesNotThrow() {
        IOTriConsumer<String, String, String> consumer = (a, b, c) -> { /* no-op */ };
        Uncheck.accept(consumer, "a", "b", "c");
    }

    @Test
    public void accept_IOIntConsumer_capturesValue() {
        AtomicInteger seen = new AtomicInteger();
        IOIntConsumer consumer = seen::set;
        Uncheck.accept(consumer, 123);
        assertEquals(123, seen.get());
    }

    // ----------------------------
    // apply(...) happy-path tests
    // ----------------------------

    @Test
    public void apply_IOFunction_identity_handlesNullAndValue() {
        IOFunction<String, String> identity = IOFunction.identity();

        assertNull(Uncheck.apply(identity, null));
        assertEquals("x", Uncheck.apply(identity, "x"));
    }

    @Test
    public void apply_IOBiFunction_returnsResult() {
        IOBiFunction<String, String, String> concat = (a, b) -> a + b;
        assertEquals("ab", Uncheck.apply(concat, "a", "b"));
    }

    @Test
    public void apply_IOTriFunction_returnsResult() {
        IOTriFunction<String, String, String, String> join = (a, b, c) -> a + "-" + b + "-" + c;
        assertEquals("a-b-c", Uncheck.apply(join, "a", "b", "c"));
    }

    @Test
    public void apply_IOQuadFunction_returnsResult() {
        IOQuadFunction<String, String, String, String, String> join4 = (a, b, c, d) -> a + b + c + d;
        assertEquals("abcd", Uncheck.apply(join4, "a", "b", "c", "d"));
    }

    // ----------------------------
    // compare(...) happy-path tests
    // ----------------------------

    @Test
    public void compare_usesGivenComparator() {
        IOComparator<String> comparator = String::compareTo;

        assertEquals(0, Uncheck.compare(comparator, "x", "x"));
        assertTrue(Uncheck.compare(comparator, "a", "b") < 0);
        assertTrue(Uncheck.compare(comparator, "b", "a") > 0);
    }

    // ----------------------------
    // get(...) and primitives happy-path tests
    // ----------------------------

    @Test
    public void get_returnsSupplierValue() {
        IOSupplier<String> supplier = () -> "value";
        assertEquals("value", Uncheck.get(supplier));
    }

    @Test
    public void get_withNullMessageSupplier_returnsSupplierValue() {
        IOSupplier<String> supplier = () -> "value";
        assertEquals("value", Uncheck.get(supplier, null));
    }

    @Test
    public void getAsBoolean_returnsValue() {
        IOBooleanSupplier s = () -> true;
        assertTrue(Uncheck.getAsBoolean(s));
    }

    @Test
    public void getAsInt_returnsValue() {
        IOIntSupplier s = () -> 42;
        assertEquals(42, Uncheck.getAsInt(s));
    }

    @Test
    public void getAsInt_withNullMessage_returnsValue() {
        IOIntSupplier s = () -> -7;
        assertEquals(-7, Uncheck.getAsInt(s, null));
    }

    @Test
    public void getAsLong_returnsValue() {
        IOLongSupplier s = () -> 123456789L;
        assertEquals(123456789L, Uncheck.getAsLong(s));
    }

    @Test
    public void getAsLong_withNullMessage_returnsValue() {
        IOLongSupplier s = () -> -5L;
        assertEquals(-5L, Uncheck.getAsLong(s, null));
    }

    // ----------------------------
    // run(...) happy-path tests
    // ----------------------------

    @Test
    public void run_noop_doesNotThrow() {
        IORunnable r = () -> { /* no-op */ };
        Uncheck.run(r);
    }

    @Test
    public void run_withNullMessage_noop_doesNotThrow() {
        IORunnable r = () -> { /* no-op */ };
        Uncheck.run(r, null);
    }

    // ----------------------------
    // test(...) happy-path tests
    // ----------------------------

    @Test
    public void test_predicate_trueAndFalse() {
        IOPredicate<String> alwaysTrue = t -> true;
        IOPredicate<String> alwaysFalse = t -> false;

        assertTrue(Uncheck.test(alwaysTrue, "x"));
        assertFalse(Uncheck.test(alwaysFalse, "x"));
    }

    // ---------------------------------
    // IOException is wrapped as runtime
    // ---------------------------------

    @Test
    public void run_wrapsIOException() {
        IORunnable r = () -> { throw new IOException("boom"); };
        UncheckedIOException e = assertThrows(UncheckedIOException.class, () -> Uncheck.run(r));
        assertTrue(e.getCause() instanceof IOException);
    }

    @Test
    public void get_wrapsIOException() {
        IOSupplier<String> s = () -> { throw new IOException("boom"); };
        UncheckedIOException e = assertThrows(UncheckedIOException.class, () -> Uncheck.get(s));
        assertTrue(e.getCause() instanceof IOException);
    }

    @Test
    public void apply_wrapsIOException_fromFunction() {
        IOFunction<String, Integer> f = in -> { throw new IOException("boom"); };
        UncheckedIOException e = assertThrows(UncheckedIOException.class, () -> Uncheck.apply(f, "x"));
        assertTrue(e.getCause() instanceof IOException);
    }

    @Test
    public void compare_wrapsIOException_fromComparator() {
        IOComparator<String> comparator = (a, b) -> { throw new IOException("boom"); };
        UncheckedIOException e = assertThrows(UncheckedIOException.class, () -> Uncheck.compare(comparator, "a", "b"));
        assertTrue(e.getCause() instanceof IOException);
    }

    @Test
    public void accept_wrapsIOException_fromConsumer() {
        IOConsumer<String> c = x -> { throw new IOException("boom"); };
        UncheckedIOException e = assertThrows(UncheckedIOException.class, () -> Uncheck.accept(c, "x"));
        assertTrue(e.getCause() instanceof IOException);
    }

    @Test
    public void test_wrapsIOException_fromPredicate() {
        IOPredicate<String> p = x -> { throw new IOException("boom"); };
        UncheckedIOException e = assertThrows(UncheckedIOException.class, () -> Uncheck.test(p, "x"));
        assertTrue(e.getCause() instanceof IOException);
    }

    // ----------------------------
    // Null argument validation
    // ----------------------------

    @Test
    public void null_IOConsumer_inAccept_throwsNPE() {
        assertThrows(NullPointerException.class, () -> Uncheck.accept((IOConsumer<Object>) null, new Object()));
    }

    @Test
    public void null_IOBiConsumer_inAccept_throwsNPE() {
        assertThrows(NullPointerException.class, () -> Uncheck.accept((IOBiConsumer<Object, Object>) null, new Object(), new Object()));
    }

    @Test
    public void null_IOTriConsumer_inAccept_throwsNPE() {
        assertThrows(NullPointerException.class, () -> Uncheck.accept((IOTriConsumer<Object, Object, Object>) null, new Object(), new Object(), new Object()));
    }

    @Test
    public void null_IOIntConsumer_inAccept_throwsNPE() {
        assertThrows(NullPointerException.class, () -> Uncheck.accept((IOIntConsumer) null, 1));
    }

    @Test
    public void null_IOFunction_inApply_throwsNPE() {
        assertThrows(NullPointerException.class, () -> Uncheck.apply((IOFunction<Object, Object>) null, new Object()));
    }

    @Test
    public void null_IOBiFunction_inApply_throwsNPE() {
        assertThrows(NullPointerException.class, () -> Uncheck.apply((IOBiFunction<Object, Object, Object>) null, new Object(), new Object()));
    }

    @Test
    public void null_IOTriFunction_inApply_throwsNPE() {
        assertThrows(NullPointerException.class, () -> Uncheck.apply((IOTriFunction<Object, Object, Object, Object>) null, new Object(), new Object(), new Object()));
    }

    @Test
    public void null_IOQuadFunction_inApply_throwsNPE() {
        assertThrows(NullPointerException.class, () -> Uncheck.apply((IOQuadFunction<Object, Object, Object, Object, Object>) null, new Object(), new Object(), new Object(), new Object()));
    }

    @Test
    public void null_Comparator_inCompare_throwsNPE() {
        assertThrows(NullPointerException.class, () -> Uncheck.compare(null, "a", "b"));
    }

    @Test
    public void null_IOSupplier_inGet_throwsNPE() {
        assertThrows(NullPointerException.class, () -> Uncheck.get(null));
    }

    @Test
    public void null_IOBooleanSupplier_inGetAsBoolean_throwsNPE() {
        assertThrows(NullPointerException.class, () -> Uncheck.getAsBoolean(null));
    }

    @Test
    public void null_IOIntSupplier_inGetAsInt_throwsNPE() {
        assertThrows(NullPointerException.class, () -> Uncheck.getAsInt(null));
    }

    @Test
    public void null_IOIntSupplier_inGetAsIntWithMessage_throwsNPE() {
        assertThrows(NullPointerException.class, () -> Uncheck.getAsInt(null, () -> "msg"));
    }

    @Test
    public void null_IOLongSupplier_inGetAsLong_throwsNPE() {
        assertThrows(NullPointerException.class, () -> Uncheck.getAsLong(null));
    }

    @Test
    public void null_IOLongSupplier_inGetAsLongWithMessage_throwsNPE() {
        assertThrows(NullPointerException.class, () -> Uncheck.getAsLong(null, () -> "msg"));
    }

    @Test
    public void null_IORunnable_inRun_throwsNPE() {
        assertThrows(NullPointerException.class, () -> Uncheck.run(null));
    }

    @Test
    public void null_IORunnable_inRunWithMessage_throwsNPE() {
        assertThrows(NullPointerException.class, () -> Uncheck.run(null, () -> "msg"));
    }

    @Test
    public void null_IOPredicate_inTest_throwsNPE() {
        assertThrows(NullPointerException.class, () -> Uncheck.test(null, "x"));
    }
}