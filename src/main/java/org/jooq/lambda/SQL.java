/**
 * Copyright (c) 2014-2016, Data Geekery GmbH, contact@datageekery.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jooq.lambda;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.NoSuchElementException;
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
 * @deprecated - See https://github.com/jOOQ/jOOL/issues/169
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
    public static <T> Seq<T> seq(PreparedStatement stmt, Function<ResultSet, T> rowFunction) {
        return seq(Unchecked.supplier(stmt::executeQuery), rowFunction, Unchecked.THROWABLE_TO_RUNTIME_EXCEPTION);
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
    public static <T> Seq<T> seq(PreparedStatement stmt, Function<ResultSet, T> rowFunction, Consumer<? super SQLException> exceptionTranslator) {
        return seq(Unchecked.supplier(
            stmt::executeQuery,
            e -> exceptionTranslator.accept((SQLException) e)
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
    public static <T> Seq<T> seq(ResultSet rs, Function<ResultSet, T> rowFunction) {
        return seq(() -> rs, rowFunction, Unchecked.THROWABLE_TO_RUNTIME_EXCEPTION);
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
    public static <T> Seq<T> seq(ResultSet rs, Function<ResultSet, T> rowFunction, Consumer<? super SQLException> exceptionTranslator) {
        return seq(() -> rs, rowFunction, exceptionTranslator);
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
    public static <T> Seq<T> seq(Supplier<? extends ResultSet> supplier, Function<ResultSet, T> rowFunction) {
        return seq(supplier, rowFunction, Unchecked.THROWABLE_TO_RUNTIME_EXCEPTION);
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
    public static <T> Seq<T> seq(Supplier<? extends ResultSet> supplier, Function<ResultSet, T> rowFunction, Consumer<? super SQLException> exceptionTranslator) {
        ResultSetIterator it = new ResultSetIterator<>(supplier, rowFunction, exceptionTranslator);
        return Seq.seq(it).onClose(() -> it.close());
    }

    static class ResultSetIterator<T> implements Iterator<T>, AutoCloseable {

        private final Supplier<? extends ResultSet>  supplier;
        private final Function<ResultSet, T>         rowFunction;
        private final Consumer<? super SQLException> exceptionTranslator;
        private       ResultSet                      rs;

        /**
         * Whether the underlying {@link ResultSet} has a next row. This
         * boolean has three states:
         * <ul>
         * <li>null: it's not known whether there is a next row</li>
         * <li>true: there is a next row, and it has been pre-fetched</li>
         * <li>false: there aren't any next rows</li>
         * </ul>
         */
        private       Boolean                        hasNext;

        ResultSetIterator(Supplier<? extends ResultSet> supplier, Function<ResultSet, T> rowFunction, Consumer<? super SQLException> exceptionTranslator) {
            this.supplier = supplier;
            this.rowFunction = rowFunction;
            this.exceptionTranslator = exceptionTranslator;
        }

        private ResultSet rs() {
            return (rs == null) ? (rs = supplier.get()) : rs;
        }

        @Override
        public boolean hasNext() {
            try {
                if (hasNext == null) {
                    hasNext = rs().next();
                }

                return hasNext;
            }
            catch (SQLException e) {
                exceptionTranslator.accept(e);
                throw new IllegalStateException("Must throw an exception in exceptionTranslator", e);
            }
        }

        @Override
        public T next() {
            try {
                if (!hasNext())
                    throw new NoSuchElementException();

                return rowFunction.apply(rs());
            }
            finally {
                hasNext = null;
            }
        }

        @Override
        public void close() {
            try {
                rs().close();
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
