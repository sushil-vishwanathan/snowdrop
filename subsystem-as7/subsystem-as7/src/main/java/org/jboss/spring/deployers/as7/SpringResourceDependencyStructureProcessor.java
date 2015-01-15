package org.jboss.spring.deployers.as7;

import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.vfs.VirtualFile;

public class SpringResourceDependencyStructureProcessor
  implements DeploymentUnitProcessor
{

  public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException
  {
    DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();

    ResourceRoot deploymentRoot = (ResourceRoot)deploymentUnit.getAttachment(Attachments.DEPLOYMENT_ROOT);

    if (deploymentRoot == null) {
      return;
    }

    VirtualFile metaInf = deploymentRoot.getRoot().getChild("META-INF");
    VirtualFile resourceDependencyLocation = metaInf.getChild("resource-dependencies.properties");
    if (resourceDependencyLocation.exists()) {
      SpringResourceDependencyStructure springResourceDependencyStructure = new SpringResourceDependencyStructure(resourceDependencyLocation);
      springResourceDependencyStructure.attachTo(deploymentUnit);
    }
  }

  public void undeploy(DeploymentUnit context)
  {
  }
}