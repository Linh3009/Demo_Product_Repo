package codegym.validate;

import codegym.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import codegym.repository.IProductRepo;

import java.util.List;

@Component
public class ValidateDuplicate implements Validator {
    @Autowired
    IProductRepo productRepo;
    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Product product = (Product) target;
        List<Product> products = (List<Product>) productRepo.findAll();
        for (Product product1:products) {
            if (product.getName().equals(product1.getName())){
                errors.rejectValue("name","","Trùng Tên Rồi");
                return;
            }
        }
    }
}
