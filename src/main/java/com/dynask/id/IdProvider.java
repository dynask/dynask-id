package com.dynask.id;

public interface IdProvider {

    long getId();

    Id getId(long id);

}
