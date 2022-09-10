package com.example.blog_site.repository;

import com.example.blog_site.model.Role;
import com.example.blog_site.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository  extends JpaRepository<Role, Long> {
    Optional<Role> getRoleByName(String name);
}