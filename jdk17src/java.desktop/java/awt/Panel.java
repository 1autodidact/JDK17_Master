/*
 * Copyright (c) 1995, 2021, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package java.awt;

import java.io.Serial;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;

/**
 * {@code Panel} is the simplest container class. A panel
 * provides space in which an application can attach any other
 * component, including other panels.
 * <p>
 * The default layout manager for a panel is the
 * {@code FlowLayout} layout manager.
 *
 * @author      Sami Shaio
 * @see     java.awt.FlowLayout
 * @since   1.0
 */
public class Panel extends Container implements Accessible {
    private static final String base = "panel";
    private static int nameCounter = 0;

    /**
     * Use serialVersionUID from JDK 1.1 for interoperability.
     */
     @Serial
     private static final long serialVersionUID = -2728009084054400034L;

    /**
     * Creates a new panel using the default layout manager.
     * The default layout manager for all panels is the
     * {@code FlowLayout} class.
     */
    public Panel() {
        this(new FlowLayout());
    }

    /**
     * Creates a new panel with the specified layout manager.
     * @param layout the layout manager for this panel.
     * @since 1.1
     */
    public Panel(LayoutManager layout) {
        setLayout(layout);
    }

    /**
     * Construct a name for this component.  Called by getName() when the
     * name is null.
     */
    String constructComponentName() {
        synchronized (Panel.class) {
            return base + nameCounter++;
        }
    }

    /**
     * Creates the Panel's peer.  The peer allows you to modify the
     * appearance of the panel without changing its functionality.
     */

    public void addNotify() {
        synchronized (getTreeLock()) {
            if (peer == null)
                peer = getComponentFactory().createPanel(this);
            super.addNotify();
        }
    }

/////////////////
// Accessibility support
////////////////

    /**
     * Gets the AccessibleContext associated with this Panel.
     * For panels, the AccessibleContext takes the form of an
     * AccessibleAWTPanel.
     * A new AccessibleAWTPanel instance is created if necessary.
     *
     * @return an AccessibleAWTPanel that serves as the
     *         AccessibleContext of this Panel
     * @since 1.3
     */
    public AccessibleContext getAccessibleContext() {
        if (accessibleContext == null) {
            accessibleContext = new AccessibleAWTPanel();
        }
        return accessibleContext;
    }

    /**
     * This class implements accessibility support for the
     * {@code Panel} class.  It provides an implementation of the
     * Java Accessibility API appropriate to panel user-interface elements.
     * @since 1.3
     */
    protected class AccessibleAWTPanel extends AccessibleAWTContainer {

        /**
         * Use serialVersionUID from JDK 1.3 for interoperability.
         */
        @Serial
        private static final long serialVersionUID = -6409552226660031050L;

        /**
         * Constructs an {@code AccessibleAWTPanel}.
         */
        protected AccessibleAWTPanel() {}

        /**
         * Get the role of this object.
         *
         * @return an instance of AccessibleRole describing the role of the
         * object
         */
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.PANEL;
        }
    }

}
