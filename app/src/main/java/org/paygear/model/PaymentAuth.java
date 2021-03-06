package org.paygear.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Software1 on 9/2/2017.
 */

public class PaymentAuth implements Serializable {

    public String token;
    @SerializedName(value="pub_key", alternate = {"key"})
    public String publicKey;

    @SerializedName(value = "ipg_url",alternate = {"ipg"})
    public String IPGUrl;


    // mpl:0,ipg:2,offline:1
    @SerializedName(value = "payment_tpe")
    public int paymentType=0;

}
