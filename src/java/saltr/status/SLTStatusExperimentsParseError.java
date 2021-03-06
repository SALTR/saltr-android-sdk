/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.status;

/**
 * The SLTStatusExperimentsParseError class represents the client experiments parse error status.
 */
public class SLTStatusExperimentsParseError extends SLTStatus {
    public SLTStatusExperimentsParseError() {
        super(SLTStatus.CLIENT_EXPERIMENTS_PARSE_ERROR, "[SALTR] Failed to decode Experiments.");
    }
}
