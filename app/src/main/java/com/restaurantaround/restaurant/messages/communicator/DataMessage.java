package com.restaurantaround.restaurant.messages.communicator;

public interface DataMessage<T> {

    void onReceiveData(T t);
}
