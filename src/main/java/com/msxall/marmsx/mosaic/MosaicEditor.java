/***************************************************************************
 *   Class MosaicEditor                                                    *
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
 * Mosaic interface control                                                *
 * MVC: Control                                                            *
 ***************************************************************************/
package com.msxall.marmsx.mosaic;

import com.msxall.marmsx.font.editor.FontEditor;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import javax.swing.JOptionPane;

public class MosaicEditor {

	private FontEditor fe;
	private MosaicEditorUI window;
	private Mosaic mosaic = new Mosaic();
	private File current_dir;
	private int mosaic_code;

	public MosaicEditor(FontEditor new_fe) {
		// Links to Font Editor
		fe = new_fe;

		// Create form
		window = new MosaicEditorUI(this);
		window.setTitle("Mosaic Editor");
		window.setBounds(100, 100, 730, 450);
		update();
		window.setModal(true);
//		window.setVisible(true);
	}

	public void start() {
		window.setVisible(true);
	}

	private void update() {
		mosaic_code = 0;
		window.updateImage(mosaic.renderImage());
		window.updateImageData(mosaic.getImgWidth(), mosaic.getImgHeight(), mosaic.getImgBlocksX(), mosaic.getImgBlocksY(), mosaic.getImgBlocksLength());
		int max = 256-mosaic.getImgBlocksLength();
		if (max < 0)
			max=0;
		window.updateSpinner(max);
	}

	private boolean checkBlocksLength() {
		if (mosaic.getImgBlocksLength() > 256) {
			JOptionPane.showMessageDialog(window, "Image block length is greater than 256", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (mosaic.getImgBlocksLength() < 1) {
			JOptionPane.showMessageDialog(window, "The mosaic is empty", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	public void comboBoxChanged(int value) {
		mosaic.setScreen(value);
		update();
	}


	//
	// Open clipart image
	//

	public void loadImageClicked() {
		JFileChooser fc_load = new JFileChooser();
		fc_load.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Images GIF (*.gif)","gif");
		fc_load.setFileFilter(filter);
		fc_load.setCurrentDirectory(current_dir);

		int result = fc_load.showOpenDialog(window);

		if (result == JFileChooser.CANCEL_OPTION)
			return;

		String filename = fc_load.getSelectedFile().toString();
		current_dir = fc_load.getCurrentDirectory();

		int error_code = mosaic.loadImage(filename);

		if (error_code == -2) {
			JOptionPane.showMessageDialog(window, "Bad file format", "Load Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (error_code == -1) {
			JOptionPane.showMessageDialog(window, "Image is TOO big to load", "Load Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (error_code == 0) {
			JOptionPane.showMessageDialog(window, "Error while loading image", "Load Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		update();
	}


	//
	// Save Basic file to open mosaic
	//

	public void saveClicked(int ascii) {
		if (!checkBlocksLength())
			return;

		// Ask if the starting position is good
		if (ascii == 0) {
			if (JOptionPane.showConfirmDialog(window, "The mosaic starts at position 0\non the ASCII table. Is that ok?", "Save Basic file", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
				return;
		}

		// Ask if include image on basic file
		int file_type = JOptionPane.showConfirmDialog(window, "Do you want to include image data in Basic file?", "Save Basic file", JOptionPane.YES_NO_OPTION);
		if (file_type == -1)
			return;
		file_type = (file_type == JOptionPane.YES_OPTION) ? 1 : 0;

		// Open save dialog
		JFileChooser fc_save = new JFileChooser();
		fc_save.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("MSX Basic file (*.bas)","bas");
		fc_save.setFileFilter(filter);
		fc_save.setCurrentDirectory(current_dir);

		int result = fc_save.showSaveDialog(window);

		if (result == JFileChooser.CANCEL_OPTION) 
			return;

		File filename = fc_save.getSelectedFile();
		current_dir = fc_save.getCurrentDirectory();

		// Check for ".bas" extension
		if (!checkFileExtension(filename.getName().toString(),".bas"))
			filename = new File(filename.toString() + ".bas");

		// Check if file exists
		if (filename.exists()) {
			if (JOptionPane.showConfirmDialog(window, "This file already exists. Overwite?", "Warning", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
				return;
		}

		// Save Basic file
		if (!mosaic.saveBasicFile(filename.toString(), file_type, ascii))
				JOptionPane.showMessageDialog(window, "Error while saving file", "Save Error", JOptionPane.ERROR_MESSAGE);

		// If image data is not included, show a reminder
		if (file_type == 0 && mosaic_code == 0)
			JOptionPane.showMessageDialog(window, "Do not forget to export the mosaic to\n the ASCII table and save it.", "Reminder", JOptionPane.INFORMATION_MESSAGE);

	}

	private boolean checkFileExtension(String name, String ext) {
		if (name.length() < 4)
			return false;

		return name.toLowerCase().substring(name.length()-4, name.length()).equals(ext);
	}


	//
	// Export mosaic to ASCII table
	//

	public void okClicked(int ascii) {
		if (!checkBlocksLength())
			return;

		// Ask if the starting position is good
		if (ascii == 0) {
			if (JOptionPane.showConfirmDialog(window, "The mosaic starts at position 0\non the ASCII table. Is that ok?", "Export mosaic", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
				return;
		}

		fe.createMosaic(mosaic, ascii);
		mosaic_code = 1;
	}

}
