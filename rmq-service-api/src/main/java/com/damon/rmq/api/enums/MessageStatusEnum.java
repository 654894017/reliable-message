package com.damon.rmq.api.enums;

/**
 * 消息状态常量
 * 
 * @author xianping_lu
 *
 */
public enum MessageStatusEnum {
    /**
     * 待确认
     */
    WAIT((byte) 0),

    /**
     * 发送中
     */
    SENDING((byte) 1),

    /**
     * 发送成功
     */
    // SEND_SUCCESS((byte)3),

    /**
     * 发送失败
     * 
     * @param args
     */
    SEND_FAILED((byte) 2);

    private byte value;

    MessageStatusEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static String format(byte value) {
        switch (value) {
        case 0:
            return "待确认";
        case 1:
            return "发送中";
        case 2:
            return "发送失败";
        default:
            return "";
        }
    }
}