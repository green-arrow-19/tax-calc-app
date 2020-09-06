package arrow.green.taxcalcapp.controller;

import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
 * @author nakulgoyal, nikhilangral
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
        if (Objects.nonNull(taxPercentage) && (taxPercentage >= 100 || taxPercentage < 0)) {
            log.error("taxPercentage should lie b/w 0 to 100");
            throw new BadCredentialsException("taxPercentage should lie b/w 0 to 100");
        }
        CommonResponse commonResponse = taxService.addItem(item, price, taxPercentage, desc, applyDefaultTax, auth);
        log.info("New item added successfully");
        return ResponseEntity.accepted().body(commonResponse);
    }
    
    @GetMapping("/item/onDate")
    public ResponseEntity<List<TaxEntry>> getItemByDate(
            @RequestHeader(name = HeaderConstants.AUTHORIZATION) String auth,
            @RequestParam(value = "date")  String onDate) {
        log.info("Request for listing items on date : {}", onDate);
        List<TaxEntry> taxEntries = taxService.getItemByDate(auth, onDate);
        return ResponseEntity.ok(taxEntries);
    }
    
    @GetMapping("/item/fromTo")
    public ResponseEntity<List<TaxEntry>> getItemBetweenDate(
            @RequestHeader(name = HeaderConstants.AUTHORIZATION) String auth,
            @RequestParam(value = "from")String fromDate,
            @RequestParam(value = "to")  String toDate){
        List<TaxEntry> taxEntries = taxService.getItemBetweenDates(auth, fromDate, toDate);
        return ResponseEntity.ok(taxEntries);
    }
    
    
    
}
