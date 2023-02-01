package demo;

public class TestRunner {

    public static void start(Class<?> someClass) {
        DataHandler handler = new DataHandler();

        handler.handleData(someClass);
    }
}