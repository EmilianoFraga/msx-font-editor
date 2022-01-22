/***************************************************************************
 *   Class Mosaic                                                          *
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
 * Mosaic services                                                         *
 * MVC: Model                                                              *
 ***************************************************************************/

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import java.lang.Math;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.ArrayList;

public class Mosaic {

	private BufferedImage msx_img;
	private int scr=0;

	public void setScreen(int new_scr) {
		if (new_scr != 1)
			new_scr=0;
		scr = new_scr;
	}

	public int getImgWidth() {
		if (msx_img == null)
			return 0;
		return msx_img.getWidth();
	}

	public int getImgHeight() {
		if (msx_img == null)
			return 0;
		return msx_img.getHeight();
	}

	public int getImgBlocksX() {
		double width_block=(scr==0)?6.0:8.0;

		return (int) Math.ceil(getImgWidth() / width_block);
	}

	public int getImgBlocksY() {
		return (int) Math.ceil(getImgHeight() / 8.0);
	}

	public int getImgBlocksLength() {
		return getImgBlocksX()*getImgBlocksY();
	}


	/************************************************
	 * Create mosaic                                *
	 ************************************************/

	public ArrayList<MSXCharacterData> createMosaic() {
		ArrayList<MSXCharacterData> list = new ArrayList<MSXCharacterData>();
		MSXCharacter chr = new MSXCharacter();;
		int bx = (scr==0)?6:8;
		int width = getImgBlocksX() * bx;
		int height = getImgBlocksY() * 8;

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = img.createGraphics();

		g2d.setColor(new Color(255,255,255));
		g2d.fillRect(0,0,512,384);

		if (msx_img != null)
			g2d.drawImage(msx_img, 0, 0, msx_img.getWidth(), msx_img.getHeight(), null);

		for (int y=0; y<height; y+=8) {
			for (int x=0; x<width; x+=bx) {
				chr.setCharacterFromImage(clipImage8x8(img, x, y));
				chr.inverse();
				list.add(chr.getCharacter());
			}
		}

		return list;
	}

	private BufferedImage clipImage8x8(BufferedImage img, int x, int y) {
		BufferedImage clip_img = new BufferedImage(8, 8, BufferedImage.TYPE_INT_RGB);
		int bx = (scr==0)?6:8;

		for (int yy=0; yy<8; yy++) {
			for (int xx=0; xx<8;xx++)
				clip_img.setRGB(xx, yy, (xx<bx)?img.getRGB(x+xx, y+yy):0xFFFFFF);
		}

		return clip_img;
	}


	/************************************************
	 * Render display image                         *
	 ************************************************/

	public BufferedImage renderImage() {
		int xi=(scr==0)?18:0;
		int dx=(scr==0)?12:16, xf=(scr==0)?499:512;

		BufferedImage img = new BufferedImage(512, 384, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = img.createGraphics();

		g2d.setColor(new Color(255,255,255));
		g2d.fillRect(0,0,512,384);

		if (msx_img != null)
			g2d.drawImage(msx_img, xi, 0, msx_img.getWidth()*2, msx_img.getHeight()*2, null);

		// Grid
		g2d.setColor(new Color(192,192,255));
		for (int y=0; y<385; y+=16) {
			g2d.drawLine(xi, y, xf-1, y);
		for (int x=xi; x<xf; x+=dx)
			g2d.drawLine(x, 0, x, 383);
		}

		// Image border
		if (msx_img != null) {
			g2d.setColor(new Color(255,0,0));
			g2d.drawRect(xi, 0, msx_img.getWidth()*2, msx_img.getHeight()*2);
		}

		return img;
	}


		
	/************************************************
	 * I/O operations                               *
	 ************************************************/

	// Error codes:
	// -2 Invalid type -1: bad image to MSX, 0: load error, 1: success
	public int loadImage(String filename) {
		BufferedImage img;

		try {
			img = ImageIO.read(new File(filename));
		}
		catch(IOException e) {
			return 0;
		}

		if (img == null)
			return -2;

		if (img.getWidth() > 256 || img.getHeight() > 192)
			return -1;

		msx_img = img;

		return 1;
	}

	public boolean saveBasicFile(String filename, int file_type, int ascii) {
		if (file_type==0)
			return saveBasicFileFont(filename, ascii);

		return saveBasicFileCodes(filename, ascii);
	}


	private boolean saveBasicFileFont(String filename, int ascii) {
		Formatter output;

		try {
			output = new Formatter(filename);
		} catch (Exception e) {
			return false;
		}

		// Write program
		try {
			output.format("5 COLOR 1,15,15%c%c", 13, 10);
			output.format("10 SCREEN %d:WIDTH %d:X=0:Y=0:KEYOFF%c%c",scr, (scr==0)?40:32, 13, 10);
			output.format("20 BLOAD\"FONT.ALF\",S,%s%c%c", (scr==0)?"&H7600":"&H6E00", 13, 10);
			output.format("30 FOR I=%d TO %d%c%c", ascii, ascii+getImgBlocksLength()-1, 13, 10);
			output.format("40 VPOKE %sX+Y*%d,I%c%c", (scr==0)?"":"6144+", (scr==0)?40:32, 13, 10);
			output.format("50 X=X+1 : IF X=%d THEN Y=Y+1 : X=0%c%c", getImgBlocksX(), 13, 10);
			output.format("60 NEXT I%c%c", 13, 10);
			output.format("70 LOCATE 0,22");

		} catch (FormatterClosedException e) {
			return false;
		}

		output.close();

		return true;
	} 

	private boolean saveBasicFileCodes(String filename, int ascii) {
		Formatter output;
		int line=110;
		byte chr_array[];

		try {
			output = new Formatter(filename);
		} catch (Exception e) {
			return false;
		}

		// Create mosaic
		ArrayList<MSXCharacterData> list = createMosaic();

		// Write program
		try {
			output.format("5 COLOR 1,15,15%c%c", 13, 10);
			output.format("10 SCREEN %d:WIDTH %d:X=0:Y=0:KEYOFF%c%c",scr, (scr==0)?40:32, 13, 10);
			output.format("20 FOR I=%d*8 TO %d*8+7%c%c", ascii, ascii+getImgBlocksLength()-1, 13, 10);
			output.format("30 READ A%c%c", 13, 10);
			output.format("40 VPOKE %s+I, A%c%c", (scr==0)?"&H800":"0", 13, 10);
			output.format("50 NEXT I%c%c", 13, 10);
			output.format("60 FOR I=%d TO %d%c%c", ascii, ascii+getImgBlocksLength()-1, 13, 10);
			output.format("70 VPOKE %sX+Y*%d,I%c%c", (scr==0)?"":"6144+", (scr==0)?40:32, 13, 10);
			output.format("80 X=X+1 : IF X=%d THEN Y=Y+1 : X=0%c%c", getImgBlocksX(), 13, 10);
			output.format("90 NEXT I%c%c", 13, 10);
			output.format("100 LOCATE 0,22%c%c", 13, 10);

			for (int i=0; i<getImgBlocksLength(); i++) {
				chr_array = list.get(i).getCharacter();
				output.format(line + " DATA ");
				for (int p=0; p<7; p++)
					output.format("%d,", chr_array[p] & 0xFF);
				output.format("%d%c%c", chr_array[7] & 0xFF, 13, 10);
				line+=10;
			}
		} catch (FormatterClosedException e) {
			return false;
		}

		output.close();

		return true;
	} 

}
