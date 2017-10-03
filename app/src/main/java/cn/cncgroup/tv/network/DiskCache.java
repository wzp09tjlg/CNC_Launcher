package cn.cncgroup.tv.network;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 硬盘缓存
 */
public class DiskCache
{
	/**
	 * 信息与内容的分界
	 */
	private static final int CACHE_MAGIC = 0x20150427;
	private final Map<String, CacheHeader> mEntries = new LinkedHashMap<String, CacheHeader>(
	        16, .75f, true);
	/**
	 * 缓存目录位置
	 */
	private final File mRootDirectory;

	public DiskCache(File rootDirectory)
	{
		mRootDirectory = rootDirectory;
	}

	private static int read(InputStream is) throws IOException
	{
		int b = is.read();
		if (b == -1)
		{
			throw new EOFException();
		}
		return b;
	}

	private static byte[] streamToBytes(InputStream in, int length)
	        throws IOException
	{
		byte[] bytes = new byte[length];
		int count;
		int pos = 0;
		while (pos < length
		        && ((count = in.read(bytes, pos, length - pos)) != -1))
		{
			pos += count;
		}
		if (pos != length)
		{
			throw new IOException("Expected " + length + " bytes, read " + pos
			        + " bytes");
		}
		return bytes;
	}

	static void writeInt(OutputStream os, int n) throws IOException
	{
		os.write((n >> 0) & 0xff);
		os.write((n >> 8) & 0xff);
		os.write((n >> 16) & 0xff);
		os.write((n >> 24) & 0xff);
	}

	static int readInt(InputStream is) throws IOException
	{
		int n = 0;
		n |= (read(is) << 0);
		n |= (read(is) << 8);
		n |= (read(is) << 16);
		n |= (read(is) << 24);
		return n;
	}

	static void writeLong(OutputStream os, long n) throws IOException
	{
		os.write((byte) (n >>> 0));
		os.write((byte) (n >>> 8));
		os.write((byte) (n >>> 16));
		os.write((byte) (n >>> 24));
		os.write((byte) (n >>> 32));
		os.write((byte) (n >>> 40));
		os.write((byte) (n >>> 48));
		os.write((byte) (n >>> 56));
	}

	static long readLong(InputStream is) throws IOException
	{
		long n = 0;
		n |= ((read(is) & 0xFFL) << 0);
		n |= ((read(is) & 0xFFL) << 8);
		n |= ((read(is) & 0xFFL) << 16);
		n |= ((read(is) & 0xFFL) << 24);
		n |= ((read(is) & 0xFFL) << 32);
		n |= ((read(is) & 0xFFL) << 40);
		n |= ((read(is) & 0xFFL) << 48);
		n |= ((read(is) & 0xFFL) << 56);
		return n;
	}

	static void writeString(OutputStream os, String s) throws IOException
	{
		byte[] b = s.getBytes("UTF-8");
		writeLong(os, b.length);
		os.write(b, 0, b.length);
	}

	static String readString(InputStream is) throws IOException
	{
		int n = (int) readLong(is);
		byte[] b = streamToBytes(is, n);
		return new String(b, "UTF-8");
	}

	/**
	 * 清空缓存文件
	 */
	public synchronized void clear()
	{
		File[] files = mRootDirectory.listFiles();
		if (files != null)
		{
			for (File file : files)
			{
				file.delete();
			}
		}
		mEntries.clear();
	}

	public synchronized Entry get(String key)
	{
		CacheHeader entry = mEntries.get(key);
		if (entry == null)
		{
			return null;
		}
		File file = getFileForKey(key);
		CountingInputStream cis = null;
		try
		{
			cis = new CountingInputStream(new FileInputStream(file));
			CacheHeader.readHeader(cis);
			byte[] data = streamToBytes(cis,
			        (int) (file.length() - cis.bytesRead));
			return entry.toCacheEntry(data);
		} catch (IOException e)
		{
			remove(key);
			return null;
		} finally
		{
			if (cis != null)
			{
				try
				{
					cis.close();
				} catch (IOException ioe)
				{
					ioe.printStackTrace();
					return null;
				}
			}
		}
	}

