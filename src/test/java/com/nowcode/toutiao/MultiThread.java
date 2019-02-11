package com.nowcode.toutiao;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class MyThread extends Thread {//继承Thread类
    private int tid;
    public MyThread(int tid) {
        this.tid = tid;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; ++i) {
                Thread.sleep(1000);
                System.out.println(String.format("T%d:%d", tid, i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Producer implements Runnable {//同步队列的生产者
    private BlockingQueue<String> q;

    public Producer(BlockingQueue<String> q) {
        this.q = q;
    }
    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; ++i) {
                Thread.sleep(10);
                q.put(String.valueOf(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable {
    private BlockingQueue<String> q;

    public Consumer(BlockingQueue<String> q) {
        this.q = q;
    }
    @Override
    public void run() {
        try {
            while (true) {
                System.out.println(Thread.currentThread().getName() + ":" + q.take());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class MultiThread {
    public static void testThread() {//继承Thread类,启动线程
        for (int i = 0; i < 10; ++i) {
            new MyThread(i).start();
        }
/*
        for (int i = 0; i < 10; ++i) {//匿名类实现Thread，可以实现new MyThread(i).start();同样效果
            final int tid = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < 10; ++i) {
                            Thread.sleep(1000);
                            System.out.println(String.format("T2%d:%d", tid, i));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }*/
    }

    private static Object obj = new Object();//锁对象

    public static void testSynchronized1() {
        synchronized (obj) {
            try {
                for (int i = 0; i < 10; ++i) {
                    Thread.sleep(1000);
                    System.out.println(String.format("T3%d", i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void testSynchronized2() {
        synchronized (new Object()) {//与1竞争
        //synchronized (obj) {//可实现与1轮流进行
            try {
                for (int i = 0; i < 10; ++i) {
                    Thread.sleep(1000);
                    System.out.println(String.format("T4%d", i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void testSynchronized() {
        for (int i = 0; i < 10; ++i) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    testSynchronized1();
                    testSynchronized2();
                }
            }).start();
        }
    }

    public static void testBlockingQueue() {//同步队列，类似redis同步队列
        BlockingQueue<String> q = new ArrayBlockingQueue<String>(10);
        //两个线程进入队列排队拿取
        new Thread(new Producer(q)).start();
        new Thread(new Consumer(q), "Consumer1").start();
        new Thread(new Consumer(q), "Consumer2").start();
    }

    private static int counter = 0;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    public static void sleep(int mills) {
        try {
         //Thread.sleep(new Random().nextInt(mills));
            Thread.sleep(mills);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void testWithAtomic() {//原子化操作
        for (int i = 0; i < 10; ++i) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sleep(1000);
                    for (int j = 0; j < 10; ++j) {
                        System.out.println(atomicInteger.incrementAndGet());
                    }
                }
            }).start();
        }
    }

    public static void testWithoutAtomic() {//对比原子类,加法是非线程安全会出现非100的结果
        for (int i = 0; i < 10; ++i) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sleep(1000);
                    for (int j = 0; j < 10; ++j) {
                        counter++;
                        System.out.println(counter);
                    }
                }
            }).start();
        }
    }

    public static void testAtomic() {
        testWithAtomic();
        testWithoutAtomic();
    }

    private static ThreadLocal<Integer> threadLocalUserIds = new ThreadLocal<>();
    private static int userId;

    public static void testThreadLocal() {
        for (int i = 0; i < 10; ++i) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {//每条线程可以保存自己的副本不受干扰
                    threadLocalUserIds.set(finalI);
                    sleep(1000);
                    System.out.println("ThreadLocal: " + threadLocalUserIds.get());
                }
            }).start();
        }

        for (int i = 0; i < 10; ++i) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    userId = finalI;
                    sleep(1000);
                    System.out.println("NonThreadLocal: " + userId);
                }
            }).start();
        }
    }

    public static void testExecutor() {//线程池任务框架
        //单线程执行器
        //ExecutorService service = Executors.newSingleThreadExecutor();
        //多线程执行器
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new Runnable() {//线程1
            @Override
            public void run() {
                for (int i = 0; i < 10; ++i) {
                    sleep(1000);
                    System.out.println("Execute1 " + i);
                }
            }
        });

        service.submit(new Runnable() {//线程2
            @Override
            public void run() {
                for (int i = 0; i < 10; ++i) {
                    sleep(1000);
                    System.out.println("Execute2 " + i);
                }
            }
        });

        service.shutdown();//关闭线程
        while (!service.isTerminated()) {//查询线程是否结束
            sleep(1000);
            System.out.println("Wait for termination.");
        }
    }

    public static void testFutrue() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                sleep(1000);
                return 1;
                //throw new IllegalArgumentException("异常");//可以抛出异常
            }
        });

        service.shutdown();

        try {
            //System.out.println(future.get());//等着1s以后返回值1
            System.out.println(future.get(100, TimeUnit.MILLISECONDS));//100ms线程超时
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] argv) {
        //testThread();
        //testSynchronized1();
        //testSynchronized();
        //testBlockingQueue();
        //testWithAtomic();
        //testWithoutAtomic();
        //testAtomic();
        //testThreadLocal();
        //testExecutor();
        testFutrue();
    }
}
