package com.dietmanager.dietician.messages.communicator;

public interface DataMessage<T> {

    void onReceiveData(T t);
}
