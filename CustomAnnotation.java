
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;

@Constraint(validatedBy = SetDefaultValueValidatorImpl.class)
@NotNull(message = "Value cannot be null")
@ReportAsSingleViolation
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SetDefaultValue {
	
	 String [] fields() default {};
	 String [] defaultValues() default {};
	 String message() default "{SetDefaultValue.message}";
	 Class<?>[] groups() default {};
	 Class<? extends Payload>[] payload() default {};
}

------------------------------

import java.lang.reflect.InvocationTargetException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetDefaultValueValidatorImpl implements ConstraintValidator<SetDefaultValue, Object> {

	private static final Logger logger = LoggerFactory.getLogger(ConstraintValidator.class);

	private String[] fieldName;
	private String[] defaultFieldValue;

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {

		if (value == null) {
			return true;
		}
		try {
			for (int i = 0; i != fieldName.length; i++) {
				String fieldValue = BeanUtils.getProperty(value, fieldName[i]);
				if (fieldValue == null) {
					BeanUtils.setProperty(value, fieldName[i], defaultFieldValue[i]);
				}
			}

		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			logger.error("Error while getting or setting bean property {}", e.getMessage(), e);
			return true;
		}

		return true;
	}

	@Override
	public void initialize(final SetDefaultValue annotation) {

		fieldName = annotation.fields();
		defaultFieldValue = annotation.defaultValues();
	}
}



----------------------------

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;

@Constraint(validatedBy = CopyValueValidatorImpl.class)
@NotNull(message = "Value cannot be null")
@ReportAsSingleViolation
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CopyValue {

	 String [] from() default {};
	 String [] to() default {};
	 String message() default "{SetDefaultValue.message}";
	 Class<?>[] groups() default {};
	 Class<? extends Payload>[] payload() default {};
}


-------------------------------------


import java.lang.reflect.InvocationTargetException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CopyValueValidatorImpl implements ConstraintValidator<CopyValue, Object>  {
	private static final Logger logger = LoggerFactory.getLogger(ConstraintValidator.class);

	private String[] from;
	private String[] to;

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {

		if (value == null) {
			return true;
		}
		try {
			for (int i = 0; i != from.length; i++) {
				String fieldValue = BeanUtils.getProperty(value, from[i]);
				if (fieldValue != null) {
					BeanUtils.setProperty(value, to[i], fieldValue);
				}
			}

		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			logger.error("Error while getting or setting bean property {}", e.getMessage(), e);
			return true;
		}

		return true;
	}

	@Override
	public void initialize(final CopyValue annotation) {

		from = annotation.from();
		to = annotation.to();
	}
}
