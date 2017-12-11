package example.weisente.top.myeventbus;

/**
 * Created by san on 2017/12/11.
 */

public class Subscription {
    final Object subscriber;
    final SubscriberMethod subscriberMethod;

    /**
     * 这个标致为false  只会在Activity 取消订阅的时候
     */
    volatile boolean active;

    Subscription(Object subscriber, SubscriberMethod subscriberMethod){
        this.subscriber = subscriber;
        this.subscriberMethod = subscriberMethod;
        active = true;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Subscription) {
            Subscription otherSubscription = (Subscription) other;
            return subscriber == otherSubscription.subscriber
                    && subscriberMethod.equals(otherSubscription.subscriberMethod);
        } else {
            return false;
        }
    }
    //就进行一个叠加
    @Override
    public int hashCode() {
        return subscriber.hashCode() + subscriberMethod.methodString.hashCode();
    }
}
