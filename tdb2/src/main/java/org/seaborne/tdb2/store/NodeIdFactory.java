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

package org.seaborne.tdb2.store;

import static org.seaborne.tdb2.store.NodeIdTypes.PTR;
import static org.seaborne.tdb2.store.NodeIdTypes.isSpecial;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.jena.atlas.lib.BitsInt;
import org.apache.jena.atlas.lib.BitsLong;
import org.apache.jena.atlas.lib.Bytes;
import org.seaborne.tdb2.TDBException;

/** Factory for NodeIds, including to/from disk forms via ByteBuffers and byte[].*/
final
public class NodeIdFactory
{
    // On-disk: may be shorter (controlled by get/set ByteBuffer and byte[]).
    // In-emmeory - always int-long
    
    // XXX Chance for a cache?

    // 64 bit create
    private static NodeId create(long value2) {
        if ( !BitsLong.isSet(value2, 63) )
            return createPtr(value2);
        // Inline.
        long v2 = value2;
        int t = (int)BitsLong.unpack(v2, 56, 63);   // 7 bits
        v2 = BitsLong.clear(v2, 56, 64);
        NodeIdTypes type = NodeIdTypes.intToEnum(t);
        if ( type == NodeIdTypes.SPECIAL )
            throw new TDBException(String.format("Attempt to create a special from a long: 0x%016", v2));
        return NodeId.createRaw(type, v2);
    }
    
    // Long create.
    private static NodeId create(int v1, long v2) {
        if ( BitsInt.isSet(v1, 32) )
            return createPtrLong(v1, v2);
        int t = v1 >> 24;
        NodeIdTypes type = NodeIdTypes.intToEnum(t);
        if ( type == NodeIdTypes.SPECIAL )
            throw new TDBException(String.format("Attempt to create a special from a long: 0x%016", v2));
        return createNew(type, 0, v2);
    }
    
    private static NodeId create(NodeIdTypes type, int v1, long v2) {
        if ( isSpecial(type) ) {
            if ( NodeId.equals(NodeId.NodeDoesNotExist, v1, v2) )
                return NodeId.NodeDoesNotExist;
            if ( NodeId.equals(NodeId.NodeIdAny, v1, v2) )
                return NodeId.NodeIdAny;
            if ( NodeId.equals(NodeId.NodeIdDefined, v1, v2) )
                return NodeId.NodeIdDefined;
            if ( NodeId.equals(NodeId.NodeIdDefined, v1, v2) )
                return NodeId.NodeIdDefined;
            if ( NodeId.equals(NodeId.NodeIdUndefined, v1, v2) )
                return NodeId.NodeIdUndefined;
            //throw new IllegalArgumentException("Special not recognized");
        }
        return createNew(type, v1, v2);
    }
    
    public static NodeId createValue(NodeIdTypes type, long value) {
        // 64 bit.
        return createNew(type, 0, value);
    }
    
    private static NodeId createNew(NodeIdTypes type, int v1, long v2) {
        return NodeId.createRaw(type, v1, v2);
    }

    public static NodeId createPtr(long lo) {
        return createNew(PTR, 0, lo);
    }

    /*package*/ /*long*/ static NodeId createPtrLong(int hi, long lo) {
        return create(PTR, hi, lo);
    }
    
    // ------- On-disk forms.
    // 
    
    /** Not relative - get from position zero */
    public static NodeId get(byte[] b) { return get(b,0); } 

    // **** 64 bit NodeId ****
    /** Relative {@code ByteBuffer} {@code get} */
    public static NodeId get(ByteBuffer b)   { 
        long value2 = b.getLong();
        return decode(value2);
    }

    public static NodeId get(byte[] b, int idx) {
        long value2 = Bytes.getLong(b, idx);
        return decode(value2);
    }

    public static NodeId get(ByteBuffer b, int idx) {
        long value2 = b.getLong(idx);
        return decode(value2);
    }

    // 64 bit version
    private static NodeId decode(long value2) {
        return NodeIdFactory.create(value2);
    }

