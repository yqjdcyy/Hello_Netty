package com.yao.scene.im.protocol;


/**
 * 自定义IM协议，Instant Messaging Protocol即时通信协议
 */
public enum IMP {
    /**
     * 系统消息
     */
    SYSTEM("SYSTEM"),
    /**
     * 登录指令
     */
    LOGIN("LOGIN"),
    /**
     * 登出指令
     */
    LOGOUT("LOGOUT"),
    /**
     * 聊天消息
     */
    CHAT("CHAT"),
    /**
     * 送鲜花
     */
    FLOWER("FLOWER"),
    /**
     * 退出
     * ！非聊天内容
     */
    EXIT("EXIT");

    private String name;

    public static boolean isIMP(String content) {
        return content.matches("^\\[(SYSTEM|LOGIN|LOGIN|CHAT)\\]");
    }

    IMP(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
