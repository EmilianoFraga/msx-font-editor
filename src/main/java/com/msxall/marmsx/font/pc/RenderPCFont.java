/***************************************************************************
 *   Class RenderPCFont                                                    *
 *                                                                         *
 *   Copyright (C) 2018 by Marcelo Teixeira Silveira, D.Sc.                *
 *   MSX Font Editor: http://marmsx.msxall.com                             *
 *   Marcelo Teixeira Silveira is Computer Engineer,                       *
 *   graduated at Universidade do Estado do Rio de Janeiro (UERJ)          *
 *   Contact: flamar98@hotmail.com                                         *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 3 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/

/***************************************************************************
 * Class description:                                                      *
 * Designed for drawing PC characters on 8x8 MSX format                    *
 * MVC: Model                                                              *
 ***************************************************************************/
package com.msxall.marmsx.font.pc;

import java.awt.Canvas;
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class RenderPCFont {

	private String font_name;

	public RenderPCFont(String new_font) {
		font_name = new_font;
	}
	
	public BufferedImage renderPCCharacter(char chr) {
		int fcolor = 0xFFFFFF, bcolor = 0;
		int width, height;

		// Calculate draw area
		Canvas c = new Canvas(); // Only to get font metrics
		Font font = new Font(font_name, Font.PLAIN, 12);
		FontMetrics metrics = c.getFontMetrics(font);
		height = metrics.getHeight();
		width = height;

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(new Color(bcolor));
		g2d.fillRect(0, 0, width, height);
		g2d.setColor(new Color(fcolor));
		g2d.setFont(font);

		// Draw letter on image
		g2d.drawString(Character.toString(chr), 0, metrics.getAscent());

		// Clip to useful area
		Rectangle rect = getBoundingBox(img);
		img = createMSXCharacter(img, rect);

		return img;
	}

	private Rectangle getBoundingBox(BufferedImage img) {
		Rectangle rect = new Rectangle();
		int xi=img.getWidth(), yi=img.getHeight(), xf=0, yf=0;

		for (int y=0; y<img.getHeight(); y++) { 
			for (int x=0; x<img.getWidth(); x++) {
				if ((img.getRGB(x,y)&0xFFFFFF) > 0) {
					if (x<xi) xi=x;
					if (y<yi) yi=y;
					if (x>xf) xf=x;
					if (y>yf) yf=y;
				}
			}
		}

		// Real coordinates instead of width and height!
		rect.setLocation(xi, yi);
		rect.setSize(xf, yf);

		return rect;
	}

	private BufferedImage createMSXCharacter(BufferedImage img, Rectangle rect) {
		BufferedImage msx_char_img = new BufferedImage(8, 8, BufferedImage.TYPE_INT_RGB);
		double fx = (rect.getWidth()-rect.getX()+2.0)/7.0, fy = (rect.getHeight()-rect.getY()+2.0)/7.0;
		int sx, sy, width, height;

		if (rect.getWidth()-rect.getX()+1.0 <= 7)
			fx=1.0;

		if (rect.getHeight()-rect.getY()+1.0 <= 7)
			fy=1.0;

		for (int y=0; y<8; y++) { 
			for (int x=0; x<8; x++) {
				sx=(int) (rect.getX()+x*fx);
				sy=(int) (rect.getY()+y*fy);
				if (sx>=0 && sx<img.getWidth() && sy>=0 && sy<img.getHeight())
					msx_char_img.setRGB(x, y, img.getRGB(sx, sy));
			}
		}

		return msx_char_img;
	}

}
