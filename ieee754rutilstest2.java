package org.apache.commons.lang3.math;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.apache.commons.lang3.LangAssertions.assertNullPointerException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.Test;

public class IEEE754rUtilsTestTest2 extends AbstractLangTest {

    @Test
    void testEnforceExceptions() {
        assertNullPointerException(() -> IEEE754rUtils.min((float[]) null), "IllegalArgumentException expected for null input");
        assertIllegalArgumentException(IEEE754rUtils::min, "IllegalArgumentException expected for empty input");
        assertNullPointerException(() -> IEEE754rUtils.max((float[]) null), "IllegalArgumentException expected for null input");
        assertIllegalArgumentException(IEEE754rUtils::max, "IllegalArgumentException expected for empty input");
        assertNullPointerException(() -> IEEE754rUtils.min((double[]) null), "IllegalArgumentException expected for null input");
        assertIllegalArgumentException(IEEE754rUtils::min, "IllegalArgumentException expected for empty input");
        assertNullPointerException(() -> IEEE754rUtils.max((double[]) null), "IllegalArgumentException expected for null input");
        assertIllegalArgumentException(IEEE754rUtils::max, "IllegalArgumentException expected for empty input");
    }
}
