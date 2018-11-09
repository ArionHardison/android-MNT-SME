package com.restaurantaround.app.messages.communicator;

public interface DataMessage<T> {

    void onReceiveData(T t);
}
