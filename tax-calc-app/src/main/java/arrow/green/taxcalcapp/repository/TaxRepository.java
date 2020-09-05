package arrow.green.taxcalcapp.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import arrow.green.taxcalcapp.model.TaxEntry;

/**
 * @author nakulgoyal
 *         04/09/20
 **/

public interface TaxRepository extends JpaRepository<TaxEntry, Long> {
    List<TaxEntry> findByUserId(Long userId);
    
    List<TaxEntry> findByUserIdAndDate(Long userId, @Param("onDate") LocalDate date);
    
    @Query(nativeQuery = true, value = "select c.item, c.totalPrice, c.taxPercentage, c.taxAmount, c.description from TaxEntry c where c.created_at between :startDate and :endDate")
    List<TaxEntry> getData_between(@Param("startDate") LocalDate date, @Param("endDate") LocalDate date2);
}


