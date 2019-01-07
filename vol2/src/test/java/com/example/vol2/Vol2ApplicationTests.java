package com.example.vol2;

import com.example.vol2.bean.AnnotatedHello;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ClassUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
//어플리케이션 컨텍스트 생성과 동시에 xml파일을 읽어오고 초기화까지 수행한다.
@ContextConfiguration(locations = "/genericApplicationContext.xml")
public class Vol2ApplicationTests {

    @Autowired
    ApplicationContext applicationContext;

    String basePath = StringUtils.cleanPath(ClassUtils.classPackageAsResourcePath(getClass())) + "/";

    @Test
    public void contextTest() {

        //IoC컨테이너 생성. 생성과 동시에 컨테이너로 동작한다.
        StaticApplicationContext ac = new StaticApplicationContext();
        //싱글톤빈으로 컨테이너에 등록한다.
        ac.registerSingleton("hello1",Hello.class);


        Hello hello1 = ac.getBean("hello1",Hello.class);
        assertThat(hello1,is(notNullValue()));


        //빈 메타정보를 담은 오브젝트를 만든다.
        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        //빈의 name프로퍼티에 들어갈 값을 지정한다,
        helloDef.getPropertyValues().addPropertyValue("name","Spring");
        //앞서 생성한 빈 메타정보를 hello2라는 이름을 가진 빈으로 해서 등록한다,
        ac.registerBeanDefinition("hello2",helloDef);


        //빈이 만들어 졌는지 확인
        Hello hello2 = ac.getBean("hello2",Hello.class);
        assertThat(hello2.sayHello(),is("Hello Spring"));

        assertThat(hello1,is(not(hello2)));
        assertThat(ac.getBeanFactory().getBeanDefinitionCount(),is(2));

    }

    @Test
    public void registerBeanWithDependency(){
        StaticApplicationContext ac = new StaticApplicationContext();

        ac.registerBeanDefinition("printer",new RootBeanDefinition(StringPrinter.class));

        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().addPropertyValue("name","Spring");
        //아이디가 printer인 빈에 대한 레퍼런스를 프로퍼티로 등록
        helloDef.getPropertyValues().addPropertyValue("printer",new RuntimeBeanReference("printer"));

        ac.registerBeanDefinition("hello",helloDef);

        Hello hello = ac.getBean("hello",Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString(),is("Hello Spring"));
    }

    @Test
    public void genericApplicationContext(){

        GenericApplicationContext ac = new GenericXmlApplicationContext("genericApplicationContext.xml");

        Hello hello = ac.getBean("hello",Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString(),is("Hello Spring"));
    }

    @Test
    public void layerApplicationContext(){
        ApplicationContext parent = new GenericXmlApplicationContext("parentContext.xml");

        //child라는 애플리케이션 컨텍스트는 parent컨텍스트를 부모 컨택스트로 갖게 된다.
        GenericApplicationContext child = new GenericApplicationContext(parent);

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(child);
        reader.loadBeanDefinitions("childContext.xml");
        child.refresh();

        Printer printer = child.getBean("printer",Printer.class);
        assertThat(printer,is(notNullValue()));

        Hello hello = child.getBean("hello",Hello.class);
        assertThat(hello,notNullValue());

        hello.print();
        assertThat(printer.toString(),is("Hello Child"));
    }

    @Test
    public void simpleBeanScanning(){
        ApplicationContext ctx = new AnnotationConfigApplicationContext("com.example.vol2.bean");
        AnnotatedHello hello = ctx.getBean("annotatedHello",AnnotatedHello.class);

        assertThat(hello,is(notNullValue()));
    }
}
