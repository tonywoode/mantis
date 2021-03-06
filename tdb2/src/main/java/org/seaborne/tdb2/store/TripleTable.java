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

package org.seaborne.tdb2.store ;

import java.util.Iterator ;

import org.apache.jena.atlas.iterator.Iter;
import org.apache.jena.atlas.lib.tuple.Tuple ;
import org.apache.jena.graph.Node ;
import org.apache.jena.graph.Triple ;
import org.seaborne.tdb2.lib.TupleLib ;
import org.seaborne.tdb2.store.nodetable.NodeTable ;
import org.seaborne.tdb2.store.tupletable.TupleIndex ;
import org.seaborne.tdb2.sys.DatasetControl ;

/**
 * TripleTable - a collection of TupleIndexes for 3-tuples together with a node
 * table. Normally, based on 3 indexes (SPO, POS, OSP) but other indexing
 * structures can be configured. The node table form can map to and from NodeIds
 * (longs)
 */

public class TripleTable extends TableBase {
    public TripleTable(TupleIndex[] indexes, NodeTable nodeTable, DatasetControl policy) {
        super(3, indexes, nodeTable, policy) ;
        // table = new NodeTupleTableConcrete(3, indexes, nodeTable, policy) ;
    }

    /** Add triple */
    public void add(Triple triple) {
        add(triple.getSubject(), triple.getPredicate(), triple.getObject()) ;
    }

    /** Add triple */
    public void add(Node s, Node p, Node o) {
        table.addRow(s, p, o) ;
    }

    /** Delete a triple */
    public void delete(Triple triple) {
        delete(triple.getSubject(), triple.getPredicate(), triple.getObject()) ;
    }

    /** Delete a triple */
    public void delete(Node s, Node p, Node o) {
        table.deleteRow(s, p, o) ;
    }

    /** Find matching triples */
    public Iterator<Triple> find(Node s, Node p, Node o) {
        Iterator<Tuple<NodeId>> iter = table.findAsNodeIds(s, p, o) ;
        if ( iter == null )
            return Iter.nullIterator() ;
        Iterator<Triple> iter2 = TupleLib.convertToTriples(table.getNodeTable(), iter) ;
        return iter2 ;
    }

    /** Clear - does not clear the associated node tuple table */
    public void clearTriples() {
        table.clear() ;
    }
}
