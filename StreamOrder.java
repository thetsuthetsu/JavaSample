import javax.crypto.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 暗号化ストリームI/Oサンプル
 */
public class StreamOrder {
	private static final String PATH = "test.txt";

	private static SecretKey genKey(String algo)
			throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance(algo);
		keyGen.init(128);
		return keyGen.generateKey();
	}

	private static byte[] genData(int size) {
		byte[] data = new byte[size];
		for (int i = 0; i < size; i++) {
			data[i] = 'a';
		}
		return data;
	}

	private static void checkData(byte[] ref, byte[] data) {
		if (ref.length != data.length) {
			throw new IllegalArgumentException("different length.");
		}
		for (int i = 0; i < ref.length; i++) {
			if (ref[i] != data[i]) {
				throw new IllegalArgumentException("different data.");
			}
		}
	}

	public static void main(String[] args) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException {
		SecretKey key = genKey("AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, key);

		byte[] data = genData(2049);

		OutputStream os = null;
		try {
			os = new GZIPOutputStream(new CipherOutputStream(
					new FileOutputStream(PATH), cipher));
			os.write(data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		InputStream is = null;
		byte[] buffer = new byte[data.length];
		try {
			is = new GZIPInputStream(new CipherInputStream(new FileInputStream(
					PATH), cipher));
			is.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		checkData(data, buffer);
	}
}
