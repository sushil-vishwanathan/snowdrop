/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.snowdrop.context.support;

import java.util.Properties;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author Marius Bogoevici
 */
public class JBossActivationSpecBeanDefinitionParser extends AbstractBeanDefinitionParser
{
   private static final String DEFAULT_ID="jbossActivationSpecFactory";

   private static String DEFAULT_JMS_ACTIVATION_SPEC_FACTORY_CLASS_NAME = "org.springframework.jms.listener.endpoint.DefaultJmsActivationSpecFactory";

   @Override
   protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) throws BeanDefinitionStoreException
   {
      return super.resolveId(element, definition, parserContext);
   }

   @Override
   protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext)
   {
      BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(DEFAULT_JMS_ACTIVATION_SPEC_FACTORY_CLASS_NAME);
      beanDefinitionBuilder.addPropertyValue("activationSpecClass", getJBossActivationSpecClass());
      Properties properties = new Properties();
      properties.setProperty("clientId", "remote");
      properties.setProperty("subscriptionName", "jca-processor");
      properties.setProperty("useDLQ", "false");
      beanDefinitionBuilder.addPropertyValue("defaultProperties", properties);
      return beanDefinitionBuilder.getBeanDefinition();
   }

   private String getJBossActivationSpecClass()
   {
      return "org.jboss.resource.adapter.jms.inflow.JmsActivationSpec";
   }
}
