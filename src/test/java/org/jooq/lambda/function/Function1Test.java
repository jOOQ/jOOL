package org.jooq.lambda.function;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class Function1Test {
    @Test
    public void compose() throws Exception {

        Function1<String, String> appendWorld = s -> s.concat("World");
        Function1<String, String> prependHello = "Hello"::concat;

        String expected = "HelloWorld";

        final Function1<String, String> composed = appendWorld.compose(prependHello);

        String actual = composed.apply("");

        assertTrue(actual.equals(expected));

    }

    @Test
    public void andThen() throws Exception {

        Function1<String, String> appendWorld = s -> s.concat("World");
        Function1<String, String> prependHello = "Hello"::concat;

        String expected = "HelloWorld";

        final Function1<String, String> composed = prependHello.andThen(appendWorld);

        String actual = composed.apply("");

        assertTrue(actual.equals(expected));
    }
}
