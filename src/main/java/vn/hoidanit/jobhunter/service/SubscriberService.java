package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.repository.SubscriberRepository;

@Service
public class SubscriberService {

    private SubscriberRepository subscriberRepository;
    private SkillRepository skillRepository;

    public SubscriberService(SubscriberRepository subscriberRepository,
            SkillRepository skillRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
    }

    public Subscriber handleCreateSubscriber(Subscriber subscriber) {
        // check skill
        if (subscriber.getSkills() != null) {
            List<Long> skillIds = subscriber.getSkills()
                    .stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(skillIds);
            subscriber.setSkills(dbSkills);
        }

        Subscriber currentSubscriber = this.subscriberRepository.save(subscriber);

        return currentSubscriber;
    }

    public boolean checkExistByEmail(String email) {
        return this.subscriberRepository.existsByEmail(email);
    }

    public Subscriber handleGetSubscriberById(long id) {
        return this.subscriberRepository.findById(id).isPresent() ? this.subscriberRepository.findById(id).get() : null;
    }

    public Subscriber handleUpdateSubscriber(Subscriber subscriber) {

        Subscriber dbSubscriber = this.handleGetSubscriberById(subscriber.getId());
        // check skill
        if (subscriber.getSkills() != null) {
            List<Long> skillIds = subscriber.getSkills()
                    .stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(skillIds);
            dbSubscriber.setSkills(dbSkills);
        }

        Subscriber currentSubscriber = this.subscriberRepository.save(dbSubscriber);

        return currentSubscriber;
    }
}
