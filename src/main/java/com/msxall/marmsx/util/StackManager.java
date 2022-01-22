/***************************************************************************
 *   Class StackManager                                                    *
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
 * Designed for managing data stack                                        *
 * MVC: Model / Value Object (VO)                                          *
 ***************************************************************************/
package com.msxall.marmsx.util;

import java.util.ArrayList;

public class StackManager<E> {
	private ArrayList<E> list = new ArrayList<E>();
	private int current_stack_pos=-1;

	public void save(E item) {
		if (current_stack_pos < list.size()-1)
			removeTopElements();
		list.add(item);
		current_stack_pos++;
	}

	public E top() {
		return list.get(current_stack_pos);
	}

	public E undo() {
		if (current_stack_pos < 1)
			return null;

		return list.get(--current_stack_pos);
	}

	public E redo() {
		if (current_stack_pos >= list.size()-1)
			return null;

		return list.get(++current_stack_pos);
	}

	private void removeTopElements() {
		for (int i=list.size()-1; i>current_stack_pos; i--)
			list.remove(i);
	}
}
