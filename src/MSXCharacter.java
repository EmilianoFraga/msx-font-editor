/***************************************************************************
 *   Class MSXCharacter                                                    *
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
 * Designed for MSX character services                                     *
 * MVC: Model / Business Object (BO)                                       *
 ***************************************************************************/

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class MSXCharacter {

	private byte data[] = new byte[8];
	
	public MSXCharacterData getCharacter() {
		return new MSXCharacterData(data);
	}

	public void setCharacter(MSXCharacterData new_data) {
		data = new_data.getCharacter();
	}


	/****************************************************
	 * Render code                                      *
	*****************************************************/

	public BufferedImage renderCharacter(boolean isSelected, int fcolor, int bcolor) {
		if (isSelected) {
			fcolor = 0xFFFF00;
			bcolor = 0x0000FF;
		}
		
		BufferedImage img = new BufferedImage(8,8,BufferedImage.TYPE_INT_RGB);

		for (int i=0; i<8; i++) {
			for (int b=7; b>=0; b--) {
				if (((data[i] & (1 << b)) >> b) == 1)
					img.setRGB(7-b, i, fcolor);
				else
					img.setRGB(7-b, i, bcolor);
			}
		}

		return img;
	}

	public BufferedImage renderCharacterArea() {
		int fcolor = 0xFFFFFF, bcolor = 0, gcolor = 0xAAAAAA;
		int width=207, height=207, x, y;
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(new Color(bcolor));
		g2d.fillRect(0, 0, width, height);

		// Render character
		g2d.setColor(new Color(fcolor));
		for (int i=0; i<8; i++) {
			for (int b=7; b>=0; b--) {
				x = (7-b)*26;
				y = i*26;
				if (getPixelBit(data[i], b))
					g2d.fillRect(x, y, 25, 25);
			}			
		}

		// Grid
		g2d.setColor(new Color(gcolor));
		for (y=25; y<height; y+=26)
			g2d.drawLine(0, y, width-1, y);
		for (x=25; x<width; x+=26)
			g2d.drawLine(x, 0, x, height-1);


		g2d.dispose();

		return img;
	}


	/****************************************************
	 * Decode image to character                        *
	*****************************************************/

	public void setCharacterFromImage(BufferedImage img) {
		if ((img.getWidth() != 8) || (img.getHeight() != 8))
			return;
		
		for (int y=0; y<8; y++) {
			data[y] = 0;
			for (int x=0; x<8; x++) {
				if ((img.getRGB(x,y) & 0xFFFFFF) > 0x808080) {
					data[y] = (byte) (data[y] | (1 << (7-x)));
				}
			}
		}
	}


	/****************************************************
	 * Modifications on character                       *
	*****************************************************/

	public void bold() {
		for (int i=0; i<8; i++)
			data[i] = (byte) (data[i] | ((data[i] & 0xFF) >> 1));
	}

	public void italic() {
		for (int i=0; i<8; i++)
			data[i] = (byte) ((data[i] & 0xFF) >> (3-(i/2)));
	}

	public void inverse() {
		for (int i=0; i<8; i++)
			data[i] = (byte) ~data[i];
	}

	public void clear() {
		for (int i=0; i<8; i++)
			data[i] = 0;
	}

	public void flipud() {
		byte tmp;
		for (int i=0; i<4; i++) {
			tmp = data[i];
			data[i] = data[7-i];
			data[7-i] = tmp;
		}
	}

	public void mirror() {
		byte tmp;
		for (int i=0; i<8; i++)
			data[i] = bitReverse(data[i]);
	}

	// Bit Reverse adapted from Stackoverflow
	private byte bitReverse(byte b) {
		b = (byte) ((b & 0xF0) >> 4 | (b & 0x0F) << 4);
		b = (byte) ((b & 0xCC) >> 2 | (b & 0x33) << 2);
		b = (byte) ((b & 0xAA) >> 1 | (b & 0x55) << 1);
		return b;
	}

	public void rotate() {
		byte new_data[] = new byte[8];
		for (int i=0; i<8; i++) {
			for (int b=7; b>=0; b--) {
				if (getPixelBit(data[i], b))
					new_data[7-b] = (byte) (new_data[7-b] | (1 << i));
			}
		}
		data = new_data;
	}

	public void shiftLeft(boolean rotate) {
		int b;
		for (int i=0; i<8; i++) {
			b = rotate ? ((data[i] & 0x80) >> 7) : 0;
			data[i] = (byte) ((data[i] << 1) + b);
		}
	}

	public void shiftRight(boolean rotate) {
		int b;
		for (int i=0; i<8; i++) {
			b = rotate ? ((data[i] & 1) << 7) : 0;
			data[i] = (byte) (((data[i] & 0xFF) >> 1) + b);
		}
	}

	public void shiftUp(boolean rotate) {
		byte b = data[0];
		for (int i=0; i<7; i++)
			data[i] = data[i+1];
		data[7] = rotate ? b : 0;
	}

	public void shiftDown(boolean rotate) {
		byte b = data[7];
		for (int i=7; i>=1; i--)
			data[i] = data[i-1];
		data[0] = rotate ? b : 0;
	}


	/****************************************************
	 * Misc                                             *
	*****************************************************/

	public void pixelClicked(int x, int y, int mode) {
		// Convert from screen coordinates to block coordinates
		x = x/26;
		y = y/26;

		if (x<0 || x>7 || y<0 || y>7)
			return;

		switch (mode) {
			case 1 : setPixel(x,y,true); break;
			case 2 : setPixel(x,y,false); break;
			default	: inversePixel(x,y);
		}
	}

	private void inversePixel(int x, int y) {
		setPixel(x, y, !getPixelBit(data[y], 7-x));
	}

	private void setPixel(int x, int y, boolean bit) {
		int mask = 1 << (7-x);

		if (bit)
			data[y] = (byte) (data[y] | mask);
		else {
			mask = ~mask;
			data[y] = (byte) (data[y] & mask);
		}
	}

	private boolean getPixelBit(byte pixel, int b) {
		return  ((pixel & (1 << b)) >> b) == 1;
	}


	/****************************************************
	 * Debug code                                       *
	*****************************************************/

	public void printCharacter() {
		for (int i=0; i<8; i++)
			printCharacterLine(data[i]);
	}

	private void printCharacterLine(byte chr) {
		for (int i=7; i>=0; i--) {
			if (getPixelBit(chr, i))
				System.out.print("1");
			else
				System.out.print("0");
		}
		System.out.println();
	}
}
