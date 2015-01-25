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

import java.util.Iterator ;

import org.apache.jena.atlas.iterator.Filter ;
import org.apache.jena.atlas.iterator.Iter ;
import org.apache.jena.atlas.logging.Log ;
import org.seaborne.jena.engine.* ;
import org.seaborne.jena.engine.row.RowBuilderBase ;

import com.hp.hpl.jena.sparql.engine.binding.Binding ;
import com.hp.hpl.jena.sparql.expr.Expr ;
import com.hp.hpl.jena.sparql.expr.ExprException ;
import com.hp.hpl.jena.sparql.expr.ExprList ;
import com.hp.hpl.jena.sparql.function.FunctionEnv ;
import com.hp.hpl.jena.tdb.store.NodeId ;
import com.hp.hpl.jena.tdb.store.nodetable.NodeTable ;

/** Filter step for TDB (which delays Binding fetching until needed) */ 
public class StepFilterTDB implements Step<NodeId> {

    private final ExprList exprs ;
    private final Filter<Row<NodeId>> filter ;
    private final NodeTable nodeTable ; 
    private final RowBuilder<NodeId> builder = new RowBuilderBase<>() ;
    
    public StepFilterTDB(ExprList expressions, final NodeTable nodeTable, final FunctionEnv funcEnv) {
        this.exprs = expressions ; 
        this.nodeTable = nodeTable ;
        this.filter = new Filter<Row<NodeId>>() {
            @Override
            public boolean accept(Row<NodeId> row) {
                Binding binding = new BindingRow(row, nodeTable) ;
                for (Expr expr : exprs)
                    if ( ! accept(binding, expr) )
                        return false ;
                return true ;
            }

            private boolean accept(Binding binding, Expr expr) {
                try {
                    if ( expr.isSatisfied(binding, funcEnv) )
                        return true ;
                    return false ;
                } catch (ExprException ex) { // Some evaluation exception
                    return false ;
                } catch (Exception ex) {
                    Log.warn(StepFilterTDB.class, "General exception in " + expr, ex) ;
                    return false ;
                }
            }
        } ; 

    
    }
    
    @Override
    public RowList<NodeId> execute(RowList<NodeId> input) {
        Iterator<Row<NodeId>> iterRowFiltered = Iter.filter(input.iterator(), filter) ;
        RowList<NodeId> rlist = RowLib.createRowList(input.vars(), iterRowFiltered) ;
        return rlist ;
    }
    
    private Binding rowToBinding(Row<NodeId> row) {
        return null ;
    }

    @Override
    public String toString() { return "Step/Filter: "+exprs ; }
}
