/*****************************************************************************
 *                                                                           *
 * Lassie - Printable to print a chart on a single page in a multipage doc   *
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

class MultiPageChartPrintable implements Printable {

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

  private final Chart chart;
  private final String headerText;
  private final String footerText;

  public MultiPageChartPrintable(Chart chart, String header, String footer) {
    this.chart = chart;
    this.headerText = header;
    this.footerText = footer;
    this.headerFont = chart.getFont().deriveFont(Font.BOLD, HEADER_FONT_SIZE);
    this.footerFont = chart.getFont().deriveFont(Font.BOLD, FOOTER_FONT_SIZE);
  }

  public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {

    Graphics2D g2 = (Graphics2D) g;
    int imgX = (int) pageFormat.getImageableX();
    int imgY = (int) pageFormat.getImageableY();
    int imgWidth = (int) pageFormat.getImageableWidth();
    int imgHeight = (int) pageFormat.getImageableHeight();

    if (imgWidth <= 0) throw new PrinterException("Width of printable area is too small.");

    // to store the bounds of the headerText and footerText text
    Rectangle2D hRect = null;
    Rectangle2D fRect = null;

    // the amount of vertical space needed for the headerText and footerText text
    int headerTextSpace = 0;
    int footerTextSpace = 0;

    // the amount of vertical space available for printing the table
    int availableSpace = imgHeight;

    // if there's headerText text, find out how much space is needed for it
    // and subtract that from the available space
    if (headerText != null) {
      g2.setFont(headerFont);
      hRect = g2.getFontMetrics().getStringBounds(headerText, g2);
      headerTextSpace = (int) Math.ceil(hRect.getHeight());
      availableSpace -= headerTextSpace + H_F_SPACE;
    }

    // if there's footerText text, find out how much space is needed for it
    // and subtract that from the available space
    if (footerText != null) {
      g2.setFont(footerFont);
      fRect = g2.getFontMetrics().getStringBounds(footerText, g2);
      footerTextSpace = (int) Math.ceil(fRect.getHeight());
      availableSpace -= footerTextSpace + H_F_SPACE;
    }

    if (availableSpace <= 0) throw new PrinterException("Height of printable area is too small.");

    g2.translate(imgX, imgY);

    // to save and store the transform
    AffineTransform oldTrans;

    // if there's footer text, print it at the bottom of the imageable area
    if (this.footerText != null) {
      oldTrans = g2.getTransform();
      g2.translate(0, imgHeight - footerTextSpace);
      printText(g2,footerText,fRect,footerFont,imgWidth);
      g2.setTransform(oldTrans);
    }

    // if there's header text, print it at the top of the imageable area
    // and then translate downwards
    if (headerText != null) {
      printText(g2,headerText,hRect,headerFont,imgWidth);
      g2.translate(0, headerTextSpace + H_F_SPACE);
    }

    boolean xscale = chart.getXScale().isVisible();
    if (!xscale) chart.setXScaleVisible(true);          /* always show x-scale */
    Rectangle bounds = new Rectangle(0,0,imgWidth,availableSpace);
    chart.print(g2,bounds);
    if (!xscale) chart.setXScaleVisible(false);
    return Printable.PAGE_EXISTS;
  }

  private void printText(Graphics2D g2d, String text, Rectangle2D rect, Font font, int imgWidth) {

    int tx;

    // if the text is small enough to fit, center it
    if (rect.getWidth() < imgWidth) {
      tx = (int) ((imgWidth - rect.getWidth()) / 2);

      // otherwise, if the table is LTR, ensure the left side of
      // the text shows; the right can be clipped
    } else if (this.chart.getComponentOrientation().isLeftToRight()) {
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

}
