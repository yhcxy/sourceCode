package com.yehui;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * @goal printMojoApi
 * @phase compile
 */
public class PrintMojo01 extends AbstractMojo {

    /**
     * @parameter expression="${name}"
     */
    private String name;
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println(name);
        System.out.println("我是第二个插件");
    }
}
