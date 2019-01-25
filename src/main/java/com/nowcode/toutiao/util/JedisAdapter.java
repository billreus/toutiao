package com.nowcode.toutiao.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

//启动redis先启动cmd输入：1.d: 2.cd D:\Redis-x64-3.2.100 3. redis-server  redis.windows.conf
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

    public long sadd(String key, String value){
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
    public long srem(String key, String value){
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

    public boolean sismember(String key, String value){
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

    public long scard(String key){
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
}
