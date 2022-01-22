/***************************************************************************
 *   Class SearchEditorUI                                                  *
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
 * Search editor for binary files                                          *
 * MVC: View                                                               *
 ***************************************************************************/

import java.awt.Image;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import javax.swing.JToggleButton;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.util.Timer;
import javax.swing.UIManager;

public class SearchEditorUI extends JDialog {

	private SearchEditor se;
	private JPanel panelFont;
	private JPanel panelChar;
	private JLabel fontLabel;
	private JPanel panelFontNav;
	private JLabel maxFontsLabel;
	private JTextField fontTextField;
	private JButton upButton;
	private JButton downButton;
	private JButton resetButton;
	private JButton okButton;


	public void setFontArea(Image img) {
		fontLabel.setIcon(new ImageIcon(img));
	}

	public void setMaxFonts(int max) {
		maxFontsLabel.setText("/ " + Integer.toString(max));
		setPanelEnabled(panelFontNav, max > 1);
	}

	public void setCurrentFont(int pos) {
		fontTextField.setText(Integer.toString(pos));
	}

	public void blockControls(boolean is_block) {
		setPanelEnabled(panelFontNav, !is_block);
		upButton.setEnabled(!is_block);
		downButton.setEnabled(!is_block);
		resetButton.setEnabled(!is_block);
		okButton.setEnabled(is_block);
	}

	public void blockDone() {
		okButton.setEnabled(false);
	}

	private void setPanelEnabled(JPanel panel, boolean enabled) {
		Component comps[] = panel.getComponents();
		for (int i=0; i<comps.length; i++) {
			comps[i].setEnabled(enabled);
		}

	}

