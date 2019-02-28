package com.nowcode.toutiao.util;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

//启动redis先启动cmd输入：1.d: 2.cd D:\Redis-x64-3.2.100 3. redis-server  redis.windows.conf
//查看数据需要重新打开一个cmd，到第三步然后redis-cli.exe -h 127.0.0.1 -p 6379
//显示所有KEY命令：KEYS *

/*测试
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
*/
@Service
public class JedisAdapter implements InitializingBean{
    private JedisPool pool = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("localhost", 6379);
    }

    private Jedis getJedis(){
        return pool.getResource();
    }
//字符串
    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return getJedis().get(key);
        } catch (Exception e) {
            // logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            //  logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
//列表
    public long sadd(String key, String value){//将一个或多个加入key集合中
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        }catch (Exception e){
            return 0;
        }
        finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }
    public long srem(String key, String value){//移除
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.srem(key, value);
        }catch (Exception e){
            return 0;
        }
        finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public boolean sismember(String key, String value){//是否存在，存在返回1
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        }catch (Exception e){
            return false;
        }
        finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public long scard(String key){//返回key的基数
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            return 0;
        }
        finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public void setex(String key, String value) {//设置值及其过期时间
        // 验证码, 防机器注册，记录上次注册时间，有效期3天
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.setex(key, 10, value);
        } catch (Exception e) {
            //  logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
//列表
    public long lpush(String key, String value) {//放入
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            //   logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> brpop(int timeout, String key) {//列表的阻塞式(blocking)弹出
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            //  logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void setObject(String key, Object obj) {//存取对象
        set(key, JSON.toJSONString(obj));
    }

    public <T> T getObject(String key, Class<T> clazz) {//读取对象
        String value = get(key);
        if (value != null) {
            return JSON.parseObject(value, clazz);
        }
        return null;
    }
}
