package org.skin.consultation.management.gui.component;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public final class RoundedJBorder extends AbstractBorder {
    int lineSize,cornerSize;
    Paint fill;
    Stroke stroke;

    public RoundedJBorder(Paint fill, int lineSize, int cornerSize) {
        this.fill = fill;
        this.lineSize = lineSize;
        this.cornerSize = cornerSize;
        stroke = new BasicStroke(lineSize);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        int size = Math.max(lineSize, cornerSize);
        if(insets == null) insets = new Insets(size, size, size, size);
        else insets.left = insets.top = insets.right = insets.bottom = size;
        return insets;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D)g;
        Paint oldPaint = g2d.getPaint();
        Stroke oldStroke = g2d.getStroke();
        Object oldAA = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        try {
            g2d.setPaint(fill!=null? fill: c.getForeground());
            g2d.setStroke(stroke);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int off = lineSize >> 1;
            g2d.drawRoundRect(x+off, y+off, width-lineSize,
                    height-lineSize, cornerSize, cornerSize);
        } finally {
            g2d.setPaint(oldPaint);
            g2d.setStroke(oldStroke);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAA);
        }
    }
}