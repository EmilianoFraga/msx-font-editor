/***************************************************************************
 *   Class MSXFont                                                         *
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
 * Designed for MSX font services                                          *
 * MVC: Model / Business Object (BO) + Data Access Object (DAO)            *
 ***************************************************************************/

import java.util.Arrays;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Font;

public class MSXFont {

	private byte font_data[] = new byte[2048];
	private byte font_color[];
	private int selected_char = -1;
	private int last_selected_char =-1;
	private boolean show_font_reference = false;
	private int pivot;
	private int msx_palette[] = { 0x0, 0x0, 0x24DB24, 0x6DFF6D, 0x2424FF, 0x496DFF, 0xB62424,
                                      0x49DBFF, 0xFF2424, 0xFF6D6D, 0xDBDB24, 0xDBDB92, 0x249224,
                                      0xDB49B6, 0xB6B6B6, 0xFFFFFF };

	public MSXFont() {
		resetColors();
	}

	private void resetColors() {
		MSXFontData font = new MSXFontData();
		font_color = font.getColors();
	}

	/****************************************************
	 * Misc                                             *
	*****************************************************/

	// Return single selection
	// -1 - no character selected
	// -2 - multiple character selected
	// 0 < n < 255 - single character ascii code
	public int getSelectedChar() {
		if (last_selected_char != -1)
			return -2;

		return selected_char;
	}

	// Return multiple selection
	public int[] getSelectedChars() {
		int sel_chars[] = new int[2];

		sel_chars[0] = selected_char;
		sel_chars[1] = last_selected_char;

		return sel_chars;
	}

	public void resetSelectedChar() {
		selected_char=-1;
	}

	public int getCharacterAtPos(int x, int y) {
		return (x/33) + 16*(y/33);
	}

	public int setSelectedChar(int x, int y, boolean multiple, boolean deselect) {
		int ascii = getCharacterAtPos(x,y);
		setSelectedChar(ascii, multiple, deselect);

		return getSelectedChar();
	}

	// Put selection in ascending order
	private void checkSelectedChars() {
		if (last_selected_char == -1)
			return;

		int aux;
		if (last_selected_char < selected_char) {
			aux = last_selected_char;
			last_selected_char = selected_char;
			selected_char = aux;
		}
	}

	// Now select first and last points (this, if multiple)
	// Pivot is used to keep the first selected point
	private void setSelectedChar(int value, boolean multiple, boolean deselect) {
		if (value < 0 || value > 255) {
			return;
		}

		// If multiple and the same place, return
		if ((value == selected_char) && (multiple))
			return;

		// Deselect all if number is the same (only for single selection)
		if ((value == selected_char) && (last_selected_char == -1) && deselect) {
			selected_char=-1;
			return;
		}

		if (!multiple) {
			selected_char=value;
			pivot=value;
			last_selected_char = -1;
		}
		else {
			if (selected_char == -1)
				return;
			selected_char=pivot;
			last_selected_char=value;
		}

		checkSelectedChars();
	}

	public MSXFontData getFont() {
		if (font_color == null)
			return new MSXFontData(font_data);
		return new MSXFontData(font_data, font_color);
	}

	public void setFont(MSXFontData new_font) {
		font_data = new_font.getFont();
		font_color = new_font.getColors();
	}

	public void setFontColor(int front_color, int bg_color, int index) {
		setFontColor((front_color << 4) + bg_color, index);
	}

	public void setFontColor(int color, int index) {
		if (index < 0 || index > 31)
			return;

		font_color[index] = (byte) (color & 0xFF);
	}

	public void setFontReference(boolean flag) {
		show_font_reference = flag;
	}


	/****************************************************
	 * Font I/O                                         *
	*****************************************************/

	public boolean loadFont(String filename, int option) {
		MSXFontDAO dao = new MSXFontDAO();
		resetColors();

		switch (option) {
			case 0 : return dao.loadFontDump(filename, font_data);
			case 1 : return dao.loadFontColorMap(filename, font_color);
		}

		return false;
	}

	public boolean saveFont(String filename, int option) {
		MSXFontDAO dao = new MSXFontDAO();

		switch (option) {
			case 0 : return dao.saveFontDump(filename, font_data, true);
			case 1 : return dao.saveFontDump(filename, font_data, false);
			case 2 : return dao.saveFontASCII(filename, font_data, font_color);
			case 3 : return dao.saveFontColorMap(filename, font_color);
			case 4 : return dao.saveFontBitmap(filename, renderFontToSave(0));
			case 5 : return dao.saveFontBitmap(filename, renderFontToSave(1));
			case 6 : return dao.saveFontBitmap(filename, renderFontToSave(2));
		}

		return false;
	}


