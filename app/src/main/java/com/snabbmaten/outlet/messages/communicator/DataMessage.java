package com.snabbmaten.outlet.messages.communicator;

public interface DataMessage<T> {

    void onReceiveData(T t);
}
