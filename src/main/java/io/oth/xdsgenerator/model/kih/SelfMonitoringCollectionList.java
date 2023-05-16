package io.oth.xdsgenerator.model.kih;

import java.util.Arrays;

public class SelfMonitoringCollectionList {
    public String DocumentUUID;
	public SelfMonitoringCollection[] SelfMonitoringCollection;

	public SelfMonitoringCollection[] getSelfMonitoringCollection() {
		return SelfMonitoringCollection;
	}

	public void setSelfMonitoringCollection(SelfMonitoringCollection[] SelfMonitoringCollection) {
		this.SelfMonitoringCollection = SelfMonitoringCollection;
	}

	@Override
	public String toString() {
		return "SelfMonitoringCollectionList{" +
				"SelfMonitoringCollection=" + getSelfMonitoringCollection().length +
				'}';
	}
}
