package com.pakupaku.outlet.messages.communicator;

public interface DataMessage<T> {

    void onReceiveData(T t);
}
