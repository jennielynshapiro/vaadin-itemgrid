//! (c) 2014 Unforgiven.pl - licensed under Apache License, Version 2.0 (see the end of this file)
package org.vaadin.miki.itemgrid;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

/**
 * Workaround for an incorrect height of a cell that contains an empty string. Whenever value of this label is empty, it becomes a zero-width space instead.
 * 
 * @author miki
 * 
 */
// this is a temporary fix to #13 until a pure client-side solution can be found
public class NonEmptyLabel extends Label {

  private static final long serialVersionUID = 20140501;

  /**
   * Constructs a label.
   */
  public NonEmptyLabel() {
    super();
  }

  /**
   * Constructs a label with predefined value.
   * 
   * @param string
   *          Label's text.
   */
  public NonEmptyLabel(String string) {
    super(string);
  }

  /**
   * Constructs a label with given property data source.
   * 
   * @param source
   *          Source.
   */
  public NonEmptyLabel(Property<?> source) {
    super(source);
  }

  /**
   * Constructs a label with given text and content mode.
   * 
   * @param string
   *          Value of the label.
   * @param mode
   *          Content mode.
   */
  public NonEmptyLabel(String string, ContentMode mode) {
    super(string, mode);
  }

  /**
   * Constructs a label with given property data source and mode.
   * 
   * @param source
   *          Property to get the value from.
   * @param mode
   *          Content mode.
   */
  public NonEmptyLabel(Property<?> source, ContentMode mode) {
    super(source, mode);
  }

  @Override
  public void beforeClientResponse(boolean initial) {
    super.beforeClientResponse(initial);
    if(this.getState().text == null || this.getState().text.isEmpty()) this.getState().text = "\u200A"; // zero-width space
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
