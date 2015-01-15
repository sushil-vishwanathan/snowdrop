package org.jboss.spring.deployers.as7;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.jboss.as.naming.deployment.ContextNames;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.msc.service.ServiceName;
import org.jboss.spring.vfs.VFSResource;
import org.jboss.vfs.VirtualFile;
import org.springframework.core.io.Resource;
import org.xml.sax.InputSource;

public class SpringResourceDependencyProcessor
  implements DeploymentUnitProcessor
{

  public void deploy(DeploymentPhaseContext phaseContext)
    throws DeploymentUnitProcessingException
  {
    try
    {
      SpringDeployment locations = SpringDeployment.retrieveFrom(phaseContext.getDeploymentUnit());

      if (locations == null) {
        return;
      }

      for (VirtualFile virtualFile : locations.getContextDefinitionLocations())
      {
        Resource resource = new VFSResource(virtualFile);
        XPath xPath = XPathFactory.newInstance().newXPath();
        xPath.setNamespaceContext(new NamespaceContext()
        {
          public String getNamespaceURI(String prefix) {
            return "http://www.springframework.org/schema/beans";
          }

          public String getPrefix(String namespaceURI)
          {
            return "beans";
          }

          public Iterator<String> getPrefixes(String namespaceURI)
          {
            return Collections.singleton("beans").iterator();
          }
        });

        String expression = "/beans:beans/beans:description";
        InputSource inputSource = new InputSource(resource.getInputStream());

        String description = xPath.evaluate(expression, inputSource);
        if (description != null) {
          Matcher pbfm = Pattern.compile("ParentBeanFactory=\\(([^)]+)\\)").matcher(description);

          if (pbfm.find())
          {

            ServiceName jndiName = ContextNames.buildServiceName(ContextNames.JBOSS_CONTEXT_SERVICE_NAME, pbfm.group(1));

            phaseContext.getServiceTarget().addDependency(jndiName);
          }

        }

      }

      SpringResourceDependencyStructure springResourceDependencyStructure = SpringResourceDependencyStructure.retrieveFrom(phaseContext.getDeploymentUnit());

      if (springResourceDependencyStructure != null) {
        List<String> resourceDependencies = springResourceDependencyStructure.getResources();

        for (String resource : resourceDependencies) {
          ServiceName resourceJndiName = ContextNames.bindInfoFor(resource).getBinderServiceName();
          phaseContext.getServiceTarget().addDependency(resourceJndiName);
        }
      }
    }
    catch (Exception e)
    {
      throw new DeploymentUnitProcessingException("Unable to establish Parent Application Context JNDI Dependency Chain", e);
    }
  }

  public void undeploy(DeploymentUnit context)
  {
  }
}