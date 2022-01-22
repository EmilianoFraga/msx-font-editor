/***************************************************************************
 *   Class FontEditorUI                                                    *
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
 * Main program interface                                                  *
 * MVC: View                                                               *
 ***************************************************************************/

import java.awt.Image;
import javax.swing.JFrame;
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
import javax.swing.SwingUtilities;


public class FontEditorUI extends JFrame {

	private FontEditor fe;
	private JPanel panelFont;
	private JPanel panelChar;
	private JToggleButton allButton;
	private JToggleButton fABCButton;
	private JLabel fontLabel;
	private JLabel charLabel;
	private JLabel lblAscii;
	private JLabel lblSelAscii;
	private JPanel panelFontNav;
	private JLabel maxFontsLabel;
	private JTextField fontTextField;
	private boolean isShiftPressed;
	private boolean isEndPoint;
	private boolean charMouseDragged;
	private Timer timer = new Timer("doubleClickTimer", false);
	private int mouseClicks;
	private int mouseX, mouseY;
	private JCheckBox rotCheckBox;
	private JLabel frontColorLabel;
	private JLabel bgColorLabel;
	private int front_color=15, bg_color=0;
	private int msx_palette[] = { 0x0, 0x0, 0x24DB24, 0x6DFF6D, 0x2424FF, 0x496DFF, 0xB62424,
                                      0x49DBFF, 0xFF2424, 0xFF6D6D, 0xDBDB24, 0xDBDB92, 0x249224,
                                      0xDB49B6, 0xB6B6B6, 0xFFFFFF };


	public boolean getAllButtonIsSelected() {
		return allButton.isSelected();
	}

	public void setFontArea(Image img) {
		fontLabel.setIcon(new ImageIcon(img));
	}

	public void setCharArea(Image img) {
		charLabel.setIcon(new ImageIcon(img));
	}

	public void setAsciiLabel(int ascii) {
		if (ascii < 0)
			lblAscii.setText("None");
		else
			lblAscii.setText(Integer.toString(ascii)+" (&H"+Integer.toHexString(ascii).toUpperCase()+")");
	}

	public void setAsciiSelLabel(int ascii) {
		switch (ascii) {
			case -1 : lblSelAscii.setText("None"); break;
			case -2 : lblSelAscii.setText("Multiple"); break;
			default : lblSelAscii.setText(Integer.toString(ascii)+" (&H"+Integer.toHexString(ascii).toUpperCase()+")");
		}
	}

	public void setMaxFonts(int max) {
		maxFontsLabel.setText("/ " + Integer.toString(max));
		setPanelEnabled(panelFontNav, max > 1);
	}

	public void setCurrentFont(int pos) {
		fontTextField.setText(Integer.toString(pos));
	}

	private void setPanelEnabled(JPanel panel, boolean enabled) {
		Component comps[] = panel.getComponents();
		for (int i=0; i<comps.length; i++) {
			comps[i].setEnabled(enabled);
		}

	}

