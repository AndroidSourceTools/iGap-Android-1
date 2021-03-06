/*
 * This is the source code of iGap for Android
 * It is licensed under GNU AGPL v3.0
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright © 2017 , iGap - www.iGap.net
 * iGap Messenger | Free, Fast and Secure instant messaging application
 * The idea of the Kianiranian Company - www.kianiranian.com
 * All rights reserved.
 */

package net.iGap.adapter.items.chat;

import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import net.iGap.G;
import net.iGap.R;
import net.iGap.adapter.MessagesAdapter;
import net.iGap.helper.DirectPayHelper;
import net.iGap.helper.HelperCalander;
import net.iGap.module.ReserveSpaceRoundedImageView;
import net.iGap.module.TimeUtils;
import net.iGap.module.accountManager.DbManager;
import net.iGap.observers.interfaces.IMessageItem;
import net.iGap.observers.interfaces.RealmMoneyTransfer;
import net.iGap.proto.ProtoGlobal;
import net.iGap.realm.RealmRegisteredInfo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LogWallet extends AbstractMessage<LogWallet, LogWallet.ViewHolder> {

    public LogWallet(MessagesAdapter<AbstractMessage> mAdapter, ProtoGlobal.Room.Type type, IMessageItem messageClickListener) {
        super(mAdapter, true, type, messageClickListener);
    }

    @Override
    public int getType() {
        return R.id.chatSubLayoutLogWallet;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.chat_sub_layout_log_wallet;
    }


    @Override
    public void bindView(final ViewHolder holder, List payloads) {
        super.bindView(holder, payloads);

        if (mMessage.getRoomMessageWallet().getType().equals(ProtoGlobal.RoomMessageWallet.Type.UNRECOGNIZED.toString())) {

            holder.titleTxt.setText(R.string.unknown_message);

            holder.amountRoot.setVisibility(View.GONE);
            holder.fromUserIdRoot.setVisibility(View.GONE);
            holder.toUserIdRoot.setVisibility(View.GONE);
            holder.traceNumberRoot.setVisibility(View.GONE);
            holder.invoiceNumberRoot.setVisibility(View.GONE);
            holder.descriptionRoot.setVisibility(View.GONE);
            holder.payTime.setVisibility(View.GONE);
            holder.cardNumberRoot.setVisibility(View.GONE);
            holder.rrnNumberRoot.setVisibility(View.GONE);

            return;
        }

        holder.amountRoot.setVisibility(View.VISIBLE);
        holder.fromUserIdRoot.setVisibility(View.VISIBLE);
        holder.toUserIdRoot.setVisibility(View.VISIBLE);
        holder.traceNumberRoot.setVisibility(View.VISIBLE);
        holder.invoiceNumberRoot.setVisibility(View.VISIBLE);
        holder.descriptionRoot.setVisibility(View.VISIBLE);
        holder.payTime.setVisibility(View.VISIBLE);
        holder.cardNumberRoot.setVisibility(View.VISIBLE);
        holder.rrnNumberRoot.setVisibility(View.VISIBLE);

        RealmMoneyTransfer realmMoneyTransfer;

        if (mMessage.getRoomMessageWallet().getType().equals(ProtoGlobal.RoomMessageWallet.Type.MONEY_TRANSFER.toString())) {
            holder.cardNumberRoot.setVisibility(View.GONE);
            holder.rrnNumberRoot.setVisibility(View.GONE);
            realmMoneyTransfer = mMessage.getRoomMessageWallet().getRealmRoomMessageWalletMoneyTransfer();
            holder.titleTxt.setText(R.string.WALLET_TRANSFER_MONEY);
            String iGapYellowWallet = "#E6F4D442";
            holder.payTime.setBackgroundColor(Color.parseColor(iGapYellowWallet));
            holder.titleTxt.setBackgroundColor(Color.parseColor(iGapYellowWallet));
        } else {
            realmMoneyTransfer = mMessage.getRoomMessageWallet().getRealmRoomMessageWalletPayment();
            holder.cardNumberRoot.setVisibility(View.VISIBLE);
            holder.rrnNumberRoot.setVisibility(View.VISIBLE);
            String cardNumber = realmMoneyTransfer.getCardNumber();
            String rrn = realmMoneyTransfer.getRrn() + "";
            if (HelperCalander.isPersianUnicode) {
                cardNumber = HelperCalander.convertToUnicodeFarsiNumber(cardNumber);
                rrn = HelperCalander.convertToUnicodeFarsiNumber(rrn);
            }
            holder.cardNumber.setText(cardNumber);
            holder.rrnNumber.setText(rrn);
            holder.titleTxt.setText(R.string.PAYMENT_TRANSFER_MONEY);
            holder.payTime.setBackgroundColor(theme.getPrimaryColor(holder.payTime.getContext()));
            holder.titleTxt.setBackgroundColor(theme.getPrimaryColor(holder.titleTxt.getContext()));
        }
        DbManager.getInstance().doRealmTask(realm -> {
            RealmRegisteredInfo mRealmRegisteredInfoFrom = RealmRegisteredInfo.getRegistrationInfo(realm, realmMoneyTransfer.getFromUserId());
            RealmRegisteredInfo mRealmRegisteredInfoTo = RealmRegisteredInfo.getRegistrationInfo(realm, realmMoneyTransfer.getToUserId());

            String persianCalender = HelperCalander.checkHijriAndReturnTime(realmMoneyTransfer.getPayTime()) + " " + "-" + " " +
                    TimeUtils.toLocal(realmMoneyTransfer.getPayTime() * DateUtils.SECOND_IN_MILLIS, G.CHAT_MESSAGE_TIME);

            String fromDisplayName = "";
            String toDisplayName = "";

            if (mRealmRegisteredInfoFrom != null) {
                fromDisplayName = mRealmRegisteredInfoFrom.getDisplayName();
            }

            if (mRealmRegisteredInfoTo != null) {
                toDisplayName = mRealmRegisteredInfoTo.getDisplayName();
            }

            holder.fromUserId.setText(fromDisplayName);
            holder.toUserId.setText(toDisplayName);
            holder.amount.setText(DirectPayHelper.convertNumberToPriceRial(realmMoneyTransfer.getAmount()));
            String traceNumber = String.valueOf(realmMoneyTransfer.getTraceNumber());
            String invoiceNumber = String.valueOf(realmMoneyTransfer.getInvoiceNumber());
            if (HelperCalander.isPersianUnicode) {
                traceNumber = HelperCalander.convertToUnicodeFarsiNumber(traceNumber);
                invoiceNumber = HelperCalander.convertToUnicodeFarsiNumber(invoiceNumber);
                persianCalender = HelperCalander.convertToUnicodeFarsiNumber(persianCalender);
            }

            holder.description.setText(realmMoneyTransfer.getDescription());
            holder.traceNumber.setText(traceNumber);
            holder.invoiceNumber.setText(invoiceNumber);
            holder.payTime.setText(persianCalender);

            if (realmMoneyTransfer.getDescription() == null || realmMoneyTransfer.getDescription().isEmpty()) {
                holder.descriptionRoot.setVisibility(View.GONE);
            }
        });
    }

    @NotNull
    @Override
    public ViewHolder getViewHolder(@NotNull View v) {
        return new ViewHolder(v);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTxt;

        private View amountRoot;
        private TextView amount;

        private View fromUserIdRoot;
        private TextView fromUserId;

        private View toUserIdRoot;
        private TextView toUserId;

        private View traceNumberRoot;
        private TextView traceNumber;

        private View invoiceNumberRoot;
        private TextView invoiceNumber;

        private View cardNumberRoot;
        private TextView cardNumber;

        private View rrnNumberRoot;
        private TextView rrnNumber;


        private View descriptionRoot;
        private TextView description;

        private TextView payTime;

        protected ReserveSpaceRoundedImageView image;

        public ViewHolder(View view) {
            super(view);
            CardView cardView = view.findViewById(R.id.rootCardView);
            cardView.setCardBackgroundColor(Color.parseColor("#E6E5E1DC"));

            titleTxt = view.findViewById(R.id.titleTxt);

            amount = view.findViewById(R.id.amount);
            amountRoot = view.findViewById(R.id.amountRoot);

            fromUserId = view.findViewById(R.id.fromUserId);
            fromUserIdRoot = view.findViewById(R.id.fromUserIdRoot);

            toUserId = view.findViewById(R.id.toUserId);
            toUserIdRoot = view.findViewById(R.id.toUserIdRoot);

            traceNumber = view.findViewById(R.id.traceNumber);
            traceNumberRoot = view.findViewById(R.id.traceNumberRoot);

            invoiceNumber = view.findViewById(R.id.invoiceNumber);
            invoiceNumberRoot = view.findViewById(R.id.invoiceNumberRoot);

            cardNumber = view.findViewById(R.id.cardNumber);
            cardNumberRoot = view.findViewById(R.id.cardNumberRoot);

            rrnNumber = view.findViewById(R.id.WalletRrnNumber);
            rrnNumberRoot = view.findViewById(R.id.WalletRrnNumberRoot);

            description = view.findViewById(R.id.description);
            descriptionRoot = view.findViewById(R.id.descriptionRoot);

            payTime = view.findViewById(R.id.payTime);
        }
    }
}
