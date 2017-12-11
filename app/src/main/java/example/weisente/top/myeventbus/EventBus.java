package example.weisente.top.myeventbus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by san on 2017/12/11.
 */

public class EventBus {
    // subscriptionsByEventType 这个集合存放的是？
    // key 是 Event 参数的类
    // value 存放的是 Subscription 的集合列表
    // Subscription 包含两个属性，一个是 subscriber 订阅者（反射执行对象），一个是 SubscriberMethod 注解方法的所有属性参数值
    private final Map<Class<?>, CopyOnWriteArrayList<Subscription>> subscriptionsByEventType;

    // typesBySubscriber 这个集合存放的是？
    // key 是所有的订阅者
    // value 是所有订阅者里面方法的参数的class
    private final Map<Object, List<Class<?>>> typesBySubscriber;

    private EventBus(){
        typesBySubscriber = new HashMap<Object, List<Class<?>>>();
        subscriptionsByEventType = new HashMap<>();
    }

    static volatile EventBus defaultInstance;

    public static EventBus getDefault(){
        if (defaultInstance == null) {
            synchronized (EventBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new EventBus();
                }
            }
        }
        return defaultInstance;
    }

    public void register(Object object){
        // 1. 解析所有方法封装成 SubscriberMethod 的集合
        List<SubscriberMethod> subscriberMethods = new ArrayList<>();
        Class<?> objClass = object.getClass();
        //反射获取一个类里面的所有方法
        Method[] methods = objClass.getDeclaredMethods();
        for (Method method : methods){
            Subscribe subscribe = method.getAnnotation(Subscribe.class);
            if(subscribe != null){
                // 所有的Subscribe属性 解析出来
                Class<?>[] parameterTypes = method.getParameterTypes();
                SubscriberMethod subscriberMethod = new SubscriberMethod(
                        method,parameterTypes[0],subscribe.threadMode(),subscribe.priority(),subscribe.sticky());
                subscriberMethods.add(subscriberMethod);
            }
        }
        for (SubscriberMethod subscriberMethod : subscriberMethods) {
            subscriber(object,subscriberMethod);//绑定关注
        }
    }

    // 2. 按照规则存放到 subscriptionsByEventType 里面去
    private void subscriber(Object object, SubscriberMethod subscriberMethod){
        Class<?> eventType = subscriberMethod.eventType;
        CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
        if(subscriptions == null){
            subscriptions = new CopyOnWriteArrayList<>();
            subscriptionsByEventType.put(eventType,subscriptions);
        }
        Subscription subscription = new Subscription(object,subscriberMethod);
        subscriptions.add(subscription);

        List<Class<?>> eventTypes = typesBySubscriber.get(object);
    }
}