	public FontEditorUI(FontEditor new_fe) {

		fe = new_fe;
		setLayout(null);
		isShiftPressed = false;
		isEndPoint = false;
		charMouseDragged=false;

		// App icon
		Image img = new ImageIcon(this.getClass().getResource("imgs/icon.png")).getImage();
		setIconImage(img);

		// Tooltips configuration
		//UIManager.put("ToolTip.background", Color.YELLOW);
		//UIManager.put("ToolTip.foreground", Color.BLACK);
		UIManager.put("ToolTip.font", new Font("Dialog", Font.BOLD, 13));


		/************************************************
		 * General Stuff                                *
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
				mouseClicks = e.getClickCount();
				mouseX = e.getX();
				mouseY = e.getY();
				isEndPoint = false;
				if (e.getClickCount() == 1) {
					timer.schedule(new java.util.TimerTask() {
						public void run() {
							if (mouseClicks == 1)
								fe.fontAreaClicked(mouseX, mouseY, isShiftPressed, true);
							else {
								fe.fontAreaClicked(mouseX, mouseY, false, false);
								fe.readCharClicked();
							}
							mouseClicks = 0;
						}
					}, 200);
				}
			}
			public void mouseReleased(MouseEvent e) {
				isEndPoint = false;
			}
		});
		fontLabel.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				fe.fontAreaClicked(e.getX(), e.getY(), isEndPoint, false);
				isEndPoint = true;
			}
			public void mouseMoved(MouseEvent e) {
				fontLabel.requestFocus();
				fe.getCharacterAtPos(e.getX(), e.getY());
			}
		});
		// Needs focus set to object fontLabel to work
		fontLabel.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SHIFT)
					isShiftPressed = true;
			}
			public void keyReleased(KeyEvent e) {
				isShiftPressed = false;
			}
		});


		// Copy selected character to character area
		JButton readCharButton = new JButton(">>");
		readCharButton.setToolTipText("Copy FROM font");
		readCharButton.setBounds(550, 502, 55, 37);
		add(readCharButton);
		readCharButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.readCharClicked();
			}
		});

		
		// Copy edited character to font area
		JButton writeCharButton = new JButton("<<");
		writeCharButton.setToolTipText("Copy TO font");
		writeCharButton.setBounds(610, 502, 55, 37);
		add(writeCharButton);
		writeCharButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.writeCharClicked();
			}
		});


		// Character area
		panelChar = new JPanel();
		panelChar.setBorder(new LineBorder(new Color(92, 92, 92)));
		panelChar.setBounds(674, 326, 215, 215);
		panelChar.setBackground(new Color(0,255,0));
		add(panelChar);
		panelChar.setLayout(null);
		
		charLabel = new JLabel("");
		charLabel.setBounds(4, 4, 207, 207);
		panelChar.add(charLabel);
		charLabel.setBackground(new Color(0, 0, 0));
		charLabel.setOpaque(true);
		charLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				mouseClicks = e.getClickCount();
				mouseX = e.getX();
				mouseY = e.getY();
				if (e.getClickCount() == 1) {
					timer.schedule(new java.util.TimerTask() {
						public void run() {
							if (mouseClicks == 1)
								fe.charAreaClicked(mouseX, mouseY, 0);
							else
								fe.writeCharClicked();
							mouseClicks = 0;
						}
					}, 200);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (charMouseDragged)
					fe.charAreaReleased();
				charMouseDragged=false;
			}
		});
		charLabel.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				charMouseDragged=true;
				int opt=1;
				if (SwingUtilities.isRightMouseButton(e)) opt=2;
				fe.charAreaClicked(e.getX(), e.getY(), opt);
			}
		});


		// Logo		
		JPanel panelLogo = new JPanel();
		panelLogo.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelLogo.setBounds(765, 10, 122, 122);
		add(panelLogo);
		panelLogo.setLayout(null);
		
		JLabel logoLabel = new JLabel("");
		logoLabel.setBounds(3, 3, 115, 115);
		panelLogo.add(logoLabel);
		Image logo = new ImageIcon(this.getClass().getResource("imgs/logo.png")).getImage();
		logoLabel.setIcon(new ImageIcon(logo));

		
		/************************************************
		 * Character Control                            *
		 ************************************************/
		
		JPanel panelCharCtrl = new JPanel();
		panelCharCtrl.setBorder(new TitledBorder(null, "Character Editor", TitledBorder.LEADING, TitledBorder.TOP, new Font("Dialog", Font.PLAIN, 15), null));
		panelCharCtrl.setBounds(659, 145, 233, 170);
		add(panelCharCtrl);
		panelCharCtrl.setLayout(null);
		
		// Undo
		JButton undoButton = new JButton("");
		undoButton.setToolTipText("Undo");
		undoButton.setBounds(10, 25, 37, 37);
		img = new ImageIcon(this.getClass().getResource("imgs/btn_undo.png")).getImage();
		undoButton.setIcon(new ImageIcon(img));
		panelCharCtrl.add(undoButton);
		undoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.undoClicked();
			}
		});


		// Redo
		JButton redoButton = new JButton("");
		redoButton.setToolTipText("Redo");
		redoButton.setBounds(53, 25, 37, 37);
		img = new ImageIcon(this.getClass().getResource("imgs/btn_redo.png")).getImage();
		redoButton.setIcon(new ImageIcon(img));
		panelCharCtrl.add(redoButton);
		redoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.redoClicked();
			}
		});
		

		// Bold
		JButton boldButton = new JButton("");
		boldButton.setToolTipText("Bold");
		boldButton.setBounds(102, 25, 37, 37);
		img = new ImageIcon(this.getClass().getResource("imgs/btn_bold.png")).getImage();
		boldButton.setIcon(new ImageIcon(img));
		panelCharCtrl.add(boldButton);
		boldButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.boldClicked();
			}
		});
		

		// Italic
		JButton italicButton = new JButton("");
		italicButton.setToolTipText("Italic");
		italicButton.setBounds(145, 25, 37, 37);
		img = new ImageIcon(this.getClass().getResource("imgs/btn_italic.png")).getImage();
		italicButton.setIcon(new ImageIcon(img));
		panelCharCtrl.add(italicButton);
		italicButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.italicClicked();
			}
		});
		

		// Invert colors
		JButton invButton = new JButton("");
		invButton.setToolTipText("Invert colors");
		invButton.setBounds(188, 25, 37, 37);
		img = new ImageIcon(this.getClass().getResource("imgs/btn_inv.png")).getImage();
		invButton.setIcon(new ImageIcon(img));
		panelCharCtrl.add(invButton);
		invButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.invClicked();
			}
		});


		// All
		allButton = new JToggleButton("");
		allButton.setToolTipText("Apply to all font");
		allButton.setBounds(10, 70, 37, 37);
		img = new ImageIcon(this.getClass().getResource("imgs/btn_all.png")).getImage();
		allButton.setIcon(new ImageIcon(img));
		panelCharCtrl.add(allButton);
		allButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (allButton.isSelected()) {
					panelFont.setBackground(new Color(0, 255, 0));
					panelChar.setBackground(null);
				}
				else {
					panelChar.setBackground(new Color(0, 255, 0));
					panelFont.setBackground(null);
				}
			}
		});

		// Clear
		JButton clearButton = new JButton("");
		clearButton.setToolTipText("Clear");
		clearButton.setBounds(53, 70, 37, 37);
		img = new ImageIcon(this.getClass().getResource("imgs/btn_clear.png")).getImage();
		clearButton.setIcon(new ImageIcon(img));
		panelCharCtrl.add(clearButton);
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.clearClicked();
			}
		});
		

		// Flup up/down	
		JButton flipudButton = new JButton("");
		flipudButton.setToolTipText("Flip up/down");
		flipudButton.setBounds(102, 70, 37, 37);
		img = new ImageIcon(this.getClass().getResource("imgs/btn_flip_ud.png")).getImage();
		flipudButton.setIcon(new ImageIcon(img));
		panelCharCtrl.add(flipudButton);
		flipudButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.flipudClicked();
			}
		});
		

		// Mirror
		JButton mirrorButton = new JButton("");
		mirrorButton.setToolTipText("Mirror");
		mirrorButton.setBounds(145, 70, 37, 37);
		img = new ImageIcon(this.getClass().getResource("imgs/btn_flip_lr.png")).getImage();
		mirrorButton.setIcon(new ImageIcon(img));
		panelCharCtrl.add(mirrorButton);
		mirrorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.mirrorClicked();
			}
		});
		

		// Rotate
		JButton rotateButton = new JButton("");
		rotateButton.setToolTipText("Rotate");
		rotateButton.setBounds(188, 70, 37, 37);
		img = new ImageIcon(this.getClass().getResource("imgs/btn_rotate.png")).getImage();
		rotateButton.setIcon(new ImageIcon(img));
		panelCharCtrl.add(rotateButton);
		rotateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.rotateClicked();
			}
		});

		// Rotate bits - yes or no
		rotCheckBox = new JCheckBox("Rot");
		rotCheckBox.setBounds(5, 115, 50, 37);
		rotCheckBox.setToolTipText("Rotate pixels when shift");
		panelCharCtrl.add(rotCheckBox);

		// Shift left
		JButton shiftLeftButton = new JButton("");
		shiftLeftButton.setToolTipText("Shift left");
		shiftLeftButton.setBounds(59, 115, 37, 37);
		img = new ImageIcon(this.getClass().getResource("imgs/shift_left.png")).getImage();
		shiftLeftButton.setIcon(new ImageIcon(img));
		panelCharCtrl.add(shiftLeftButton);
		shiftLeftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.shiftLeftClicked(rotCheckBox.isSelected());
			}
		});

		// Shift right
		JButton shiftRightButton = new JButton("");
		shiftRightButton.setToolTipText("Shift right");
		shiftRightButton.setBounds(102, 115, 37, 37);
		img = new ImageIcon(this.getClass().getResource("imgs/shift_right.png")).getImage();
		shiftRightButton.setIcon(new ImageIcon(img));
		panelCharCtrl.add(shiftRightButton);
		shiftRightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.shiftRightClicked(rotCheckBox.isSelected());
			}
		});

		// Shift up
		JButton shiftUpButton = new JButton("");
		shiftUpButton.setToolTipText("Shift up");
		shiftUpButton.setBounds(145, 115, 37, 37);
		img = new ImageIcon(this.getClass().getResource("imgs/shift_up.png")).getImage();
		shiftUpButton.setIcon(new ImageIcon(img));
		panelCharCtrl.add(shiftUpButton);
		shiftUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.shiftUpClicked(rotCheckBox.isSelected());
			}
		});

		// Shift down
		JButton shiftDownButton = new JButton("");
		shiftDownButton.setToolTipText("Shift down");
		shiftDownButton.setBounds(188, 115, 37, 37);
		img = new ImageIcon(this.getClass().getResource("imgs/shift_down.png")).getImage();
		shiftDownButton.setIcon(new ImageIcon(img));
		panelCharCtrl.add(shiftDownButton);
		shiftDownButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.shiftDownClicked(rotCheckBox.isSelected());
			}
		});

	
		/************************************************
		 * Font Control                                 *
		 ************************************************/
		
		JPanel panelFontCtrl = new JPanel();
		panelFontCtrl.setLayout(null);
		panelFontCtrl.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Font", TitledBorder.LEADING, TitledBorder.TOP, new Font("Dialog", Font.PLAIN, 15), null));
		panelFontCtrl.setBounds(550, 10, 200, 120);
		add(panelFontCtrl);
		
		// Load
		JButton loadButton = new JButton("");		
		loadButton.setToolTipText("Load font");
		loadButton.setBounds(10, 25, 37, 37);
		img = new ImageIcon(this.getClass().getResource("imgs/fileopen.png")).getImage();
		loadButton.setIcon(new ImageIcon(img));
		panelFontCtrl.add(loadButton);
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.loadFontClicked();
			}
		});


		// Save
		JButton saveButton = new JButton("");
		saveButton.setToolTipText("Save font");
		saveButton.setBounds(10, 70, 37, 37);
		img = new ImageIcon(this.getClass().getResource("imgs/msxdisk.png")).getImage();
		saveButton.setIcon(new ImageIcon(img));
		panelFontCtrl.add(saveButton);
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.saveFontClicked();
			}
		});


		// Font extractor
		JButton fsearchButton = new JButton("");
		fsearchButton.setToolTipText("Extract font from file");
		img = new ImageIcon(this.getClass().getResource("imgs/fsearch.png")).getImage();
		fsearchButton.setIcon(new ImageIcon(img));
		fsearchButton.setBounds(55, 25, 37, 37);
		panelFontCtrl.add(fsearchButton);
		fsearchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.fSearchClicked();
			}
		});

		// Font reference
		fABCButton = new JToggleButton("");
		fABCButton.setToolTipText("Show ASCII default charcters");
		img = new ImageIcon(this.getClass().getResource("imgs/abc.png")).getImage();
		fABCButton.setIcon(new ImageIcon(img));
		fABCButton.setBounds(55, 70, 37, 37);
		panelFontCtrl.add(fABCButton);
		fABCButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.setFontReference(fABCButton.isSelected());
			}
		});

		// PC font
		JButton pcfontButton = new JButton("");
		pcfontButton.setToolTipText("Import PC font");
		img = new ImageIcon(this.getClass().getResource("imgs/pc_font.png")).getImage();
		pcfontButton.setIcon(new ImageIcon(img));
		pcfontButton.setBounds(100, 25, 37, 37);
		panelFontCtrl.add(pcfontButton);
		pcfontButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.pcFontClicked();
			}
		});


		// Mosaic
		JButton mosaicButton = new JButton("");
		mosaicButton.setToolTipText("Create mosaic");
		img = new ImageIcon(this.getClass().getResource("imgs/mosaic.png")).getImage();
		mosaicButton.setIcon(new ImageIcon(img));
		mosaicButton.setBounds(100, 70, 37, 37);
		panelFontCtrl.add(mosaicButton);
		mosaicButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.mosaicClicked();
			}
		});


		// Copy
		JButton copyButton = new JButton("");
		copyButton.setToolTipText("Copy selected characters");
		img = new ImageIcon(this.getClass().getResource("imgs/btn_copy.png")).getImage();
		copyButton.setIcon(new ImageIcon(img));
		copyButton.setBounds(145, 25, 37, 37);
		panelFontCtrl.add(copyButton);
		copyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.copySelectedCharacters();
			}
		});


		// Paste
		JButton pasteButton = new JButton("");
		pasteButton.setToolTipText("Paste selected characters");
		img = new ImageIcon(this.getClass().getResource("imgs/btn_paste.png")).getImage();
		pasteButton.setIcon(new ImageIcon(img));
		pasteButton.setBounds(145, 70, 37, 37);
		panelFontCtrl.add(pasteButton);
		pasteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.pasteSelectedCharacters();
			}
		});


		// Help mouse
		JButton mhelpButton = new JButton("");
		mhelpButton.setToolTipText("Mouse help");
		mhelpButton.setBounds(585, 448, 45, 45);
		img = new ImageIcon(this.getClass().getResource("imgs/btn_mouse.png")).getImage();
		mhelpButton.setIcon(new ImageIcon(img));
		add(mhelpButton);
		mhelpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Wizard wizard = new Wizard();
				wizard.setModal(true);
				wizard.setVisible(true);
			}
		});


		/************************************************
		 * Font color control                           *
		 ************************************************/
		
		JPanel fontColorCtrl = new JPanel();
		fontColorCtrl.setBorder(new TitledBorder(null, "Color", TitledBorder.LEADING, TitledBorder.TOP, new Font("Dialog", Font.PLAIN, 15), null));
		fontColorCtrl.setBounds(550, 145, 100, 170);
		add(fontColorCtrl);
		fontColorCtrl.setLayout(null);

		JLabel colorLabel = new JLabel("");
		colorLabel.setBounds(15, 25, 72, 72);
		fontColorCtrl.add(colorLabel);
		img = new ImageIcon(this.getClass().getResource("imgs/msx1_palette.png")).getImage();
		colorLabel.setIcon(new ImageIcon(img));
		colorLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int i, x = (e.getX() / 18), y = (e.getY() / 18);
				i = (x % 4) + y*4;
				if (e.isMetaDown()) {
					bgColorLabel.setBackground(new Color(msx_palette[i]));
					bg_color = i;
				}
				else {
					frontColorLabel.setBackground(new Color(msx_palette[i]));
					front_color = i;
				}
			}
		});

		frontColorLabel = new JLabel("");
		frontColorLabel.setBounds(15, 105, 18, 18);
		frontColorLabel.setBorder(new LineBorder(new Color(92, 92, 92)));
		frontColorLabel.setBackground(new Color(255,255,255));
		frontColorLabel.setOpaque(true);
		fontColorCtrl.add(frontColorLabel);

		JLabel frontColorTextLabel = new JLabel("F");
		frontColorTextLabel.setBounds(35, 105, 18, 18);
		fontColorCtrl.add(frontColorTextLabel);

		bgColorLabel = new JLabel("");
		bgColorLabel.setBounds(51, 105, 18, 18);
		bgColorLabel.setBorder(new LineBorder(new Color(92, 92, 92)));
		bgColorLabel.setBackground(new Color(0,0,0));
		bgColorLabel.setOpaque(true);
		fontColorCtrl.add(bgColorLabel);

		JLabel bgColorTextLabel = new JLabel("B");
		bgColorTextLabel.setBounds(71, 105, 18, 18);
		fontColorCtrl.add(bgColorTextLabel);

		JButton applyButton = new JButton("Apply");
		applyButton.setToolTipText("Apply colors to selected octet(s)");
		applyButton.setBounds(10, 133, 80, 25);
		fontColorCtrl.add(applyButton);
		applyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.changeFontColors(front_color, bg_color);
			}
		});


		/************************************************
		 * Font Browser                                 *
		 ************************************************/
		
		panelFontNav = new JPanel();
		panelFontNav.setLayout(null);
		panelFontNav.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Font browser", TitledBorder.LEADING, TitledBorder.TOP, new Font("Dialog", Font.PLAIN, 15), null));
		panelFontNav.setBounds(10, 545, 529, 67);
		panelFontNav.setEnabled(false);
		add(panelFontNav);
		
		// Previous font
		JButton prevButton = new JButton("");
		prevButton.setToolTipText("Previous font");
		img = new ImageIcon(this.getClass().getResource("imgs/btn_left.png")).getImage();
		prevButton.setIcon(new ImageIcon(img));
		prevButton.setBounds(10, 20, 45, 37);
		panelFontNav.add(prevButton);
		prevButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.prevClicked();
			}
		});
		

		// Next font
		JButton nextButton = new JButton("");
		nextButton.setToolTipText("Next font");
		img = new ImageIcon(this.getClass().getResource("imgs/btn_right.png")).getImage();
		nextButton.setIcon(new ImageIcon(img));
		nextButton.setBounds(472, 20, 45, 37);
		panelFontNav.add(nextButton);
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fe.nextClicked();
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
					fe.fontTextFieldEvent(Integer.parseInt(fontTextField.getText()), false);
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
					fe.fontTextFieldEvent(Integer.parseInt(fontTextField.getText()),true);
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
		setPanelEnabled(panelFontNav, false);


		/************************************************
		 * Current character panel                      *
		 ************************************************/

		JPanel panelCharNo = new JPanel();
		panelCharNo.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Character", TitledBorder.LEADING, TitledBorder.TOP, new Font("Dialog", Font.PLAIN, 15), null));
		panelCharNo.setBounds(555, 320, 110, 120);
		panelCharNo.setLayout(null);
		add(panelCharNo);

		JLabel lblCharacter = new JLabel("Cursor on:");
		lblCharacter.setBounds(5, 25, 80, 15);
		panelCharNo.add(lblCharacter);
		
		lblAscii = new JLabel("None");
		lblAscii.setBounds(5, 45, 77, 15);
		panelCharNo.add(lblAscii);
		
		JLabel lblSelCharacter = new JLabel("Selected:");
		lblSelCharacter.setBounds(5, 75, 80, 15);
		panelCharNo.add(lblSelCharacter);
		
		lblSelAscii = new JLabel("None");
		lblSelAscii.setBounds(5, 95, 77, 15);
		panelCharNo.add(lblSelAscii);
		
		
		/************************************************
		 * MarMSX                                       *
		 ************************************************/
		
		JLabel lblMarMSX = new JLabel("");
		lblMarMSX.setBounds(678, 553, 207, 56);
		img = new ImageIcon(this.getClass().getResource("imgs/marmsx.png")).getImage();
		lblMarMSX.setIcon(new ImageIcon(img));
		add(lblMarMSX);
	}
}
