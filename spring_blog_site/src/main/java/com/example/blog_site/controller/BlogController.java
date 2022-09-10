package com.example.blog_site.controller;

import com.example.blog_site.FileUploadUtil;
import com.example.blog_site.exception.PostNotFoundException;
import com.example.blog_site.model.Blog;
import com.example.blog_site.model.Comment;
import com.example.blog_site.service.BlogService;
import com.example.blog_site.service.CommentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/blogs")
public class BlogController {

    private final BlogService blogService;
    private final CommentService commentService;

    public BlogController(BlogService blogService, CommentService commentService) {
        this.blogService = blogService;
        this.commentService = commentService;
    }


    @GetMapping
    public String getBlogs(Model model) {
        model.addAttribute("blogList", blogService.getAllFirstNCharacters(100));
        return "blog/index";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/create")
    public String createBlog(Model model) {
        model.addAttribute("blog", new Blog());
        return "blog/create";
    }

    @GetMapping("/{id}/view")
    public String getBlog(@PathVariable("id") Blog blog, Model model) {
        model.addAttribute("blog", blog);
        model.addAttribute("commentBox", new Comment());
        return "blog/view";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/edit")
    public String editBlog(@PathVariable("id") Blog blog, Model model){
        model.addAttribute("blog", blog);
        return "blog/edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/delete")
    public String deleteBlog(@PathVariable("id") long id, Blog blog,
                             RedirectAttributes redirectAttributes) {

        try {
            blogService.deleteBlog(blog);

            String uploadDir = FileUploadUtil.BLOG_UPLOAD_DIRECTORY + id;
            FileUploadUtil.removeDirectory(uploadDir);

            redirectAttributes.addFlashAttribute("message",
                    "The brand ID " + id + " has been deleted successfully");
        } catch (PostNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/blogs";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public String createBlog(Blog blog, RedirectAttributes redirectAttributes,
                               @RequestParam("fileImage") MultipartFile multipartFile) throws IOException  {
        Blog savedBlog;
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            blog.setPhotos(fileName);
            savedBlog = blogService.saveBlog(blog);

            String uploadDir = FileUploadUtil.BLOG_UPLOAD_DIRECTORY + savedBlog.getId();
            FileUploadUtil.cleanDirectory(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        } else {
            savedBlog = blogService.saveBlog(blog);
        }

        redirectAttributes.addFlashAttribute("message", "The blog has been saved successfully.");
        return "redirect:/blogs";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/edit")
    public String editBlog(@PathVariable("id") Blog blog, Blog blogDTO) {
        blog.setContent(blogDTO.getContent());
        blog.setTitle(blogDTO.getTitle());
        blogService.saveBlog(blog);
        return "redirect:/blogs/" + blog.getId() + "/view";
    }



}