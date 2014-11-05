/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.response;

import java.util.List;
import java.util.Map;

public class SLTResponse {
    protected List<Map<String, String>> response;

    public List<Map<String, String>> getResponse() {
        return response;
    }

    public void setResponse(List<Map<String, String>> response) {
        this.response = response;
    }
}
