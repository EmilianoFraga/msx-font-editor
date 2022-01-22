/***************************************************************************
 *   Class FontManager                                                     *
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
 * Designed for managing the fonts and characters                          *
 * MVC: Model                                                              *
 ***************************************************************************/

import java.util.ArrayList;
import java.awt.image.BufferedImage;

class FontManager {

	private StackManager<MSXCharacterData> char_stack = new StackManager<MSXCharacterData>();
	private StackManager<MSXFontData> font_stack = new StackManager<MSXFontData>();
	private ArrayList<StackManager<MSXFontData>> font_list = new ArrayList<StackManager<MSXFontData>>();
	private MSXFont fnt = new MSXFont();
	private MSXCharacter chr = new MSXCharacter();
	private int current_font=0;
	private Clipboard cb;

	public FontManager() {
		cb = new Clipboard();
		StackManager<MSXFontData> stack = new StackManager<MSXFontData>();
		stack.save(fnt.getFont());
		font_list.add(stack);

		setCurrentFont();
		saveFont();
		saveCharacter();
	}


	/************************************************
	 * Save, undo and redo services                 *
	 ************************************************/

	public void undo(boolean all) {
		if (all)
			undoFont();
		else
			undoCharacter();
	}

	private void undoFont() {
		MSXFontData tmp = font_stack.undo();

		if (tmp != null)
			fnt.setFont(tmp);
	}

	private void undoCharacter() {
		MSXCharacterData tmp = char_stack.undo();

		if (tmp != null)
			chr.setCharacter(tmp);
	}

	public void redo(boolean all) {
		if (all)
			redoFont();
		else
			redoCharacter();
	}

	private void redoFont() {
		MSXFontData tmp = font_stack.redo();

		if (tmp != null)
			fnt.setFont(tmp);
	}

	private void redoCharacter() {
		MSXCharacterData tmp = char_stack.redo();

		if (tmp != null)
			chr.setCharacter(tmp);
	}

	private void checkModification() {
		if (cb.getFontID() == current_font)
			cb.setModified();
	}

	private void saveFont() {
		font_stack.save(fnt.getFont());
		checkModification();
	}

	private void saveCharacter() {
		char_stack.save(chr.getCharacter());
	}


	/************************************************
	 * Character services                           *
	 ************************************************/

	private void applyCharModification(int option, boolean parm1) {
		switch (option) {
			case 0 : chr.bold(); break;
			case 1 : chr.italic(); break;
			case 2 : chr.inverse(); break;
			case 3 : chr.flipud(); break;
			case 4 : chr.mirror(); break;
			case 5 : chr.rotate(); break;
			case 6 : chr.clear(); break;
			case 7 : chr.shiftLeft(parm1); break;
			case 8 : chr.shiftRight(parm1); break;
			case 9 : chr.shiftUp(parm1); break;
			case 10 : chr.shiftDown(parm1); break;
		}
	}

	private void modifyFont(int option, boolean parm1) {
		MSXCharacterData tmp = chr.getCharacter();
		int sel[] = fnt.getSelectedChars();
		int begin=0, end=255;

		// If nothing is selected, modify all
		// Otherwise modify selected
		if (sel[0] != -1) {
			begin = sel[0];
			end = begin;
			if (sel[1] != -1) end = sel[1];
		}		

		for (int i=begin; i<=end; i++) {
			chr.setCharacter(fnt.getCharacterAt(i));
			applyCharModification(option, parm1);
			fnt.setCharacterAt(chr.getCharacter(), i);
		}

		// Restore character
		chr.setCharacter(tmp);
		saveFont();
	}

	private void modifyCharacter(int option, boolean parm1) {
		applyCharModification(option, parm1);
		saveCharacter();
	}

	public void modify(int option, boolean all) {
		modify(option, all, false);
	}

	public void modify(int option, boolean all, boolean parm1) {
		if (all)
			modifyFont(option, parm1);
		else
			modifyCharacter(option, parm1);
	}

	public void copyCharFromFont() {
		int ascii = fnt.getSelectedChar();
		if (ascii >= 0) {
			chr.setCharacter(fnt.getCharacterAt(ascii));
			saveCharacter();
		}
	}

	public void copyChartoFont() {
		int ascii = fnt.getSelectedChar();
		if (ascii >= 0)
			fnt.setCharacterAt(chr.getCharacter(), ascii);
		saveFont();
	}


	/************************************************
	 * Font services                                *
	 ************************************************/

	// Font number on iterface: from 1 to N
	public int getCurrentFont() {
		return current_font+1;
	}

	public int getNumberOfFonts() {
		return font_list.size();
	}

	public void nextFont() {
		if (current_font >= font_list.size()-1)
			return;

		current_font++;
		setCurrentFont();
	}

