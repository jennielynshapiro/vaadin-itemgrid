//! (c) 2014 Unforgiven.pl - licensed under Apache License, Version 2.0 (see the end of this file)
package org.vaadin.miki.itemgrid.client.itemgrid;

import java.util.HashSet;
import java.util.Set;

import com.vaadin.shared.AbstractFieldState;

/**
 * State of an item grid.
 * 
 * @author miki
 * 
 */
public class ItemGridState extends AbstractFieldState {

  private static final long serialVersionUID = 20130203L;

  {
    this.primaryStyleName = "m-itemgrid";
  }

  /**
   * Number of columns.
   */
  public int columns = 3;

  /**
   * Indices of currently selected items.
   */
  public Set<Integer> selected = new HashSet<Integer>();

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
