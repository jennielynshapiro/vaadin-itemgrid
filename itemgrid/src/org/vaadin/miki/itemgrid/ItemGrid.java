//! (c) 2014 Unforgiven.pl - licensed under Apache License, Version 2.0 (see the end of this file)
package org.vaadin.miki.itemgrid;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.vaadin.miki.itemgrid.client.itemgrid.ItemGridClientRpc;
import org.vaadin.miki.itemgrid.client.itemgrid.ItemGridServerRpc;
import org.vaadin.miki.itemgrid.client.itemgrid.ItemGridState;

import com.vaadin.data.Container;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;

/**
 * Simple component that displays items in a grid, each with a custom component defined through {@link ItemComponentGenerator}.
 * 
 * @author miki
 * 
 */
public class ItemGrid extends AbstractSelect implements HasComponents {

  private static final long serialVersionUID = 20140513L;

  private boolean selectable = true;

  private ItemComponentGenerator generator = new DefaultItemComponentGenerator();

  private final LinkedHashMap<Object, Component> components = new LinkedHashMap<Object, Component>();

  private final ItemGridServerRpc rpc = new ItemGridServerRpc() {
    private static final long serialVersionUID = 20140513L;

    @Override
    public void clickItemAtIndex(int index) {
      ItemGrid.this.clickItemAtIndex(index);
    }
  };

  /**
   * Constructs an item grid with default number of columns and no container data source.
   */
  public ItemGrid() {
    super();
    this.registerRpc(this.rpc);
  }

  /**
   * Constructs an item grid with given number of columns and no container data source.
   * 
   * @param columns
   *          Number of columns.
   */
  public ItemGrid(int columns) {
    super();
    this.registerRpc(this.rpc);
    this.setColumnCount(columns);
  }

  /**
   * Constructs an item grid with default number of columns and no container data source, with a given caption.
   * 
   * @param caption
   *          Grid caption.
   */
  public ItemGrid(String caption) {
    super(caption);
    this.registerRpc(this.rpc);
  }

  /**
   * Constructs an item grid with given number of columns and no container data source, with a given caption.
   * 
   * @param caption
   *          Grid caption.
   * @param columns
   *          Number of columns.
   */
  public ItemGrid(String caption, int columns) {
    super(caption);
    this.setColumnCount(columns);
    this.registerRpc(this.rpc);
  }

  /**
   * Constructs an item grid with default number of columns and given caption and container data source.
   * 
   * @param caption
   *          Grid caption.
   * @param container
   *          Container data source.
   */
  public ItemGrid(String caption, Container container) {
    super(caption, container);
    this.registerRpc(this.rpc);
    this.updateItemComponents();
  }

  /**
   * Constructs an item grid with given number of columns and given caption and container data source.
   * 
   * @param caption
   *          Grid caption.
   * @param container
   *          Container data source.
   * @param columns
   *          Number of columns.
   */
  public ItemGrid(String caption, Container container, int columns) {
    super(caption, container);
    this.registerRpc(this.rpc);
    this.setColumnCount(columns);
    this.updateItemComponents();
  }

  /**
   * Constructs an item grid with default number of columns, given caption and a container data source with items from the collection.
   * 
   * @param caption
   *          Grid caption.
   * @param options
   *          Items to put in the container data source.
   */
  public ItemGrid(String caption, Collection<?> options) {
    super(caption, options);
    this.registerRpc(this.rpc);
    this.updateItemComponents();
  }

  /**
   * Constructs an item grid with given number of columns, given caption and a container data source with items from the collection.
   * 
   * @param caption
   *          Grid caption.
   * @param options
   *          Items to put in the container data source.
   * @param columns
   *          Number of columns.
   */
  public ItemGrid(String caption, Collection<?> options, int columns) {
    super(caption, options);
    this.registerRpc(this.rpc);
    this.setColumnCount(columns);
    this.updateItemComponents();
  }

  /**
   * Sets the number of columns.
   * 
   * @param columns
   *          New number of columns.
   * @throws IllegalArgumentException
   *           when number of columns is less than 1.
   */
  public void setColumnCount(int columns) {
    if(columns < 1) throw new IllegalArgumentException("ItemGrid must have at least 1 column");
    this.getState().columns = columns;
    this.markAsDirty();
  }

  /**
   * Returns current number of columns.
   * 
   * @return Number of columns.
   */
  public int getColumnCount() {
    return this.getState().columns;
  }

  @Override
  public ItemGridState getState() {
    return (ItemGridState)super.getState();
  }

  /**
   * Returns whether or not grid cells can be selected through the UI.
   * 
   * @return When <b>true</b>, grid cells can be selected by clicking in the UI.
   */
  public boolean isSelectable() {
    return this.selectable;
  }

  /**
   * Sets selectability of grid cells through the UI to the new state. Non-selectable grid can still have its items selected through {@link #setValue(Object)}.
   * 
   * @param state
   *          When <b>false</b>, grid cells cannot be selected by clicking through the UI.
   */
  public void setSelectable(boolean state) {
    this.selectable = state;
    this.getRpcProxy(ItemGridClientRpc.class).setSelectable(state);
  }

  /**
   * Returns current item component generator, i.e. object that produces components to be displayed in cells.
   * 
   * @return Current item component generator.
   */
  public ItemComponentGenerator getItemComponentGenerator() {
    return this.generator;
  }

