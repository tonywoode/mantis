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

package org.seaborne.dboe.trans.bplustree;

import org.junit.runner.RunWith ;
import org.junit.runners.Suite ;
import org.seaborne.dboe.trans.bplustree.rewriter.TestBPlusTreeRewriterNonTxn ;

@RunWith(Suite.class)
@Suite.SuiteClasses( {
    
    // Non-transactional tests -- that is, algorithms and machinary. 
    TestBPTreeRecordsNonTxn.class,
    TestBPlusTreeIndexNonTxn.class,
    TestBPlusTreeNonTxn.class,
    TestBPTreeModes.class,
    
    // Transactional tests
    TestBPlusTreeTxn.class,
    
    // Rewriter
    TestBPlusTreeRewriterNonTxn.class
} )

public class TS_TxnBPTree
{ }
