//! (c) 2014 Unforgiven.pl - licensed under Apache License, Version 2.0 (see the end of this file)
package org.vaadin.miki.widgets;

import java.util.Arrays;

import javax.servlet.annotation.WebServlet;

import org.vaadin.miki.itemgrid.ItemComponentGenerator;
import org.vaadin.miki.itemgrid.ItemGrid;
import org.vaadin.miki.itemgrid.NonEmptyLabel;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Slider;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * UI class that demoes ItemGrid.
 * 
 * @author miki
 * 
 */
@Theme("itemgrid")
public class ItemGridUI extends UI {

  private static final long serialVersionUID = 20140501;

  /**
   * Servlet definition.
   * 
   * @author miki
   * 
   */
  @WebServlet(value = "/*", asyncSupported = true)
  @VaadinServletConfiguration(productionMode = false, ui = ItemGridUI.class, widgetset = "org.vaadin.miki.itemgrid.ItemGridWidgetset")
  public static class Servlet extends VaadinServlet {
    private static final long serialVersionUID = 20140501;
  }

  private final IndexedContainer primary = new IndexedContainer();

  private final IndexedContainer secondary = new IndexedContainer();

  private final ItemComponentGenerator generator = new ItemComponentGenerator() {

    private static final long serialVersionUID = 20140513;

    @Override
    public Component getComponentForItem(Object itemId, Item item, boolean selected) {
      if(itemId.equals(4)) return null;
      else return new NonEmptyLabel("Component " + itemId.toString() + (selected ? " (selected)" : ""));
    }
  };

  @SuppressWarnings("unchecked")
  @Override
  protected void init(VaadinRequest request) {

    this.primary.addContainerProperty("text", String.class, "");
    this.primary.addContainerProperty("number", Integer.class, 0);
    this.primary.addContainerProperty("bool", Boolean.class, false);

    for(int zmp1 = 0; zmp1 < 15; zmp1++) {
      Item item = this.primary.addItem(zmp1);
      item.getItemProperty("text").setValue("number " + zmp1);
      item.getItemProperty("number").setValue(zmp1 * 3);
      item.getItemProperty("bool").setValue((zmp1 % 3) == 1);
    }

    this.secondary.addContainerProperty("caption", String.class, "default");
    this.secondary.addContainerProperty("price", Double.class, 1.99);

    for(int zmp1 = 5; zmp1 < 60; zmp1 += 3) {
      Item item = this.secondary.addItem(zmp1);
      item.getItemProperty("caption").setValue("this is item with id " + zmp1);
      item.getItemProperty("price").setValue(1.0 / zmp1);
    }

    final VerticalLayout layout = new VerticalLayout();
    layout.setMargin(true);
    this.setContent(layout);

    final ItemGrid grid = new ItemGrid();
    grid.setContainerDataSource(this.primary);

    grid.setItemComponentGenerator(this.generator);

    layout.addComponent(grid);

    Slider slider = new Slider(1, 20);
    slider.setCaption("Select number of columns:");
    slider.setValue(new Double(grid.getColumnCount()));
    slider.addValueChangeListener(new Property.ValueChangeListener() {

      private static final long serialVersionUID = 20140509;

      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        Object number = event.getProperty().getValue();
        if(number != null) grid.setColumnCount((int)Math.round((Double)number));
      }
    });
    slider.setSizeFull();
    layout.addComponent(slider);

    CheckBox selectable = new CheckBox("Selectable?", grid.isSelectable());
    selectable.addValueChangeListener(new Property.ValueChangeListener() {

      private static final long serialVersionUID = 20140510;

      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        grid.setSelectable((Boolean)event.getProperty().getValue());
      }
    });

    layout.addComponent(selectable);

    CheckBox nullSelection = new CheckBox("Null selection allowed?", grid.isNullSelectionAllowed());
    nullSelection.addValueChangeListener(new Property.ValueChangeListener() {

      private static final long serialVersionUID = 20140512;

      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        grid.setNullSelectionAllowed((Boolean)event.getProperty().getValue());
      }
    });
    layout.addComponent(nullSelection);

    CheckBox multiSelection = new CheckBox("Multiple selection allowed?", grid.isMultiSelect());
    multiSelection.addValueChangeListener(new Property.ValueChangeListener() {

      private static final long serialVersionUID = 20140512;

      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        grid.setMultiSelect((Boolean)event.getProperty().getValue());
      }
    });
    layout.addComponent(multiSelection);

    CheckBox defaultGenerator = new CheckBox("Use ItemGrid's default component generator?");
    defaultGenerator.addValueChangeListener(new Property.ValueChangeListener() {

      private static final long serialVersionUID = 20140513;

      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        if((Boolean)event.getProperty().getValue()) grid.setItemComponentGenerator(null);
        else grid.setItemComponentGenerator(ItemGridUI.this.generator);
      }
    });
    layout.addComponent(defaultGenerator);

    ListSelect container = new ListSelect("Container data source", Arrays.asList(this.primary, this.secondary));
    container.setNullSelectionAllowed(false);
    container.setItemCaption(this.primary, "Primary container");
    container.setItemCaption(this.secondary, "Backup container");
    container.select(this.primary);
    container.setImmediate(true);
    container.addValueChangeListener(new Property.ValueChangeListener() {

      private static final long serialVersionUID = 20140514;

      @Override
      public void valueChange(ValueChangeEvent event) {
        grid.setContainerDataSource((Container)event.getProperty().getValue());
      }
    });
    layout.addComponent(container);

    Button addItem = new Button("Add item to current container", new Button.ClickListener() {

      private static final long serialVersionUID = 20140515;

      @Override
      public void buttonClick(ClickEvent event) {
        grid.getContainerDataSource().addItem(grid.getContainerDataSource().size() + 0.5);
      }
    });
    layout.addComponent(addItem);

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
