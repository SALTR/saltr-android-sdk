/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.response;

import java.io.Serializable;
import java.util.List;

public class SLTResponsePack implements Serializable {
    private Long id;
    private String name;
    private String token;
    private Integer order;
    private Integer index;
    private List<SLTResponseLevel> levels;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public List<SLTResponseLevel> getLevels() {
        return levels;
    }

    public void setLevels(List<SLTResponseLevel> levels) {
        this.levels = levels;
    }
}
