//! (c) 2014 Unforgiven.pl - licensed under Apache License, Version 2.0 (see the end of this file)
package test.org.vaadin.miki.itemgrid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.miki.itemgrid.DefaultItemComponentGenerator;
import org.vaadin.miki.itemgrid.ItemComponentGenerator;
import org.vaadin.miki.itemgrid.ItemGrid;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;

/**
 * Unit tests for ItemGrid.
 * 
 * @author miki
 * 
 */
public class ItemGridTest {

  private Container container;

  private boolean eventFlag;

  private final Property.ValueChangeListener flagger = new Property.ValueChangeListener() {

    private static final long serialVersionUID = 20140509;

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
      ItemGridTest.this.eventFlag = true;
    }
  };

  private static final class OpenItemGrid extends ItemGrid {
    private static final long serialVersionUID = 20140511;

    @Override
    public void clickItemAtIndex(int index) {
      this.updateItemComponents(); // make sure components are created
      super.clickItemAtIndex(index);
    }
  }

  /**
   * Construct the grid with 27 items, ids from 0 to 26.
   */
  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    IndexedContainer container = new IndexedContainer();
    container.addContainerProperty("number", Integer.class, 21);
    container.addContainerProperty("text", String.class, "");

    for(int zmp1 = 0; zmp1 < 27; zmp1++) {
      Item item = container.addItem(zmp1);
      item.getItemProperty("number").setValue(zmp1 * 17);
      item.getItemProperty("text").setValue("item number " + (zmp1 + 1));
    }

    this.container = container;
    this.eventFlag = false;
  }

  /**
   * Default right-after-construction state.
   */
  @Test
  public void testWhenCreated() {
    ItemGrid grid = new ItemGrid();
    grid.setContainerDataSource(this.container);
    assertEquals(3, grid.getColumnCount());
    assertNull(grid.getValue());
    assertTrue(grid.isNullSelectionAllowed());
    assertFalse(grid.isMultiSelect());
    ItemComponentGenerator generator = grid.getItemComponentGenerator();
    assertNotNull(generator);
    assertTrue(generator instanceof DefaultItemComponentGenerator);
  }

  /**
   * State when constructed with passing number of columns.
   */
  @Test
  public void testColumnCountConstructorCreated() {
    ItemGrid grid = new ItemGrid(6);
    grid.setContainerDataSource(this.container);
    assertEquals(6, grid.getColumnCount());
    assertTrue(grid.isNullSelectionAllowed());
    assertNull(grid.getValue());
    assertFalse(grid.isMultiSelect());
    ItemComponentGenerator generator = grid.getItemComponentGenerator();
    assertNotNull(generator);
    assertTrue(generator instanceof DefaultItemComponentGenerator);
  }

  /**
   * Test changing number of columns.
   */
  @Test
  public void testSetColumnCount() {
    ItemGrid grid = new ItemGrid();
    grid.setContainerDataSource(this.container);
    ItemComponentGenerator generator = grid.getItemComponentGenerator();

    assertEquals(3, grid.getColumnCount());
    assertNull(grid.getValue());
    assertNotNull(generator);
    assertTrue(generator instanceof DefaultItemComponentGenerator);

    grid.setColumnCount(5);
    assertEquals(5, grid.getColumnCount());
    assertNull(grid.getValue());
    assertNotNull(generator);
    assertTrue(generator instanceof DefaultItemComponentGenerator);
  }

  /**
   * Test setting invalid number of columns.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testSetColumnCountError() {
    ItemGrid grid = new ItemGrid();
    grid.setContainerDataSource(this.container);
    grid.setColumnCount(0);
  }

  /**
   * Setting value fires events.
   */
  @Test
  public void testSetValue() {
    ItemGrid grid = new ItemGrid();
    grid.setContainerDataSource(this.container);
    grid.addValueChangeListener(this.flagger);
    grid.setValue(3);
    assertEquals(3, grid.getValue());
    assertTrue(this.eventFlag);
  }

  /**
   * Setting value even if not selectable works from code.
   */
  @Test
  public void testSetValueNotSelectable() {
    // is selectable works only for the ui!
    ItemGrid grid = new ItemGrid();
    grid.setSelectable(false);
    grid.setContainerDataSource(this.container);
    grid.addValueChangeListener(this.flagger);
    grid.setValue(3);
    assertEquals(3, grid.getValue());
    assertTrue(this.eventFlag);
  }

  /**
   * Setting invalid value does nothing.
   */
  @Test
  public void testSetInvalidValue() {
    ItemGrid grid = new ItemGrid();
    grid.setContainerDataSource(this.container);
    grid.addValueChangeListener(this.flagger);
    grid.setValue("nothing like this!");
    assertNull(grid.getValue());
    assertFalse(this.eventFlag);
  }

  /**
   * Selection changes.
   */
  @Test
  public void testSelectionChanges() {
    OpenItemGrid grid = new OpenItemGrid();
    grid.setContainerDataSource(this.container);
    grid.addValueChangeListener(this.flagger);
    // indices and ids are in this example the same
    grid.setValue(3);
    assertEquals(3, grid.getValue());
    assertTrue(this.eventFlag);
    this.eventFlag = false;
    grid.setValue(7);
    assertEquals(7, grid.getValue());
    assertTrue(this.eventFlag);
  }

  /**
   * Selecting the same item twice causes it to be deselected by default.
   */
  @Test
  public void testSelectingItemTwice() {
    OpenItemGrid grid = new OpenItemGrid();
    grid.setContainerDataSource(this.container);
    grid.addValueChangeListener(this.flagger);
    assertTrue(grid.isNullSelectionAllowed());
    assertNull(grid.getValue());
    // indices and ids are in this example the same
    grid.clickItemAtIndex(5);
    assertTrue(this.eventFlag);
    this.eventFlag = false;
    assertEquals(5, grid.getValue());
    grid.clickItemAtIndex(5);
    assertNull(grid.getValue());
    assertTrue(this.eventFlag);
  }

  /**
   * Selecting the same item twice when null selection is disallowed does nothing.
   */
  @Test
  public void testSelectingItemTwiceNullDisallowed() {
    OpenItemGrid grid = new OpenItemGrid();
    grid.setContainerDataSource(this.container);
    grid.addValueChangeListener(this.flagger);
    grid.setNullSelectionAllowed(false);
    assertFalse(grid.isNullSelectionAllowed());
    assertNull(grid.getValue());
    // indices and ids are in this example the same
    grid.clickItemAtIndex(5);
    assertEquals(5, grid.getValue());
    assertTrue(this.eventFlag);
    this.eventFlag = false;
    grid.clickItemAtIndex(5);
    assertEquals(5, grid.getValue());
    assertFalse(this.eventFlag);
  }

  /**
   * Test multiple selection through select(id).
   */
  @Test
  public void testMultiSelect() {
    OpenItemGrid grid = new OpenItemGrid();
    grid.setContainerDataSource(this.container);
    grid.addValueChangeListener(this.flagger);
    grid.setMultiSelect(true);
    grid.select(3);
    assertTrue(this.eventFlag);
    Object value = grid.getValue();
    assertTrue(value instanceof Collection);
    Collection<?> collection = (Collection<?>)value;
    assertEquals(1, collection.size());
    assertTrue(collection.contains(3));

    this.eventFlag = false;
    grid.select(5);
    value = grid.getValue();
    assertTrue(value instanceof Collection);
    collection = (Collection<?>)value;
    assertEquals(2, collection.size());
    assertTrue(collection.contains(3));
    assertTrue(collection.contains(5));
  }

  /**
   * Test multiple selection through clicks.
   */
  @Test
  public void testMultiSelectAtIndex() {
    OpenItemGrid grid = new OpenItemGrid();
    grid.setContainerDataSource(this.container);
    grid.addValueChangeListener(this.flagger);
    grid.setMultiSelect(true);
    grid.clickItemAtIndex(3);
    assertTrue(this.eventFlag);
    Object value = grid.getValue();
    assertTrue(value instanceof Collection);
    Collection<?> collection = (Collection<?>)value;
    assertEquals(1, collection.size());
    assertTrue(collection.contains(3));

    this.eventFlag = false;
    grid.clickItemAtIndex(5);
    value = grid.getValue();
    assertTrue(value instanceof Collection);
    collection = (Collection<?>)value;
    assertEquals(2, collection.size());
    assertTrue(collection.contains(3));
    assertTrue(collection.contains(5));
  }

  /**
   * Test multiple selection through select(id), then deselect already selected item.
   */
  @Test
  public void testMultiSelectDeselectElement() {
    OpenItemGrid grid = new OpenItemGrid();
    grid.setContainerDataSource(this.container);
    grid.addValueChangeListener(this.flagger);
    grid.setMultiSelect(true);
    grid.select(3);
    grid.select(6);
    grid.select(11);

    assertTrue(this.eventFlag);
    Object value = grid.getValue();
    assertTrue(value instanceof Collection);
    Collection<?> collection = (Collection<?>)value;
    assertEquals(3, collection.size());
    assertTrue(collection.contains(3));
    assertTrue(collection.contains(6));
    assertTrue(collection.contains(11));

    this.eventFlag = false;
    grid.clickItemAtIndex(6);
    value = grid.getValue();
    assertTrue(value instanceof Collection);
    collection = (Collection<?>)value;
    assertEquals(2, collection.size());
    assertTrue(collection.contains(3));
    assertTrue(collection.contains(11));
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
