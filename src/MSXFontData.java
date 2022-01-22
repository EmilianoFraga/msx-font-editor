/***************************************************************************
 *   Class MSXFontData                                                     *
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

public class MSXFontData {

	// 256 characters, size of 8 bytes and file header of 7 bytes
	// 256 x 8 + 7 = 2055
	private byte font_data[] = new byte[2048];
	private byte color_octets[] = new byte[32];

	public MSXFontData() {
		setDefaultColors();
	}

	public MSXFontData(byte new_data[]) {
		setFont(new_data);
		setDefaultColors();
	}

	public MSXFontData(byte new_data[], byte new_colors[]) {
		setFont(new_data);
		setColors(new_colors);
	}

	private void setDefaultColors() {
		for (int i=0; i<32; i++)
			color_octets[i] = (byte) 0xF0;
	}

	public byte getColor(int ascii_code) {
		if (ascii_code < 0 || ascii_code > 255)
			return 0;

		return color_octets[ascii_code/8];
	}

	public byte[] getColors() {
		byte tmp_data[] = new byte[32];

		for (int i=0; i<32; i++)
			tmp_data[i] = color_octets[i];

		return tmp_data;
	}

	public void setColors(byte new_colors[]) {
		if (new_colors.length != 32)
			return;

		for (int i=0; i<32; i++)
			color_octets[i] = new_colors[i];
	}

	public byte[] getFont() {
		byte tmp_data[] = new byte[2048];

		for (int i=0; i<2048; i++)
			tmp_data[i] = font_data[i];

		return tmp_data;
	}

	public void setFont(byte new_data[]) {
		if (new_data.length != 2048)
			return;

		for (int i=0; i<2048; i++)
			font_data[i] = new_data[i];
	}

}
