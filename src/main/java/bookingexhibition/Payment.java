package bookingexhibition;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="Payment_table")
public class Payment {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long bookingId;
    private String bookingStatus;
    private Long amt;

    @PostPersist
    public void onPostPersist(){

        // circuit breaker start
        // try {
        //     Thread.currentThread().sleep((long) (400 + Math.random() * 220));
        // } catch (Exception e) {
        //     //TODO: handle exception
        //    e.printStackTrace();
        // }
        // circuit breaker end

        CompletedPayment completedPayment = new CompletedPayment();
        BeanUtils.copyProperties(this, completedPayment);
        completedPayment.publishAfterCommit();

    }


    @PreRemove
    public void onPreRemove(){
        CanceledPayment canceledPayment = new CanceledPayment();
        BeanUtils.copyProperties(this, canceledPayment);
        canceledPayment.publishAfterCommit();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public Long getAmt() {
        return amt;
    }

    public void setAmt(Long amt) {
        this.amt = amt;
    }

}