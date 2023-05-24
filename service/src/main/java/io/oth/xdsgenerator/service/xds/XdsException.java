package io.oth.xdsgenerator.service.xds;

import java.util.LinkedList;
import java.util.List;

public class XdsException extends Exception {

    private static final long serialVersionUID = 1L;

    List<String> errors = new LinkedList<String>();

    public String addError(String error) {
        this.errors.add(error);
        return error;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
