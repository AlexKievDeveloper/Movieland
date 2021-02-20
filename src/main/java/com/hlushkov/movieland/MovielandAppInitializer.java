package com.hlushkov.movieland;

import com.hlushkov.movieland.web.MovielandWebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MovielandAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    public Class<?>[] getRootConfigClasses() {
        return new Class[]{MovielandApplicationContext.class};
    }

    @Override
    public Class<?>[] getServletConfigClasses() {
        return new Class[]{MovielandWebApplicationContext.class};
    }

    @Override
    public String[] getServletMappings() {
        return new String[]{"/api/v1/*"};
    }

}
