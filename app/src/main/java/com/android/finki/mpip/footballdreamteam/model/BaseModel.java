package com.android.finki.mpip.footballdreamteam.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Borce on 29.07.2016.
 */
public abstract class BaseModel <T> {

    public abstract T getId();

    public abstract void setId(T id);
}
