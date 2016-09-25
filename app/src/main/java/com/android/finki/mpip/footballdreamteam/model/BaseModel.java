package com.android.finki.mpip.footballdreamteam.model;

/**
 * Created by Borce on 17.09.2016.
 */
public abstract class BaseModel {

    public abstract boolean same(BaseModel model);

    /**
     * Check if the given field are equal.
     *
     * @param field1 first field
     * @param field2 second field
     * @return whatever the field are equal
     */
    protected boolean equalsFields(Object field1, Object field2) {
        return (field1 == null && field2 == null) ||
                field1 != null && field1.equals(field2);
    }

    /**
     * Checks if the given models are same.
     *
     * @param model1 first model
     * @param model2 second model
     * @return whatever the model are same
     */
    protected boolean sameModels(BaseModel model1, BaseModel model2) {
        return (model1 == null && model2 == null) ||
                model1 != null && model1.same(model2);
    }
}
