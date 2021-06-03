package home.work.filmolike.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Constraint(validatedBy = EnumValidatorImpl.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@NotNull(message = "Не должно быть null")
@ReportAsSingleViolation
public @interface EnumValidator {
    Class<? extends Enum<?>> enumClazz();
    String message() default "Должно содержать значение из соответствующего enum";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default  {};
}
