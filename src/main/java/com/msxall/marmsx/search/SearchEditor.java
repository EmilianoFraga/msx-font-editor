/***************************************************************************
 *   Class SearchEditor                                                    *
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
 * Search editor to extract fonts from files                               *
 * MVC: Control                                                            *
 ***************************************************************************/
package com.msxall.marmsx.search;

import com.msxall.marmsx.font.msx.MSXFont;
import com.msxall.marmsx.font.msx.MSXFontData;

import java.io.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class SearchEditor {

	private SearchEditorUI window;
	private File current_dir;
	private MSXFont fnt = new MSXFont();
	private byte[] MSX_file;
	private int num_pages = 0;
	private int curr_page = 0;
	private int char_offset = 0;
	private MSXFontData font_data;

	public SearchEditor() {
		// Create main form
		window = new SearchEditorUI(this);
		window.setTitle("MarMSX MSX Font Editor - MSX File Font Extraction");
		window.setBounds(100, 100, 770, 665);
		window.setModal(true);
		update();
		window.setVisible(true);
	}

	public MSXFontData getFontData() {
		return font_data;
	}

	private void update() {
		window.setFontArea(fnt.renderFontArea());
		window.setCurrentFont(curr_page + 1);
		window.blockControls((fnt.getSelectedChar() >= 0) | (MSX_file == null));
		if (MSX_file == null)
			window.blockDone();
	}

	public void fontAreaClicked(int x, int y, boolean multiple, boolean deselect) {
		fnt.setSelectedChar(x,y,multiple,deselect);
		update();
	}

	private byte[] getFileDataFromPage() {
		return getFileDataFromPage(0);
	}

	private byte[] getFileDataFromPage(int ascii) {
		byte [] data = new byte[2048];
		int p_start = curr_page * 2048 + char_offset + ascii*8;
		int p_end = p_start + 2048 - 1;
		int d_start = (p_start >= 0) ? 0 : p_start * -1;
		p_start = (p_start < 0) ? 0 : p_start;
		p_end = (p_end >= MSX_file.length) ? MSX_file.length - 1 : p_end;
		int len = p_end - p_start + 1;

		System.arraycopy(MSX_file, p_start, data, d_start, len);

		return data;
	}

	private void setCurrentPageLayout() {
		MSXFontData fntdata = new MSXFontData(getFileDataFromPage());
		fnt.setFont(fntdata);
		fnt.resetSelectedChar();
		update();
	}


	/************************************************
	 * Font controls                                *
	 ************************************************/

	public void loadFontClicked() {
		JFileChooser fc_load = new JFileChooser();
		fc_load.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc_load.setMultiSelectionEnabled(false);
		fc_load.setAcceptAllFileFilterUsed(true);
		fc_load.setCurrentDirectory(current_dir);

		int result = fc_load.showOpenDialog(window);

		if (result == JFileChooser.CANCEL_OPTION)
			return;

		// Get file name and directory
		File filename = fc_load.getSelectedFile();
		current_dir = fc_load.getCurrentDirectory();

		// Load MSX File
		if (!loadMSXFile(filename.toString())) {
			JOptionPane.showMessageDialog(window, "Error while opening file.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Set file configurations
		setFileConfigs();

		// Modify current page
		setCurrentPageLayout();

		// Enable controls
		window.blockControls(false);
	}

	private boolean loadMSXFile(String filename) {
		boolean success_flag=true;

		File arq = new File(filename);
		MSX_file = new byte[(int) arq.length()];

		try {
			InputStream is = new FileInputStream(filename);
			is.read(MSX_file);
		}
		catch (IOException e) {
			success_flag=false;
		}
		return success_flag;
	}

	private void setFileConfigs() {
		num_pages = MSX_file.length / 2048;
		if (num_pages * 2048 < MSX_file.length)
			num_pages++;

		curr_page = 0;
		char_offset = 0;

		window.setMaxFonts(num_pages);
	}


	/************************************************
	 * Font Browser                                 *
	 ************************************************/

	public void upButtonClicked() {
		char_offset++;
		setCurrentPageLayout();
	}

	public void downButtonClicked() {
		char_offset--;
		setCurrentPageLayout();
	}

	public void resetButtonClicked() {
		char_offset = 0;
		setCurrentPageLayout();
	}

	public void prevClicked() {
		curr_page--;
		if (curr_page < 0) curr_page = 0;

		setCurrentPageLayout();
	}

	public void nextClicked() {
		curr_page++;
		if (curr_page >= num_pages) curr_page = num_pages-1;

		setCurrentPageLayout();
	}

	public void fontTextFieldEvent(int value, boolean load) {
		if (value < 1) value = 1;
		if (value > num_pages) value = num_pages;

		curr_page = value - 1;

		setCurrentPageLayout();
	}


	/************************************************
	 * Copy font to editor                          *
	 ************************************************/

	public void onDoneClicked() {
		font_data = new MSXFontData(getFileDataFromPage(fnt.getSelectedChar() - 65));
	}
}
