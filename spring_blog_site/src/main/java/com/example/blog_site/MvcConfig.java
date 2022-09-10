package com.example.blog_site;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {


        exposeDirectory(registry, FileUploadUtil.BLOG_UPLOAD_FOLDER);
    }
    private void exposeDirectory(ResourceHandlerRegistry registry, String pathPattern) {
        Path pathDir = Paths.get(pathPattern);
        String absolutePath = pathDir.toFile().getAbsolutePath();

        String logicalPath = "/" + pathPattern + "/**";

        registry.addResourceHandler(logicalPath)
                .addResourceLocations("file:" + absolutePath + "/");
    }

}
