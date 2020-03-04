package com.sonim.bft.compress;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TAR {
	final static int BUFFER = 2048;

	private TAR() {

	}

	public static void compress(String name, File... files) throws IOException {
		System.out.println("starting compresion");
		try (TarArchiveOutputStream out = getTarArchiveOutputStream(name)) {
			for (File file : files) {
				addToArchiveCompression(out, file, ".");
			}
		}
		System.out.println("Finished Compresion");
	}

	public static void decompress(String in, File out) throws IOException {
		System.out.println(in);
		try (TarArchiveInputStream fin = new TarArchiveInputStream(
				new GzipCompressorInputStream(new FileInputStream(in)))) {
			TarArchiveEntry entry;
			FileOutputStream fout = null;
			while ((entry = fin.getNextTarEntry()) != null) {
				if (entry.isDirectory()) {
					continue;
				}
				File curfile = new File(out, entry.getName());
				File parent = curfile.getParentFile();
				if (!parent.exists()) {
					parent.mkdirs();
				}
				fout = new FileOutputStream(curfile);
				IOUtils.copy(fin, fout);
			}
			fin.close();
			fout.close();
			System.out.println("clsoed");
		}

	}

	private static TarArchiveOutputStream getTarArchiveOutputStream(String name) throws IOException {
		TarArchiveOutputStream taos = new TarArchiveOutputStream(new FileOutputStream(name));
		// TAR has an 8 gig file limit by default, this gets around that
		taos.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_STAR);
		// TAR originally didn't support long file names, so enable the support for it
		taos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
		taos.setAddPaxHeadersForNonAsciiNames(true);
		return taos;
	}

	private static void addToArchiveCompression(TarArchiveOutputStream out, File file, String dir) throws IOException {
		String entry = dir + File.separator + file.getName();
		if (file.isFile()) {
			out.putArchiveEntry(new TarArchiveEntry(file, entry));
			try (FileInputStream in = new FileInputStream(file)) {
				IOUtils.copy(in, out);
			}
			out.closeArchiveEntry();
		} else if (file.isDirectory()) {
			File[] children = file.listFiles();
			if (children != null) {
				for (File child : children) {
					addToArchiveCompression(out, child, entry);
				}
			}
		} else {
			System.out.println(file.getName() + " is not supported");
		}
	}

	public static void unTar(String name,String dir) {
		try {
			FileInputStream fin = new FileInputStream(name);
			BufferedInputStream in = new BufferedInputStream(fin);
			GzipCompressorInputStream gzIn = new GzipCompressorInputStream(in);
			TarArchiveInputStream tarIn = new TarArchiveInputStream(gzIn);

			TarArchiveEntry entry = null;

			/** Read the tar entries using the getNextEntry method **/

			while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {

				System.out.println("Extracting: " + entry.getName());

				/** If the entry is a directory, create the directory. **/

				if (entry.isDirectory()) {

					File f = new File(dir+entry.getName());
					f.mkdirs();
				}
				/**
				 * If the entry is a file,write the decompressed file to the disk and close
				 * destination stream.
				 **/
				else {
					int count;
					byte data[] = new byte[BUFFER];

					FileOutputStream fos = new FileOutputStream(dir+entry.getName());
					BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
					while ((count = tarIn.read(data, 0, BUFFER)) != -1) {
						dest.write(data, 0, count);
					}
					dest.close();
				}
			}

			/** Close the input stream **/

			tarIn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("untar completed successfully!!");
	}
}