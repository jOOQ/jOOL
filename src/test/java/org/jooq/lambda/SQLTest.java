/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.lambda;

import org.junit.Test;

import java.sql.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
