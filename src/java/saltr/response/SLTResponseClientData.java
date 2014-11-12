/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr.response;

import java.io.Serializable;

public class SLTResponseClientData extends SLTResponseTemplate implements Serializable {
    private Boolean registrationRequired;

    public Boolean getRegistrationRequired() {
        return registrationRequired;
    }

    public void setRegistrationRequired(Boolean registrationRequired) {
        this.registrationRequired = registrationRequired;
    }
}
