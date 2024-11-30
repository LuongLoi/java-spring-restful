package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.service.SubscriberService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.anotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.EmailInvalidException;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/v1")
public class SubscriberController {

    private SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    public ResponseEntity<Subscriber> create(@RequestBody Subscriber subscriber) 
            throws EmailInvalidException {
        //TODO: process POST request

        if (this.subscriberService.checkExistByEmail(subscriber.getEmail()) == true)
            throw new EmailInvalidException("Email đã tồn tại, vui lòng sử dụng email khác!");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.subscriberService.handleCreateSubscriber(subscriber));
    }
    
    @PutMapping("/subscribers")

    public ResponseEntity<Subscriber> update(@RequestBody Subscriber subscriber) 
            throws IdInvalidException {
        //TODO: process PUT request
        if (this.subscriberService.handleGetSubscriberById(subscriber.getId()) == null)
            throw new IdInvalidException("Subcriber với id = " + subscriber.getId() + " không tồn tại!");
        return ResponseEntity.ok(this.subscriberService.handleUpdateSubscriber(subscriber));
    }
    
    @PostMapping("/subscribers/skills")
    @ApiMessage("Get subscriber's skill")
    public ResponseEntity<Subscriber> getSubscribersSkill() throws IdInvalidException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        return ResponseEntity.ok().body(this.subscriberService.findByEmail(email));
    }

}
