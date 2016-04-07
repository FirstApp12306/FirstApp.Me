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
    //主url120.25.169.221
//    public static final String SERVER_URL = "http://192.168.191.1:8080/fundSys/";
    public static final String SERVER_URL = "http://120.25.169.221:8080/fundSys/";
    public static final String FILE_URL = "http://file.firstapp.me/";
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
    //客户端提示中我的评论列表数据接口
    public static final String NOTICE_COMMENTS_LIST_URL = SERVER_URL + "/automake/controller/firstapp/NoticeCommentListController";
    //客户端提示中我的点赞列表数据接口
    public static final String NOTICE_SUPPORTS_LIST_URL = SERVER_URL + "/automake/controller/firstapp/NoticeSupportListController";
    //客户端加关注用户接口
    public static final String ADD_FRIEND_URL = SERVER_URL + "/automake/controller/firstapp/FansInsertController";
    //客户端取消关注用户接口
    public static final String DELETE_FRIEND_URL = SERVER_URL + "/automake/controller/firstapp/FansDeleteController";
    //客户端提示中我的粉丝列表数据接口
    public static final String NOTICE_FANS_LIST_URL = SERVER_URL + "/automake/controller/firstapp/NoticeFansListController";
    //客户端首页数据获取接口
    public static final String FRIEND_NOTIES_LIST_URL = SERVER_URL + "/automake/controller/firstapp/FriendNotesListController";
    //客户端精选页面获取数据接口
    public static final String FIND_NOTIES_LIST_URL = SERVER_URL + "/automake/controller/firstapp/FindNotesListController";
    //客户端获取我的话题数据接口
    public static final String MY_TOPICS_LIST_URL = SERVER_URL + "/automake/controller/firstapp/MyTopicsListController";
    //客户端更新用户头像接口
    public static final String UPDATE_AVATAR_URL = SERVER_URL + "/automake/controller/firstapp/UpdateAvatarController";
    //客户端更新用户名接口
    public static final String UPDATE_USER_NAME_URL = SERVER_URL + "/automake/controller/firstapp/UpdateUserNameController";
    //客户端更新用户性别接口
    public static final String UPDATE_USER_SEX_URL = SERVER_URL + "/automake/controller/firstapp/UpdateUserSexController";
    //客户端更新用户签名接口
    public static final String UPDATE_USER_SIGNATURE_URL = SERVER_URL + "/automake/controller/firstapp/UpdateUserSignatureController";
    //客户端更新用户城市接口
    public static final String UPDATE_USER_CITY_URL = SERVER_URL + "/automake/controller/firstapp/UpdateUserCityController";
    //客户端更新用户密码接口
    public static final String UPDATE_USER_PASSWORD_URL = SERVER_URL + "/automake/controller/firstapp/UpdateUserPasswordController";
    //客户端帖子的赞列表接口
    public static final String SUPPORT_OF_NOTE_LIST_URL = SERVER_URL + "/automake/controller/firstapp/SupportOfNoteListController";
    //客户端联系人列表接口
    public static final String CONTACTS_LIST_URL = SERVER_URL + "/automake/controller/firstapp/ContactsListController";
    //客户端搜索接口
    public static final String SEARCH_URL = SERVER_URL + "/automake/controller/firstapp/SearchController";
    //客户端用户搜索查询
    public static final String USER_SEARCH_URL = SERVER_URL + "/automake/controller/firstapp/UserSearchController";
    //客户端获取版本信息接口
    public static final String APK_VERSION_URL = SERVER_URL + "/automake/controller/firstapp/ApkVersionController";
    //客户端获取博客列表接口
    public static final String BLOG_LIST_URL = SERVER_URL + "/automake/controller/firstapp/BlogListController";
}
