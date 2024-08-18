package com.example.BookStore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "authorities")
//- Khi bạn gọi getAuthorities() trong UserPrincipal, bạn đang trả về một danh sách các Authority đã được chuyển đổi thành danh sách các GrantedAuthority.
// -Điều này giúp Spring Security hiểu được các quyền của người dùng một cách trực tiếp mà không cần thêm bất kỳ chuyển đổi nào.
public class Authority implements GrantedAuthority {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer  id;

    @Column(name = "authority")
    private String authority;

    @ManyToMany(mappedBy = "authorities", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
//    @JsonBackReference(value = "user-authority")
//    @JsonIgnore
    private List<User> users;
}
