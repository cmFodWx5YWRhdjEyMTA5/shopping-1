package com.nahuo.quicksale.hyphenate.presenter;

import android.content.Context;
import android.widget.BaseAdapter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.nahuo.quicksale.hyphenate.model.EaseDingMessageHelper;
import com.nahuo.quicksale.hyphenate.widget.chatrow.EaseChatRow;
import com.nahuo.quicksale.hyphenate.widget.chatrow.EaseChatRowText;

/**
 * Created by zhangsong on 17-10-12.
 */

public class EaseChatTextPresenter extends EaseChatRowPresenter {
    private static final String TAG = "EaseChatTextPresenter";

    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter) {
        return new EaseChatRowText(cxt, message, position, adapter);
    }

    @Override
    public void onBubbleClick(EMMessage message) {
        super.onBubbleClick(message);

        if (!EaseDingMessageHelper.get().isDingMessage(message)) {
            return;
        }

        // If this msg is a ding-type msg, click to show a list who has already read this message.
//        Intent i = new Intent(getContext(), EaseDingAckUserListActivity.class);
//        i.putExtra("msg", message);
//        getContext().startActivity(i);
    }

    @Override
    protected void handleReceiveMessage(EMMessage message) {
        if (!message.isAcked() && message.getChatType() == EMMessage.ChatType.Chat) {
            try {
                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
            return;
        }

        // Send the group-ack cmd type msg if this msg is a ding-type msg.
        EaseDingMessageHelper.get().sendAckMessage(message);
    }
}
