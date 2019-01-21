package com.nowcode.toutiao.util;

import redis.clients.jedis.Jedis;
//启动redis先启动cmd输入：1.d: 2.cd D:\Redis-x64-3.2.100 3. redis-server  redis.windows.conf
/*
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENT = "EVENT";

    public static String getEventQueueKey() {
        return BIZ_EVENT;
    }

    public static String getLikeKey(int entityId, int entityType) {
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityId, int entityType) {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
}
*/
public class RedisKeyUtil{
    public static void print(int index, Object obj){
        System.out.println(String.format("%d,%s", index, obj.toString()));;
    }
    public static void main(String[] argv){
        Jedis jedis = new Jedis();
        jedis.flushAll();

        jedis.set("hello", "world");
        print(1, jedis.get("hello"));
        jedis.rename("hello", "newhello");
        print(1, jedis.get("newhello"));
        //
        jedis.set("pv", "100");
        jedis.incrBy("pv", 5);
        print(2, jedis.get("pv"));
    }
}