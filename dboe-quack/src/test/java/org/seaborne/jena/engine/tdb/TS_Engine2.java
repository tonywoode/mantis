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

package org.seaborne.jena.engine.tdb;

import org.junit.AfterClass ;
import org.junit.BeforeClass ;
import org.junit.runner.RunWith ;
import org.junit.runners.Suite ;
import org.junit.runners.Suite.SuiteClasses ;
import org.seaborne.jena.engine.Quack ;

import com.hp.hpl.jena.sparql.engine.optimizer.reorder.ReorderLib ;
import com.hp.hpl.jena.sparql.engine.optimizer.reorder.ReorderTransformation ;
import com.hp.hpl.jena.tdb.sys.SystemTDB ;

@RunWith(Suite.class)
@SuiteClasses( {
    TestEngine.class ,
    TestSolverExecution.class ,
    TestPatterns.class 
})
public class TS_Engine2 {
    static ReorderTransformation original = null ;
    
    @BeforeClass static public void beforeClass() {
        Quack.init() ;
        original = SystemTDB.defaultReorderTransform ;
        // Turn off optimization so test queries execute as written.
        SystemTDB.defaultReorderTransform = ReorderLib.identity() ;
    }
    
    @AfterClass static public void afterClass() {
        SystemTDB.defaultReorderTransform = original ;
    }
}