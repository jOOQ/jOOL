package org.jooq.lambda.fi.util.function;

import org.jooq.lambda.Seq;
import org.jooq.lambda.Unchecked;
import org.jooq.lambda.UncheckedException;
import org.junit.Test;

import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CheckedFunction1Test {

    private String throwingFunction(boolean shouldThrow) throws Exception {
        if (shouldThrow) {
            throw new Exception("I fail on purpose");
        } else {
            return "OK";
        }
    }

    @Test
    public void uncheckedHappyPath() throws Exception {
        Seq.of(false)
                .map(Unchecked.function1(this::throwingFunction))
                .collect(Collectors.toSet());
    }

    @Test
    public void uncheckedUnhappyPath() throws Exception {
        try {
            Seq.of(true).map(Unchecked.function1(this::throwingFunction)).collect(Collectors.toSet());
            fail("RuntimeException was expected to be thrown");
        } catch (RuntimeException ex) {
            assertTrue(ex instanceof UncheckedException);
            assertTrue(ex.getMessage().equals("java.lang.Exception: I fail on purpose"));
        }
    }
}