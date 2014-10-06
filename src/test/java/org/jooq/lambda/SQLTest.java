/**
 * Copyright (c) 2014, Data Geekery GmbH, contact@datageekery.com
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
 * . Neither the name "jOOQ" nor the names of its contributors may be
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

import org.jooq.impl.DSL;
import org.jooq.lambda.tuple.Tuple;
import org.junit.Test;

import java.sql.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.jooq.lambda.tuple.Tuple.tuple;
import static org.junit.Assert.assertEquals;

/**
 * @author Lukas Eder
 */
public class SQLTest {

    private Connection connection() {
        try {
            Class.forName("org.h2.Driver");
            return DriverManager.getConnection("jdbc:h2:~/jool-test", "sa", "");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testTakeWhileWithSQL() throws SQLException {
        try (Connection c = connection();
             PreparedStatement s = c.prepareStatement("SELECT first_name, last_name FROM author ORDER BY id");
             ResultSet rs = s.executeQuery()) {

            assertEquals(
                Arrays.asList(
                    tuple("FIRST_NAME", "George"),
                    tuple("LAST_NAME", "Orwell"),
                    tuple("FIRST_NAME", "Paulo"),
                    tuple("LAST_NAME", "Coelho")
                ),
                Seq.generate()
                   .limitWhile(Unchecked.predicate(v -> rs.next()))
                   .flatMap(Unchecked.function(v -> IntStream
                        .range(0, rs.getMetaData().getColumnCount())
                        .mapToObj(Unchecked.intFunction(i -> tuple(
                            rs.getMetaData().getColumnLabel(i + 1),
                            rs.getObject(i + 1)
                        )))
                   ))
                   .toList()
            );
        }
    }

    @Test
    public void testStreamStatement() throws SQLException {
        try (Connection c = connection();
             PreparedStatement s = c.prepareStatement("SELECT id FROM author ORDER BY id")) {

            List<Integer> ids =
            SQL.seq(s, Unchecked.function((ResultSet rs) -> rs.getInt(1)))
               .collect(Collectors.toList());

            assertEquals(Arrays.asList(1, 2), ids);
        }
    }

    @Test
    public void testStreamStatementForEmptyResults() throws SQLException {
        try (Connection c = connection();
             PreparedStatement s = c.prepareStatement("SELECT id FROM author WHERE 1 = 0")) {

            List<Integer> ids =
            SQL.seq(s, Unchecked.function((ResultSet rs) -> rs.getInt(1)))
               .collect(Collectors.toList());

            assertEquals(Collections.<Integer>emptyList(), ids);
        }
    }

    @Test
    public void testStreamEmptyResultSet() throws SQLException {
        try (Connection c = connection();
             PreparedStatement s = c.prepareStatement("SELECT id FROM author WHERE 1 = 0");
             ResultSet rs = s.executeQuery()) {

            List<Integer> ids =
            SQL.seq(rs, Unchecked.function((ResultSet r) -> r.getInt(1)))
               .collect(Collectors.toList());

            assertEquals(Collections.<Integer>emptyList(), ids);
        }
    }
}