	/**
	 * 初始化本地缓存信息
	 */
	public synchronized void initialize()
	{
		if (!mRootDirectory.exists())
		{
			mRootDirectory.mkdirs();
		}

		File[] files = mRootDirectory.listFiles();
		if (files == null)
		{
			return;
		}
		for (File file : files)
		{
			FileInputStream fis = null;
			try
			{
				fis = new FileInputStream(file);
				CacheHeader entry = CacheHeader.readHeader(fis);
				entry.size = file.length();
				putEntry(entry.key, entry);
			} catch (IOException e)
			{
				if (file != null)
				{
					file.delete();
				}
			} finally
			{
				try
				{
					if (fis != null)
					{
						fis.close();
					}
				} catch (IOException ignored)
				{
				}
			}
		}
	}

	public synchronized void put(String key, Entry entry)
	{
		File file = getFileForKey(key);
		try
		{
			FileOutputStream fos = new FileOutputStream(file);
			CacheHeader e = new CacheHeader(key, entry);
			boolean success = e.writeHeader(fos);
			if (!success)
			{
				fos.close();
				throw new IOException();
			}
			fos.write(entry.data);
			fos.close();
			putEntry(key, e);
			return;
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		file.delete();
	}

	// 以下为读写操作工具方法

	public synchronized void remove(String key)
	{
		getFileForKey(key).delete();
		removeEntry(key);
	}

	private String getFilenameForKey(String key)
	{
		int firstHalfLength = key.length() / 2;
		String localFilename = String.valueOf(key.substring(0, firstHalfLength)
		        .hashCode());
		localFilename += String.valueOf(key.substring(firstHalfLength)
		        .hashCode());
		return localFilename;
	}

	public File getFileForKey(String key)
	{
		return new File(mRootDirectory, getFilenameForKey(key));
	}

	private void putEntry(String key, CacheHeader entry)
	{
		mEntries.put(key, entry);
	}

	private void removeEntry(String key)
	{
		CacheHeader entry = mEntries.get(key);
		if (entry != null)
		{
			mEntries.remove(key);
		}
	}

	/**
	 * 缓存头信息
	 */
	static class CacheHeader
	{
		public long size;
		public String key;
		public long saveDate;
		public long ttl;

		private CacheHeader()
		{
		}

		public CacheHeader(String key, Entry entry)
		{
			this.key = key;
			this.size = entry.data.length;
			this.saveDate = entry.saveDate;
			this.ttl = entry.ttl;
		}

		public static CacheHeader readHeader(InputStream is) throws IOException
		{
			CacheHeader entry = new CacheHeader();
			int magic = readInt(is);
			if (magic != CACHE_MAGIC)
			{
				throw new IOException();
			}
			entry.key = readString(is);
			entry.saveDate = readLong(is);
			entry.ttl = readLong(is);
			return entry;
		}

		public Entry toCacheEntry(byte[] data)
		{
			Entry e = new Entry();
			e.data = data;
			e.saveDate = saveDate;
			e.ttl = ttl;
			return e;
		}

		public boolean writeHeader(OutputStream os)
		{
			try
			{
				writeInt(os, CACHE_MAGIC);
				writeString(os, key);
				writeLong(os, saveDate);
				writeLong(os, ttl);
				os.flush();
				return true;
			} catch (IOException e)
			{
				return false;
			}
		}
	}

	/**
	 * 可以记录当前读取到位置的InputStream
	 */
	private static class CountingInputStream extends FilterInputStream
	{
		private int bytesRead = 0;

		private CountingInputStream(InputStream in)
		{
			super(in);
		}

		@Override
		public int read() throws IOException
		{
			int result = super.read();
			if (result != -1)
			{
				bytesRead++;
			}
			return result;
		}

		@Override
		public int read(byte[] buffer, int offset, int count)
		        throws IOException
		{
			int result = super.read(buffer, offset, count);
			if (result != -1)
			{
				bytesRead += result;
			}
			return result;
		}
	}

	public static class Entry
	{
		public byte[] data;
		public long saveDate;
		public long ttl;

		/**
		 * 缓存是否过期
		 */
		public boolean isExpired()
		{
			return this.ttl < System.currentTimeMillis();
		}

	}

}
