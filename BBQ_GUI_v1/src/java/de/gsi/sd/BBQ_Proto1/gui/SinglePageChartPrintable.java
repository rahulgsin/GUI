/*****************************************************************************
 *                                                                           *
 * Lassie - Printable to print multiple charts on a single page              *
 *                                                                           *
 * modified: 2009-08-13 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/


package de.gsi.sd.BBQ_Proto1.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import cern.jdve.Chart;

class SinglePageChartPrintable implements Printable {

  /** Vertical space to leave between table and headerText/footerText text. */
  private static final int H_F_SPACE = 8;
  /** Font size for the headerText text. */
  private static final float HEADER_FONT_SIZE = 14.0f;
  /** Font size for the footerText text. */
  private static final float FOOTER_FONT_SIZE = 10.0f;
  /** The font to use in rendering headerText text. */
  private final Font headerFont;
  /** The font to use in rendering footerText text. */
  private final Font footerFont;
  /** The font to use in rendering labels. */
  private final Font labelFont;
  /** Array of charts to print */
  private final Chart[] charts;
  /** Array of associated labels */
  private final String[] labels;

  private final String headerText;
  private final String footerText;

  public SinglePageChartPrintable(Chart[] charts, String[] labels, String header, String footer) {
    this.charts = charts;
    this.labels = labels;
    this.headerText = header;
    this.footerText = footer;
    this.headerFont = charts[0].getFont().deriveFont(Font.BOLD, HEADER_FONT_SIZE);
    this.footerFont = charts[0].getFont().deriveFont(Font.BOLD, FOOTER_FONT_SIZE);
    this.labelFont = charts[0].getFont().deriveFont(Font.BOLD, FOOTER_FONT_SIZE);
  }

  public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException 
  {
    Graphics2D g2 = (Graphics2D)g;
    int imgX = (int) pageFormat.getImageableX();
    int imgY = (int) pageFormat.getImageableY();
    int imgWidth = (int) pageFormat.getImageableWidth();
    int imgHeight = (int) pageFormat.getImageableHeight();

    // to store the bounds of the headerText and footerText text
    Rectangle2D hRect = null;
    Rectangle2D fRect = null;

    /* get number of charts to display */
    int nCharts = 0;
    for (int i=0;i<charts.length;i++)
    {
      if (charts[i].isVisible()) nCharts++;
    }

    /* calculate the amount of space available for printing the charts */
    int verticalSpace = imgHeight;
    int horizontalSpace = imgWidth;
    int headerTextSpace = 0;
    int footerTextSpace = 0;
    if (headerText != null) {
      g2.setFont(headerFont);
      hRect = g2.getFontMetrics().getStringBounds(headerText, g2);
      headerTextSpace = (int) Math.ceil(hRect.getHeight());
      verticalSpace -= headerTextSpace + H_F_SPACE;
    }
    if (footerText != null) {
      g2.setFont(footerFont);
      fRect = g2.getFontMetrics().getStringBounds(footerText, g2);
      footerTextSpace = (int) Math.ceil(fRect.getHeight());
      verticalSpace -= footerTextSpace + H_F_SPACE;
    }
    int labelWidth = 0;
    g2.setFont(labelFont);
    for (int i=0;i<nCharts;i++)
    {
      Rectangle2D r = g2.getFontMetrics().getStringBounds(labels[i], g2);
      if (r.getWidth() > labelWidth) labelWidth = (int) Math.ceil(r.getWidth());
    }
    horizontalSpace -= labelWidth;
    if (horizontalSpace <= 0) throw new PrinterException("Width of printable area is too small.");
    if (verticalSpace <= 0) throw new PrinterException("Height of printable area is too small.");

    g2.translate(imgX, imgY);

    // to save and store the transform
    AffineTransform oldTrans;

    /* print footer text if available */
    if (this.footerText != null) {
      oldTrans = g2.getTransform();
      g2.translate(0, imgHeight - footerTextSpace);
      printText(g2,footerText,fRect,footerFont,imgWidth);
      g2.setTransform(oldTrans);
    }

    /* print footer text if available */
    if (headerText != null) {
      printText(g2,headerText,hRect,headerFont,imgWidth);
      g2.translate(0, headerTextSpace + H_F_SPACE);
    }

    if (nCharts == 0) return Printable.PAGE_EXISTS;

    int h = verticalSpace / nCharts;
    for (int i=0;i<nCharts;i++)
    {
      oldTrans = g2.getTransform();
      g2.setFont(labelFont);
      printText(g2,labels[i],labelWidth);
      g2.translate(labelWidth,0);
      Rectangle bounds = new Rectangle(0,0,horizontalSpace,h);
      charts[i].print(g2,bounds);
      g2.setTransform(oldTrans);
      g2.translate(0,h);
    }
    return Printable.PAGE_EXISTS;
  }

  
  private void printText(Graphics2D g2d, String text, Rectangle2D rect, Font font, int imgWidth) {

    int tx;

    // if the text is small enough to fit, center it
    if (rect.getWidth() < imgWidth) {
      tx = (int) ((imgWidth - rect.getWidth()) / 2);

      // otherwise, if the table is LTR, ensure the left side of
      // the text shows; the right can be clipped
    } else if (charts[0].getComponentOrientation().isLeftToRight()) {
      tx = 0;

      // otherwise, ensure the right side of the text shows
    } else {
      tx = -(int) (Math.ceil(rect.getWidth()) - imgWidth);
    }

    int ty = (int) Math.ceil(Math.abs(rect.getY()));
    g2d.setColor(Color.BLACK);
    g2d.setFont(font);
    g2d.drawString(text, tx, ty);
  }

  private void printText(Graphics2D g2d, String text, int width) {

    int tx = 0;
    Rectangle2D r = g2d.getFontMetrics().getStringBounds(text,g2d);
    if (!charts[0].getComponentOrientation().isLeftToRight()) 
    {
      tx = -(int)(Math.ceil(r.getWidth()) - width);
    }
    int ty = (int) Math.ceil(Math.abs(r.getY()));
    g2d.setColor(Color.BLACK);
    g2d.drawString(text,tx,ty);
  }

}

