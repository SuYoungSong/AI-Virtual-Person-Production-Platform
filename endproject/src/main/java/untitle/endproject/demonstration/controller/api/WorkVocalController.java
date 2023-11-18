package untitle.endproject.demonstration.controller.api;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import untitle.endproject.demonstration.domain.WorkChar;
import untitle.endproject.demonstration.repository.WorkVocalRepository;
import untitle.endproject.demonstration.domain.WorkVocal;

@RestController
@RequestMapping("/api/workvocal")
@CrossOrigin
public class WorkVocalController {
    @Autowired
    private WorkVocalRepository workVocalRepository;

    @GetMapping("/")
    public List<WorkVocal> GetWorkVocal(){
        return workVocalRepository.findAll();
    }

    @GetMapping("/all/{id}")
    public List<WorkVocal> GetWorkCharsById(@PathVariable String id) {
        return workVocalRepository.findByIdEquals(id);
    }

    @GetMapping("/{id}")
    public WorkVocal GetWorkVocal(@PathVariable String id){
        return workVocalRepository.findById(id).orElse(null);
    }

    @PostMapping("/")
    public WorkVocal PostWorkVocal(@RequestBody WorkVocal workVocal){
        return workVocalRepository.save(workVocal);
    }
    @PutMapping("/")
    public WorkVocal PutWorkVocal(@RequestBody WorkVocal workVocal){
        WorkVocal old = workVocalRepository.findById(workVocal.getId()).orElse(null);
        old.setId(workVocal.getId());
        old.setVocal_name(workVocal.getVocal_name());
        return workVocalRepository.save(old);
    }

    @DeleteMapping("/{id}")
    public String DeleteWorkVocal(@PathVariable String uuid){
        workVocalRepository.deleteById(uuid);
        return uuid;
    }
}