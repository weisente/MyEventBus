package example.weisente.top.myeventbus;

/**
 * Created by san on 2017/12/11.
 */

public enum ThreadMode {

    POSTING,//同一个线程，在哪个线程发送事件，那么该方法就在哪个线程执行
    MAIN,//在主线程中执行
    BACKGROUND,//子线程：如果发布事件的线程是主线程，那么调用线程池中的子线程来执行订阅方法；否则直接执行；
    ASYNC// 异步线程：无论发布事件执行在主线程还是子线程，都利用一个异步线程来执行订阅方法。
}
