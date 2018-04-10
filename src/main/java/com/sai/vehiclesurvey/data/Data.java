/*
 *  Copyright © - Sai Pradeep Dandem. All rights reserved.
 */
package com.sai.vehiclesurvey.data;

import java.util.Objects;

/**
 * Data object to hold the data details of northBound and southBound.
 *
 * @author sai.dandem
 */
public class Data<T> {

    private T northBoundData;

    private T southBoundData;

    public Data(T northBoundData, T southBoundData) {
        this.northBoundData = northBoundData;
        this.southBoundData = southBoundData;
    }

    public T getNorthBoundData() {
        return northBoundData;
    }

    public void setNorthBoundData(T northBoundData) {
        this.northBoundData = northBoundData;
    }

    public T getSouthBoundData() {
        return southBoundData;
    }

    public void setSouthBoundData(T southBoundData) {
        this.southBoundData = southBoundData;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.northBoundData);
        hash = 23 * hash + Objects.hashCode(this.southBoundData);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Data<?> other = (Data<?>)obj;
        if (!Objects.equals(this.northBoundData, other.northBoundData)) {
            return false;
        }
        if (!Objects.equals(this.southBoundData, other.southBoundData)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Data{" + "northBoundData=" + northBoundData + ", southBoundData=" + southBoundData + '}';
    }

}
