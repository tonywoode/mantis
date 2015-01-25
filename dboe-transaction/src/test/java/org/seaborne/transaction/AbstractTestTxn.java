/**
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

package org.seaborne.transaction;

import static org.junit.Assert.assertEquals ;

import java.util.Arrays ;
import java.util.List ;

import org.junit.After ;
import org.junit.Before ;
import org.seaborne.jena.tdb.base.file.Location ;
import org.seaborne.transaction.txn.TransactionComponentWrapper ;
import org.seaborne.transaction.txn.TransactionCoordinator ;
import org.seaborne.transaction.txn.TransactionalBase ;
import org.seaborne.transaction.txn.TransactionalComponent ;
import org.seaborne.transaction.txn.journal.Journal ;

public abstract class AbstractTestTxn {
    protected TransactionCoordinator txnMgr ;
    protected TransInteger counter1 = new TransInteger(0) ; 
    protected TransInteger counter2 = new TransInteger(0) ;
    protected TransMonitor monitor  = new TransMonitor() ;
    protected Transactional unit ;
    
    @Before public void setup() {
        Journal jrnl = Journal.create(Location.mem()) ;
        List<TransactionalComponent> cg = Arrays.asList
            (counter1, new TransactionComponentWrapper(counter2), monitor) ;
        txnMgr = new TransactionCoordinator(jrnl, cg) ;
        unit = new TransactionalBase(txnMgr) ;
    }
    
    @After public void clearup() {
        txnMgr.shutdown(); 
    }
    
    protected void checkClear() {
        assertEquals(0, txnMgr.countActive()) ;
        assertEquals(0, txnMgr.countBegin()-txnMgr.countFinished()) ;
    }
}
