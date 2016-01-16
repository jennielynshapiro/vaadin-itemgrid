//! (c) 2014 Unforgiven.pl - licensed under Apache License, Version 2.0 (see the end of this file)
package org.vaadin.miki.itemgrid;

import java.io.Serializable;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;

/**
 * Interface for generating components for a given item.
 * 
 * @author miki
 * 
 */
public interface ItemComponentGenerator extends Serializable {
  /**
   * Returns a component that will be used to display an item.
   * 
   * @param itemId
   *          Id of an item to display.
   * @param item
   *          Item to display.
   * @param selected
   *          <b>true</b> if the item is currently selected, <b>false</b> otherwise.
   * @return Component to be displayed in the grid.
   */
  public Component getComponentForItem(Object itemId, Item item, boolean selected);
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
