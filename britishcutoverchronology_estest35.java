// Original Test
public class BritishCutoverChronology_ESTestTest35 extends BritishCutoverChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test34() throws Throwable {
        BritishCutoverChronology britishCutoverChronology0 = new BritishCutoverChronology();
        HashMap<TemporalField, Long> hashMap0 = new HashMap<TemporalField, Long>();
        ChronoField chronoField0 = ChronoField.YEAR_OF_ERA;
        Long long0 = new Long((-4135L));
        hashMap0.put(chronoField0, long0);
        ResolverStyle resolverStyle0 = ResolverStyle.STRICT;
        // Undeclared exception!
        try {
            britishCutoverChronology0.resolveDate(hashMap0, resolverStyle0);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            //
            // Invalid value for YearOfEra (valid values 1 - 999999): -4135
            //
            verifyException("java.time.temporal.ValueRange", e);
        }
    }
}