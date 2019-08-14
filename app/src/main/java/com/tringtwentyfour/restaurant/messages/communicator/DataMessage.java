package com.tringtwentyfour.restaurant.messages.communicator;

public interface DataMessage<T> {

    void onReceiveData(T t);
}
