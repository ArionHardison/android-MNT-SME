package com.dalejoak.outlet.messages.communicator;

public interface DataMessage<T> {

    void onReceiveData(T t);
}
