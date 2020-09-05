package arrow.green.taxcalcapp.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private TaxRepository taxRepository;
    
    public CommonResponse addItem(TaxItem item, Double price, Double taxPercentage, String desc,
            Boolean applyDefaultTax, String auth) {
        User user = authenticationService.extractUser(auth);
        log.info("Adding item for user : {}, item : {}, price : {}, taxPercentage : {}, desc : {}",
                 user.getUsername(), item, price, taxPercentage, desc);
    
        if(applyDefaultTax.equals(true) || item.equals(TaxItem.NO_TAX)) {
            taxPercentage = item.defaultTaxPercentage;
        }
        Double taxAmount = null;
        if(Objects.isNull(taxPercentage)) {
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
        return taxPercentage*price/100;
    }
    
    public List<TaxEntry> getItems(String auth) {
        User user = authenticationService.extractUser(auth);
        log.info("Get all items request for user : {}", user.getUsername());
        return taxRepository.findByUserId(user.getId());
    }
    
    public List<TaxEntry> getItemByDate(String auth, LocalDate onDate) {
        User user = authenticationService.extractUser(auth);
        log.info("Get all items for user : {} added on date : {}", user, onDate);
        return taxRepository.findByUserIdAndDate(user.getId(), onDate);
    }
    
    public List<TaxEntry> getItem_betweenDates(String auth, LocalDate startDate, LocalDate endDate) {
        User user = authenticationService.extractUser(auth);
        log.info("Get all items for user : {} between date {} and {}", user, startDate, endDate);
        return taxRepository.getData_between(startDate, endDate);
    }
}


