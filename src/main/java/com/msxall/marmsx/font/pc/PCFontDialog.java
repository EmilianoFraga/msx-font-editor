/***************************************************************************
 *   Class PCFontDialog                                                    *
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
 * PC font interface control                                               *
 * MVC: Control                                                            *
 ***************************************************************************/
package com.msxall.marmsx.font.pc;

import com.msxall.marmsx.font.manager.FontManager;

public class PCFontDialog {

	private FontManager fm;
	private PCFontDialogUI window;

	public PCFontDialog(FontManager new_fm) {
		// Links to Font Manager
		fm = new_fm;

		// Create form
		window = new PCFontDialogUI(this);
		window.setTitle("PC Font");
		window.setBounds(100, 100, 378, 230);
		window.setModal(true);
		window.setVisible(true);
	}

	public void okButtonClicked(String s) {
		fm.createPCFont(s);
		window.dispose();
	}

}
