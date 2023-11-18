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
import untitle.endproject.demonstration.repository.WorkCharRepository;
import untitle.endproject.demonstration.domain.WorkChar;

@RestController
@RequestMapping("/api/workchar")
@CrossOrigin
public class WorkCharController {
    @Autowired
    private WorkCharRepository workCharRepository;

    @GetMapping("/")
    public List<WorkChar> GetWorkChar(){
        return workCharRepository.findAll();
    }

    @GetMapping("/all/{id}")
    public List<WorkChar> GetWorkCharsById(@PathVariable String id) {
        return workCharRepository.findByIdEquals(id);
    }

    @GetMapping("/{id}")
    public WorkChar GetWorkChar(@PathVariable String id){
        return workCharRepository.findById(id).orElse(null);
    }

    @PostMapping("/")
    public WorkChar PostWorkChar(@RequestBody WorkChar workChar){
        return workCharRepository.save(workChar);
    }
    @PutMapping("/")
    public WorkChar PutWorkChar(@RequestBody WorkChar workChar){
        WorkChar old = workCharRepository.findById(workChar.getId()).orElse(null);
        old.setId(workChar.getId());
        old.setChar_name(workChar.getChar_name());
        old.setImage_prompt(workChar.getImage_prompt());
        old.setVoice_prompt(workChar.getVoice_prompt());
        return workCharRepository.save(old);
    }

    @DeleteMapping("/{id}")
    public String DeleteWorkChar(@PathVariable String uuid){
        workCharRepository.deleteById(uuid);
        return uuid;
    }
}