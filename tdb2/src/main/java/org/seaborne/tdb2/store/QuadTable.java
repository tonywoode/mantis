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
import org.apache.jena.sparql.core.Quad ;
import org.seaborne.tdb2.lib.TupleLib ;
import org.seaborne.tdb2.store.nodetable.NodeTable ;
import org.seaborne.tdb2.store.tupletable.TupleIndex ;
import org.seaborne.tdb2.sys.DatasetControl ;

/**
 * Quad table - a collection of TupleIndexes for 4-tuples together with a node
 * table.
 */

public class QuadTable extends TableBase {
    public QuadTable(TupleIndex[] indexes, NodeTable nodeTable, DatasetControl policy) {
        super(4, indexes, nodeTable, policy) ;
    }

    /** Add a quad - return true if it was added, false if it already existed */
    public void add(Quad quad) {
        add(quad.getGraph(), quad.getSubject(), quad.getPredicate(), quad.getObject()) ;
    }

    /**
     * Add a quad (as graph node and triple) - return true if it was added,
     * false if it already existed
     */
    public void add(Node gn, Triple triple) {
        add(gn, triple.getSubject(), triple.getPredicate(), triple.getObject()) ;
    }

    /** Add a quad - return true if it was added, false if it already existed */
    public void add(Node g, Node s, Node p, Node o) {
        table.addRow(g, s, p, o) ;
    }

    /** Delete a quad - return true if it was deleted, false if it didn't exist */
    public void delete(Quad quad) {
        delete(quad.getGraph(), quad.getSubject(), quad.getPredicate(), quad.getObject()) ;
    }

    /**
     * Delete a quad (as graph node and triple) - return true if it was deleted,
     * false if it didn't exist
     */
    public void delete(Node gn, Triple triple) {
        delete(gn, triple.getSubject(), triple.getPredicate(), triple.getObject()) ;
    }

    /** Delete a quad - return true if it was deleted, false if it didn't exist */
    public void delete(Node g, Node s, Node p, Node o) {
        table.deleteRow(g, s, p, o) ;
    }

    /** Find matching quads */
    public Iterator<Quad> find(Node g, Node s, Node p, Node o) {
        Iterator<Tuple<NodeId>> iter = table.findAsNodeIds(g, s, p, o) ;
        if ( iter == null )
            return Iter.nullIterator() ;
        Iterator<Quad> iter2 = TupleLib.convertToQuads(table.getNodeTable(), iter) ;
        return iter2 ;
    }

    /** Clear - does not clear the associated node tuple table */
    public void clearQuads() {
        table.clear() ;
    }
}
