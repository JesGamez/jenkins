
package servicios;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Pattern;

@FacesValidator(value = "validarDni")
public class ValidatorDNI implements Validator {
    

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String dni = value.toString().trim();
        if (dni.length() != 0 && dni.length() < 8) {
            String plantilla = "^\\d\\d\\d\\d\\d\\d\\d\\d$";
            boolean vali = Pattern.matches(plantilla, dni);
            if (!vali) {
                throw new ValidatorException(new FacesMessage("El formato debe ser de 8 carácteres"));

            }
        }
    }
}


