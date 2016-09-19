package com.android.finki.mpip.footballdreamteam.model;

/**
 * Created by Borce on 29.07.2016.
 */
public abstract class IdModel<T> extends BaseModel {

    public abstract T getId();

    public abstract void setId(T id);
}
