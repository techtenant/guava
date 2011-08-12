/*
 * Copyright (C) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.collect.testing.testers;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.NoSuchElementException;

/**
 * A generic JUnit test which tests operations on a NavigableSet. Can't be
 * invoked directly; please see {@code SetTestSuiteBuilder}.
 *
 * @author Jesse Wilson
 * @author Louis Wasserman
 */
public class SetNavigationTester<E> extends AbstractSetTester<E> {

  private NavigableSet<E> navigableSet;
  private List<E> values;
  private E a;
  private E b;
  private E c;

  @Override public void setUp() throws Exception {
    super.setUp();
    navigableSet = (NavigableSet<E>) getSet();
    values = Helpers.copyToList(getSubjectGenerator().getSampleElements(
        getSubjectGenerator().getCollectionSize().getNumElements()));
    Collections.sort(values, navigableSet.comparator());

    // some tests assume SEVERAL == 3
    if (values.size() >= 1) {
      a = values.get(0);
      if (values.size() >= 3) {
        b = values.get(1);
        c = values.get(2);
      }
    }
  }
  
  /**
   * Resets the contents of navigableSet to have elements a, c, for the
   * navigation tests.
   */
  protected void resetWithHole() {
    super.resetContainer(getSubjectGenerator().create(a, c));
    navigableSet = (NavigableSet<E>) getSet();
  }

  @CollectionSize.Require(ZERO)
  public void testEmptySetFirst() {
    try {
      navigableSet.first();
      fail();
    } catch (NoSuchElementException e) {
    }
  }

  @CollectionFeature.Require(SUPPORTS_REMOVE)
  @CollectionSize.Require(ZERO)
  public void testEmptySetPollFirst() {
    assertNull(navigableSet.pollFirst());
  }

  @CollectionSize.Require(ZERO)
  public void testEmptySetNearby() {
    assertNull(navigableSet.lower(samples.e0));
    assertNull(navigableSet.floor(samples.e0));
    assertNull(navigableSet.ceiling(samples.e0));
    assertNull(navigableSet.higher(samples.e0));
  }

  @CollectionSize.Require(ZERO)
  public void testEmptySetLast() {
    try {
      navigableSet.last();
      fail();
    } catch (NoSuchElementException e) {
    }
  }

  @CollectionFeature.Require(SUPPORTS_REMOVE)
  @CollectionSize.Require(ZERO)
  public void testEmptySetPollLast() {
    assertNull(navigableSet.pollLast());
  }

  @CollectionSize.Require(ONE)
  public void testSingletonSetFirst() {
    assertEquals(a, navigableSet.first());
  }

  @CollectionFeature.Require(SUPPORTS_REMOVE)
  @CollectionSize.Require(ONE)
  public void testSingletonSetPollFirst() {
    assertEquals(a, navigableSet.pollFirst());
    assertTrue(navigableSet.isEmpty());
  }

  @CollectionSize.Require(ONE)
  public void testSingletonSetNearby() {
    assertNull(navigableSet.lower(samples.e0));
    assertEquals(a, navigableSet.floor(samples.e0));
    assertEquals(a, navigableSet.ceiling(samples.e0));
    assertNull(navigableSet.higher(samples.e0));
  }

  @CollectionSize.Require(ONE)
  public void testSingletonSetLast() {
    assertEquals(a, navigableSet.last());
  }

  @CollectionFeature.Require(SUPPORTS_REMOVE)
  @CollectionSize.Require(ONE)
  public void testSingletonSetPollLast() {
    assertEquals(a, navigableSet.pollLast());
    assertTrue(navigableSet.isEmpty());
  }

  @CollectionSize.Require(SEVERAL)
  public void testFirst() {
    assertEquals(a, navigableSet.first());
  }

  @CollectionFeature.Require(SUPPORTS_REMOVE)
  @CollectionSize.Require(SEVERAL)
  public void testPollFirst() {
    assertEquals(a, navigableSet.pollFirst());
    assertEquals(
        values.subList(1, values.size()), Helpers.copyToList(navigableSet));
  }

  @CollectionFeature.Require(absent = SUPPORTS_REMOVE)
  public void testPollFirstUnsupported() {
    try {
      navigableSet.pollFirst();
      fail();
    } catch (UnsupportedOperationException e) {
    }
  }

  @CollectionSize.Require(SEVERAL)
  public void testLower() {
    resetWithHole();
    assertEquals(null, navigableSet.lower(a));
    assertEquals(a, navigableSet.lower(b));
    assertEquals(a, navigableSet.lower(c));
  }
  @CollectionSize.Require(SEVERAL)
  public void testFloor() {
    resetWithHole();
    assertEquals(a, navigableSet.floor(a));
    assertEquals(a, navigableSet.floor(b));
    assertEquals(c, navigableSet.floor(c));
  }

  @CollectionSize.Require(SEVERAL)
  public void testCeiling() {
    resetWithHole();
    assertEquals(a, navigableSet.ceiling(a));
    assertEquals(c, navigableSet.ceiling(b));
    assertEquals(c, navigableSet.ceiling(c));
  }

  @CollectionSize.Require(SEVERAL)
  public void testHigher() {
    resetWithHole();
    assertEquals(c, navigableSet.higher(a));
    assertEquals(c, navigableSet.higher(b));
    assertEquals(null, navigableSet.higher(c));
  }

  @CollectionSize.Require(SEVERAL)
  public void testLast() {
    assertEquals(c, navigableSet.last());
  }

  @CollectionFeature.Require(SUPPORTS_REMOVE)
  @CollectionSize.Require(SEVERAL)
  public void testPollLast() {
    assertEquals(c, navigableSet.pollLast());
    assertEquals(
        values.subList(0, values.size() - 1), Helpers.copyToList(navigableSet));
  }

  @CollectionFeature.Require(absent = SUPPORTS_REMOVE)
  public void testPollLastUnsupported() {
    try {
      navigableSet.pollLast();
      fail();
    } catch (UnsupportedOperationException e) {
    }
  }

  @CollectionSize.Require(SEVERAL)
  public void testDescendingNavigation() {
    List<E> descending = new ArrayList<E>();
    for (Iterator<E> i = navigableSet.descendingIterator(); i.hasNext();) {
      descending.add(i.next());
    }
    Collections.reverse(descending);
    assertEquals(values, descending);
  }
}