	/****************************************************
	 * Modify character from/to editor                  *
	*****************************************************/

	public MSXCharacterData getCharacterAt(int ascii) {
		if ((ascii < 0) || (ascii>255))
			ascii = 0;
		
		int pos = ascii*8;
		byte tmp[] = Arrays.copyOfRange(font_data, pos, pos+8);
		MSXCharacterData tmp_data = new MSXCharacterData(tmp);

		return tmp_data;		
	}

	public void setCharacterAt(MSXCharacterData data, int ascii) {
		if ((ascii < 0) || (ascii>255))
			ascii = 0;
		
		byte tmp[] = data.getCharacter();
		int pos = ascii*8;
		System.arraycopy(tmp, 0, font_data, pos, tmp.length);
	}


	/****************************************************
	 * Copy characters from a font to another           *
	*****************************************************/

	public void copyCharacters(MSXFontData data_src, int begin, int end, int offset) {
		MSXFont fnt_src = new MSXFont();
		fnt_src.setFont(data_src);

		if (end ==-1) end=begin;

		for (int i=begin; i<=end; i++) {
			setCharacterAt(fnt_src.getCharacterAt(i), i+offset);
		}
	}



	/****************************************************
	 * Render font table                                *
	*****************************************************/

	// Render for file saving
	public BufferedImage renderFontToSave(int mode) {
		int c_color, cf_color, cb_color;
		int width = (mode == 2) ? 256 : 128, height = (mode == 2) ? 192 : 128, x, y;
		int chr_width = (mode == 2) ? 32 : 16;
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(new Color(0,0,0));
		g2d.fillRect(0, 0, width, height);

		// Render characters
		MSXCharacter chr = new MSXCharacter();
		BufferedImage chr_img;
		for (int i=0; i<256; i++) {
			chr.setCharacter(getCharacterAt(i));
			c_color = (mode == 1) ? font_color[i/8] : 0xF0;
			cf_color = msx_palette[(c_color >> 4) & 0xF];
			cb_color = msx_palette[c_color & 0xF];
			chr_img = chr.renderCharacter(false, cf_color, cb_color);

			x = (i % chr_width)*8;
			y = (i/chr_width)*8;
			g2d.drawImage(chr_img, x, y, 8, 8, null);
		}

		if (mode == 2) {
			g2d.copyArea(0, 0, 256, 64, 0, 65);
			g2d.copyArea(0, 0, 256, 64, 0, 129);
		}

		g2d.dispose();

		return img;
	}

	// Render for app
	public BufferedImage renderFontArea() {
		int fcolor = 0xFFFFFF, bcolor = 0, gcolor = 0xAAAAAA, c_color, cf_color, cb_color;
		int width=527, height=527, x, y;
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(new Color(bcolor));
		g2d.fillRect(0, 0, width, height);

		// Render characters
		if (!show_font_reference) {
			MSXCharacter chr = new MSXCharacter();
			BufferedImage chr_img;
			for (int i=0; i<256; i++) {
				chr.setCharacter(getCharacterAt(i));
				c_color = font_color[i/8];
				cf_color = msx_palette[(c_color >> 4) & 0xF];
				cb_color = msx_palette[c_color & 0xF];
				chr_img = chr.renderCharacter(i==selected_char || (i>=selected_char && i<=last_selected_char), cf_color, cb_color);

				x = (i % 16)*33;
				y = (i / 16)*33;
				g2d.drawImage(chr_img, x, y, 32, 32, null);
			}
		}
		else {
			g2d.setColor(new Color(0x00AA00));
			g2d.setFont(new Font("Times New Roman", Font.BOLD, 16));
			for (int i=0; i<256; i++) {
				x = (i % 16)*33;
				y = (i / 16)*33;
				g2d.drawString(Character.toString((char) i), x+10, y+20);
			}
		}

		// Grid
		g2d.setColor(new Color(gcolor));
		for (y=32; y<height; y+=33)
			g2d.drawLine(0, y, width-1, y);
		for (x=32; x<width; x+=33)
			g2d.drawLine(x, 0, x, height-1);

		g2d.dispose();

		return img;
	}

}
