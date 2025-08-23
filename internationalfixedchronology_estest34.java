public class InternationalFixedChronology_ESTestTest34 extends InternationalFixedChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        InternationalFixedChronology internationalFixedChronology0 = InternationalFixedChronology.INSTANCE;
        Clock clock0 = MockClock.systemDefaultZone();
        ChronoUnit chronoUnit0 = ChronoUnit.FOREVER;
        Duration duration0 = chronoUnit0.getDuration();
        Clock clock1 = MockClock.offset(clock0, duration0);
        // Undeclared exception!
        try {
            internationalFixedChronology0.dateNow(clock1);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            //
            // long overflow
            //
            verifyException("java.lang.Math", e);
        }
    }
}