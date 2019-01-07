/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jena.sparql.path ;

import org.apache.jena.sparql.util.NodeIsomorphismMap ;

public class P_ZeroOrOne extends P_Path1 {
    public P_ZeroOrOne(Path path) {
        super(path) ;
    }

    @Override
    public boolean equalTo(Path path2, NodeIsomorphismMap isoMap) {
        if ( !(path2 instanceof P_ZeroOrOne) )
            return false ;
        P_ZeroOrOne other = (P_ZeroOrOne)path2 ;
        return getSubPath().equalTo(other.getSubPath(), isoMap) ;
    }

    @Override
    public int hashCode() {
        return hashZeroOrOne ^ getSubPath().hashCode() ;
    }

    @Override
    public void visit(PathVisitor visitor) {
        visitor.visit(this) ;
    }
}
