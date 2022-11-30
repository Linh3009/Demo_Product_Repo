package codegym.controller;

import codegym.model.Category;
import codegym.repository.ICategoryRepo;
import codegym.validate.ValidateDuplicate;
import codegym.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import codegym.repository.IProductRepo;

import javax.validation.Valid;
import java.util.List;

@Controller

public class ProductController {
    @Autowired
    IProductRepo iProductRepo;

    @Autowired
    ICategoryRepo iCategoryRepo;

    @Autowired
    ValidateDuplicate validateDuplicate;

    @GetMapping("/products")
    public ModelAndView show(@RequestParam(defaultValue = "0") int page) {
        ModelAndView modelAndView = new ModelAndView("show");
        Page<Product> products = iProductRepo.findAll(PageRequest.of(page, 1));
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @ModelAttribute(name = "categories")
    public List<Category> categories() {
        return (List<Category>) iCategoryRepo.findAll();
    }

    @GetMapping("/create")
    public ModelAndView getCreate() {
        ModelAndView modelAndView = new ModelAndView("create");
        modelAndView.addObject("product", new Product());
        return modelAndView;
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult,@RequestParam long id_Category, RedirectAttributes redirect) {
        validateDuplicate.validate(product, bindingResult);
        if (bindingResult.hasErrors()){
            return "/create";
        } else {
            Category category = iCategoryRepo.findById(id_Category).get();
            product.setCategory(category);
            redirect.addFlashAttribute("success", "Add product successfully!");
            iProductRepo.save(product);
            return "redirect:/products";
        }
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable long id) {
        iProductRepo.deleteById(id);
        ModelAndView modelAndView = new ModelAndView("redirect:/products");
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView getEdit(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("edit");
        Product product = iProductRepo.findById(id).get();
        modelAndView.addObject("product", product);
        modelAndView.addObject("categories", iCategoryRepo.findAll());
        return modelAndView;
    }

    @PostMapping("/edit/{id}")
    public ModelAndView edit(@ModelAttribute("product") Product product, @RequestParam long id_Category) {
        Category category = new Category();
        category.setId(id_Category);
        product.setCategory(category);
        iProductRepo.save(product);
        ModelAndView modelAndView = new ModelAndView("redirect:/products");
        return modelAndView;
    }
}
