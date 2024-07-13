package com.xhq.common.Constant;

//常量
public class Constant {
    //MQ交换机的名称
    public static final String MQ_EXCHANGE="message_exchange";
    //MQ队列的名称
    public static final String MQ_QUENE_NAME="message_quene";
    //队列的路由键
    public static final String MQ_ROUTING_KEY="message";

    //提交到数据库的问题，的答案的生成状态
    public static final String QUESTION_STATUS_WAIT="wait";
    public static final String QUESTION_STATUS_RUNNING="running";
    public static final String QUESTION_STATUS_SUCCEED="succeed";
    public static final String QUESTION_STATUS_FAILED="failed";

    //通易千问的Message的角色
    public static final String QWEN_MESSAGE_ROLE_SYSTEM="system";
    public static final String QWEN_MESSAGE_ROLE_ASSISTANT="assistant";
    public static final String QWEN_MESSAGE_ROLE_USER="user";

}