    /** Not relative - set at position zero */
    public static void set(NodeId nodeId, byte[] b) {
        long v2 = encode(nodeId);
        Bytes.setLong(v2, b, 0);
    }

    private static long encode(NodeId nodeId) {
        long x = nodeId.value2;
        if ( nodeId.isPtr() )
            return x;
        x = BitsLong.pack(x, nodeId.getTypeValue(), 56, 63);
        // Set high it.
        x = BitsLong.set(x, 63);
        return x; 
    }

    /** Relative {@code set} */
    public static void set(NodeId nodeId, ByteBuffer b) {
        long v2 = encode(nodeId);
        b.putLong(v2);
    }

    public static void set(NodeId nodeId, byte[] b, int idx) {
        long v2 = encode(nodeId);
        Bytes.setLong(v2, b, idx);
    }

    public static void set(NodeId nodeId, ByteBuffer b, int idx) {
        long v2 = encode(nodeId);
        b.putLong(idx, v2);
    }
    
    public static void setNext(NodeId nodeId, byte[] b, int idx) {
        long v2 = encode(nodeId);
        Bytes.setLong(v2+1, b, idx);
    }

//    /** Relative {@code ByteBuffer} {@code get} */
//    public static NodeId get(ByteBuffer b)   {
//        int value1 = b.getInt();
//        long value2 = b.getLong();
//        return decode(value1,value2);
//    }
//    
//    public static NodeId get(byte[] b, int idx) {
//        int value1 = Bytes.getInt(b, idx);
//        long value2 = Bytes.getLong(b, idx+SystemTDB.SizeOfInt);
//        return decode(value1, value2);
//    }
//
//    public static NodeId get(ByteBuffer b, int idx) {
//        int value1 = b.getInt(idx);
//        long value2 = b.getLong(idx+SystemTDB.SizeOfInt);
//        return decode(value1,value2);
//    }
//    
//    private static NodeId decode(int value1, long value2) {
//        return NodeIdFactory.create(value1, value2);
//    }
//    
//    /** Not relative - set at position zero */
//    public static void set(NodeId nodeId, byte[] b) {
//        int v1 = encode(nodeId.value1);    
//        Bytes.setInt(v1, b, 0);
//        Bytes.setLong(nodeId.value2, b, SystemTDB.SizeOfInt);
//    }
//
//    /** Relative {@code set} */
//    public static void set(NodeId nodeId, ByteBuffer b) {
//        int v1 = encode(nodeId.value1);    
//        b.putInt(v1);
//        b.putLong(nodeId.value2);
//    }
//
//    public static void set(NodeId nodeId, byte[] b, int idx) {
//        int v1 = encode(nodeId.value1);    
//        Bytes.setInt(v1, b, idx);
//        Bytes.setLong(nodeId.value2, b, idx+SystemTDB.SizeOfInt);
//    }
//
//    public static void set(NodeId nodeId, ByteBuffer b, int idx) {
//        int v1 = encode(nodeId.value1);    
//        b.putInt(idx, v1);
//        b.putLong(idx+SystemTDB.SizeOfInt, nodeId.value2);
//    }
//    
//    public static void setNext(NodeId nodeId, byte[] b, int idx) {
//        int v1 = encode(nodeId.value1);
//        long v2 = nodeId.value2;
//        // Unsigned 96 bit add! 
//        if ( v2 < 0 ) {
//            if ( v2 == Long.MIN_VALUE )
//                v1++ ;
//            else
//                v2--;
//        } else
//            v2++ ;
//        Bytes.setInt(v1, b, idx);
//        Bytes.setLong(v2, b, idx+Integer.SIZE);
//    }
//    private int encode(NodeId nodeId) {
//        long x = nodeId.value1;
//        if ( nodeId.isPtr() )
//            return x;
//        x = BitsInt.pack(x, nodeId.getTypeValue(), 24, 32);
//        return x; 
//    }




    private static AtomicInteger counter = new AtomicInteger(0xB0);
    public static NodeId genUnique() {
        return NodeIdFactory.create(NodeIdTypes.SPECIAL, counter.incrementAndGet(), 0);
    }
}
