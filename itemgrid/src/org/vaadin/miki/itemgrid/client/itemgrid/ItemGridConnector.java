//! (c) 2014 Unforgiven.pl - licensed under Apache License, Version 2.0 (see the end of this file)
package org.vaadin.miki.itemgrid.client.itemgrid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.vaadin.miki.itemgrid.ItemGrid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent.ConnectorHierarchyChangeHandler;
import com.vaadin.client.HasComponentsConnector;
import com.vaadin.client.Util;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractFieldConnector;
import com.vaadin.shared.ui.Connect;

/**
 * Connector for ItemGrid.
 * 
 * @author miki
 * 
 */
@Connect(ItemGrid.class)
public class ItemGridConnector extends AbstractFieldConnector implements HasComponentsConnector {

  private static final long serialVersionUID = 20140501;

  ItemGridServerRpc rpc = RpcProxy.create(ItemGridServerRpc.class, this);

  private final HashMap<ComponentConnector, Integer> widgetItemIds = new HashMap<ComponentConnector, Integer>();

  private boolean selectable = true;

  /**
   * Constructs the widget with all the default settings.
   */
  public ItemGridConnector() {
    this.registerRpc(ItemGridClientRpc.class, new ItemGridClientRpc() {
      private static final long serialVersionUID = 20140501;

      @Override
      public void setSelectable(boolean state) {
        ItemGridConnector.this.selectable = state;
      }

    });

    this.getWidget().addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        // only do things when in selectable mode
        if(ItemGridConnector.this.selectable) {
          Element element = ((Element)event.getNativeEvent().getEventTarget().cast());
          ComponentConnector connector = Util.getConnectorForElement(ItemGridConnector.this.getConnection(), ItemGridConnector.this.getWidget(), element);
          while(!ItemGridConnector.this.widgetItemIds.containsKey(connector) && connector.getParent() != null
              && connector.getParent() instanceof ComponentConnector)
            connector = (ComponentConnector)connector.getParent();
          Integer index = ItemGridConnector.this.widgetItemIds.get(connector);
          ItemGridConnector.this.rpc.clickItemAtIndex(index == null ? -1 : index.intValue());
        }
      }
    });

  }

  @Override
  protected Widget createWidget() {
    return GWT.create(ItemGridWidget.class);
  }

  @Override
  public ItemGridWidget getWidget() {
    return (ItemGridWidget)super.getWidget();
  }

  @Override
  public ItemGridState getState() {
    return (ItemGridState)super.getState();
  }

  private void repaintGrid() {
    List<ComponentConnector> children = this.getChildComponents();
    int itemCount = children == null ? 0 : children.size();
    int rows = itemCount == 0 ? 0 : ((itemCount - 1) / this.getState().columns) + 1;
    this.getWidget().clear(true);
    this.getWidget().resize(rows, this.getState().columns);
    for(int zmp1 = 0; zmp1 < itemCount; zmp1++) {
      int column = zmp1 % this.getState().columns;
      int row = zmp1 / this.getState().columns;
      ComponentConnector connector = children.get(zmp1);
      Widget widget = connector.getWidget();
      this.getWidget().setWidget(row, column, widget);
      if(this.getState().selected.contains(zmp1)) widget.addStyleName("m-itemgrid-selected");
      else widget.removeStyleName("m-itemgrid-selected");
    }
  }

  @Override
  public void onStateChanged(StateChangeEvent stateChangeEvent) {
    super.onStateChanged(stateChangeEvent);
    this.repaintGrid();
  }

  @Override
  public void updateCaption(ComponentConnector connector) {
    ; // nothing
  }

  @Override
  public List<ComponentConnector> getChildComponents() {
    return new ArrayList<ComponentConnector>(this.widgetItemIds.keySet());
  }

  @Override
  public void setChildComponents(List<ComponentConnector> children) {
    this.widgetItemIds.clear();
    for(int zmp1 = 0; zmp1 < children.size(); zmp1++) {
      ComponentConnector connector = children.get(zmp1);
      Widget widget = connector.getWidget();
      // #12 this should in theory solve it, but does so only after something happens with the component
      if(widget.getElement() != null && (widget.getElement().getInnerText() == null || widget.getElement().getInnerText().isEmpty())) {
        widget.getElement().setInnerText("\u200A");
      }
      widget.addStyleName("m-itemgrid-item");
      this.widgetItemIds.put(connector, zmp1);
      this.repaintGrid();
    }
  }

  @Override
  public HandlerRegistration addConnectorHierarchyChangeHandler(ConnectorHierarchyChangeHandler handler) {
    return null;
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
