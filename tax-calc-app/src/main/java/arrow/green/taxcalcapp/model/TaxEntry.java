package arrow.green.taxcalcapp.model;

import java.util.Date;
import java.util.Objects;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.CreationTimestamp;
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
    
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;
    
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
    @JsonBackReference
    private User user;
    
    @Override
    public int hashCode() {
        return Objects.hash(id, item, createdAt, totalPrice, taxPercentage, taxAmount, description);
    }
}


