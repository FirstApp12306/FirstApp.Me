package com.me.firstapp.global;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:定义全局参数
 */
public class GlobalContants {
    //服务器用户号
    public static final String SERVER_USER_NO = "9xjyuy";
    //服务器密码
    public static final String SERVER_USER_PASSWORD = "x5z1zoh9";
    //app编号
    // public static final String APP_ID = "ee252ba63500463cb59edea481b54291";
    //主url
    public static final String SERVER_URL = "http://192.168.191.1:8080/fundSys/";
    //服务器用户登录接口
    public static final String SERVER_LOGIN_URL = SERVER_URL + "automake/controller/system/login/LoginAction.do";
    //客户端用户注册接口
    public static final String SIGN_UP_URL = SERVER_URL + "automake/controller/firstapp/SignUpController";
    //客户端用户登陆接口
    public static final String LOGIN_URL = SERVER_URL + "automake/controller/firstapp/LoginController";
    //客户端用户发布话题接口
    public static final String PUB_TOPIC_URL = SERVER_URL + "/automake/controller/firstapp/TopicInsertController";
    //客户端获取发现话题列表接口
    public static final String FIND_TOPICS_LIST_URL = SERVER_URL + "/automake/controller/firstapp/FindTopicsListController";
    //客户端获取最新话题列表接口
    public static final String NEW_TOPICS_LIST_URL = SERVER_URL + "/automake/controller/firstapp/NewTopicsListController";
    //客户端获取上传图片文件的凭证(token)
    public static final String GET_TOKEN_URL = SERVER_URL + "/automake/controller/firstapp/MakeTokenController";
    //客户端用户发布帖子接口
    public static final String SEND_NOTE_URL = SERVER_URL + "/automake/controller/firstapp/NoteInsertController";
    //记录话题浏览量的接口
    public static final String TOPIC_BROWSE_COUNT_URL = SERVER_URL + "/automake/controller/firstapp/TopicBrowseController";
    //客户端获取最新帖子列表接口
    public static final String NOTES_LIST_URL = SERVER_URL + "/automake/controller/firstapp/NotesListController";
    //客户端帖子点赞新增接口
    public static final String NOTE_SUPPORT_ADD_URL = SERVER_URL + "/automake/controller/firstapp/SupportInsertController";
    //客户端获取帖子点赞和评论列表接口
    public static final String NOTE_SUPPORT_COMMENT_LIST_URL = SERVER_URL + "/automake/controller/firstapp/SupportAndCommController";
    //客户端帖子评论新增接口
    public static final String NOTE_COMMENT_ADD_URL = SERVER_URL + "/automake/controller/firstapp/CommentInsertController";
    //客户端中我的评论列表数据接口
    public static final String NOTICE_COMMENTS_LIST_URL = SERVER_URL + "/automake/controller/firstapp/NoticeCommentListController";
    //客户端中我的点赞列表数据接口
    public static final String NOTICE_SUPPORTS_LIST_URL = SERVER_URL + "/automake/controller/firstapp/NoticeSupportListController";
}
