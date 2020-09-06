package arrow.green.taxcalcapp.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import arrow.green.taxcalcapp.model.CommonResponse;
import arrow.green.taxcalcapp.model.TaxEntry;
import arrow.green.taxcalcapp.model.TaxItem;
import arrow.green.taxcalcapp.model.User;
import arrow.green.taxcalcapp.repository.TaxRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * @author nakulgoyal
 *         04/09/20
 **/

@Slf4j
@Service
public class TaxService {
    
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private TaxRepository taxRepository;
    
    public CommonResponse addItem(TaxItem item, Double price, Double taxPercentage, String desc,
            Boolean applyDefaultTax, String auth) {
        User user = authenticationService.extractUser(auth);
        log.info("Adding item for user : {}, item : {}, price : {}, taxPercentage : {}, desc : {}", user.getUsername(),
                 item, price, taxPercentage, desc);
        
        if (applyDefaultTax.equals(true) || item.equals(TaxItem.NO_TAX)) {
            taxPercentage = item.defaultTaxPercentage;
        }
        Double taxAmount = null;
        if (Objects.isNull(taxPercentage)) {
            taxPercentage = 0.0;
            taxAmount = 0.0;
        } else {
            taxAmount = calculateTaxAmount(taxPercentage, price);
        }
        
        TaxEntry taxEntry = new TaxEntry();
        taxEntry.setUser(user);
        taxEntry.setItem(item);
        taxEntry.setTotalPrice(price);
        taxEntry.setTaxPercentage(taxPercentage);
        taxEntry.setDescription(desc);
        taxEntry.setTaxAmount(taxAmount);
        
        taxRepository.save(taxEntry);
        return new CommonResponse("SUCCESS");
    }
    
    private Double calculateTaxAmount(Double taxPercentage, Double price) {
        return taxPercentage * price / 100;
    }
    
    public List<TaxEntry> getItems(String auth) {
        User user = authenticationService.extractUser(auth);
        log.info("Get all items request for user : {}", user.getUsername());
        return taxRepository.findByUserId(user.getId());
    }
    
    public List<TaxEntry> getItemByDate(String auth, String onDate) {
        User user = authenticationService.extractUser(auth);
        log.info("Get all items for user : {} added on date : {}", user, onDate);
        List<TaxEntry> taxEntries = taxRepository.findByUserId(user.getId());
        
        return taxEntries.stream().filter(taxEntry -> onDate.equals(convertToStringDate(taxEntry.getCreatedAt())))
                         .collect(Collectors.toList());
    }
    
    public List<TaxEntry> getItemBetweenDates(String auth, String startDate, String endDate) {

        User user = authenticationService.extractUser(auth);
        log.info("Get all items for user : {} between date {} and {}", user, startDate, endDate);
        List<TaxEntry> taxEntries = taxRepository.findByUserId(user.getId());

        return taxEntries.stream().filter(taxEntry -> {
            String createdAt = convertToStringDate(taxEntry.getCreatedAt());
            return startDate.compareTo(createdAt) <= 0 && endDate.compareTo(createdAt) >= 0;
        }).collect(Collectors.toList());

    }
    
    private String convertToStringDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        return formatter.format(date);
    }
}


