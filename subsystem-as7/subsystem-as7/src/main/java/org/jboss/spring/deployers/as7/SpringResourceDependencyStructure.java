package org.jboss.spring.deployers.as7;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jboss.as.server.deployment.AttachmentKey;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.spring.vfs.VFSResource;
import org.jboss.vfs.VirtualFile;

public class SpringResourceDependencyStructure
{
  public static final AttachmentKey<SpringResourceDependencyStructure> ATTACHMENT_KEY = AttachmentKey.create(SpringResourceDependencyStructure.class);

  private List<String> resourceDependencies = new ArrayList<String>();

  public SpringResourceDependencyStructure(VirtualFile resourceDependencyLocation) {
    parseDependencies(resourceDependencyLocation);
  }

  private void parseDependencies(VirtualFile resourceDependencyLocation) {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(new VFSResource(resourceDependencyLocation).getInputStream()));
      String resource;
      while ((resource = reader.readLine()) != null)
        this.resourceDependencies.add(resource);
    }
    catch (Exception e)
    {
      throw new RuntimeException("Unable to parse the resource dependencies for this module");
    }
    finally {
      if (reader != null)
        try {
          reader.close();
        }
        catch (Exception e2)
        {
        }
    }
  }

  public List<String> getResources()
  {
    return Collections.unmodifiableList(this.resourceDependencies);
  }

  public void attachTo(DeploymentUnit deploymentUnit) {
    deploymentUnit.putAttachment(ATTACHMENT_KEY, this);
  }

  public static SpringResourceDependencyStructure retrieveFrom(DeploymentUnit deploymentUnit)
  {
    return (SpringResourceDependencyStructure)deploymentUnit.getAttachment(ATTACHMENT_KEY);
  }
}