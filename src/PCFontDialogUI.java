/***************************************************************************
 *   Class PCFontDialogUI                                                  *
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
 * PC Font interface                                                       *
 * MVC: View                                                               *
 ***************************************************************************/

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PCFontDialogUI extends JDialog {

//	private JComboBox<String> comboBox = new JComboBox<String>();
	private JComboBox comboBox = new JComboBox();
	private JLabel testLabel;
	private PCFontDialog pc;

	public PCFontDialogUI(PCFontDialog new_pc) {

		setLayout(null);
		pc = new_pc;

		JLabel lblFontExample = new JLabel("Font example:");
		lblFontExample.setBounds(31, 12, 126, 15);
		add(lblFontExample);

		testLabel = new JLabel("MSX Font Editor");
		testLabel.setBounds(31, 39, 318, 33);
		add(testLabel);
		
		comboBox.setBounds(31, 89, 318, 33);
		add(comboBox);
		String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for (int i=0; i<fonts.length; i++)
			comboBox.addItem(fonts[i]);
		testLabel.setFont(new Font(fonts[0], Font.BOLD, 32));
		comboBox.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				testLabel.setFont(new Font(String.valueOf(comboBox.getSelectedItem()), Font.BOLD, 32));
			}
		});
		
		JButton btnOk = new JButton("Ok");
		btnOk.setBounds(57, 144, 117, 25);
		add(btnOk);
		btnOk.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				pc.okButtonClicked(String.valueOf(comboBox.getSelectedItem()));
			}
		});
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(206, 144, 117, 25);
		add(btnCancel);
		btnCancel.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
}
