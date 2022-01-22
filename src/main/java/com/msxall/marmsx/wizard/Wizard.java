package com.msxall.marmsx.wizard;

import com.msxall.marmsx.util.ResourceLoader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Wizard extends JDialog {

	private JPanel contentPanel = new JPanel();
	private ArrayList<JPanel> panel_list = new ArrayList<JPanel>();
	private JButton backButton;
	private JButton nextButton;
	private int page = 0;

	public Wizard() {
		setBounds(100, 100, 550, 400);
		setTitle("MSX Font Editor Wizard");
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);
		add(contentPanel, BorderLayout.CENTER);

		JPanel buttonPane = new JPanel();
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		buttonPane.setLayout(new BorderLayout(0, 0));
		
		buttonPane.add(new JSeparator(), BorderLayout.NORTH);
		
		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.setBorder(new EmptyBorder(5, 10, 5, 10));
		
		backButton = new JButton("< Back");
		horizontalBox.add(backButton);
		getRootPane().setDefaultButton(backButton);
		backButton.setEnabled(false);
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setPrevPage();
			}
		});
		
		horizontalBox.add(Box.createHorizontalStrut(10));

		nextButton = new JButton("Next >");		
		horizontalBox.add(nextButton);
		getRootPane().setDefaultButton(nextButton);
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setNextPage();
			}
		});
		
		horizontalBox.add(Box.createHorizontalStrut(30));

		JButton okButton = new JButton("Ok");
		horizontalBox.add(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		buttonPane.add(horizontalBox, BorderLayout.EAST);

		startPanels();	
	}

	// Change page
	private void changePage() {
		contentPanel.removeAll();
		contentPanel.add(panel_list.get(page));
		contentPanel.repaint();
	}

	// Set next page
	private void setNextPage() {
		if (page >= panel_list.size() - 1)
			return;
		page++;

		if (page == panel_list.size() - 1)
			nextButton.setEnabled(false);

		backButton.setEnabled(true);
		changePage();
	}

	// Set previous page
	private void setPrevPage() {
		if (page <= 0)
			return;
		page--;

		if (page == 0)
			backButton.setEnabled(false);

		nextButton.setEnabled(true);
		changePage();
	}

	// Panel factory
	private JPanel createPanel(String msg) {
		JPanel panel = new JPanel();

		panel.setBounds(0, 0, 548, 334);
		panel.setLayout(null);
		
		// Component 0 - title
		JLabel lblMsxFontEditor = new JLabel("MSX Font Editor Wizard");
		lblMsxFontEditor.setHorizontalAlignment(SwingConstants.CENTER);
		lblMsxFontEditor.setFont(new Font("Dialog", Font.BOLD, 30));
		lblMsxFontEditor.setBounds(0, 12, 548, 36);
		lblMsxFontEditor.setBackground(new Color(192, 192, 192));
		lblMsxFontEditor.setOpaque(true);
		panel.add(lblMsxFontEditor);

		// Component 1 - description text
		JLabel msgLabel = new JLabel(msg);
		msgLabel.setVerticalAlignment(SwingConstants.TOP);
		msgLabel.setFont(new Font("Dialog", Font.BOLD, 14));
		msgLabel.setBounds(10, 60, 526, 262);
		panel.add(msgLabel);

		return panel;
	}

	// Panel factory
	private JPanel createPanel(String msg, Rectangle[] bounds, String... img_path) {
		JPanel panel = createPanel(msg);

		// Images
		for (int i = 0; i < img_path.length; i++) {
			JLabel imgLabel = new JLabel("");
			panel.add(imgLabel);
			imgLabel.setBounds(bounds[i]);
			imgLabel.setIcon(ResourceLoader.loadImageIconFromAbsoluteResource(img_path[i]));
		}

		return panel;
	}

	//
	// Here, the wizard data is filled
	//

	private void startPanels() {
		JPanel panel;
		String msg;
		Rectangle[] bounds = new Rectangle[2];

		// Page 1
		msg = "<html>MSX Font Editor is quite simple to use.<br>This tutorial will show how to operate the program using the mouse.<br><br>Click on the next button ...</html>";
		panel = createPanel(msg);
		panel_list.add(panel);
		contentPanel.add(panel);

		// Page 2
		msg = "<html>The first area is the <i>font area</i>. Here, we can see all the 256 characters from the current font.</html>";
		bounds[0] = new Rectangle(300, 100, 200, 200);
		panel = createPanel(msg, bounds, "/wizard/font_panel.png");
		panel_list.add(panel);

		// Page 3
		msg = "<html>Left click on the desired character to select it.</html>";
		bounds[0] = new Rectangle(50, 100, 232, 166);
		bounds[1] = new Rectangle(340, 130, 68, 106);
		panel = createPanel(msg, bounds, "/wizard/font_01.png", "/wizard/mouse_left.png");
		panel_list.add(panel);

		// Page 4
		msg = "<html>Double click on the desired character to copy it to the character editor area.</html>";
		bounds[0] = new Rectangle(50, 110, 232, 166);
		bounds[1] = new Rectangle(340, 140, 68, 106);
		panel = createPanel(msg, bounds, "/wizard/font_02.png", "/wizard/mouse_dbl.png");
		panel_list.add(panel);

		// Page 5
		msg = "<html>Click on a character and drag the mouse to select multiple characters.<br>This operation can also be done by clicking on a character, holding the SHIFT key and clicking on the last character.</html>";
		bounds[0] = new Rectangle(50, 150, 232, 166);
		bounds[1] = new Rectangle(340, 180, 68, 106);
		panel = createPanel(msg, bounds, "/wizard/font_03.png", "/wizard/mouse_left.png");
		panel_list.add(panel);

		// Page 6
		msg = "<html>Note:<br><br>If multiple characters are selected, no character will be copied to the character editor.<br>Multiple selection is used to copy characters between fonts or to apply some character tool effect when the button ALL is pressed.</html>";
		panel = createPanel(msg, null, null);
		panel_list.add(panel);

		// Page 7
		msg = "<html>The second area is the <i>character area</i>. Here, we define the character shape.</html>";
		bounds[0] = new Rectangle(300, 100, 150, 150);
		panel = createPanel(msg, bounds, "/wizard/char_panel.png");
		panel_list.add(panel);

		// Page 8
		msg = "<html>Left click on a pixel to invert its value. If it is black, it turns white. If it is white, it turns black.</html>";
		bounds[0] = new Rectangle(50, 100, 232, 166);
		bounds[1] = new Rectangle(280, 130, 68, 106);
		panel = createPanel(msg, bounds, "/wizard/char_01.png", "/wizard/mouse_left.png");
		panel_list.add(panel);

		// Page 9
		msg = "<html>Hold left click and drag the mouse to fill pixels only with white color.</html>";
		bounds[0] = new Rectangle(50, 100, 232, 166);
		bounds[1] = new Rectangle(280, 130, 68, 106);
		panel = createPanel(msg, bounds, "/wizard/char_02.png", "/wizard/mouse_left.png");
		panel_list.add(panel);

		// Page 10
		msg = "<html>Hold right click and drag the mouse to fill pixels only with black color.</html>";
		bounds[0] = new Rectangle(50, 100, 232, 166);
		bounds[1] = new Rectangle(280, 130, 68, 106);
		panel = createPanel(msg, bounds, "/wizard/char_03.png", "/wizard/mouse_right.png");
		panel_list.add(panel);

		// Page 11
		msg = "<html>Double click on the editor to copy the edited character back to the font editor. It will only work if there is a single character selected in the font area.</html>";
		bounds[0] = new Rectangle(50, 120, 232, 166);
		bounds[1] = new Rectangle(280, 150, 68, 106);
		panel = createPanel(msg, bounds, "/wizard/char_04.png", "/wizard/mouse_dbl.png");
		panel_list.add(panel);

		// Page 12
		msg = "<html>Since version 1.5, it is possible to change font color octets like screen 1.</html>";
		bounds[0] = new Rectangle(300, 100, 200, 200);
		panel = createPanel(msg, bounds, "/wizard/font_color.png");
		panel_list.add(panel);

		// Page 13
		msg = "<html>Left click on the desired color to select the front color (ink).</html>";
		bounds[0] = new Rectangle(100, 100, 109, 178);
		bounds[1] = new Rectangle(250, 136, 68, 106);
		panel = createPanel(msg, bounds, "/wizard/font_color_01.png", "/wizard/mouse_left.png");
		panel_list.add(panel);

		// Page 14
		msg = "<html>Right click on the desired color to select the background color (paper).</html>";
		bounds[0] = new Rectangle(100, 100, 109, 178);
		bounds[1] = new Rectangle(250, 136, 68, 106);
		panel = createPanel(msg, bounds, "/wizard/font_color_02.png", "/wizard/mouse_right.png");
		panel_list.add(panel);

		// Page 15
		msg = "<html>Select as many characters you want. Then, press apply button to change colors.<br>If no character is selected, ALL the characters will be affected.</html>";
		bounds[0] = new Rectangle(50, 126, 232, 166);
		bounds[1] = new Rectangle(300, 120, 109, 178);
		panel = createPanel(msg, bounds, "/wizard/font_03.png", "/wizard/font_color_03.png");
		panel_list.add(panel);

		// Page 16
		msg = "<html>You can undo color changes by selecting ALL and pressing undo button on the Character editor.";
		bounds[0] = new Rectangle(270, 110, 238, 175);
		panel = createPanel(msg, bounds, "/wizard/undo.png");
		panel_list.add(panel);

		// Final Page
		msg = "<html><u>MSX Font Editor</u><br><br>Developed by: Marcelo Silveira<br>Homepage: http://marmsx.msxall.com<br>E-mail: flamar98@hotmail.com<br><br>License: GNU-GPL v. 3.x</html>";
		panel = createPanel(msg, null, null);
		panel_list.add(panel);
		contentPanel.add(panel);
	}

}
