/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.spring.vfs;

import java.io.IOException;
import java.net.URL;

import org.jboss.virtual.VFS;
import org.jboss.virtual.VirtualFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * VFS based ResourcePatternResolver.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 * @author <a href="mailto:mariusb@redhat.com">Marius Bogoevici</a>
 */
public class VFSResourcePatternResolver extends PathMatchingResourcePatternResolver
{
   public VFSResourcePatternResolver()
   {
      super(new VFSResourceLoader());
   }

   public VFSResourcePatternResolver(ClassLoader classLoader)
   {
      super(new VFSResourceLoader(classLoader));
   }

   protected Resource[] findPathMatchingResources(String locationPattern) throws IOException
   {
      if (locationPattern.startsWith(CLASSPATH_ALL_URL_PREFIX))
         locationPattern = locationPattern.substring(CLASSPATH_ALL_URL_PREFIX.length());
      String rootDirPath = determineRootDir(locationPattern);
      return VFSResourcePatternResolvingHelper.locateResources(locationPattern, rootDirPath, getClassLoader(), getPathMatcher());
   }

   protected Resource convertClassLoaderURL(URL url)
   {
      try
      {
         VirtualFile file = VFS.getRoot(url);
         return new VFSResource(file);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

}