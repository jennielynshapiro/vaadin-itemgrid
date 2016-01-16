//! (c) 2014 Unforgiven.pl - licensed under Apache License, Version 2.0 (see the end of this file)
package org.vaadin.miki.itemgrid.client.itemgrid;

import com.vaadin.shared.communication.ClientRpc;

/**
 * RPC for server-to-client connectivity.
 * 
 * @author miki
 * 
 */
public interface ItemGridClientRpc extends ClientRpc {

  /**
   * Sets the selectability of the grid's widget.
   * 
   * @param state
   *          Whether or not grid can be selected through clicks.
   */
  public void setSelectable(boolean state);

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
