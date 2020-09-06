package arrow.green.taxcalcapp.model;

/**
 * @author nakulgoyal
 *         04/09/20
 **/
public enum TaxItem {
    FOOD(5.0),
    ELECTRONICS(5.0),
    CLOTHES(5.0),
    AUTO_MOBILE(5.0),
    NO_TAX(0.0),
    OTHERS(1.0);
    
    public final Double defaultTaxPercentage;
    TaxItem(Double defaultTaxPercentage) {
        this.defaultTaxPercentage = defaultTaxPercentage;
    }
}



