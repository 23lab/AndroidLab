package us.stupidx.tools;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.zip.ZipException;

public class ZipCommentReader {

	public static final class ZipLong implements Cloneable {

		private long value;

		/**
		 * Create instance from bytes.
		 * 
		 * @since 1.1
		 */
		public ZipLong(byte[] bytes) {
			this(bytes, 0);
		}

		/**
		 * Create instance from the four bytes starting at offset.
		 * 
		 * @since 1.1
		 */
		public ZipLong(byte[] bytes, int offset) {
			value = bytes[offset + 3] << 24 & 0xFF000000L;
			value += bytes[offset + 2] << 16 & 0xFF0000;
			value += bytes[offset + 1] << 8 & 0xFF00;
			value += bytes[offset] & 0xFF;
		}

		/**
		 * Create instance from a number.
		 * 
		 * @since 1.1
		 */
		public ZipLong(long value) {
			this.value = value;
		}

		/**
		 * Override to make two instances with same value equal.
		 * 
		 * @since 1.1
		 */
		@Override
		public boolean equals(Object o) {
			if (o == null || !(o instanceof ZipLong)) {
				return false;
			}
			return value == ((ZipLong) o).getValue();
		}

		/**
		 * Get value as two bytes in big endian byte order.
		 * 
		 * @since 1.1
		 */
		public byte[] getBytes() {
			byte[] result = new byte[4];
			result[0] = (byte) (value & 0xFF);
			result[1] = (byte) ((value & 0xFF00) >> 8);
			result[2] = (byte) ((value & 0xFF0000) >> 16);
			result[3] = (byte) ((value & 0xFF000000l) >> 24);
			return result;
		}

		/**
		 * Get value as Java int.
		 * 
		 * @since 1.1
		 */
		public long getValue() {
			return value;
		}

		/**
		 * Override to make two instances with same value equal.
		 * 
		 * @since 1.1
		 */
		@Override
		public int hashCode() {
			return (int) value;
		}
	}

	public static final class ZipShort implements Cloneable {

		private int value;

		/**
		 * Create instance from bytes.
		 * 
		 * @since 1.1
		 */
		public ZipShort(byte[] bytes) {
			this(bytes, 0);
		}

		/**
		 * Create instance from the two bytes starting at offset.
		 * 
		 * @since 1.1
		 */
		public ZipShort(byte[] bytes, int offset) {
			value = bytes[offset + 1] << 8 & 0xFF00;
			value += bytes[offset] & 0xFF;
		}

		/**
		 * Create instance from a number.
		 * 
		 * @since 1.1
		 */
		public ZipShort(int value) {
			this.value = value;
		}

		/**
		 * Override to make two instances with same value equal.
		 * 
		 * @since 1.1
		 */
		@Override
		public boolean equals(Object o) {
			if (o == null || !(o instanceof ZipShort)) {
				return false;
			}
			return value == ((ZipShort) o).getValue();
		}

		/**
		 * Get value as two bytes in big endian byte order.
		 * 
		 * @since 1.1
		 */
		public byte[] getBytes() {
			byte[] result = new byte[2];
			result[0] = (byte) (value & 0xFF);
			result[1] = (byte) ((value & 0xFF00) >> 8);
			return result;
		}

		/**
		 * Get value as Java int.
		 * 
		 * @since 1.1
		 */
		public int getValue() {
			return value;
		}

		/**
		 * Override to make two instances with same value equal.
		 * 
		 * @since 1.1
		 */
		@Override
		public int hashCode() {
			return value;
		}
	}

	private static final int MIN_EOCD_SIZE =
	/* end of central dir signature */4 +
	/* number of this disk */2 +
	/* number of the disk with the */+
	/* start of the central directory */2 +
	/* total number of entries in */+
	/* the central dir on this disk */2 +
	/* total number of entries in */+
	/* the central dir */2 +
	/* size of the central directory */4 +
	/* offset of start of central */+
	/* directory with respect to */+
	/* the starting disk number */4 +
	/* zipfile comment length */2;
	private static final int CFD_LOCATOR_OFFSET =
	/* end of central dir signature */4 +
	/* number of this disk */2 +
	/* number of the disk with the */+
	/* start of the central directory */2 +
	/* total number of entries in */+
	/* the central dir on this disk */2 +
	/* total number of entries in */+
	/* the central dir */2 +
	/* size of the central directory */4;

	protected static final ZipLong EOCD_SIG = new ZipLong(0X06054B50L);

	public static void main(String[] args) throws Throwable {
		positionAtCentralDirectory("D:\\lab.zip");
	}

	public static String positionAtCentralDirectory(String filePath) {
	    if (filePath == null || "".equals(filePath)) {
	        return "";
	    }
		RandomAccessFile archive = null;
		try {
			archive = new RandomAccessFile(new File(filePath), "r");
			// 跳转到一个接近0X06054B50L标识的位置
			long off = archive.length() - MIN_EOCD_SIZE;
			archive.seek(off);
			byte[] sig = EOCD_SIG.getBytes();
			int curr = archive.read();

			boolean found = false;
			// 查找0X06054B50L标记的开头位置
			while (curr != -1) {
				if (curr == sig[0]) {
					curr = archive.read();
					if (curr == sig[1]) {
						curr = archive.read();
						if (curr == sig[2]) {
							curr = archive.read();
							if (curr == sig[3]) {
								found = true;
								break;
							}
						}
					}
				}
				archive.seek(--off);
				curr = archive.read();
			}
			if (!found) {
				throw new ZipException("archive is not a ZIP archive");
			}

			// 跳转到注释的长度字段位置
			archive.seek(off + CFD_LOCATOR_OFFSET + 4);
			// 读取注释长度
			byte[] cfdOffset = new byte[2];
			archive.readFully(cfdOffset);
			int value = new ZipShort(cfdOffset).getValue();

			// 根据注释长度读取注释内容
			cfdOffset = new byte[value];
			archive.readFully(cfdOffset);

			if (null != archive) {
				archive.close();
			}
			return new String(cfdOffset);
		} catch (IOException e) {
		    e.printStackTrace();
		    Logger.d("ZipCommentReader read file error");
		    return "";
		}
	}

}
