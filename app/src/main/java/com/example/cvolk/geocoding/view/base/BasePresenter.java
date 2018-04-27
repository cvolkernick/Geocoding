package com.example.cvolk.geocoding.view.base;

public interface BasePresenter<V extends BaseView> {

    void attachView(V view);

    void detachView();

}
