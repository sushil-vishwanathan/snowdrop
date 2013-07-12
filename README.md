SNOWDROP
========

This an experimental version of snowdrop, specifically to see how well Custom Component Scanner is received.

**To build:**

`mvn clean install`

Then install snowdrop:

**Automatic:**

`cd install`

`mvn package -P${desired-spring-version} -DJBOSS_HOME=/path/to/jboss_home -Dversion.snowdrop=2.1.1-SNAPSHOT`

**Manual:**

To install the Snowdrop Deployment subsystem, unzip the `subsystem-as7/subsystem-as7/target/jboss-spring-deployer-as7.zip` file in the `$JBOSS_HOME/modules` dir.

Then extract the `subsystem-as7/modules/spring-x.x/target/spring-x.x-module.zip` in the `$JBOSS_HOME/modules` directory of your JBoss Application Server installation.

The final step in the installation is to change `$JBOSS_HOME/standalone/configuration/standalone.xml` by including `<extension module="org.jboss.snowdrop"/>` inside the `<extensions>` element, as well as including `<subsystem xmlns="urn:jboss:domain:snowdrop:1.0"/>` inside the `<profile>` element.

**Then in your Spring Project add the following dependency:**

    <dependency>
        <groupId>org.jboss.snowdrop</groupId>
        <artifactId>snowdrop-deployers</artifactId>
        <version>2.1.1-SNAPSHOT</version>
    </dependency>

    <dependency>
        <artifactId>snowdrop-parent</artifactId>
        <groupId>org.jboss.snowdrop</groupId>
        <version>2.1.1-SNAPSHOT</version>
    </dependency>

In your project add to your schemas:

`<beans xmlns="http://www.springframework.org/schema/beans"
       **xmlns:jboss="http://www.jboss.org/schema/spring"**
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       **http://www.jboss.org/schema/snowdrop http://www.jboss.org/schema/snowdrop/snowdrop.xsd**">`


And replace `<context:component-scan base-package="xyz"/>` with `<jboss:component-scan base-package="xyz"/>`

Lastly, create a jboss-spring.xml in your META-INF folder and put in:

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE beans PUBLIC "-//SPRING//DTD BEAN//EN" "http://www.springframework.org/dtd/spring-beans.dtd">

<beans>

	<description>BeanFactory=(MyApp)</description>

</beans>
```

This file lets JBoss know this is a spring deployment and to use the custom scanner

Proceed by deploying to jboss as you normally would and the scanning of components will be much faster.

We would appreciate any feedback: <https://community.jboss.org/message/827689>.