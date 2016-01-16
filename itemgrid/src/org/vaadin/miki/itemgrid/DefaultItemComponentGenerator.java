//! (c) 2014 Unforgiven.pl - licensed under Apache License, Version 2.0 (see the end of this file)
package org.vaadin.miki.itemgrid;


import com.vaadin.data.Item;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Default, built-in item component generator. Displays item id and all properties.
 * 
 * @author miki
 * 
 */
public class DefaultItemComponentGenerator implements ItemComponentGenerator {
  private static final long serialVersionUID = 20140513L;

  @Override
  public Component getComponentForItem(Object itemId, Item item, boolean selected) {
    VerticalLayout layout = new VerticalLayout();
    layout.addComponent(new Label("<strong><em>" + itemId.toString() + "</em></strong>", ContentMode.HTML));
    for(Object propId: item.getItemPropertyIds()) {
      StringBuilder sb = new StringBuilder();
      sb.append("<em>" + propId.toString() + "</em> = ");
      Object value = item.getItemProperty(propId).getValue();
      if(value == null) sb.append("(null value)");
      else sb.append(value.toString());
      layout.addComponent(new Label(sb.toString(), ContentMode.HTML));
    }
    return layout;
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
