package demo;

import demo.anno.AfterSuite;
import demo.anno.BeforeSuite;
import demo.anno.Test;

public class TestClass {

    public void someMethod() {
        System.out.println("Won't print");
    }

//    @Test(priority = 11)
//    public void testMethod() {
//        System.out.println("Exception...");
//    }

    @Test(priority = 10)
    public void testMethod10() {
        System.out.println("Test method, priority 10");
    }

    @Test(priority = 6)
    public void testMethod6() {
        System.out.println("Test method, priority 6");
    }

    @Test(priority = 3)
    public void testMethod3() {
        System.out.println("Test method, priority 3");
    }

    @Test(priority = 8)
    public void testMethod8() {
        System.out.println("Test method, priority 8");
    }

    @Test(priority = 4)
    public void testMethod4() {
        System.out.println("Test method, priority 4");
    }

    @AfterSuite
    public void testAfterMethod() {
        System.out.println("AfterSuite");
    }


    @BeforeSuite
    public void testBeforeMethod() {
        System.out.println("BeforeSuite");
    }

//    @BeforeSuite
//    public void test2BeforeMethod() {
//        System.out.println("Exception...");
//    }


}
