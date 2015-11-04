package com.github.rojanu.mojo;

import com.facebook.swift.generator.swift2thrift.PublicSwift2ThriftGenerator;
import com.facebook.swift.generator.swift2thrift.Swift2ThriftGeneratorConfig;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;

/**
 * Process Java byte codes and generate IDL files.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class Swift2ThriftMojo extends AbstractMojo {

    /**
     * Skip the plugin execution.
     */
    @Parameter(defaultValue = "false")
    private boolean skip = false;

    /**
     * IDL file to produce.
     */
    @Parameter(required = true)
    private String idlFile;

    /**
     * Set the Output folder for generated code.
     */
    @Parameter(defaultValue = "${project.build.directory}/generated-sources/thrift", required = true)
    private String outputFolder = null;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    protected MavenProject project;

    @Parameter(defaultValue = "false")
    private boolean verbose = false;

    @Parameter(required = true)
    private String javaPackage = "";

    @Parameter(required = true)
    private List<String> swiftClassNames;

    public final void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("########################## Generating idl file #######################");
        try {
            if (!skip) {
                ImmutableMap.Builder<String, String> mapBuilder = ImmutableMap.builder();
                ImmutableMap.Builder<String, String> namespaceMapBuilder = ImmutableMap.builder();
                namespaceMapBuilder.put("java", javaPackage);

                List<String> classpathElements = project.getCompileClasspathElements();
                classpathElements.addAll(project.getRuntimeClasspathElements());
                classpathElements.add(project.getBuild().getOutputDirectory());

                HashSet<URL> classPathUrls = new HashSet<URL>();
                for (String s : classpathElements) {
                    classPathUrls.add(new File(s).toURI().toURL());
                }


                File outputFile = new File(outputFolder + File.separator + idlFile);
                FileUtils.forceMkdir(new File(outputFolder));
                Swift2ThriftGeneratorConfig.Builder configBuilder = Swift2ThriftGeneratorConfig
                        .builder()
                        .outputFile(outputFile)
                        .includeMap(mapBuilder.build())
                        .verbose(verbose)
                        .defaultPackage("")
                        .namespaceMap(namespaceMapBuilder.build())
                        .allowMultiplePackages(javaPackage)
                        .recursive(true)
                        .classLoader(
                                new URLClassLoader(
                                        classPathUrls.toArray(
                                                new URL[]{}),
                                        Thread.currentThread().getContextClassLoader()
                                )
                        );

                new PublicSwift2ThriftGenerator(configBuilder.build()).parse(swiftClassNames);
            }
        } catch (Exception e) {
            Throwables.propagateIfInstanceOf(e, MojoExecutionException.class);
            Throwables.propagateIfInstanceOf(e, MojoFailureException.class);

            getLog().error(String.format("While executing Mojo %s", this.getClass().getSimpleName()), e);
            throw new MojoExecutionException("Failure:", e);
        }
    }
}
