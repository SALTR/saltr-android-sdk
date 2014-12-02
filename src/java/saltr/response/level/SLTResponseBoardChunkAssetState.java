/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.response.level;

import java.io.Serializable;

public class SLTResponseBoardChunkAssetState implements Serializable {
    private String token;
    private Object properties;
    private Integer pivotX;
    private Integer pivotY;

    public Integer getPivotX() {
        return pivotX;
    }

    public void setPivotX(Integer pivotX) {
        this.pivotX = pivotX;
    }

    public Integer getPivotY() {
        return pivotY;
    }

    public void setPivotY(Integer pivotY) {
        this.pivotY = pivotY;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object getProperties() {
        return properties;
    }

    public void setProperties(Object properties) {
        this.properties = properties;
    }
}
