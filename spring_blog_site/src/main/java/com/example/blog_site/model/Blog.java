package com.example.blog_site.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Entity
@Table(name = "post")
public class Blog {

    public static final String DEFAULT_BLOG_IMAGE_PATH = "/images/default-blog.png";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    @Size(min = 1, max = 40)
    private String title;

    @Column(name = "content")
    @Size(min = 6, max = 25000)
    private String content;

    @Column(length = 64)
    private String photos;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "blog_id")
    private List<Comment> comments;

    @Transient
    public String getPhotosImagePath() {

        if (id == null || photos == null) {
            return DEFAULT_BLOG_IMAGE_PATH;
        }
        return "/blog-photos/" + this.id + "/" + this.photos;
    }

}