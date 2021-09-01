package bookingexhibition;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface PaymentRepository extends CrudRepository<Payment, Long> {

    List<Payment> findByBookingId(Long bookingId);

}
