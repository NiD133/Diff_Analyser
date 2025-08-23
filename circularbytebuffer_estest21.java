@Test(timeout = 4000)
public void test20() throws Throwable {
    CircularByteBuffer circularByteBuffer0 = null;
    try {
        circularByteBuffer0 = new CircularByteBuffer((-21));
        fail("Expecting exception: NegativeArraySizeException");
    } catch (NegativeArraySizeException e) {
        //
        // no message in exception (getMessage() returned null)
        //
        verifyException("org.apache.commons.io.IOUtils", e);
    }
}