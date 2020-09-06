package arrow.green.taxcalcapp.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import arrow.green.taxcalcapp.model.TaxEntry;

/**
 * @author nakulgoyal
 *         04/09/20
 **/

public interface TaxRepository extends JpaRepository<TaxEntry, Long> {
    List<TaxEntry> findByUserId(Long userId);
    
}


