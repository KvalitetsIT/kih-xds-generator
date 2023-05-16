package io.oth.xdsgenerator.model.xds;

public class PatientIdAuthority implements IdAuthority {

    private static final String PATIENT_ID_PATTERN = "%s^^^&%s&ISO";

    private String patientIdAuthority;

    public PatientIdAuthority(String patientIdAuthority) {
        this.patientIdAuthority = patientIdAuthority;
    }

    protected String formatPatientIdentifier(String patientIdentifier) {
        return String.format(PATIENT_ID_PATTERN, patientIdentifier, patientIdAuthority);
    }


    protected String formatePatientInfo(String patientIdentifier) {
        return "PID-3|" + formatPatientIdentifier(patientIdentifier);
    }

    @Override
    public String getPatientIdAuthority() {
        return patientIdAuthority;
    }

}