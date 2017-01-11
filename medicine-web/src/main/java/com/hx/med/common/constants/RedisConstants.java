package com.hx.med.common.constants;

/**
 * Created by admin on 2016/9/8.
 * Redis分为更新时需要删除的sdk服务器redis时。
 * 与平台自己的redis缓存。
 * @since 1.6
 */

public final class RedisConstants {
 /**
  * sdk redis key,在执行对应更新时需要清空。使用jdk序列化.
  **/
 public static final String SDK_CACHE_USER_PREFIX = "ledaologin_username:";
 /**
  * sdk redis key,以用户id存储用户对象.
  * */
 public static final String SDK_CACHE_USERID_PREFIX = "ledaologin_userid:";
 /**
  * redis缓存时间配置,单位 秒,默认一周 .
  **/
 public static final int DEFAULT_CACHE_TIMEOUT = 60 * 60 * 24 * 7;


 /**
  * USER_QUEUE  redis  sortset类型  作为用户入库队列 .
  * USER_REGISTER_NAME redis set类型 用于用户写入缓存时加锁
  * USER_INQUEUE_ID  用于将用户写入USER_QUEUE队列时的锁
  */
 public static final String SDK_USER_QUEUE = "ledaologin_user_queue";

 /**
  * 将域名作为key前缀.
  */
 public static final String DOMAIN = "hx.med.com";

 /**
  * 验证码缓存key.
  */
 public static final String VERIFY_CODE_CACHE_KEY = DOMAIN + ':' + "check_code" + ':' + "{0}";

 /**
  * 手机邮箱验证码缓存key过期时间.
  */
 public static final Long VERIFY_CODE_CACHE_KEY_EXPIRATION = 3600 * 2L;

 /**
  * 主播对应的房间号.
  */
 public static final String ROOM_NUM = DOMAIN + ':' + "room_num";

 /**
  * uid 和 roomnum 的关系前缀.
  */
 public static final String ROOM_NUM_UID = DOMAIN + ':' + "room_num_uid" + ':' + "{0}";

 /**
  * uid 和 roomnum 的关系过期时间.
  */
 public static final Long ROOM_NUM_UID_EXPIRATION = 12 * 3600 * 2L;

 /**
  * roomnum 和 streamKey 的关系前缀.
  */
 public static final String ROOM_NUM_STREAMKEY = DOMAIN + ':' + "room_num_streamkey" + ':' + "{0}";

 /**
  * roomnum 和 streamKey 的关系过期时间.
  */
 public static final Long ROOM_NUM_STREAMKEY_EXPIRATION = 12 * 3600 * 2L;

 /**
  * 观看人数.
  */
 public static final String WATCH_NUM = DOMAIN + ':' + "app" + ':' + "[{0}]" + "watch_num";

 /**
  * 工具类构造函数 .
  */
 private RedisConstants() {

 }
}
