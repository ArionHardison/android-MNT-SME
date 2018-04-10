package com.tomoeats.restaurant.messages.communicator;

public interface DataMessage<T> {

    void onReceiveData(T t);
}
