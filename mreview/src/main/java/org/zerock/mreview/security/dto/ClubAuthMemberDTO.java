package org.zerock.mreview.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Log4j2
@Getter
@Setter
@ToString
public class ClubAuthMemberDTO extends User {


    private String email;

    private String name;

    private boolean fromSocial;

    public ClubAuthMemberDTO(String username, String password, boolean fromSocial,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.email = username; // spring security에서 넘어오는 username은 id이고, 현재 email을 id로 사용하고 있기에 이렇게 코딩
        this.fromSocial = fromSocial; // 소셜로그인 여부. 구글로그인으로 로그인해서 데이터가 insert되면 true(1), 아니면 false(0)
    }

}
