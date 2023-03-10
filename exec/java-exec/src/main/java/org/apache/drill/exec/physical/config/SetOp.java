/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.drill.exec.physical.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.apache.calcite.sql.SqlKind;
import org.apache.drill.exec.ops.QueryContext;
import org.apache.drill.exec.physical.base.AbstractMultiple;
import org.apache.drill.exec.physical.base.PhysicalOperator;
import org.apache.drill.exec.physical.base.PhysicalVisitor;

import java.util.List;

@JsonTypeName("set-op")
public class SetOp extends AbstractMultiple {
  public SqlKind kind;
  public boolean all;

  @JsonCreator
  public SetOp(@JsonProperty("children") List<PhysicalOperator> children, @JsonProperty("sqlKind") SqlKind kind, @JsonProperty("all") boolean all) {
    super(children);
    this.kind = kind;
    this.all = all;
  }

  public SqlKind getKind() {
    return kind;
  }

  public boolean isAll() {
    return all;
  }

  @Override
  public <T, X, E extends Throwable> T accept(PhysicalVisitor<T, X, E> physicalVisitor, X value) throws E {
    return physicalVisitor.visitSetOp(this, value);
  }

  @Override
  public PhysicalOperator getNewWithChildren(List<PhysicalOperator> children) {
    return new SetOp(children, kind, all);
  }

  @Override
  public String getOperatorType() {
    return kind.name() + (all ? "_ALL" : "");
  }

  @Override @JsonIgnore
  public boolean isBufferedOperator(QueryContext queryContext) { return true; }
}
