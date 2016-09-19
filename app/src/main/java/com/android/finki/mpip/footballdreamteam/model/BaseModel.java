package com.android.finki.mpip.footballdreamteam.model;

/**
 * Created by Borce on 17.09.2016.
 */
public abstract class BaseModel {

    public abstract boolean same(BaseModel model);

    /**
     * Check if the given field are equal.
     *
     * @return whatever the field are equal
     */
    protected boolean equalsFields(Object field1, Object field2) {
        return (field1 == null && field2 == null) ||
                field1 != null && field1.equals(field2);
    }
}
