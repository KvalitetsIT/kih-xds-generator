package io.oth.xdsgenerator.model.kih;

import java.util.Arrays;

public class SelfMonitoringCollection {

	public Citizen Citizen;
	
	public Author Author;
	
	public LegalAuthenticator LegalAuthenticator;
	
	public Custodian Custodian;
	
	public SelfMonitoringSamples[] SelfMonitoringSamples;

	public Citizen getCitizen() {
		return Citizen;
	}

	public void setCitizen(Citizen citizen) {
		Citizen = citizen;
	}

	public Author getAuthor() {
		return Author;
	}

	public void setAuthor(Author author) {
		Author = author;
	}

	public LegalAuthenticator getLegalAuthenticator() {
		return LegalAuthenticator;
	}

	public void setLegalAuthenticator(LegalAuthenticator legalAuthenticator) {
		LegalAuthenticator = legalAuthenticator;
	}

	public Custodian getCustodian() {
		return Custodian;
	}

	public void setCustodian(Custodian custodian) {
		Custodian = custodian;
	}

	public SelfMonitoringSamples[] getSelfMonitoringSamples() {
		return SelfMonitoringSamples;
	}

	public void setSelfMonitoringSamples(SelfMonitoringSamples[] selfMonitoringSamples) {
		SelfMonitoringSamples = selfMonitoringSamples;
	}

	@Override
	public String toString() {
		return "SelfMonitoringCollection [Citizen=" + Citizen.toString() + ", Author=" + Author + ", LegalAuthenticator="
				+ LegalAuthenticator + ", Custodian=" + Custodian + ", SelfMonitoringSamples="
				+ SelfMonitoringSamples.length + "]";
	}
	
}
