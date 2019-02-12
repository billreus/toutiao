package com.nowcode.toutiao;

import com.nowcode.toutiao.service.LikeService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ToutiaoApplication.class)
public class LikeServiceTests {
    @Autowired
    LikeService likeService;

    @Test
    public void testLike() {
        //likeService.like(123, 1, 1);
        //assert断言，测试是否如此，否报错
        //Assert.assertEquals(1, likeService.getLikeStatus(123, 1, 1));

        likeService.disLike(1, 1, 1);
        Assert.assertEquals(-1, likeService.getLikeStatus(123, 1, 1));
    }

    @Test//其次跑测试用例
    public void testB() {
        System.out.println("B");
    }

    @Test(expected = IllegalArgumentException.class)//测试异常
    public void testException() {
        throw new IllegalArgumentException("异常");
    }

    @Before//最先，初始化环境
    public void setUp() {
        System.out.println("setUp");
    }

    @After//最后一步
    public void tearDown() {
        System.out.println("tearDown");
    }

    @BeforeClass//class之前跑
    public static void beforeClass() {
        System.out.println("beforeClass");
    }

    @AfterClass//class之后跑
    public static void afterClass() {
        System.out.println("afterClass");
    }

}
