/**
 * Copyright (c) 2011-2014, Data Geekery GmbH, contact@datageekery.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOU" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.lambda;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Simplifications for the JDBC APIs when used with Java 8.
 *
 * @author Lukas Eder
 */
public final class SQL {

    /**
     * Obtain a stream from a JDBC {@link ResultSet} that is obtained from a {@link PreparedStatement} with a row
     * mapping function.
     * <p>
     * Clients are responsible themselves for closing the <code>ResultSet</code>.
     *
     * @param stmt        The JDBC <code>PreparedStatement</code> that generates a <code>ResultSet</code> to be wrapped
     *                    in a {@link Stream}
     * @param rowFunction The row mapping function that maps <code>ResultSet</code> rows to a custom type.
     * @param <T>         The custom type.
     * @return A <code>Stream</code> wrapping the <code>ResultSet</code>
     */
    public static <T> Stream<T> stream(PreparedStatement stmt, Function<ResultSet, T> rowFunction) {
        return stream(Unchecked.supplier(() -> stmt.executeQuery()), rowFunction, Unchecked.THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Obtain a stream from a JDBC {@link ResultSet} that is obtained from a {@link PreparedStatement} with a row
     * mapping function.
     * <p>
     * Clients are responsible themselves for closing the <code>ResultSet</code>.
     *
     * @param stmt                The JDBC <code>PreparedStatement</code> that generates a <code>ResultSet</code> to be
     *                            wrapped in a {@link Stream}
     * @param rowFunction         The row mapping function that maps <code>ResultSet</code> rows to a custom type.
     * @param exceptionTranslator A custom exception translator.
     * @param <T>                 The custom type.
     * @return A <code>Stream</code> wrapping the <code>ResultSet</code>
     */
    public static <T> Stream<T> stream(PreparedStatement stmt, Function<ResultSet, T> rowFunction, Consumer<? super SQLException> exceptionTranslator) {
        return stream(Unchecked.supplier(
            () -> stmt.executeQuery(),
            e -> {
                exceptionTranslator.accept((SQLException) e);
            }
        ), rowFunction, exceptionTranslator);
    }

    /**
     * Obtain a stream from a JDBC {@link ResultSet} with a row mapping function.
     * <p>
     * Clients are responsible themselves for closing the <code>ResultSet</code>.
     *
     * @param rs          The JDBC <code>ResultSet</code> to wrap in a {@link Stream}
     * @param rowFunction The row mapping function that maps <code>ResultSet</code> rows to a custom type.
     * @param <T>         The custom type.
     * @return A <code>Stream</code> wrapping the <code>ResultSet</code>
     */
    public static <T> Stream<T> stream(ResultSet rs, Function<ResultSet, T> rowFunction) {
        return stream(() -> rs, rowFunction, Unchecked.THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Obtain a stream from a JDBC {@link ResultSet} with a row mapping function.
     * <p>
     * Clients are responsible themselves for closing the <code>ResultSet</code>.
     *
     * @param rs                  The JDBC <code>ResultSet</code> to wrap in a {@link Stream}
     * @param rowFunction         The row mapping function that maps <code>ResultSet</code> rows to a custom type.
     * @param exceptionTranslator A custom exception translator.
     * @param <T>                 The custom type.
     * @return A <code>Stream</code> wrapping the <code>ResultSet</code>
     */
    public static <T> Stream<T> stream(ResultSet rs, Function<ResultSet, T> rowFunction, Consumer<? super SQLException> exceptionTranslator) {
        return stream(() -> rs, rowFunction, exceptionTranslator);
    }

    /**
     * Obtain a stream from a JDBC {@link ResultSet} {@link Supplier} with a row mapping function.
     * <p>
     * Clients are responsible themselves for closing the <code>ResultSet</code>.
     *
     * @param supplier    The JDBC <code>ResultSet</code> <code>Supplier</code> to wrap in a {@link Stream}
     * @param rowFunction The row mapping function that maps <code>ResultSet</code> rows to a custom type.
     * @param <T>         The custom type.
     * @return A <code>Stream</code> wrapping the <code>ResultSet</code>
     */
    public static <T> Stream<T> stream(Supplier<? extends ResultSet> supplier, Function<ResultSet, T> rowFunction) {
        return stream(supplier, rowFunction, Unchecked.THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Obtain a stream from a JDBC {@link ResultSet} {@link Supplier} with a row mapping function.
     * <p>
     * Clients are responsible themselves for closing the <code>ResultSet</code>.
     *
     * @param supplier            The JDBC <code>ResultSet</code> <code>Supplier</code> to wrap in a {@link Stream}
     * @param rowFunction         The row mapping function that maps <code>ResultSet</code> rows to a custom type.
     * @param exceptionTranslator A custom exception translator.
     * @param <T>                 The custom type.
     * @return A <code>Stream</code> wrapping the <code>ResultSet</code>
     */
    public static <T> Stream<T> stream(Supplier<? extends ResultSet> supplier, Function<ResultSet, T> rowFunction, Consumer<? super SQLException> exceptionTranslator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new ResultSetIterator<>(supplier, rowFunction, exceptionTranslator), 0), false);
    }

    static class ResultSetIterator<T> implements Iterator<T> {

        private final Supplier<? extends ResultSet>  supplier;
        private final Function<ResultSet, T>         rowFunction;
        private final Consumer<? super SQLException> exceptionTranslator;
        private       ResultSet                      rs;
        private       boolean                        afterFirst;
        
        ResultSetIterator(Supplier<? extends ResultSet> supplier, Function<ResultSet, T> rowFunction, Consumer<? super SQLException> exceptionTranslator) {
            this.supplier = supplier;
            this.rowFunction = rowFunction;
            this.exceptionTranslator = exceptionTranslator;
            this.afterFirst = false;
        }

        private ResultSet rs() {
            return (rs == null) ? (rs = supplier.get()) : rs;
        }

        @Override
        public boolean hasNext() {
            try {
                return ( afterFirst && !rs().isLast() ) || rs().isBeforeFirst();
            }
            catch (SQLException e) {
                exceptionTranslator.accept(e);
                throw new IllegalStateException("Must throw an exception in exceptionTranslator", e);
            }
        }

        @Override
        public T next() {
            try {
                if (rs().next()) {
                	afterFirst = true;
                    return rowFunction.apply(rs());
                }
                else {
                    throw new IllegalStateException("ResultSet has no more rows");
                }
            }
            catch (SQLException e) {
                exceptionTranslator.accept(e);
                throw new IllegalStateException("Must throw an exception in exceptionTranslator", e);
            }
        }
    }

    private SQL() {
    }
}
