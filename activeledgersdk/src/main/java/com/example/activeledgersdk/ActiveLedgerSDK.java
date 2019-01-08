/*
 * MIT License (MIT)
 * Copyright (c) 2018
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.example.activeledgersdk;

import android.content.Context;

import com.example.activeledgersdk.key.KeyGenApi;
import com.example.activeledgersdk.onboard.OnboardIdentity;
import com.example.activeledgersdk.utility.ContractUploading;
import com.example.activeledgersdk.utility.KeyType;
import com.example.activeledgersdk.utility.Utility;

import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ActiveLedgerSDK {

    public static KeyType keyType;
    public static String KEYNAME = "AwesomeKey";
    private static ActiveLedgerSDK instance = null;

    public static synchronized ActiveLedgerSDK getInstance() {
        if (instance == null)
            instance = new ActiveLedgerSDK();
        return instance;
    }

    // function takes trnascation JSON object, sigs JSON object and self sign flag) and creates and return an onboard transaction
    public static JSONObject createBaseTransaction(JSONObject $tx, Boolean selfsign, JSONObject
            $sigs) {
        return ContractUploading.createBaseTransaction(null,$tx, selfsign, $sigs);
    }

    // this method can be used to sign a message using private key
    public static String signMessage(byte[] message, KeyPair keyPair, KeyType type) {
        try {
            return OnboardIdentity.signMessage(message, keyPair, keyType);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }

    // by given a file name this function reads the file from application directory and returns content as String.
    public static String readFileAsString(String fileName) throws IOException {
        return Utility.getInstance().readFileAsString(fileName);
    }

    // base method that has to be called before using SDK
    public void initSDK(Context context, String protocol, String url, String port) {
        Utility.getInstance().initSDK(context, protocol, url, port);
    }

    // function generates and set the default keypair of the SDK
    public Observable<KeyPair> generateAndSetKeyPair(KeyType keyType, boolean saveKeysToFile) {

        KeyGenApi keyGenApi = new KeyGenApi();
        setKeyType(keyType);
        return Observable.just(keyGenApi.generateKeyPair(keyType, saveKeysToFile));
    }

    // creates an onboard transaction and execute the http request to the ledger
    public Observable<String> onBoardKeys(KeyPair keyPair, String keyName) {

        KEYNAME = keyName;
        JSONObject transaction = OnboardIdentity.getInstance().onboard(keyPair, getKeyType());

        String transactionJson = Utility.getInstance().convertJSONObjectToString(transaction);
        return executeTransaction(transactionJson);
    }

    // this method is used to an http request and execute a transaction over the ledger
    public Observable<String> executeTransaction(String transactionJson) {

        return HttpClient.getInstance().sendTransaction(transactionJson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


    }

    // does an HTTP hit and return territoriality details
    public Observable<String> getTerritorialityStatus() {

        return HttpClient.getInstance().getTerritorialityStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


    }

    public KeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(KeyType keyType) {
        this.keyType = keyType;
    }


}
