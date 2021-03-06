/*
 * Copyright (c) 2011, Clemens Eisserer, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package net.java.openjdk.awt.peer.web;

import java.util.*;

/**
 * A ScreenUpdate represents a change of the WebSurfaceData, which is sent to
 * the browser.
 * 
 * @author Clemens Eisserer <linuxhippy@gmail.com>
 */
public abstract class ScreenUpdate {
    WebRect updateArea;

    /**
     * @param updateArea
     *            the area modified by the ScreenUpdate-Operation
     */
    public ScreenUpdate(WebRect updateArea) {
	this.updateArea = updateArea;
    }

    public WebRect getUpdateArea() {
	return updateArea;
    }

    public void setUpdateArea(WebRect updateArea) {
	this.updateArea = updateArea;
    }

    /**
     * @param pendingUpdates
     * @return a bounding box, which spans over all ScreenUpdates contained in
     *         the list.
     */
    public static WebRect getScreenUpdateBoundingBox(List<ScreenUpdate> pendingUpdates) {
	WebRect unionRectangle = null;

	for (ScreenUpdate update : pendingUpdates) {
	    WebRect updateArea = update.getSurfaceDataUpdateArea();

	    if (unionRectangle == null) {
		unionRectangle = updateArea;
	    } else {
		unionRectangle.union(updateArea);
	    }
	}

	return unionRectangle != null ? unionRectangle : new WebRect();
    }

    /**
     * @return The updated area of the SurfaceData. Will not be affected by
     *         evacuating.
     */
    public abstract WebRect getSurfaceDataUpdateArea();

    /**
     * Appends the coordinates and commands of the current ScreenUpdate to the
     * cmdList, which will later be serialized and sent to the browser.
     * 
     * @param cmdList
     */
    public abstract void writeToCmdStream(List<Integer> cmdList);
}
