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
package org.apache.drill.exec.client;

import org.apache.curator.utils.ZKPaths;
import org.apache.drill.common.config.DrillConfig;
import org.apache.drill.exec.ExecConstants;
import org.apache.drill.exec.TestWithZookeeper;
import org.apache.drill.exec.coord.zk.ZKClusterCoordinator;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

public class DrillClientStateTest extends TestWithZookeeper {

  @Test
  public void testNotExistZkRoot() throws Exception {
    // There is no drillbit startup, therefore the root path is not saved in ZK.
    DrillConfig config = DrillConfig.create();
    String connString = zkHelper.getConnectionString();
    String zkRoot = config.getString(ExecConstants.ZK_ROOT); // does not exist
    String connStringWithZKRoot = connString + ZKPaths.PATH_SEPARATOR + zkRoot;

    NullPointerException nullPointerException = Assert.assertThrows(NullPointerException.class, () -> {
      try (ZKClusterCoordinator coordinator = new ZKClusterCoordinator(config, connStringWithZKRoot)) {
        coordinator.start(10000);
      }
    });

    MatcherAssert.assertThat(nullPointerException.getMessage(), is("The root path does not exist in the Zookeeper."));
  }

}