	public SearchEditorUI(SearchEditor new_se) {

		se = new_se;
		setLayout(null);

		// App icon
		Image img = new ImageIcon(this.getClass().getResource("imgs/icon.png")).getImage();
		setIconImage(img);

		// Tooltips configuration
		//UIManager.put("ToolTip.background", Color.YELLOW);
		//UIManager.put("ToolTip.foreground", Color.BLACK);
		UIManager.put("ToolTip.font", new Font("Dialog", Font.BOLD, 13));


		/************************************************
		 * Font Area                                    *
		 ************************************************/

		// Font area
		panelFont = new JPanel();
		panelFont.setBorder(new LineBorder(new Color(92, 92, 92)));
		panelFont.setBounds(6, 6, 536, 536);
		add(panelFont);
		panelFont.setLayout(null);

		fontLabel = new JLabel("");
		fontLabel.setBounds(4, 4, 527, 527);
		panelFont.add(fontLabel);
		fontLabel.setBackground(new Color(0, 0, 0));
		fontLabel.setOpaque(true);
		fontLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				se.fontAreaClicked(e.getX(), e.getY(), false, true);
			}
		});

		
		/************************************************
		 * Font Control                                 *
		 ************************************************/

		JLabel lblTip01 = new JLabel("1. Load MSX binary file");
		lblTip01.setBounds(550, 10, 180, 30);
		add(lblTip01);
		
		// Load
		JButton loadButton = new JButton("");
		loadButton.setToolTipText("Load font");
		loadButton.setBounds(550, 40, 37, 37);
		img = new ImageIcon(this.getClass().getResource("imgs/fileopen.png")).getImage();
		loadButton.setIcon(new ImageIcon(img));
		add(loadButton);
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				se.loadFontClicked();
			}
		});

		JLabel lblTip02a = new JLabel("2. Navigate through file");
		lblTip02a.setBounds(550, 120, 180, 30);
		add(lblTip02a);

		JLabel lblTip02b = new JLabel("until find a MSX font.");
		lblTip02b.setBounds(550, 140, 180, 30);
		add(lblTip02b);

		JLabel lblTip03a = new JLabel("3. Make fine adjustments");
		lblTip03a.setBounds(550, 200, 200, 30);
		add(lblTip03a);

		JLabel lblTip03b = new JLabel("using the up/down arrows.");
		lblTip03b.setBounds(550, 220, 200, 30);
		add(lblTip03b);

		upButton = new JButton("");		
		upButton.setToolTipText("Fine adjustment up");
		upButton.setBounds(550, 250, 37, 37);
		img = new ImageIcon(this.getClass().getResource("imgs/shift_up.png")).getImage();
		upButton.setIcon(new ImageIcon(img));
		add(upButton);
		upButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				se.upButtonClicked();
			}
		});

		downButton = new JButton("");		
		downButton.setToolTipText("Fine adjustment down");
		downButton.setBounds(600, 250, 37, 37);
		img = new ImageIcon(this.getClass().getResource("imgs/shift_down.png")).getImage();
		downButton.setIcon(new ImageIcon(img));
		add(downButton);
		downButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				se.downButtonClicked();
			}
		});

		resetButton = new JButton("");		
		resetButton.setToolTipText("Reset adjustment to ZERO");
		resetButton.setBounds(650, 250, 37, 37);
		img = new ImageIcon(this.getClass().getResource("imgs/reset.png")).getImage();
		resetButton.setIcon(new ImageIcon(img));
		add(resetButton);
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				se.resetButtonClicked();
			}
		});

		JLabel lblTip04a = new JLabel("4. Select 'A' character ");
		lblTip04a.setBounds(550, 330, 200, 30);
		add(lblTip04a);

		JLabel lblTip04b = new JLabel("inside de font editor.   ");
		lblTip04b.setBounds(550, 350, 200, 30);
		add(lblTip04b);

		JLabel lblTip05a = new JLabel("5. Press 'Done' if ok, or");
		lblTip05a.setBounds(550, 410, 200, 30);
		add(lblTip05a);

		JLabel lblTip05b = new JLabel("'Cancel' to abort all.   ");
		lblTip05b.setBounds(550, 430, 200, 30);
		add(lblTip05b);

		okButton = new JButton("Done");		
		okButton.setToolTipText("Extract font and back");
		okButton.setBounds(550, 460, 90, 37);
		add(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				se.onDoneClicked();
				dispose();
			}
		});

		JButton cancelButton = new JButton("Cancel");		
		cancelButton.setToolTipText("Cancel extraction and back");
		cancelButton.setBounds(660, 460, 90, 37);
		add(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});


		/************************************************
		 * Font Browser                                 *
		 ************************************************/
		
		panelFontNav = new JPanel();
		panelFontNav.setLayout(null);
		panelFontNav.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "File page browser", TitledBorder.LEADING, TitledBorder.TOP, new Font("Dialog", Font.PLAIN, 15), null));
		panelFontNav.setBounds(10, 545, 529, 67);
		panelFontNav.setEnabled(false);
		add(panelFontNav);
		
		// Previous font
		JButton prevButton = new JButton("");
		prevButton.setToolTipText("Previous page");
		img = new ImageIcon(this.getClass().getResource("imgs/btn_left.png")).getImage();
		prevButton.setIcon(new ImageIcon(img));
		prevButton.setBounds(10, 20, 45, 37);
		panelFontNav.add(prevButton);
		prevButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				se.prevClicked();
			}
		});
		

		// Next font
		JButton nextButton = new JButton("");
		nextButton.setToolTipText("Next page");
		img = new ImageIcon(this.getClass().getResource("imgs/btn_right.png")).getImage();
		nextButton.setIcon(new ImageIcon(img));
		nextButton.setBounds(472, 20, 45, 37);
		panelFontNav.add(nextButton);
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				se.nextClicked();
			}
		});
		

		// Current font
		fontTextField = new JTextField();
		fontTextField.setFont(new Font("Dialog", Font.PLAIN, 15));
		fontTextField.setText("1");
		fontTextField.setBounds(204, 27, 70, 25);
		panelFontNav.add(fontTextField);
		fontTextField.setColumns(100);
		fontTextField.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent ev) {
				try {
					se.fontTextFieldEvent(Integer.parseInt(fontTextField.getText()), false);
				} catch (Exception e) {
					fontTextField.setText("1");
				}
			}
		});
		fontTextField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent ev) {
				if (ev.getKeyCode() != KeyEvent.VK_ENTER)
					return;

				try {
					se.fontTextFieldEvent(Integer.parseInt(fontTextField.getText()),true);
				} catch (Exception e) {
					fontTextField.setText("1");
				}
			}
		});


		// Total fonts queue
		maxFontsLabel = new JLabel("/ 1");
		maxFontsLabel.setFont(new Font("Dialog", Font.BOLD, 15));
		maxFontsLabel.setBounds(292, 27, 67, 22);
		panelFontNav.add(maxFontsLabel);


		// Disable browser
//		setPanelEnabled(panelFontNav, false);
	}
}
