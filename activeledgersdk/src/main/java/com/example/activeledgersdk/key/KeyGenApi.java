package com.example.activeledgersdk.key;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.ECGenParameterSpec;
<<<<<<< HEAD

=======
>>>>>>> 07acfea02737258a82eeea44fde1fb955581c20e
import com.example.activeledgersdk.utility.KeyType;
import com.example.activeledgersdk.utility.Utility;
import org.spongycastle.crypto.DataLengthException;

<<<<<<< HEAD
import io.reactivex.Observable;

=======
>>>>>>> 07acfea02737258a82eeea44fde1fb955581c20e

public class KeyGenApi {


<<<<<<< HEAD
        public KeyPair generateKeyPair(KeyType keyType, boolean saveKeysToFile) {
=======
        public KeyPair generateKeyPair(KeyType keyType,boolean saveKeysToFile) {
>>>>>>> 07acfea02737258a82eeea44fde1fb955581c20e

			KeyPair keyPair= null;
	    	try {

				Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());


	    		if(keyType == KeyType.RSA) {
						keyPair = createRSAKeyPair();
				}
	    		else {
		    		keyPair=createSecp256k1KeyPair();
	    		}



				PrivateKey priv =keyPair.getPrivate();
				PublicKey pub =  keyPair.getPublic();

				System.out.println("Format pri key:"+priv.getFormat());
				System.out.println("Format pub key:"+pub.getFormat());



                try {
                    Utility.writePem(Utility.PUBLICKEY_FILE, "PUBLIC KEY", pub);

                    if(saveKeysToFile) {

					Utility.writePem(Utility.PRIVATEKEY_FILE, "PRIVATE KEY", priv);

				}

				} catch (IOException e) {
					e.printStackTrace();
				}
	    		
			} 
	    	catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (DataLengthException e) {
				e.printStackTrace();
			}

<<<<<<< HEAD

=======
>>>>>>> 07acfea02737258a82eeea44fde1fb955581c20e
		return keyPair;
		}
		
		public KeyPair createSecp256k1KeyPair()  {

			KeyPairGenerator keyPairGenerator = null;
			try {
				keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "SC");

			ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
			keyPairGenerator.initialize(ecGenParameterSpec,new SecureRandom());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchProviderException e) {
				e.printStackTrace();
			} catch (InvalidAlgorithmParameterException e) {
				e.printStackTrace();
			}
			return keyPairGenerator.generateKeyPair();

		}
	
		private KeyPair createRSAKeyPair()  {

			KeyPairGenerator keyPairGenerator = null;
			try {
				keyPairGenerator = KeyPairGenerator.getInstance("RSA", "SC");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchProviderException e) {
				e.printStackTrace();
			}
			keyPairGenerator.initialize(2048);
			 return keyPairGenerator.generateKeyPair();
		}
		
		static boolean isValidRSAPair(KeyPair pair)
		{
		  Key key = pair.getPrivate();
		  if (key instanceof RSAPrivateCrtKey) {
		    RSAPrivateCrtKey pvt = (RSAPrivateCrtKey) key;
		    BigInteger e = pvt.getPublicExponent();
		    RSAPublicKey pub = (RSAPublicKey) pair.getPublic();
		    return e.equals(pub.getPublicExponent()) && 
		      pvt.getModulus().equals(pub.getModulus());
		  }
		  else {
		    throw new IllegalArgumentException("Not a CRT RSA key.");
		  }
		}

	
		

		
	}

