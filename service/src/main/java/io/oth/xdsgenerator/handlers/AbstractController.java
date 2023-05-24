package io.oth.xdsgenerator.handlers;


import io.oth.xdsgenerator.model.kih.ValidationError;
import io.oth.xdsgenerator.model.kih.ValidationErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractController {

	private static Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);
	
	@ExceptionHandler(HttpMessageNotReadableException.class )
	@ResponseStatus(code=HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ValidationErrors handleControllerParseExceptionException(HttpServletRequest req, HttpMessageNotReadableException httpMessageNotReadableException) {
		String errorMessage = httpMessageNotReadableException.getRootCause().getLocalizedMessage();
		LOGGER.debug("Parse error for input - error:["+errorMessage+"]");
		ValidationErrors validationErrors = new ValidationErrors();
		validationErrors.setMessage(errorMessage);
		return validationErrors;
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(code=HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ValidationErrors handleIllegalArgumentException(HttpServletRequest req, IllegalArgumentException illegalArgumentException) {
		String errorMessage = illegalArgumentException.getMessage();
		LOGGER.debug("Parse error for input - error:["+errorMessage+"]");
		ValidationErrors validationErrors = new ValidationErrors();
		validationErrors.setMessage(errorMessage);
		return validationErrors;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(code=HttpStatus.UNPROCESSABLE_ENTITY)
	@ResponseBody
	public ValidationErrors handleControllerValidationException(HttpServletRequest req, MethodArgumentNotValidException methodArgumentNotValidException) {
		ValidationErrors validationErrors = new ValidationErrors();
		validationErrors.setMessage("Validation Error");

		List<ValidationError> validationErrorList = new LinkedList<>();
		List<FieldError> fieldErrors = methodArgumentNotValidException.getBindingResult().getFieldErrors();

		for (FieldError fieldError : fieldErrors) {
			String field = fieldError.getField();
			String errorCode = fieldError.getCode();
			LOGGER.debug("Validation error on input - error:["+field+":"+errorCode+"]");
			ValidationError validationError = new ValidationError(field, errorCode);
			validationErrorList.add(validationError);
		}
		validationErrors.setErrors(validationErrorList);

		return validationErrors;
	}

}