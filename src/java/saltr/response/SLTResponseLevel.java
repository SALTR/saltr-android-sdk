/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.response;

import java.io.Serializable;
import java.util.Map;

public class SLTResponseLevel implements Serializable {
    private String id;
    private Integer order;
    private Integer index;
    private Integer localIndex;
    private String url;
    private Integer version;
    private Map<String, Object> properties;
    private Long activeVariationId;

    public Integer getLocalIndex() {
        return localIndex;
    }

    public void setLocalIndex(Integer localIndex) {
        this.localIndex = localIndex;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Long getActiveVariationId() {
        return activeVariationId;
    }

    public void setActiveVariationId(Long activeVariationId) {
        this.activeVariationId = activeVariationId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
