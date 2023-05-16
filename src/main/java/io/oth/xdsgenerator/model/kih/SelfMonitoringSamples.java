package io.oth.xdsgenerator.model.kih;

public class SelfMonitoringSamples {

    public SelfMonitoringSample SelfMonitoringSample;

	public SelfMonitoringSample getSelfMonitoringSample() {
		return SelfMonitoringSample;
	}

	public void setSelfMonitoringSample(SelfMonitoringSample selfMonitoringSample) {
		SelfMonitoringSample = selfMonitoringSample;
	}

	@Override
	public String toString() {
		return "SelfMonitoringSamples [SelfMonitoringSample=" + SelfMonitoringSample.toString() + "]";
	}
    
}
