/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  See the NOTICE file distributed with this work for additional
 *  information regarding copyright ownership.
 */

package org.seaborne.jena.engine;

import org.seaborne.jena.engine.JoinKey ;
import org.seaborne.jena.engine.RowList ;
import org.seaborne.jena.engine.join.HashJoinConcrete ;
import org.seaborne.jena.engine.join.RowOrder ;
import org.seaborne.jena.engine.row.RowBuilderBase ;

/** Tests hash join where the code calculates the entire join at once */  
public class TestHashJoinConcrete extends AbstractTestJoin1 {

    @Override
    public <X> RowList<X> join(JoinKey joinKey , RowList<X> left , RowList<X> right , RowOrder<X> comparator ) {
        return HashJoinConcrete.hashJoinConcrete(joinKey, left, right, new RowBuilderBase<X>()) ;
    }

}