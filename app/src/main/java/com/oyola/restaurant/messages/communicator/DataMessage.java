package com.oyola.restaurant.messages.communicator;

public interface DataMessage<T> {

    void onReceiveData(T t);
}
