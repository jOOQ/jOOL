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

/**
 * A window containing the data for its partition, to perform
 * calculations upon.
 *
 * @author Lukas Eder
 */
public interface Window<T, U> extends FramedWindow<T, U> {
    
    /**
     * The row number of the current row within the partition.
     */
    long rowNumber();
    
    /**
     * The bucket number ("ntile") of the current row within the partition.
     */
    long ntile(long buckets);
}
