/***************************************************************************
 *   Class FontEditor                                                      *
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
 * Main program interface control                                          *
 * MVC: Control                                                            *
 ***************************************************************************/
package com.msxall.marmsx.font.editor;

import com.msxall.marmsx.font.manager.FontManager;
import com.msxall.marmsx.font.pc.PCFontDialog;
import com.msxall.marmsx.mosaic.Mosaic;
import com.msxall.marmsx.mosaic.MosaicEditor;
import com.msxall.marmsx.search.SearchEditor;
import com.msxall.marmsx.util.ResourceLoader;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class FontEditor {

	private FontEditorUI window;
	private FontManager fm;
	private File current_dir;
	private MosaicEditor me;
	private SearchEditor se;

	public FontEditor() {
		// Create new Font Manager
		fm = new FontManager();

		// Create main form
		window = new FontEditorUI(this);
		window.setTitle(ResourceLoader.getAbsoluteResourceAsString("/appname.txt"));
		window.setBounds(100, 100, 915, 665);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		update();
	}

	private void update() {
		window.setFontArea(fm.getFontTable());
		window.setCharArea(fm.getCharTable());
	}

	public void setFontReference(boolean flag) {
		fm.setFontReference(flag);
		update();
	}

	public void changeFontColors(int front_color, int bg_color) {
		fm.changeFontColors(front_color, bg_color);
		update();
	}

	public void fontAreaClicked(int x, int y, boolean multiple, boolean deselect) {
		int ascii = fm.fontAreaClicked(x, y, multiple, deselect);
		window.setAsciiSelLabel(ascii);
		update();
	}

	public void charAreaClicked(int x, int y, int mode) {
		fm.charAreaClicked(x, y, mode);
		update();
	}

	public void charAreaReleased() {
		fm.charAreaReleased();
	}

	public void getCharacterAtPos(int x, int y) {
		int ascii = fm.getCharacterAtPos(x,y);
		window.setAsciiLabel(ascii);
	}

	public void readCharClicked() {
		fm.copyCharFromFont();
		update();
	}

	public void writeCharClicked() {
		fm.copyChartoFont();
		update();
	}


	/************************************************
	 * Character Control                            *
	 ************************************************/

	public void undoClicked() {
		fm.undo(window.getAllButtonIsSelected());
		update();
	}

	public void redoClicked() {
		fm.redo(window.getAllButtonIsSelected());
		update();
	}

	public void boldClicked() {
		fm.modify(0, window.getAllButtonIsSelected());
		update();
	}

	public void italicClicked() {
		fm.modify(1, window.getAllButtonIsSelected());
		update();
	}

	public void invClicked() {
		fm.modify(2, window.getAllButtonIsSelected());
		update();
	}

	public void flipudClicked() {
		fm.modify(3, window.getAllButtonIsSelected());
		update();
	}

	public void mirrorClicked() {
		fm.modify(4, window.getAllButtonIsSelected());
		update();
	}

	public void rotateClicked() {
		fm.modify(5, window.getAllButtonIsSelected());
		update();
	}

	public void clearClicked() {
		fm.modify(6, window.getAllButtonIsSelected());
		update();
	}

	public void shiftLeftClicked(boolean rotate) {
		fm.modify(7, window.getAllButtonIsSelected(), rotate);
		update();
	}

	public void shiftRightClicked(boolean rotate) {
		fm.modify(8, window.getAllButtonIsSelected(), rotate);
		update();
	}

	public void shiftUpClicked(boolean rotate) {
		fm.modify(9, window.getAllButtonIsSelected(), rotate);
		update();
	}

	public void shiftDownClicked(boolean rotate) {
		fm.modify(10, window.getAllButtonIsSelected(), rotate);
		update();
	}


	/************************************************
	 * Font controls                                *
	 ************************************************/

	public void loadFontClicked() {
		JFileChooser fc_load = new JFileChooser();
		fc_load.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc_load.setMultiSelectionEnabled(true);
		fc_load.setAcceptAllFileFilterUsed(false);
		fc_load.addChoosableFileFilter(new FileNameExtensionFilter("MSX Font (*.alf)","alf"));
		fc_load.addChoosableFileFilter(new FileNameExtensionFilter("Screen 1 color map (*.s1c)","s1c"));
		fc_load.setCurrentDirectory(current_dir);

		int result = fc_load.showOpenDialog(window);

		if (result == JFileChooser.CANCEL_OPTION)
			return;

		// Get filter option
		FileFilter sel_filter = fc_load.getFileFilter();
		int filter_option = -1;
		if (sel_filter.getDescription().equals("MSX Font (*.alf)")) filter_option = 0;
		if (sel_filter.getDescription().equals("Screen 1 color map (*.s1c)")) filter_option = 1;

		// Check if one file filter was choosen
		if (filter_option == -1) {
			JOptionPane.showMessageDialog(window, "Error. No file filter selected.", "Load Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Get file name and directory
		File[] filename = fc_load.getSelectedFiles();
		current_dir = fc_load.getCurrentDirectory();

		// Load only the color map
		if (filter_option == 1) {
			if (!fm.loadFontColorMap(filename[0].toString())) {
				JOptionPane.showMessageDialog(window, "Error while loading font color map", "Load Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			update();
			return;
		}

		// Start to load fonts
		fm.clearAllFonts();

		for (File file : filename) {
			if (!fm.loadFont(file.toString())) {
				JOptionPane.showMessageDialog(window, "Error while loading font", "Load Error", JOptionPane.ERROR_MESSAGE);
				fm.clearAllFonts();
				return;
			}
		}

		fm.setCurrentFont(1);

		// Update form data
		window.setMaxFonts(fm.getNumberOfFonts());
		window.setCurrentFont(1);

		update();
	}

	public void saveFontClicked() {
		JFileChooser fc_save = new JFileChooser();
		String [] f_ext = {".alf", ".hex", ".asm", ".s1c", ".gif", ".gif", ".gif"};
		fc_save.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc_save.setAcceptAllFileFilterUsed(false);
		fc_save.addChoosableFileFilter(new FileNameExtensionFilter("MSX Font (*.alf)","alf"));
		fc_save.addChoosableFileFilter(new FileNameExtensionFilter("MSX Font no header (*.hex)","hex"));
		fc_save.addChoosableFileFilter(new FileNameExtensionFilter("MSX Font text file (*.asm)","asm"));
		fc_save.addChoosableFileFilter(new FileNameExtensionFilter("Screen 1 color map (*.s1c)","s1c"));
		fc_save.addChoosableFileFilter(new FileNameExtensionFilter("Screen 0 layout (*.gif)","gif"));
		fc_save.addChoosableFileFilter(new FileNameExtensionFilter("Screen 1 layout (*.gif)","gif"));
		fc_save.addChoosableFileFilter(new FileNameExtensionFilter("Screen 2 layout (*.gif)","gif"));
		fc_save.setCurrentDirectory(current_dir);

		int result = fc_save.showSaveDialog(window);

		if (result == JFileChooser.CANCEL_OPTION) 
			return;

		// Get filter option
		FileFilter sel_filter = fc_save.getFileFilter();
		int filter_option = -1;
		if (sel_filter.getDescription().equals("MSX Font (*.alf)")) filter_option = 0;
		if (sel_filter.getDescription().equals("MSX Font no header (*.hex)")) filter_option = 1;
		if (sel_filter.getDescription().equals("MSX Font text file (*.asm)")) filter_option = 2;
		if (sel_filter.getDescription().equals("Screen 1 color map (*.s1c)")) filter_option = 3;
		if (sel_filter.getDescription().equals("Screen 0 layout (*.gif)")) filter_option = 4;
		if (sel_filter.getDescription().equals("Screen 1 layout (*.gif)")) filter_option = 5;
		if (sel_filter.getDescription().equals("Screen 2 layout (*.gif)")) filter_option = 6;

		// Check if one file filter was choosen
		if (filter_option == -1) {
			JOptionPane.showMessageDialog(window, "Error. No file filter selected.", "Save Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Get file name
		File filename = fc_save.getSelectedFile();
		current_dir = fc_save.getCurrentDirectory();

		// Check for ".alf" extension
		if (!checkFileExtension(filename.getName(), f_ext[filter_option]))
			filename = new File(filename + f_ext[filter_option]);

		// Check if file exists
		if (filename.exists()) {
			if (JOptionPane.showConfirmDialog(window, "This file already exists. Overwite?", "Warning", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
				return;
		}

		// Save file
		if (!fm.saveFont(filename.toString(), filter_option))
				JOptionPane.showMessageDialog(window, "Error while saving file", "Save Error", JOptionPane.ERROR_MESSAGE);

	}

	private boolean checkFileExtension(String name, String ext) {
		if (name.length() < 4)
			return false;

		return name.toLowerCase().substring(name.length()-4, name.length()).equals(ext);
	}

	public void pcFontClicked() {
		PCFontDialog pc = new PCFontDialog(fm);
		update();
	}

	public void mosaicClicked() {
		if (me==null)
			me = new MosaicEditor(this);

		me.start();
		update();
	}

	public void fSearchClicked() {
		se = new SearchEditor();

		if (se.getFontData() != null) {
			fm.createFontFromFile(se.getFontData());
			update();
		}
	}

	public void createMosaic(Mosaic mosaic, int ascii) {
		fm.createMosaic(mosaic, ascii);
		update();
	}

	public void copySelectedCharacters() {
		if (!fm.copySelectedCharacters())
			JOptionPane.showMessageDialog(window, "There is no selected character.", "Error", JOptionPane.ERROR_MESSAGE);
	}

	public void pasteSelectedCharacters() {
		int flag = fm.pasteSelectedCharacters();
		if (flag == 0)
			JOptionPane.showMessageDialog(window, "The clipboard is empty.", "Error", JOptionPane.ERROR_MESSAGE);
		if (flag == -1)
			JOptionPane.showMessageDialog(window, "The source font was modified.\nPlease, select characters again.", "Error", JOptionPane.ERROR_MESSAGE);
		if (flag == -2)
			JOptionPane.showMessageDialog(window, "Origin and destiny selection size mismatch.", "Error", JOptionPane.ERROR_MESSAGE);
		if (flag == -3)
			JOptionPane.showMessageDialog(window, "Please, select a destiny.", "Error", JOptionPane.ERROR_MESSAGE);
		update();
	}


	/************************************************
	 * Font Browser                                 *
	 ************************************************/

	public void prevClicked() {
		fm.previousFont();
		window.setCurrentFont(fm.getCurrentFont());
		update();
	}

	public void nextClicked() {
		fm.nextFont();
		window.setCurrentFont(fm.getCurrentFont());
		update();
	}

	public void fontTextFieldEvent(int value, boolean load) {
		boolean hasError=false;

		if (value < 1) {
			value=1;
			hasError=true;
		}

		if (value>fm.getNumberOfFonts()) {
			value = fm.getNumberOfFonts();
			hasError=true;
		}

		window.setCurrentFont(value);

		if (!load || hasError)
			return;

		fm.setCurrentFont(value);
		update();
	}
	
}
