/***************************************************************************
 *   Class Clipboard                                                       *
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
 * Designed for storing selected font and the charcters range              *
 * MVC: Model                                                              *
 ***************************************************************************/

public class Clipboard {

	private int chr_begin;
	private int chr_end;
	private int font_id;
	boolean selection_mod;
	boolean is_empty;

	public Clipboard() {
		selection_mod = true;
		is_empty = true;
	}

	public void set(int begin, int end, int id) {
		chr_begin = begin;
		chr_end = end;
		font_id = id;
		selection_mod = false;
		is_empty = false;
	}

	// This flag notifies if early selection was modified
	// Modified selecton may cause inconsistency
	public void setModified() {
		selection_mod = true;
	}

	public boolean isModified() {
		return selection_mod;
	}

	public boolean isEmpty() {
		return is_empty;
	}

	public int getBegin() {
		return chr_begin;
	}

	public int getEnd() {
		return chr_end;
	}

	public int getFontID() {
		return font_id;
	}

	// Debug
	public void print() {
		System.out.println("First character: " + chr_begin);
		System.out.println("Last character: " + chr_end);
		System.out.println("Current font ID: " + font_id);
	}
}
