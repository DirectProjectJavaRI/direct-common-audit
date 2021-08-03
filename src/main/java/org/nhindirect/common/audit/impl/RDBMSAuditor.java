/* 
Copyright (c) 2010, NHIN Direct Project
All rights reserved.

Authors:
   Greg Meyer      gm2552@cerner.com
 
Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer 
in the documentation and/or other materials provided with the distribution.  Neither the name of the The NHIN Direct Project (nhindirect.org). 
nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS 
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
THE POSSIBILITY OF SUCH DAMAGE.
*/

package org.nhindirect.common.audit.impl;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.management.openmbean.ArrayType;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;

import org.nhindirect.common.audit.AbstractAuditor;
import org.nhindirect.common.audit.AuditContext;
import org.nhindirect.common.audit.AuditEvent;
import org.nhindirect.common.audit.AuditorMBean;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Implementation of the DirectProject RI auditor that writes records to a configurable database.
 * Also implements the AuditorMBean interface for management access to audit events.
 * @author Greg Meyer
 * @since 1.0
 */
@Slf4j
public class RDBMSAuditor extends AbstractAuditor implements AuditorMBean
{
    
	private String[] itemNames;
	private CompositeType eventType;
    
	@Autowired
	protected RDBMSAuditEventRepository eventRepo;
	
	@Autowired
	protected RDBMSAuditContextRepository contextRepo;
	
	
	/**
	 * Constructor
	 */
    public RDBMSAuditor()
    {	
    	super();
		// register the auditor as an MBean
    	
		registerMBean();
    }
	
	/**
	 * Constructor
	 */
    public RDBMSAuditor(RDBMSAuditEventRepository eventRepo, RDBMSAuditContextRepository contextRepo)
    {	
    	this();
		// register the auditor as an MBean
    	
    	setEventRepo(eventRepo);
    	setContextRepo(contextRepo);
    }
    
    
    public void setEventRepo(RDBMSAuditEventRepository eventRepo)
    {
    	this.eventRepo = eventRepo;
    }
    
    public void setContextRepo(RDBMSAuditContextRepository contextRepo)
    {
    	this.contextRepo = contextRepo;
    }
    
	/*
	 * Register the MBean
	 */
	private void registerMBean()
	{
		
		log.info("Registering RDBMSAuditor MBean");
		
		try
		{
			itemNames = new String[] {"Event Id", "Event Time", "Event Principal", "Event Name", "Event Type", "Contexts"};
			
			OpenType<?>[] types = {SimpleType.STRING, SimpleType.STRING, SimpleType.STRING, SimpleType.STRING, 
					SimpleType.STRING, ArrayType.getArrayType(SimpleType.STRING)};
			
			eventType = new CompositeType("AuditEvent", "Direct Auditable Event", itemNames, itemNames, types);
		}
		catch (OpenDataException e)
		{
			log.error("Failed to create settings composite type: {}",  e.getLocalizedMessage(), e);
			return;
		}
		
		final Class<?> clazz = this.getClass();
		final StringBuilder objectNameBuilder = new StringBuilder(clazz.getPackage().getName());
		objectNameBuilder.append(":type=").append(clazz.getSimpleName());
		objectNameBuilder.append(",name=").append(UUID.randomUUID());
				
		try
		{			
			final StandardMBean mbean = new StandardMBean(this, AuditorMBean.class);
		
			final MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
			mbeanServer.registerMBean(mbean, new ObjectName(objectNameBuilder.toString()));
		}
		catch (JMException e)
		{
			log.error("Unable to register the RDBMSAuditors MBean", e);
		}		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeEvent(UUID eventId, Calendar eventTimeStamp,
			String principal, AuditEvent event, Collection<? extends AuditContext> contexts) 
	{
		org.nhindirect.common.audit.impl.entity.AuditEvent newEvent =
				new org.nhindirect.common.audit.impl.entity.AuditEvent();
		
		newEvent.setEventName(event.getName());
		newEvent.setEventType(event.getType());

		newEvent.setEventTime(LocalDateTime.now());
		newEvent.setPrincipal(principal);
		newEvent.setUUID(eventId.toString());
		
		newEvent = eventRepo.save(newEvent).block();

		if (contexts != null && !contexts.isEmpty())
		{
			final Collection<org.nhindirect.common.audit.impl.entity.AuditContext> entityContexts = new ArrayList<>();
			for (AuditContext context : contexts)
			{
				final org.nhindirect.common.audit.impl.entity.AuditContext newContext = 
						new org.nhindirect.common.audit.impl.entity.AuditContext();
				
				newContext.setContextName(context.getContextName());
				newContext.setContextValue(context.getContextValue());
				newContext.setAuditEventId(newEvent.getId());
				entityContexts.add(newContext);
			}
			
			contextRepo.saveAll(entityContexts).collectList().block();
		}

	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getEventCount() 
	{
		return eventRepo.count().block().intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CompositeData[] getEvents(Integer eventCount) 
	{
		if (eventType == null || eventCount == 0)
			return null;
		
		final Vector<CompositeData> retVal = new Vector<CompositeData>();
		
		final List<CompositeDataSupport> dataSupports = eventRepo.findAllOrderByEventTimeDesc()
		.take(eventCount)
		.flatMap(evt -> 
		{
			return contextRepo.findByAuditEventId(evt.getId()).collectList()
			//.switchIfEmpty(Mono.just(Collections.emptyList()))
			.flatMap(ctxs -> 
			{
				String[] contexts = null;
				
				if (!ctxs.isEmpty())
				{
	        		contexts = new String[ctxs.size()];
	        		int idx = 0;
					for (org.nhindirect.common.audit.impl.entity.AuditContext ctx : ctxs)
					{
						contexts[idx++] = ctx.getContextName() + ":" + ctx.getContextValue();
					}
				}
				else
					contexts = new String[] {" "};
				
				final Object[] eventValues = {evt.getUUID(), evt.getEventTime().toString(), evt.getPrincipal(), 
						evt.getEventName(), evt.getEventType(), contexts};
				
				try
				{
					return Mono.just(new CompositeDataSupport(eventType, itemNames, eventValues));
				}
				catch (Exception e)
				{
					log.error("Error create composit data for audit event.", e);
					return Mono.empty();
				}
			});
		})
		.collectList().block();
		
		
		if (dataSupports.isEmpty())
			return null;
		
		retVal.addAll(dataSupports);
		
        
		return retVal.toArray(new CompositeData[retVal.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CompositeData getLastEvent() 
	{
		final CompositeData[] events = getEvents(1); 

		if (events == null || events.length == 0)
			return null;
		
		return events[0];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() 
	{
		contextRepo.deleteAll()
			.then(eventRepo.deleteAll()).block();

	}
}