  /**
   * Sets new item component generator, i.e. object that produces components to be displayed in cells.
   * 
   * @param generator
   *          New item component generator. When new generator is <b>null</b>, an instance of {@link DefaultItemComponentGenerator} will be used.
   */
  public void setItemComponentGenerator(ItemComponentGenerator generator) {
    this.generator = generator;
    if(this.generator == null) this.generator = new DefaultItemComponentGenerator();
    this.updateItemComponents();
  }

  /**
   * Returns a default component for an item of given id. This method is called only when item component generator returned <b>null</b> for that item. This
   * method <b>must not</b> return <b>null</b>.
   * 
   * @param itemId
   *          Id of an item.
   * @return Component to represent an item, for which component generator returned <b>null<b>. Must not be <b>null</b>.
   */
  // #13 #12
  protected Component getNullComponent(Object itemId) {
    return new NonEmptyLabel("");
  }

  /**
   * Recreates contents of the grid.
   */
  protected void updateItemComponents() {
    // make sure new components are in the state
    // this.getState().items.clear();
    for(Component c: this.components.values())
      c.setParent(null);
    this.components.clear();
    Container container = this.getContainerDataSource();
    if(container != null) for(Object itemId: container.getItemIds()) {
      Component c = this.getComponentForItem(itemId);
      if(c == null) c = this.getNullComponent(itemId);
      c.setParent(this);
      this.components.put(itemId, c);
    }
    this.markAsDirty();
  }

  @Override
  public void beforeClientResponse(boolean initial) {

    this.getState().selected.clear();

    // all item ids in order
    List<Object> ids = Arrays.asList(this.components.keySet().toArray());

    // current value
    Object value = this.getValue();

    // for simplicity, make sure it is a collection
    if(!(value instanceof Collection)) value = Collections.singleton(value);

    for(Object id: (Collection<?>)value)
      this.getState().selected.add(ids.indexOf(id));
    super.beforeClientResponse(initial);
  }

  @Override
  public Iterator<Component> iterator() {
    return this.components.values().iterator();
  }

  @Override
  public Class<? extends Object> getType() {
    return Object.class;
  }

  @Override
  public void containerItemSetChange(Container.ItemSetChangeEvent event) {
    super.containerItemSetChange(event);
    this.updateItemComponents();
  }

  @Override
  public void containerPropertySetChange(Container.PropertySetChangeEvent event) {
    super.containerPropertySetChange(event);
    this.updateItemComponents();
  }

  /**
   * Checks whether an item at given index is selected.
   * 
   * @param index
   *          Index.
   * @return <b>true</b> if item at given index is selected, <b>false</b> otherwise.
   */
  protected boolean isItemAtIndexSelected(int index) {
    return index > -1 && index < this.components.size() && this.getValue() != null && this.getValue().equals(this.getItemAtIndex(index));
  }

  /**
   * Returns id of an item at given index.
   * 
   * @param index
   *          Index.
   * @return Id of an item at that index.
   * @throws IndexOutOfBoundsException
   *           when index is out of bounds ;)
   */
  protected Object getItemAtIndex(int index) {
    return this.components.keySet().toArray()[index];
  }

  /**
   * Checks if an item can be unselected. In single selection mode this means that null selection is allowed. In multi-select, this means that null selection is
   * allowed or there are more items selected.
   * 
   * @param itemId
   *          Id of an item to check whether or not it can be unselected.
   * @return <b>true</b> if a given item can be unselected.
   */
  protected boolean canUnelect(Object itemId) {
    return (!this.isMultiSelect() && this.isNullSelectionAllowed()) || (this.isMultiSelect() && (this.isNullSelectionAllowed() || ((Collection<?>)this.getValue()).size() > 1));
  }

  @Override
  protected void fireValueChange(boolean repaintIsNotNeeded) {
    // this is the last moment to update components
    if(!repaintIsNotNeeded) this.updateItemComponents();
    super.fireValueChange(repaintIsNotNeeded);
  }

  @Override
  public void setContainerDataSource(Container newDataSource) {
    super.setContainerDataSource(newDataSource);
    // #24, setting a container may not trigger recreating components through value change event
    if(this.components != null && (this.getValue() == null || (this.getValue() instanceof Collection && ((Collection<?>)this.getValue()).isEmpty()))) this.updateItemComponents();
  }

  /**
   * Clicks item at index. Use -1 to set value to <b>null</b>.
   * 
   * @param index
   *          Index.
   */
  // this is needed for unit tests to simulate user clicks
  protected void clickItemAtIndex(int index) {
    if(!ItemGrid.this.selectable) return;
    else if(index == -1) this.setValue(null);
    else {
      Object itemAtIndex = this.getItemAtIndex(index);
      if(this.isSelected(itemAtIndex) && this.canUnelect(itemAtIndex)) this.unselect(itemAtIndex);
      else this.select(itemAtIndex);
    }
  }

  /**
   * Constructs a component for item of given id.
   * 
   * @param itemId
   *          Id of an item.
   * @return Item component, as returned by current item component generator.
   */
  protected Component getComponentForItem(Object itemId) {
    Container container = this.getContainerDataSource();

    return this.generator.getComponentForItem(itemId, container.getItem(itemId), this.isSelected(itemId));
  }

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
