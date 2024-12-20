package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.domain.dto.response.email.ResEmailJob;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.repository.SubscriberRepository;

@Service
public class SubscriberService {

    private SubscriberRepository subscriberRepository;
    private SkillRepository skillRepository;
     private final JobRepository jobRepository;
    private final EmailService emailService;

    public SubscriberService(SubscriberRepository subscriberRepository,
            SkillRepository skillRepository,
            JobRepository jobRepository,
            EmailService emailService        
            ) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
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

    public ResEmailJob convertJobToSendEmail(Job job) {
        ResEmailJob res = new ResEmailJob();
        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setCompany(new ResEmailJob.CompanyEmail(job.getCompany().getName()));
        List<Skill> skills = job.getSkills();
        List<ResEmailJob.SkillEmail> s = skills.stream().map(skill -> new ResEmailJob.SkillEmail(skill.getName()))
                .collect(Collectors.toList());
        res.setSkills(s);
        return res;
    }
    
    public void sendSubscribersEmailJobs() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (listSubs != null && listSubs.size() > 0) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && listSkills.size() > 0) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && listJobs.size() > 0) {
                        List<ResEmailJob> arr = listJobs.stream().map(
                                job -> this.convertJobToSendEmail(job)).collect(Collectors.toList());
                        this.emailService.sendEmailFromTemplateSync(
                                sub.getEmail(),
                                "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                                "job",
                                sub.getName(),
                                arr);
                    }
                }
            }
        }
    }

     public Subscriber findByEmail(String email) {
        return this.subscriberRepository.findByEmail(email);
    }
}
