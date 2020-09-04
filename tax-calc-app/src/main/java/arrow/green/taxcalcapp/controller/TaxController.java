package arrow.green.taxcalcapp.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import arrow.green.taxcalcapp.model.CommonResponse;
import arrow.green.taxcalcapp.model.HeaderConstants;
import arrow.green.taxcalcapp.model.TaxEntry;
import arrow.green.taxcalcapp.model.TaxItem;
import arrow.green.taxcalcapp.service.TaxService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author nakulgoyal
 *         04/09/20
 **/

@Slf4j
@RestController
public class TaxController {
    
    @Autowired
    TaxService taxService;
    
    @GetMapping("/items")
    public ResponseEntity<List<TaxEntry>> getItems(@RequestHeader(name = HeaderConstants.AUTHORIZATION) String auth) {
        List<TaxEntry> taxEntries = taxService.getItems(auth);
        return ResponseEntity.ok(taxEntries);
    }
    
    @PostMapping("/item/add")
    public ResponseEntity<CommonResponse> addItem(@RequestHeader(name = HeaderConstants.AUTHORIZATION) String auth,
            @RequestParam(name = "item", defaultValue = "NO_TAX") TaxItem item,
            @RequestParam(name = "price") Double price,
            @RequestParam(name = "taxPercentage", required = false) Double taxPercentage,
            @RequestParam(name = "description", required = false) String desc,
            @RequestParam(name = "applyDefaultTax", defaultValue = "false") Boolean applyDefaultTax) {
        log.info("Add new item request");
        if (Objects.isNull(price) || price <= 0) {
            log.error("price cannot be null, empty or negative");
            throw new BadCredentialsException("price cannot be null, empty or negative");
        }
        if(Objects.nonNull(taxPercentage) && (taxPercentage >= 100 || taxPercentage < 0)) {
            log.error("taxPercentage should lie b/w 0 to 100");
            throw new BadCredentialsException("taxPercentage should lie b/w 0 to 100");
        }
        CommonResponse commonResponse = taxService.addItem(item, price, taxPercentage, desc, applyDefaultTax, auth);
        log.info("New item added successfully");
        return ResponseEntity.accepted().body(commonResponse);
    }
    
}


