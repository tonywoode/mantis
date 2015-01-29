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

package org.seaborne.dboe.index.bplustree;

import org.junit.AfterClass ;
import org.junit.BeforeClass ;
import org.seaborne.dboe.base.record.RecordLib ;
import org.seaborne.dboe.index.AbstractTestRangeIndex ;
import org.seaborne.dboe.index.RangeIndex ;
import org.seaborne.dboe.index.bplustree.BPlusTree ;
import org.seaborne.dboe.index.bplustree.BPlusTreeParams ;
import org.seaborne.dboe.sys.SystemIndex ;

public class TestBPlusTree extends AbstractTestRangeIndex
{
    static boolean originalNullOut ; 
    @BeforeClass static public void beforeClass()
    {
        BPlusTreeParams.CheckingNode = true ;
        //BPlusTreeParams.CheckingTree = true ;   // Breaks with block tracking.
        originalNullOut = SystemIndex.getNullOut() ;
        SystemIndex.setNullOut(true) ;    
    }
    
    @AfterClass static public void afterClass()
    {
        SystemIndex.setNullOut(originalNullOut) ;    
    }
    
    @Override
    protected RangeIndex makeRangeIndex(int order, int minRecords)
    {
        BPlusTree bpt = BPlusTree.makeMem(order, minRecords, RecordLib.TestRecordLength, 0) ;
        if ( false )
        {
            // Breaks with CheckingTree = true ; because they deep reads the tree.
            BPlusTreeParams.CheckingNode = true ;
            BPlusTreeParams.CheckingTree = false ;
            bpt = BPlusTree.addTracking(bpt) ;
        }
        return bpt ; 
    }
}