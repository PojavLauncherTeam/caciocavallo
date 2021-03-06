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

var imgData;

/**
 * Decodes run-length encoded image data into an ImageData 
 * object and puts the image-data onto a canvas, which is
 * later used as "img" source when interpreting the command
 * buffer.
 * 
 * Because canvas-creation is very heavyweight, the canvas
 * is re-used if it is large enough.
 * 
 * ImageData is re-used if the cached instance has exactly
 * the same dimensions.
 */
function decodeRLEImageData() {
	var cmdLength = readShort(intArray, 0);
	var imgDataStartPos = 2 * (cmdLength + 1);
	
	if(intArray.length <= (imgDataStartPos + 1)) {
		return;
	}
	
	var w = readShort(intArray, imgDataStartPos);
	var h = readShort(intArray, imgDataStartPos + 2);

	if(!img || img.getAttribute('width') < w || img.getAttribute('height') < h) {
	   img = document.createElement('canvas');
	   img.setAttribute('width', w);
	   img.setAttribute('height', h);
    }

    var ctx = img.getContext('2d');
	if(!imgData || imgData.width != w || imgData.height != h) {
		imgData = ctx.createImageData(w, h);
	}
	var imgDataArray = imgData.data;
   
	rleDecodeLoop(intArray, imgDataArray, imgDataStartPos);
	
	ctx.putImageData(imgData, 0, 0);
}

/**
 * Actual decode loop, see RLEImageEncoder.java for an
 * in depth-description of the RLE format used.
 */
function rleDecodeLoop(intArray, imgDataArray, imgDataStartPos) {
	var runDataLength = readInt(intArray, imgDataStartPos + 4);
   	var runLengthDataOffset = imgDataStartPos + 8;
	var pixelDataOffset = runLengthDataOffset + runDataLength;
	
	var imgDataOffset = 0;
	var lastRed = 0, lastGreen = 0, lastBlue = 0;
    for(var i= 0; i < runDataLength; i++) {
		var cmd = intArray[runLengthDataOffset + i];
		var length = cmd & 127;
		
		if(cmd < 128) {
			//runs
			while(length-- > 0) {
				imgDataArray[imgDataOffset++] = lastRed;
				imgDataArray[imgDataOffset++] = lastGreen;
				imgDataArray[imgDataOffset++] = lastBlue;
				imgDataArray[imgDataOffset++] = 255;
			}
		}else {
			//no-runs
			while(length-- > 0) {
				imgDataArray[imgDataOffset++] = lastRed =  intArray[pixelDataOffset++];
				imgDataArray[imgDataOffset++] = lastGreen = intArray[pixelDataOffset++];
				imgDataArray[imgDataOffset++] = lastBlue = intArray[pixelDataOffset++];
				imgDataArray[imgDataOffset++] = 255;
			}
		}
	}
}
