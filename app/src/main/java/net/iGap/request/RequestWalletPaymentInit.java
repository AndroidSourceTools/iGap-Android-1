/*
 * This is the source code of iGap for Android
 * It is licensed under GNU AGPL v3.0
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright © 2017 , iGap - www.iGap.net
 * iGap Messenger | Free, Fast and Secure instant messaging application
 * The idea of the Kianiranian Company - www.kianiranian.com
 * All rights reserved.
 */

package net.iGap.request;

import net.iGap.proto.ProtoGlobal;
import net.iGap.proto.ProtoWalletPaymentInit;

public class RequestWalletPaymentInit {

    public void walletPaymentInit(ProtoGlobal.Language language, String jwt, long toUserId, long amount, String description) {
        ProtoWalletPaymentInit.WalletPaymentInit.Builder builder = ProtoWalletPaymentInit.WalletPaymentInit.newBuilder();
        builder.setLanguage(language);
        builder.setJwt(jwt);
        builder.setToUserId(toUserId);
        builder.setAmount(amount);
        builder.setDescription(description);

        RequestWrapper requestWrapper = new RequestWrapper(9001, builder);
        try {
            RequestQueue.sendRequest(requestWrapper);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
