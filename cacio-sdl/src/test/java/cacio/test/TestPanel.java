/*
 * Copyright 2009 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package cacio.test;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import sun.awt.AWTAccessor;

public class TestPanel {

    public static void main(String[] args) {
        Frame f = new Frame();
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        Panel p = new Panel();
        Button b = new Button("Test");
        p.setFont(new Font("Dialog", Font.ITALIC, 12));
        p.setForeground(Color.RED);
        p.setBackground(Color.GREEN);
        p.setEnabled(false);
        System.err.println("panel enabled: " + p.isEnabled());
        System.err.println("button enabled: " + b.isEnabled());
        b.setEnabled(true);
        System.err.println("panel enabled: " + p.isEnabled());
        System.err.println("button enabled: " + b.isEnabled());
        b.setEnabled(false);
        System.err.println("panel enabled: " + p.isEnabled());
        System.err.println("button enabled: " + b.isEnabled());
        p.setEnabled(true);
        System.err.println("panel enabled: " + p.isEnabled());
        System.err.println("button enabled: " + b.isEnabled());
        p.add(b);
        f.add(p);
        f.setSize(100, 100);
        f.setVisible(true);
        System.err.println("Frame props: " + f.getForeground() + ", " + f.getBackground() + ", " + f.getFont());
        System.err.println("Panel props: " + p.getForeground() + ", " + p.getBackground() + ", " + p.getFont());
        System.err.println("Button props: " + b.getForeground() + ", " + b.getBackground() + ", " + b.getFont());
        System.err.println("real Button props: " + AWTAccessor.getComponentAccessor().getForeground(b) + ", " + AWTAccessor.getComponentAccessor().getBackground(b) + ", " + AWTAccessor.getComponentAccessor().getFont(b));
    }
}
