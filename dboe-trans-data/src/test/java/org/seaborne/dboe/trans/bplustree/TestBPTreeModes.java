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

import java.util.Arrays ;
import java.util.Collection ;

import org.junit.After ;
import org.junit.AfterClass ;
import org.junit.Before ;
import org.junit.BeforeClass ;
import org.junit.runner.RunWith ;
import org.junit.runners.Parameterized ;
import org.junit.runners.Parameterized.Parameters ;

@RunWith(Parameterized.class)
public class TestBPTreeModes extends TestBPlusTreeNonTxn
{
    @Parameters(name="N={0}, R= {1}")
    public static Collection<Object[]> data()
    {
        return Arrays.asList(new Object[][]{
            {true, true},
            {true, false},
            {false, true},
            {false, false}
            }) ;
    }
    
    public TestBPTreeModes(boolean nodeMode, boolean recordsMode) {
        
        BPT.promoteDuplicateNodes = nodeMode ;
        BPT.promoteDuplicateRecords = recordsMode ;
    }

    boolean modeAtStartNodes ;
    boolean modeAtStartRecords ;
    
    @BeforeClass public static void setupSuite() {
        BPT.forcePromoteModes = true ;
    }
    
    @AfterClass public static void resetSuite() {
        BPT.forcePromoteModes = false ;
    }
    
    @Before public void setModes() {
        BPT.forcePromoteModes = true ;
        modeAtStartNodes = BPT.promoteDuplicateNodes ;
        modeAtStartRecords = BPT.promoteDuplicateRecords ;
    }

    
    @After public void resetModes() {
        BPT.promoteDuplicateNodes = modeAtStartNodes ;
        BPT.promoteDuplicateRecords = modeAtStartRecords ;
    }

}