	public void previousFont() {
		if (current_font <= 0)
			return;

		current_font--;
		setCurrentFont();
	}

	public void clearAllFonts() {
		font_list.clear();
		current_font=0;
	}

	private void setCurrentFont() {
		font_stack = font_list.get(current_font);
		fnt.setFont(font_stack.top());
	}

	public void setCurrentFont(int font) {
		if (font < 1 || font > getNumberOfFonts())
			return;

		current_font = font-1;
		setCurrentFont();
	}

	public boolean saveFont(String filename, int option) {
		return fnt.saveFont(filename, option);
	}

	public boolean loadFontColorMap(String filename) {
		if (!fnt.loadFont(filename, 1))
			return false;

		saveFont();

		return true;
	}

	public boolean loadFont(String filename) {
		if (!fnt.loadFont(filename, 0))
			return false;

		StackManager<MSXFontData> stack = new StackManager<MSXFontData>();
		stack.save(fnt.getFont());
		font_list.add(stack);

		cb.setModified();
		
		return true;
	}

	public boolean copySelectedCharacters() {
		int sel[] = fnt.getSelectedChars();
		if (sel[0] == -1)
			return false;

		cb.set(sel[0], sel[1], current_font);

		return true;
	}

	private boolean checkNewSelectionSize(int b1, int e1, int b2, int e2 ) {
		int size = e1-b1+1;
		int size2 = e2-b2+1;

		if (b2+size-1 > 255)
			return false;

		if ((size2 > 1) && (size != size2))
			return false;

		return true;
	}

	public int pasteSelectedCharacters() {
		if (cb.isEmpty())
			return 0;
		if (cb.isModified())
			return -1;

		int id, b1, e1, b2, e2;
		int sel[] = fnt.getSelectedChars();
		id = cb.getFontID();
		b1 = cb.getBegin();
		e1 = cb.getEnd();
		b2 = sel[0];
		e2 = sel[1];

		if (b2==-1)
			return -3;

		if (e1 ==-1) e1=b1;
		if (e2 ==-1) e2=b2;

		if (!checkNewSelectionSize(b1,e1,b2,e2))
			return -2;

		fnt.copyCharacters(font_list.get(id).top(), b1, e1, b2-b1);
		saveFont();

		return 1;
	}

	public void changeFontColors(int front_color, int bg_color) {
		int sel[] = fnt.getSelectedChars();
		int begin=0, end=255;

		// If nothing is selected, modify all
		// Otherwise modify selected
		if (sel[0] != -1) {
			begin = sel[0];
			end = begin;
			if (sel[1] != -1) end = sel[1];
		}		

		for (int i=begin; i<=end; i+=8)
			fnt.setFontColor(front_color, bg_color, i/8);

		saveFont();
	}

	public void setFontReference(boolean flag) {
		fnt.setFontReference(flag);
	}


	/************************************************
	 * Render services                              *
	 ************************************************/

	public BufferedImage getFontTable() {
		return fnt.renderFontArea();
	}

	public BufferedImage getCharTable() {
		return chr.renderCharacterArea();
	}


	/************************************************
	 * Font and character areas services            *
	 ************************************************/

	public int fontAreaClicked(int x, int y, boolean multiple, boolean deselect) {
		return fnt.setSelectedChar(x,y,multiple,deselect);
	}

	public void charAreaClicked(int x, int y, int mode) {
		chr.pixelClicked(x,y,mode);
		if (mode==0) saveCharacter();
	}

	public void charAreaReleased() {
		saveCharacter();
	}

	public int getCharacterAtPos(int x, int y) {
		return fnt.getCharacterAtPos(x,y);
	}


	/************************************************
	 * PC Font services                             *
	 ************************************************/

	public void createPCFont(String fontname) {
		RenderPCFont render = new RenderPCFont(fontname);
		MSXCharacter chr = new MSXCharacter();

		for (int i=0; i<256; i++) {
			chr.setCharacterFromImage(render.renderPCCharacter((char) i));
			fnt.setCharacterAt(chr.getCharacter(), i);
		}

		saveFont();
	}


	/************************************************
	 * Mosaic services                              *
	 ************************************************/

	public void createMosaic(Mosaic mosaic, int ascii) {
		ArrayList<MSXCharacterData> list = new ArrayList<MSXCharacterData>();

		list = mosaic.createMosaic();

		for (int i=0; i<mosaic.getImgBlocksLength(); i++)
			fnt.setCharacterAt(list.get(i), i+ascii);

		saveFont();
	}


	/************************************************
	 * File extraction                              *
	 ************************************************/

	public void createFontFromFile(MSXFontData data) {
		fnt.setFont(data);
		saveFont();
	}

}
