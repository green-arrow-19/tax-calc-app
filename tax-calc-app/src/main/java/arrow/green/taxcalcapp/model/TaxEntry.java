package arrow.green.taxcalcapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

/**
 * @author nakulgoyal
 *         04/09/20
 **/

@Data
@Entity
@Table(name = "tax_entry")
public class TaxEntry {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "item", nullable = false)
    private TaxItem item;
    
    @Column(name = "totalPrice", nullable = false)
    private Double totalPrice;
    
    @Column(name = "taxPercentage", nullable = false)
    private Double taxPercentage;
    
    @Column(name = "taxAmount", nullable = false)
    private Double taxAmount;
    
    @Column(name = "description", length = 50)
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user")
    private User user;
    
}


