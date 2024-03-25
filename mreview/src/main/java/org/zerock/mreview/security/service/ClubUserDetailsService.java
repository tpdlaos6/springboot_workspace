package org.zerock.mreview.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.mreview.entity.ClubMember;
import org.zerock.mreview.repository.ClubMemberRepository;
import org.zerock.mreview.security.dto.ClubAuthMemberDTO;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ClubUserDetailsService implements UserDetailsService {
    //UserDetailsService : 사용자의 정보처리. 스프링부트에서는 사용자 정보처리 방법이 딱 세 가지가 있음
    //id , pw , enabled
    //그러나 실제론 이메일, 전화번호 등을 사용하는 방법들도 있기 때문에... 위 3가지를 그때그때 상황에 맞게 커스터마이징해서 사용해야 함.
    //로그인폼과 사용자 정보처리 부분을 커스터마이징해서 사용해야 함.

    private final ClubMemberRepository clubMemberRepository;

    @Override // loadUserByUsername() : spring security 자체 메서드. username(ID)으로 정보를 읽어오는 메서드
    // UserDetailsService를 사용할 때는 loadUserByUsername를 오버라이딩해야 함
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("ClubUserDetailsService loadUserByUsername " + username);


        Optional<ClubMember> result = clubMemberRepository.findByEmail(username, false);

        if(result.isEmpty()){
            throw new UsernameNotFoundException("Check User Email or from Social ");
        }

        ClubMember clubMember = result.get();

        log.info("-----------------------------");
        log.info(clubMember);

        ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
                clubMember.getEmail(),
                clubMember.getPassword(),
                clubMember.isFromSocial(), // lombok에서 타입이 boolean일 경우, getter 이름의 시작이 get으로 시작하는 게 아니라 is로 시작
                clubMember.getRoleSet().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
                        .collect(Collectors.toSet())
        );

        clubAuthMember.setName(clubMember.getName());
        clubAuthMember.setFromSocial(clubMember.isFromSocial());

        return clubAuthMember;
    }
}
