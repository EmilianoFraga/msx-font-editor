/***************************************************************************
 *   Class MSXFontDAO                                                      *
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
 * Designed for MSX font services                                          *
 * MVC: Model / Business Object (BO) + Data Access Object (DAO)            *
 ***************************************************************************/

import java.util.Formatter;
import java.util.FormatterClosedException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

public class MSXFontDAO {

	//
	// Load services
	//

	public boolean loadFontDump(String filename, byte [] font_data) {
		boolean success_flag=true;
		try {
			InputStream is = new FileInputStream(filename);
			is.skip(7);
			is.read(font_data);
		}
		catch (IOException e) {
			success_flag=false;
		}
		return success_flag;
	}

	public boolean loadFontColorMap(String filename, byte [] font_color) {
		byte [] data = new byte[39];
		boolean success_flag=true;
		try {
			InputStream is = new FileInputStream(filename);
			is.read(data);
			for (int i=0; i<32; i++)
				font_color[i] = data[i+7];
		}
		catch (IOException e) {
			success_flag=false;
		}
		return success_flag;
	}

	//
	// Save services
	//

	// MSX VRAM dump with header	
	public boolean saveFontDump(String filename, byte [] font_data, boolean has_header) {
		boolean success_flag=true;
		byte header[] = {(byte)0xFE, 0x00, (byte)0x92, (byte)0xFF, (byte)0x99, 0x00, (byte)0x92 };

		try {
			OutputStream os = new FileOutputStream(filename);
			if (has_header)
				os.write(header);
			os.write(font_data);
		}
		catch (IOException e) {
			success_flag=false;
		}
		return success_flag;
	}

	// MSX VRAM ASCII format
	public boolean saveFontASCII(String filename, byte [] font_data, byte [] font_color) {
		boolean success_flag=true;
		Formatter output;

		try {
			output = new Formatter(filename);
		} catch (Exception e) {
			return false;
		}

		// Write program
		try {
			output.format("MarMSX - MSX Font Editor%c%c", 13, 10); // add "/n/b"
			output.format("%c%c", 13, 10);
			output.format("Characters:");

			for (int i=0; i<font_data.length; i++) {
				if (i % 16 == 0)
					output.format("%c%cDB ", 13, 10);
				if ((i+1) % 16 != 0 && (i+1) < font_data.length)
					output.format("$%02x,", font_data[i]);
				else
					output.format("$%02x", font_data[i]);
			}

			output.format("%c%c%c%c", 13, 10, 13, 10);
			output.format("Colors:");

			for (int i=0; i<font_color.length; i++) {
				if (i % 16 == 0)
					output.format("%c%cDB ", 13, 10);
				if ((i+1) % 16 != 0 && (i+1) < font_color.length)
					output.format("$%02x,", font_color[i]);
				else
					output.format("$%02x", font_color[i]);
			}

		} catch (FormatterClosedException e) {
			return false;
		}

		output.close();

		return success_flag;
	}

	// MSX VRAM screen 1 color map
	public boolean saveFontColorMap(String filename, byte [] font_color) {
		boolean success_flag=true;
		byte header[] = {(byte)0xFE, 0, 0x20, 0x1F, 0x20, 0, 0x20 };

		try {
			OutputStream os = new FileOutputStream(filename);
			os.write(header);
			os.write(font_color);
		}
		catch (IOException e) {
			success_flag=false;
		}
		return success_flag;
	}

	// MSX characters as bitmap
	public boolean saveFontBitmap(String filename, BufferedImage img) {
		File f = new File(filename);
		try {
			ImageIO.write(img, "gif", f);
		}
		catch(IOException e) {
			return false;
		}

		return true;
	}

}
