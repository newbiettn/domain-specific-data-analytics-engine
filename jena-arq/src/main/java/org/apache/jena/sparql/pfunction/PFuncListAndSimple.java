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

package org.apache.jena.sparql.pfunction;

import org.apache.jena.graph.Node ;
import org.apache.jena.sparql.engine.ExecutionContext ;
import org.apache.jena.sparql.engine.QueryIterator ;
import org.apache.jena.sparql.engine.binding.Binding ;

/** The case of:
 *  <ul> 
 *  <li>subject is a list</li>
 *  <li>object is a node, not a list</li>
 *  <li>call the implementation with one binding at a time</li>
 *  </ul>
 */

public abstract
class PFuncListAndSimple extends PropertyFunctionEval
{
    protected PFuncListAndSimple()
    {
        super(PropFuncArgType.PF_ARG_SINGLE, PropFuncArgType.PF_ARG_LIST) ;
    }

    @Override
    public QueryIterator execEvaluated(Binding binding, PropFuncArg argSubject, Node predicate, PropFuncArg argObject, ExecutionContext execCxt)
    {
        return execEvaluated(binding, argSubject, predicate, argObject.getArg(), execCxt) ;
    }

    /** 
     * @param binding   Current solution from previous query stage 
     * @param subject   List in subject slot, after substitution of any bound variables in this binding
     * @param predicate This predicate
     * @param object    Node in object slot, after substitution if a bound variable in this binding
     * @param execCxt   Execution context
     * @return          QueryIterator
     */
    public abstract QueryIterator execEvaluated(Binding binding, 
                                                PropFuncArg subject, Node predicate, Node object,
                                                ExecutionContext execCxt) ;
}
