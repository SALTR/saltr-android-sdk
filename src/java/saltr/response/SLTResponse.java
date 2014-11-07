/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.response;

import java.util.List;
import java.util.Map;

public class SLTResponse<T> {
    private List<T> response;

    public List<T> getResponse() {
        return response;
    }

    public void setResponse(List<T> response) {
        this.response = response;
    }
}
