/***************************************************************************
 *   Class MosaicEditorUI                                                  *
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
 * Mosaic editor                                                           *
 * MVC: View                                                               *
 ***************************************************************************/
package com.msxall.marmsx.mosaic;

import com.msxall.marmsx.util.ResourceLoader;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MosaicEditorUI extends JDialog {

	private MosaicEditor me;
	private JLabel lblScreen;
	private JLabel widthLabel;
	private JLabel heightLabel;
	private JLabel blocksLabel;
	private JLabel lblLength;
	private JLabel lengthLabel;
	private JComboBox comboBox;
	private JSpinner asciiSpinner;

	public void updateImageData(int width, int height, int blocks_x, int blocks_y, int length) {
		widthLabel.setText(Integer.toString(width));
		heightLabel.setText(Integer.toString(height));
		blocksLabel.setText(Integer.toString(blocks_x)+" x "+Integer.toString(blocks_y));
		Color c = new Color(0,0,0);
		if (length > 256)
			c = new Color(255,0,0);
		lengthLabel.setForeground(c);
		lblLength.setForeground(c);
		lengthLabel.setText(Integer.toString(length));
	}

	public void updateImage(Image img) {
		lblScreen.setIcon(new ImageIcon(img));
	}

	public void updateSpinner(int max) {
		Integer val = (Integer) asciiSpinner.getValue();
		if (val > max) val=max;
		asciiSpinner.setModel(new SpinnerNumberModel((int) val, 0, max, 1));
	}

	public MosaicEditorUI(MosaicEditor new_me) {

		setLayout(null);
		me = new_me;

		//
		// Img label
		//

		JPanel panelImg = new JPanel();
		panelImg.setBorder(new LineBorder(new Color(92, 92, 92)));
		panelImg.setBounds(10, 10, 514, 386);
		add(panelImg);
		panelImg.setLayout(null);
		
		lblScreen = new JLabel("");
		lblScreen.setBackground(new Color(255, 255, 255));
		lblScreen.setOpaque(true);
		lblScreen.setBounds(1, 1, 512, 384);
		panelImg.add(lblScreen);


		//
		// Clipart Image
		//

		JPanel panel_image_lay = new JPanel();
		panel_image_lay.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Clipart image", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_image_lay.setBounds(540, 10, 160, 152);
		add(panel_image_lay);
		panel_image_lay.setLayout(null);
		
		JPanel panel_image = new JPanel();
		panel_image.setBounds(5, 17, 150, 130);
		panel_image_lay.add(panel_image);
		panel_image.setLayout(null);
		
		JLabel lblWidth = new JLabel("Width:");
		lblWidth.setBounds(7, 10, 60, 15);
		panel_image.add(lblWidth);
		
		JLabel lblHeight = new JLabel("Height:");
		lblHeight.setBounds(7, 30, 60, 15);
		panel_image.add(lblHeight);
		
		JLabel lblBlocks = new JLabel("Blocks:");
		lblBlocks.setBounds(7, 50, 60, 15);
		panel_image.add(lblBlocks);

		lblLength = new JLabel("Length:");
		lblLength.setBounds(7, 70, 60, 15);
		panel_image.add(lblLength);
		
		widthLabel = new JLabel("0");
		widthLabel.setBounds(70, 10, 40, 15);
		panel_image.add(widthLabel);
		
		heightLabel = new JLabel("0");
		heightLabel.setBounds(70, 30, 40, 15);
		panel_image.add(heightLabel);
		
		blocksLabel = new JLabel("0 x 0");
		blocksLabel.setBounds(70, 50, 70, 15);
		panel_image.add(blocksLabel);

		lengthLabel = new JLabel("0");
		lengthLabel.setBounds(70, 70, 70, 15);
		panel_image.add(lengthLabel);

		JButton btnLoad = new JButton("");
		btnLoad.setToolTipText("Load PC image");
		btnLoad.setBounds(7, 95, 55, 27);
		btnLoad.setIcon(ResourceLoader.loadImageIconFromAbsoluteResource("/imgs/fileopen.png"));
		panel_image.add(btnLoad);
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				me.loadImageClicked();
			}
		});


		//
		// MSX ASCII Table
		//

		JPanel panel_ascii_lay = new JPanel();
		panel_ascii_lay.setLayout(null);
		panel_ascii_lay.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "MSX ASCII Table", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_ascii_lay.setBounds(540, 168, 160, 124);
		add(panel_ascii_lay);
		
		JPanel panel_ascii = new JPanel();
		panel_ascii.setLayout(null);
		panel_ascii.setBounds(5, 17, 150, 100);
		panel_ascii_lay.add(panel_ascii);

		JLabel lblPicture = new JLabel("Screen mode:");
		lblPicture.setBounds(7, 10, 138, 15);
		panel_ascii.add(lblPicture);
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Screen 0", "Screen 1"}));
		comboBox.setBounds(7, 30, 120, 24);
		panel_ascii.add(comboBox);
		comboBox.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				me.comboBoxChanged(comboBox.getSelectedIndex());
			}
		});
		
		JLabel lblStartAt = new JLabel("Start at:");
		lblStartAt.setBounds(7, 77, 60, 15);
		panel_ascii.add(lblStartAt);
		
		asciiSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
		asciiSpinner.setToolTipText("Set where image starts on the ASCII table");
		asciiSpinner.setBounds(75, 75, 50, 20);
		panel_ascii.add(asciiSpinner);


		//
		// Mosaic
		//

		JPanel panel_mosaic_lay = new JPanel();
		panel_mosaic_lay.setLayout(null);
		panel_mosaic_lay.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Mosaic", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_mosaic_lay.setBounds(540, 298, 160, 82);
		add(panel_mosaic_lay);
		
		JPanel panel_mosaic = new JPanel();
		panel_mosaic.setLayout(null);
		panel_mosaic.setBounds(5, 17, 150, 60);
		panel_mosaic_lay.add(panel_mosaic);

		// Save		
		JButton btnSave = new JButton("");
		btnSave.setToolTipText("Create a MSX Basic program to open the mosaic");
		btnSave.setBounds(7, 10, 55, 32);
		btnSave.setIcon(ResourceLoader.loadImageIconFromAbsoluteResource("/imgs/msxdisk.png"));
		panel_mosaic.add(btnSave);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				me.saveClicked((Integer) asciiSpinner.getValue());
			}
		});

		// Export
		JButton btnExport = new JButton();
		btnExport.setToolTipText("Export image to current ASCII table on editor");
		btnExport.setBounds(80, 10, 55, 32);
		btnExport.setIcon(ResourceLoader.loadImageIconFromAbsoluteResource("/imgs/export.png"));
		panel_mosaic.add(btnExport);
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				me.okClicked((Integer) asciiSpinner.getValue());
			}
		});
	}

}
