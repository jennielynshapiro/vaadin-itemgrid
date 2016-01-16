//! (c) 2014 Unforgiven.pl - licensed under Apache License, Version 2.0 (see the end of this file)
package org.vaadin.miki.itemgrid.client.itemgrid;

import com.vaadin.shared.communication.ServerRpc;

/**
 * Server-side RPC for ItemGrid.
 * 
 * @author miki
 * 
 */
public interface ItemGridServerRpc extends ServerRpc {

  /**
   * Clicks item at given index. This can either select the item, deselect it, add to existing selection, or do nothing, depending on the settings of the grid.
   * 
   * @param index
   *          Index to select item at. Selecting the same item again and again has no effect. Use -1 to clear selection.
   */
  public void clickItemAtIndex(int index);

}

/*!
 * Copyright 2012-2014 Unforgiven.pl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
