#settings.gradle

settings.gradle에 전체 프로젝트의 구조를 빌드한다.
```gradle
rootProject.name="project-name"
include "project-name"
```
으로 한 개의 프로젝트를 구성하지만,
여러 모듈 프로젝트를 포함하는 경우엔
```gradle
rootProject.name="project-name"
include "project-name1"
include "project-name2"
```
이런 식으로 사용할 수 있다.

만약, 모듈 프로젝트 많아서 group로 관리하고 싶으면 자동 빌드하는 스크립트를 쓸 수도 있다.

어... 이거 잡아서 사용하고 있네.
```gradle
rootProject.name = 'security-gradle3'

["comp", "web", "server"].each {

    def compDir = new File(rootDir, it)
    if(!compDir.exists()){
        compDir.mkdirs()
    }

    compDir.eachDir {subDir ->

        def gradleFile = new File(subDir.absolutePath, "build.gradle")
        if(!gradleFile.exists()){
            gradleFile.text =
                    """

                    dependencies {

                    }

                    """.stripIndent(20)
        }

        [
                "src/main/java/com/sp/fc",
                "src/main/resources",
                "src/test/java/com/sp/fc",
                "src/test/resources"
        ].each {srcDir->
            def srcFolder = new File(subDir.absolutePath, srcDir)
            if(!srcFolder.exists()){
                srcFolder.mkdirs()
            }
        }

        def projectName = ":${it}-${subDir.name}";
        include projectName
        project(projectName).projectDir = subDir
    }
}
```
아무것도 없는데 이거 쓰니까 폴더를 만들어줌.
e... 이걸로 폴더 트리를 구성할 수도 있구나 대단쓰;;

comp, web, server 폴더가 만들어졌음...
방법이 진짜 다양하구나...;

IDE에서 제공하는 거 써도 되고, 직접 만들어도 되고, 이렇게 스크립트로 만들 수도 있구나



---
build.gradle
전체 하위 프로젝트의 공통 설정에 대한 사항을 기술해 넣는다.
```gradle
buildscript {
    ext {
        spring = "2.4.1"
        boot = "org.springframework.boot"
        lombok = "org.projectlombok:lombok"
    }
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("$boot:spring-boot-gradle-plugin:$spring")
    }
}

allprojects {
    group = "com.sp.fc"
    version = "1.0.0"
}

subprojects {

    apply plugin: "java"
    apply plugin: boot
    apply plugin: "io.spring.dependency-management"
    apply plugin: "idea"

    repositories {
        mavenCentral()
    }

    configurations {
        developmentOnly
        runtimeClasspath {
            extendsFrom developmentOnly
        }
    }

    dependencies {
        developmentOnly("$boot:spring-boot-devtools")
        implementation "$boot:spring-boot-starter-security"
        implementation 'com.fasterxml.jackson.core:jackson-annotations'

        compileOnly lombok
        testCompileOnly lombok
        annotationProcessor lombok
        testAnnotationProcessor lombok

        testImplementation "$boot:spring-boot-starter-test"
    }

    test {
        useJUnitPlatform()
    }

}


["comp", "web"].each {
    def subProjectDir = new File(projectDir, it)
    subProjectDir.eachDir {dir->
        def projectName = ":${it}-${dir.name}"
        project(projectName){
            bootJar.enabled(false)
            jar.enabled(true)
        }
    }
}
["server"].each {
    def subProjectDir = new File(projectDir, it)
    subProjectDir.eachDir {dir->
        def projectName = ":${it}-${dir.name}"
        project(projectName){

        }
    }
}

help.enabled(false)
```

와 실화냐고 ㅋㅋㅋ
어케했노;;

이렇게 구성할 수 있다 정도만 알고 있자;;

