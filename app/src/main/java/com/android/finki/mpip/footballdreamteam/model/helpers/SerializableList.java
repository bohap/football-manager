package com.android.finki.mpip.footballdreamteam.model.helpers;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Borce on 26.09.2016.
 */
public class SerializableList<T extends Serializable> implements Serializable {

    private List<T> list;

    public SerializableList(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }
}
