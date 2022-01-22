/***************************************************************************
 *   Class MSXCharacterData                                                *
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
 * Designed for storing character data                                     *
 * MVC: Model / Value Object (VO)                                          *
 ***************************************************************************/

public class MSXCharacterData {

	private byte data[] = new byte[8];

	public MSXCharacterData() {
	}

	public MSXCharacterData(byte new_data[]) {
		setCharacter(new_data);
	}

	public void setCharacter(byte new_data[]) {
		if (new_data.length !=8)
			return;

		for (int i=0; i<8; i++)
			data[i] = new_data[i];
	}

	public byte[] getCharacter() {
		byte tmp_data[] = new byte[8];

		for (int i=0; i<8; i++)
			tmp_data[i] = data[i];

		return tmp_data;
	}
}
