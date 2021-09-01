package bookingexhibition;

import bookingexhibition.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyHandler{
    @Autowired PaymentRepository paymentRepository;

    //PMJ Start
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverBooked_Payment(@Payload Booked booked){

        if(!booked.validate()) return;

        System.out.println("\n\n##### listener payment : " + booked.toJson() + "\n\n");

        Payment payment = new Payment();
        payment.setBookingId(booked.getId());
        payment.setBookingStatus("Approved Payment");
        payment.setAmt(booked.getAmt());

        paymentRepository.save(payment);    

    }
    //PMJ End

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCanceledBooking_CanceledPayment(@Payload CanceledBooking canceledBooking){

        if(!canceledBooking.validate()) return;

        System.out.println("\n\n##### listener CanceledPayment : " + canceledBooking.toJson() + "\n\n");

        // PMJ Start
        List<Payment> paymentList = paymentRepository.findByBookingId(canceledBooking.getId());
        if ((paymentList != null) && !paymentList.isEmpty()){
            paymentRepository.deleteAll(paymentList);
        }
        // PMJ End
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}


}