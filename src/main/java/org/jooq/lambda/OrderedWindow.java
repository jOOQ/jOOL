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

import java.util.Optional;

/**
 * A window containing the data for its ordered partition, to perform
 * calculations upon.
 *
 * @author Lukas Eder
 */
public interface OrderedWindow<T, U> extends Window<T, U> {

    /**
     * The rank of the current row within the partition.
     */
    long rank();
    
    /**
     * The dense rank of the current row within the partition.
     */
    long denseRank();
    
    /**
     * The precent rank of the current row within the partition.
     */
    double percentRank();
}
