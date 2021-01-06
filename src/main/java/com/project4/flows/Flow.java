package com.project4.flows;

import java.util.Objects;

/**
 * Bean class to store flow details.
 */
public class Flow {

    /**
     * Stores the flow ID
     */
    private final String ID;

    private final int numberOfPackets;

    public Flow(String ID, int numberOfPackets) {
        this.ID = ID;
        this.numberOfPackets = numberOfPackets;
    }

    public String getID() {
        return ID;
    }

    public int getNumberOfPackets() {
        return numberOfPackets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Flow)) return false;
        Flow flow = (Flow) o;
        return numberOfPackets == flow.numberOfPackets &&
                ID.equals(flow.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
}
