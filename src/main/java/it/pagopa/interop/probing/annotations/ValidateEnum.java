package it.pagopa.interop.probing.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import it.pagopa.interop.probing.annotations.validator.EnumValidator;


@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
public @interface ValidateEnum {

	 Class<? extends Enum<?>> enumClass();

    String message() default "{uk.dds.ideskos.validator.ValidateString.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { }; 
}
