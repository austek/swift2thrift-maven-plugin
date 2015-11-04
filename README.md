# Swift Maven Plugin

Generate thrift IDL from Swift compatible java code.

# Usage

Add the plugin to the build plugins section of the pom.

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.github.rojanu</groupId>
            <artifactId>swift2thrift-maven-plugin</artifactId>
            <version>0.1-SNAPSHOT</version>
            <executions>
                <execution>
                    <phase>process-classes</phase>
                    <goals>
                        <goal>generate</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <idlFile>mame-of-generated.idl</idlFile>
                <javaPackage>com.package.pojo.to.be.thrift</javaPackage>
                <swiftClassNames>
                    <swiftClassName>com.package.pojo.long.package.ClassName</swiftClassName>
                    <swiftClassName>com.package.pojo.OtherClassName</swiftClassName>
                </swiftClassNames>
            </configuration>
        </plugin>
    </plugins>
</build>
```

Additional help can be displayed with <tt>mvn com.github.rojanu:swift2thrift-maven-plugin:help</tt>.

# Supported configuration parameters

## skip

Skip the plugin completely. Can be used to turn plugin execution on or off.

<tt>skip</tt> is optional, default is "false".

    <skip>true|false</skip>

## idlFile

Name of the IDL file to be generatd

<tt>idFile</tt> must be defined in the POM, otherwise the plugin will report an error.

    <idlFile>mame-of-generated.idl</idlFile>

## outputFolder

Determines the output folder for the generated sources.

<tt>outputFolder</tt> is optional with a default of <tt>${project.build.directory}/generated-sources/thrift</tt>.

## javaPackage

Allow input classes to reside in different packages. The value of this flag defines the generated java.swift namespace. Note that Swift classes generated from the resultant Thrift file will all reside in one Java package

<tt>javaPackage</tt> must be defined in the POM, otherwise the plugin will report an error.

    <javaPackage>com.package.pojo.to.be.thrift</javaPackage>

## swiftClassNames

A list of Swift compatible Java classes to be included in generated IDL file

<tt>swiftClassNames</tt> must be defined in the POM, otherwise the plugin will report an error.

    <swiftClassNames>
      <swiftClassName>com.package.pojo.long.package.ClassName</swiftClassName>
      <swiftClassName>com.package.pojo.OtherClassName</swiftClassName>
      ...
    </swiftClassNames>

# Maven lifecycle

The <tt>generate</tt> goal of the plugin is by default hooked into the <tt>generate-sources</tt> phase of the maven lifecycle.

