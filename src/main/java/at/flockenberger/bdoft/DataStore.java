package at.flockenberger.bdoft;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * <h1>DataStore</h1><br>
 * Stores any kind of {@link Serializable} data to a file on the harddrive.<br>
 * 
 * @author Florian Wagner
 *
 */
public class DataStore {

	/**
	 * the secret key to for the cipher
	 */
	private static SecretKey secKey;

	/**
	 * the cipher to en/decrypt the data
	 */
	private static Cipher cipher;

	static {
		secKey = new SecretKeySpec(new byte[] { 0x22, 0x51, 0x12, 0x03, 0x24, 0x25, 0x26, 0x57 }, "Blowfish");
		try {
			cipher = Cipher.getInstance("Blowfish");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new {@link DataStore} object.
	 */
	public DataStore() {

	}

	/**
	 * Saves the data to the given path. The data is saved using encryption. For
	 * custom encryption use the {@link #setSecret(SecretKey)} method.
	 * 
	 * @param filePath the path to save
	 * @throws IOException
	 */
	public static void storeObject(String filePath, Serializable object) {
		storeObject(Paths.get(filePath), object);
	}

	/**
	 * Saves the data to the given path. The data is saved using encryption. For
	 * custom encryption use the {@link #setSecret(SecretKey)} method.
	 * 
	 * @param path the path to save
	 * @throws IOException
	 */
	public static void storeObject(Path path, Serializable object) {

		File filePath = path.toFile();
		try {
			filePath.createNewFile();
		} catch (IOException e) {
		}

		try {
			cipher.init(Cipher.ENCRYPT_MODE, secKey);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}

		SealedObject sealedObject = null;
		try {
			sealedObject = new SealedObject(object, cipher);
		} catch (IllegalBlockSizeException | IOException e) {
			e.printStackTrace();
		}
		if (sealedObject != null) {
			CipherOutputStream cipherOutputStream;
			try {
				cipherOutputStream = new CipherOutputStream(new BufferedOutputStream(new FileOutputStream(filePath)),
						cipher);

				ObjectOutputStream outputStream = new ObjectOutputStream(cipherOutputStream);
				outputStream.writeObject(sealedObject);
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Loades the data from the given path. For custom decryption use the
	 * {@link #setSecret(SecretKey)} method.
	 * 
	 * @param filePath the path to save
	 * @throws IOException
	 */
	public static <T> T loadObject(String filePath) {
		return loadObject(Paths.get(filePath));
	}

	/**
	 * Loades the data from the given path. For custom decryption use the
	 * {@link #setSecret(SecretKey)} method.
	 * 
	 * @param filePath the path to save
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T loadObject(Path path) {
		File filePath = path.toFile();
		try {
			filePath.createNewFile();
		} catch (IOException e) {
		}

		try {
			cipher.init(Cipher.DECRYPT_MODE, secKey);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		try {
			CipherInputStream cipherInputStream = new CipherInputStream(
					new BufferedInputStream(new FileInputStream(filePath)), cipher);
			ObjectInputStream inputStream = new ObjectInputStream(cipherInputStream);
			SealedObject sealedObject = (SealedObject) inputStream.readObject();

			inputStream.close();

			try {
				return (T) sealedObject.getObject(cipher);
			} catch (IllegalBlockSizeException | BadPaddingException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		return null;
	}

}
