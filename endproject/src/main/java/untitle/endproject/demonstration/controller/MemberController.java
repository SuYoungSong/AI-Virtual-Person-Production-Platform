package untitle.endproject.demonstration.controller;

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
import untitle.endproject.demonstration.repository.MemberRepository;
import untitle.endproject.demonstration.domain.Member;

@RestController
@RequestMapping("/api/member")
@CrossOrigin
public class MemberController {
    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/")
    public List<Member> GetMembers(){
        return memberRepository.findAll();
    }

    @GetMapping("/{id}")
    public Member GetUser(@PathVariable String id){
        return memberRepository.findById(id).orElse(null);
    }

    @PostMapping("/")
    public Member PostUser(@RequestBody Member member){
        return memberRepository.save(member);
    }
    @PutMapping("/")
    public Member PutUser(@RequestBody Member member){
        Member oldUser = memberRepository.findById(member.getId()).orElse(null);
        oldUser.setPw(member.getPw());
        oldUser.setNickname(member.getNickname());
        oldUser.setUserName(member.getUserName());
        oldUser.setPhone(member.getPhone());
        return memberRepository.save(oldUser);
    }

    @DeleteMapping("/{id}")
    public String DeleteUser(@PathVariable String id){
        memberRepository.deleteById(id);
        return id;
    }
}